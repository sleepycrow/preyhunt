import java.util.ListResourceBundle;

public class messages extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                // Errors
                {"errGameActive", "§4The game is already in progress!"},
                {"errGameNotActive", "§4The game has not started yet!"},
                {"errNoManagementPerms", "§4You do not have permission to manage the game."},
                {"errCantStartTeamEmpty", "§4The game needs at least one player in each team!"},
                {"errPlayerOnlyCommand", "§4This command can only be run by players!"},
                {"errNotASpectator", "§4You are not a spectator!"},
                {"errPlayerNotFound", "§4Player %s could not be found!"},

                // Command usage messages
                {"generalUsage", "/mh [help|start|end|spec|join]"},
                {"usageSpec", "/mh spec <player>"},
                {"usageJoin", "/mh join <hunters|prey>"},

                // Status command output
                {"statusGameActive", "§eThe game is §acurrently in progress."},
                {"statusGameNotActive", "§eThe game has §cyet to start."},
                {"statusPrey", "§ePrey (%d): %s"},
                {"statusHunters", "§eHunters (%d): %s"},

                // Announcements
                {"annPlayerJoinedPrey", "§e%s joined the manhunt as a §cprey§e!"},
                {"annPlayerJoinedHunters", "§e%s joined the manhunt as a §chunter§e!"},
                {"annPreyDisqualified", "§e%s has been disqualified! There are %d prey remaining."},
                {"annCompassUpdated", "§eYour compass is now pointing at %s."},
                {"annCompassNoPlayers", "§cNo players could be found nearby."},
                {"annManhuntBegun", "§eThe hunt begins!"},
                {"annManhuntEnded", "§eThe hunt ends."},
                {"annPreyWon", "§eThe §cprey §ehave won!"},
                {"annHuntersWon", "§eThe §chunters §ehave won."},
                {"annYouArePrey", "§eYou are §cprey§e."},
                {"annYouAreHunter", "§eYou are a §chunter§e."},
                {"annCompassUpdatesInHand", "§c!!! THE COMPASS ONLY UPDATES WHEN HELD !!!"}

        };
    }

}
