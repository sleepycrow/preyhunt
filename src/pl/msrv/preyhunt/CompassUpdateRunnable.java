package pl.msrv.preyhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassUpdateRunnable extends BukkitRunnable {

    private final Preyhunt plugin;

    public CompassUpdateRunnable(Preyhunt plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Cancel self if no game is active
        if(!plugin.gameActive){
            cancel();
            return;
        }

        // Update every hunter's compass
        for(Player hunter : plugin.hunters) {
            boolean isInOffHand;
            ItemStack compass;

            // Try to find a compass in either of the hunter's hands. Omit this player if none is found.
            if(hunter.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)){
                isInOffHand = true;
                compass = hunter.getInventory().getItemInOffHand();
            }else if(hunter.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)){
                isInOffHand = false;
                compass = hunter.getInventory().getItemInMainHand();
            }else{
                continue;
            }

            // Find the location of the prey closest to the hunter
            Player nearestPrey = null;
            double nearestDistance = -1f;
            for(Player prey : plugin.prey){
                if(!prey.getWorld().equals(hunter.getWorld())) continue;

                if(nearestDistance < 0f ||
                        prey.getLocation().distance(hunter.getLocation()) < nearestDistance) {
                    nearestPrey = prey;
                    nearestDistance = prey.getLocation().distance(hunter.getLocation());
                }
            }

            // Update the compass to point at the prey
            if(nearestPrey != null) {
                // Set compass target
                hunter.setCompassTarget(nearestPrey.getLocation());

                // Inform hunter
                hunter.sendMessage(I18n.tl("annCompassUpdated", nearestPrey.getDisplayName()));

                // Make the compass' name the nearest player's name
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
                if(!compassMeta.getDisplayName().equalsIgnoreCase(nearestPrey.getDisplayName())){
                    compassMeta.setDisplayName(nearestPrey.getDisplayName());
                    compass.setItemMeta(compassMeta);

                    // Hand the compass back
                    if(isInOffHand) hunter.getInventory().setItemInOffHand(compass);
                    else hunter.getInventory().setItemInMainHand(compass);
                }
            }else{
                hunter.sendMessage(I18n.tl("annCompassNoPlayers"));
            }
        }
    }

}
