import java.util.ListResourceBundle;

public class messages_pl extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                // Errors
                {"errGameActive", "§4Gra jest już w toku!"},
                {"errGameNotActive", "§4Gra jeszcze się nie zaczęła!"},
                {"errNoManagementPerms", "§4Nie jesteś uprawniony do zarządzania grą."},
                {"errCantStartTeamEmpty", "§4Gra wymaga przynajmniej 1 gracza w każdej drużynie!"},
                {"errPlayerOnlyCommand", "§4Tę komendę mogą wykonywać tylko gracze!"},
                {"errNotASpectator", "§4Nie jesteś widzem!"},
                {"errPlayerNotFound", "§4Nie znaleziono gracza %s!"},

                // Command usage messages
                {"generalUsage", "/mh [help|start|end|spec|join]"},
                {"usageSpec", "/mh spec <gracz>"},
                {"usageJoin", "/mh join <hunters|prey|spec>"},

                // Status command output
                {"statusGameActive", "§eGra jest §aaktualnie w toku."},
                {"statusGameNotActive", "§eGra §ajeszcze się nie zaczęła."},
                {"statusPrey", "§eOfiary (%d): %s"},
                {"statusHunters", "§eŁowcy (%d): %s"},

                // Announcements
                {"annPlayerJoinedPrey", "§e%s dołączył do łowów jako §cofiara§e!"},
                {"annPlayerJoinedHunters", "§e%s dołączył do łowów jako §cłowca§e!"},
                {"annPlayerJoinedSpecs", "§e%s dołączył do łowów jako §awidz§e!"},
                {"annPreyDisqualified", "§e%s został zdyskwalifikowany! Pozostało %d ofiar."},
                {"annCompassUpdated", "§eTwój kompas wskazuje teraz na %s."},
                {"annCompassNoPlayers", "§cNie znaleziono żadnych graczy w pobliżu."},
                {"annManhuntBegun", "§eŁowy rozpoczęły się!"},
                {"annManhuntEnded", "§eŁowy zakończyły się!"},
                {"annPreyWon", "§cOfiary §ewygrały!"},
                {"annHuntersWon", "§cŁowcy §ewygrali."},
                {"annYouArePrey", "§eJesteś §cofiarą§e."},
                {"annYouAreHunter", "§eJesteś §cłowcą§e."},
                {"annCompassUpdatesInHand", "§c!!! KOMPAS AKTUALIZUJE SIĘ JEDYNIE GDY JEST W RĘCE !!!"}


        };
    }

}
