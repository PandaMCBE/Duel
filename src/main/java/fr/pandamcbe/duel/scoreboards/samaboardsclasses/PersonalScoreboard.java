package fr.pandamcbe.duel.scoreboards.samaboardsclasses;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.duel.arenas.DuelArenaManager;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import fr.pandamcbe.mgapi.serverutilapi.scoreboards.ObjectiveSign;
import org.bukkit.entity.Player;

public class PersonalScoreboard extends fr.pandamcbe.mgapi.serverutilapi.scoreboards.PersonalScoreboard {

    public PersonalScoreboard(Player player){
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "§5§lDuels");
        reloadData();
        objectiveSign.addReceiver(player);
    }
    @Override
    public void reloadData() {
    }
    @Override
    public void setLines(String ip) {
        DuelArenaManager duelArenaManager = Duel.getInstance().arenaManager;

        DuelArena arena = (DuelArena) duelArenaManager.getArenaByPlayer(player.getUniqueId(), Duel.getInstance().getArenas.getArenaList());
        Player opponent = Duel.getInstance().getArenaManager().getOpponent(player.getUniqueId(), arena);
        StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getAccount(Duel.getInstance().mainMap, player.getUniqueId(), AllNames.statsPojo(AllNames.DUELS.getName()));
        Team pTeam = duelArenaManager.getTeamByPlayer(player, arena);
        Team oTeam = duelArenaManager.getTeamByPlayer(opponent, arena);


            if (!arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                this.objectiveSign.setLine(1, "§8§m----------------");
                this.objectiveSign.setLine(2, "§7Coins : §f" + statsAccount.getCoins());
                this.objectiveSign.setLine(3, "§7Elos : §f" + statsAccount.getElos());
                this.objectiveSign.setLine(4, "§7Kills : §f" + (statsAccount.getKills() + (statsAccount.getKillstreaks() * 5)));
                this.objectiveSign.setLine(5, "§7Arène : §f" + arena.getName());
                this.objectiveSign.setLine(6, "§8§m--------------");
                this.objectiveSign.setLine(7, ip);
            } else {
                this.objectiveSign.setLine(1, "§8§m----------------");
                this.objectiveSign.setLine(2, pTeam.getString() + " §7: §f" + pTeam.getPoints());
                this.objectiveSign.setLine(3, oTeam.getString() + " §7: §f" + oTeam.getPoints());
                this.objectiveSign.setLine(4, "§7Coins : §f" + statsAccount.getCoins());
                this.objectiveSign.setLine(5, "§7Elos : §f" + statsAccount.getElos());
                this.objectiveSign.setLine(6, "§7Kills : §f" + (statsAccount.getKills() + (statsAccount.getKillstreaks() * 5)));
                this.objectiveSign.setLine(7, "§7Arène : §f" + arena.getName());
                this.objectiveSign.setLine(8, "§8§m--------------");
                this.objectiveSign.setLine(9, ip);
            }

            this.objectiveSign.updateLines();

    }


//    String getRound(Arena arena, Player player){
//        if (!arena.getDuelMode().equalsIgnoreCase(DuelModesManager.MLGRUSH.getName())){
//            return "Aucun";
//        }else {
//
//            return arena.getPoints().get(player)+"§6/5";
//        }
//    }

}