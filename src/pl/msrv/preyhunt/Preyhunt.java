package pl.msrv.preyhunt;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

public class Preyhunt extends JavaPlugin implements Listener {

    public static Logger log;
    private FileConfiguration config;
    private I18n i18n;

    // Game
    public boolean gameActive;
    public ArrayList<Player> hunters;
    public ArrayList<Player> prey;

    @Override
    public void onEnable(){
        log = this.getLogger();

        // Create/load the config
        log.info("Loading config...");
        saveDefaultConfig();
        config = getConfig();

        // (Re)initialize everything
        gameActive = false;
        hunters = new ArrayList<>();
        prey = new ArrayList<>();
        i18n = new I18n(config.getString("lang", "en"));

        // Register commands and event listeners
        this.getCommand("mh").setExecutor(new CommandMh(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable(){
        // nothing lol
    }

    public void startGame(){
        String startWorldName = config.getString("start-world", "world");
        int hunterDebuffLength = config.getInt("hunter-debuff-length", 5);
        int compassUpdateInterval = config.getInt("compass-update-interval", 5);
        int spawnSpread = config.getInt("spawn-spread", 5);

        gameActive = true;

        // Prepare the world
        World startWorld = getServer().getWorld(startWorldName);
        startWorld.setTime(0);
        startWorld.setClearWeatherDuration(20 * 60 * 20); // Set clear weather for 20 minutes (1 in game day)

        // Teleport everyone to spawn
        Object[] players = getServer().getOnlinePlayers().toArray();
        for(Object playerObj : players){
            Location loc = randomizeLocation(startWorld.getSpawnLocation(), spawnSpread);
            ((Player) playerObj).teleport(loc);
        }

        // Prepare the hunters
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, hunterDebuffLength * 20, 1);
        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, hunterDebuffLength * 20, 1);
        for(Player player : hunters){
            resetPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().addItem(compass);
            player.addPotionEffect(slow);
            player.addPotionEffect(blind);
        }

        // Prepare the prey
        ItemStack preyFood = new ItemStack(Material.COOKED_PORKCHOP, 2);
        for(Player player : prey) {
            resetPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().addItem(preyFood);
        }

        // Schedule periodical compass updates
        new CompassUpdateRunnable(this).runTaskTimer(this, 0, compassUpdateInterval * 20);

        // Inform players
        startWorld.playSound(startWorld.getSpawnLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
        getServer().broadcastMessage(I18n.tl("annManhuntBegun"));

        for(Player player : hunters){
            player.sendTitle(
                    I18n.tl("annManhuntBegun"),
                    I18n.tl("annYouAreHunter"),
                    5, 20, 5);
            player.sendMessage(I18n.tl("annCompassUpdatesInHand"));
        }

        for(Player player : prey){
            player.sendTitle(
                    I18n.tl("annManhuntBegun"),
                    I18n.tl("annYouArePrey"),
                    5, 20, 5);
        }
    }

    public void endGame(){
        // Announce victory
        getServer().broadcastMessage(I18n.tl("annManhuntEnded"));
        if(this.prey.size() <= 0)
            getServer().broadcastMessage(I18n.tl("annHuntersWon"));
        else
            getServer().broadcastMessage(I18n.tl("annPreyWon"));

        // Mark game as inactive
        gameActive = false;

        // Clear lists
        hunters.clear();
        prey.clear();

        // Put everyone into spectator
        Object[] players = getServer().getOnlinePlayers().toArray();
        for(Object playerObj : players){
            ((Player) playerObj).setGameMode(GameMode.SPECTATOR);
        }
    }

    private void resetPlayer(Player player){
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.setTotalExperience(0);
        player.setBedSpawnLocation(null);

        // Revoke all player advancements
        Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();
        while (advancements.hasNext()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancements.next());
            for (String s : progress.getAwardedCriteria()) {
                progress.revokeCriteria(s);
            }
        }
    }

    private Location randomizeLocation(Location loc, int radius){
        Random rnd = new Random();

        loc.setX(loc.getX() + ((rnd.nextDouble() * radius * 2) - radius));
        loc.setZ(loc.getZ() + ((rnd.nextDouble() * radius * 2) - radius));

        return findSafeElevation(loc);
    }

    private Location findSafeElevation(Location loc){
        // Make sure there's nothing to obstruct the player's breathing.
        Location above = loc.clone();
        above.setY(above.getY() + 1);
        while( !(loc.getBlock().getType() == Material.AIR && above.getBlock().getType() == Material.AIR) && loc.getY() < 255 ){
            loc.setY(loc.getY() + 1);
            above.setY(above.getY() + 1);
        }

        // Make sure there's some ground below the player. Wouldn't wanna fall off a cliff first thing the game starts!
        Location below = loc.clone();
        while(below.getBlock().getType() == Material.AIR && loc.getY() > 1){
            loc.setY(loc.getY() - 1);
            below.setY(loc.getY() - 1);
        }

        return loc;
    }

}
