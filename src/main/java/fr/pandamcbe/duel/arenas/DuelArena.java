package fr.pandamcbe.duel.arenas;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.mGConstructorAPI.arenas.MGArena;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.arenas.Team;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.core.localmemory.MemAccountsManipulation;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DuelArena extends MGArena {

    //    private List<Location> locations;
    private String DuelMode;
    private Location red_bed;
    private Location blue_bed;

    public DuelArena(String DuelMode, Location MiddleSpawnLoc, String name, Integer totalTeams) {
//        this.locations = new ArrayList<>();
        this.DuelMode = DuelMode;
        this.name = name;
        this.red_bed = new Location(MiddleSpawnLoc.getWorld(), 0, 0, 0);
        this.blue_bed = new Location(MiddleSpawnLoc.getWorld(), 0, 0, 0);
        this.locByTeam = new HashMap<>();
        this.totalTeams = totalTeams;
        this.middlespawn = MiddleSpawnLoc;
        restart();
    }

//    public List<Location> getSpawns() {
//        return locations;
//    }


    public Location getRed_bed() {
        return red_bed;
    }

    public void setRed_bed(Location red_bed) {
        this.red_bed = red_bed;
    }

    public Location getBlue_bed() {
        return blue_bed;
    }

    public void setBlue_bed(Location blue_bed) {
        this.blue_bed = blue_bed;
    }

    public String getDuelMode() {
        return DuelMode;
    }

    String serverName = AllNames.DUELS.getName();
    MemAccountsManipulation memoryProvider = Main.getInstance().manipulation;
//MLGRUSH ------------>

    private void OnChangingRound(Arena arena, Team team) {
        DuelArenaManager arenaManager = Duel.getInstance().getArenaManager();

        for(UUID uuid : team.getUuidList()) {
            Player winner = Bukkit.getPlayer(uuid);
            winner.sendMessage("§7§l+ §d§l1 elo !");
            winner.sendMessage("§7§l+ §6§l5 coins !");
            for (Entity entity : arena.getMiddlespawn().getWorld().getNearbyEntities(winner.getLocation(), 20, -10, 20)) {
                if (entity instanceof Item)
                    entity.remove();
            }
        }
        arenaManager.breakingPlacedBlocks(arena);

    }

    public void ifLastRound(Arena arena, DuelArenaManager arenaManager) {
        arenaManager.placingBedInTeamsLoc((DuelArena) arena);
        ifLastRoundCoreTeam(arena, arenaManager, Duel.getInstance().getArenas.getArenaList());

    }

    @Override
    public void nextMGRound(Arena arena, Object o) {
        Team team = (Team) o;
        nextRoundTeam(Duel.getInstance().getArenas.getArenaList(), team, Duel.getInstance().mainMap, 5, 1);
        OnChangingRound(arena, team);
    }

    @Override
    public void checkMGwin(Object o) {
        Team team = (Team) o;
        DuelArenaManager arenaManager = Duel.getInstance().arenaManager;
        DuelArena arena = (DuelArena) arenaManager.getArenaByPlayer(team.getUuidList().getFirst(), Duel.getInstance().getArenas.getArenaList());
        if (arena.getAlluuids().size() == 2) {
            for(UUID winnerUUID : team.getUuidList()) {
                Player winner = Bukkit.getPlayer(winnerUUID);

                StatsAccount statsAccount = (StatsAccount) memoryProvider.getAccount(Duel.getInstance().mainMap, winner.getUniqueId(), AllNames.statsPojo(serverName));
                statsAccount.setCoins(statsAccount.getCoins() + 10);
                if (arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                    statsAccount.setWins(statsAccount.getWins() + 1);
                } else statsAccount.setKills(statsAccount.getKills() + 1);
                statsAccount.setElos(statsAccount.getElos() + 2);

                User user = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(winner);
                winner.sendMessage("§7§l+ §d§l2 elos !");
                winner.sendMessage("§7§l+ §b§l1 win !");
                winner.sendMessage("§7§l+ §6§l10 coins !");

            }
            Bukkit.broadcastMessage("§a§lDuel > §l" + " §l§r" + team.getString() + " §b§lRemporte le duel ! §6§lGG");

            for (UUID uuid : arena.getAlluuids()) {
                Player pls = Bukkit.getPlayer(uuid);
                pls.sendMessage("§a§lDuel > §l§3Une nouvelle partie va débuter dans 10s !");
            }
            checkWinCoreTeam(team, Duel.getInstance().waitingPlayers, Duel.getInstance().getArenas.getArenaList());
        }
    }
    
    public void eliminate(Player victim, List<Arena> list) {
        DuelArena victimArena = (DuelArena) Duel.getInstance().getArenaManager().getArenaByPlayer(victim.getUniqueId(), list);
        Team team = Duel.getInstance().arenaManager.getTeamByPlayer(victim, victimArena);
        StatsAccount statsAccount = (StatsAccount) memoryProvider.getAccount(Duel.getInstance().mainMap, victim.getUniqueId(), AllNames.statsPojo(serverName));
        statsAccount.setElos(statsAccount.getElos() - 1);
        victim.sendMessage("§cVous avez perdu ! Gros Noob !");
        victim.sendMessage("§7§l- §d§l1 elo !");
        eliminateCoreTeam(team, list);
    }

}
