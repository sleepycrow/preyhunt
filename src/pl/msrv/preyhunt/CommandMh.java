package pl.msrv.preyhunt;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMh implements CommandExecutor {

    private final Preyhunt plugin;

    public CommandMh(Preyhunt plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(args.length == 0){
            return statusCommand(sender, args);
        }else{

            if(args[0].equalsIgnoreCase("start"))
                return startCommand(sender, args);
            else if(args[0].equalsIgnoreCase("end"))
                return endCommand(sender, args);
            else if(args[0].equalsIgnoreCase("spec"))
                return specCommand(sender, args);
            else if(args[0].equalsIgnoreCase("join"))
                return joinCommand(sender, args);

        }

        sender.sendMessage(I18n.tl("generalUsage"));
        return true;

    }

    private boolean statusCommand(CommandSender sender, String[] args){
        if(plugin.gameActive)
            sender.sendMessage(I18n.tl("statusGameActive"));
        else
            sender.sendMessage(I18n.tl("statusGameNotActive"));

        StringBuilder sb;

        sb = new StringBuilder();
        for(Player prey : plugin.prey) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(prey.getDisplayName());
        }
        sender.sendMessage(I18n.tl("statusPrey", plugin.prey.size(), sb.toString()));

        sb = new StringBuilder();
        for(Player hunter : plugin.hunters) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(hunter.getDisplayName());
        }
        sender.sendMessage(I18n.tl("statusHunters", plugin.hunters.size(), sb.toString()));

        return true;
    }

    private boolean startCommand(CommandSender sender, String[] args){
        if(!sender.hasPermission("preyhunt.manage") && !sender.isOp()){
            sender.sendMessage(I18n.tl("errNoManagementPerms"));
            return true;
        }

        if(plugin.gameActive){
            sender.sendMessage(I18n.tl("errGameActive"));
            return true;
        }

        if(plugin.hunters.size() <= 0 || plugin.prey.size() <= 0){
            sender.sendMessage(I18n.tl("errCantStartTeamEmpty"));
            return true;
        }

        plugin.startGame();
        return true;
    }

    private boolean endCommand(CommandSender sender, String[] args){
        if(!sender.hasPermission("preyhunt.manage") && !sender.isOp()){
            sender.sendMessage(I18n.tl("errNoManagementPerms"));
            return true;
        }

        if(!plugin.gameActive){
            sender.sendMessage(I18n.tl("errGameNotActive"));
            return true;
        }

        plugin.endGame();
        return true;
    }

    private boolean specCommand(CommandSender sender, String[] args){
        // This command can only be run by players
        if(!(sender instanceof Player)){
            sender.sendMessage(I18n.tl("errPlayerOnlyCommand"));
            return true;
        }

        Player player = (Player) sender;

        if(!plugin.gameActive){
            sender.sendMessage(I18n.tl("errGameNotActive"));
            return true;
        }

        if(player.getGameMode() != GameMode.SPECTATOR){
            sender.sendMessage(I18n.tl("errNotASpectator"));
            return true;
        }

        if(args.length < 2){
            sender.sendMessage(I18n.tl("usageSpec"));
            return true;
        }

        Player specTarget = plugin.getServer().getPlayer(args[1]);
        if(specTarget != null){
            player.teleport(specTarget.getLocation());
        }else{
            sender.sendMessage(I18n.tl("errPlayerNotFound", args[1]));
        }

        return true;
    }

    private boolean joinCommand(CommandSender sender, String[] args){
        // This command can only be run by players
        if(!(sender instanceof Player)){
            sender.sendMessage(I18n.tl("errPlayerOnlyCommand"));
            return true;
        }

        if(plugin.gameActive){
            sender.sendMessage(I18n.tl("errGameActive"));
            return true;
        }

        Player player = (Player) sender;

        if(args.length >= 2){
            if(args[1].equalsIgnoreCase("hunters")){
                plugin.prey.remove(player);
                plugin.hunters.add(player);
                plugin.getServer().broadcastMessage(I18n.tl("annPlayerJoinedHunters", player.getDisplayName()));
                return true;
            }else if(args[1].equalsIgnoreCase("prey")){
                plugin.hunters.remove(player);
                plugin.prey.add(player);
                plugin.getServer().broadcastMessage(I18n.tl("annPlayerJoinedPrey", player.getDisplayName()));
                return true;
            }
        }

        sender.sendMessage(I18n.tl("usageJoin"));
        return true;
    }

}
