package pl.msrv.preyhunt;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final Preyhunt plugin;

    public PlayerListener(Preyhunt plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(!plugin.gameActive) return;
        if(e.getEntityType() != EntityType.PLAYER) return;

        Player player = e.getEntity();
        if(plugin.prey.contains(player)) {
            plugin.prey.remove(player);
            plugin.getServer().broadcastMessage(I18n.tl("annPreyDisqualified", player.getDisplayName(), plugin.prey.size()));

            player.setGameMode(GameMode.SPECTATOR);
        }

        if(plugin.prey.size() <= 0 || plugin.hunters.size() <= 0)
            plugin.endGame();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if(!plugin.gameActive) return;

        Player player = e.getPlayer();
        if(plugin.hunters.contains(player)) {
            ItemStack compass = new ItemStack(Material.COMPASS, 1);
            player.getInventory().addItem(compass);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntityType() == EntityType.ENDER_DRAGON)
            plugin.endGame();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();

        plugin.prey.remove(player);
        plugin.hunters.remove(player);

        if(plugin.gameActive) {
            if (plugin.prey.size() <= 0 || plugin.hunters.size() <= 0)
                plugin.endGame();
        }
    }

}
