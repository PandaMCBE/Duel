package fr.pandamcbe.duel.arenas;

import fr.pandamcbe.commons.ProfilAccount;
import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.arenas.TeamArena;
import fr.pandamcbe.mGConstructorAPI.managers.GameSessionManager;
import fr.pandamcbe.mGConstructorAPI.managers.WaitingRoom;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.core.localmemory.DirtyManager;
import fr.pandamcbe.mgapi.core.localmemory.MemAccountsManipulation;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AccountType;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DuelArena extends TeamArena {

    public DuelArena(String DuelMode, Location MiddleSpawnLoc, String name, Integer maxPlayers, Integer totalTeams) {
//        this.locations = new ArrayList<>();
        this.mode = DuelMode;
        this.name = name;
        this.spawnByTeam = new HashMap<>();
        this.goalByTeam = new HashMap<>();
        this.maxPlayersAllowed = maxPlayers;
        this.totalTeams = totalTeams;
        this.middlespawn = MiddleSpawnLoc;
        this.waitingRoom = new WaitingRoom();
        this.teams = new ArrayList<>();
        restart();
    }

    private final String serverName = AllNames.DUELS.getName();
    private final GameSessionManager gameSessionManager = MGConstructorAPI.getInstance().getGameManager();
    private final Team team = MGConstructorAPI.getInstance().getTeam();
    private final DirtyManager dirtyManager = new DirtyManager();
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
        arenaManager.breakOrPlace(arena);

    }

    public void nextRound(TeamArena teamArena, Team team) {
        OnChangingRound(teamArena, team);
        gameSessionManager.nextRoundTeam(Duel.getInstance().getArenas.getArenaList(), team, Duel.getInstance().mainMap, 5, 1);
        Duel.getInstance().getArenaManager().placingBedInTeamsLoc((DuelArena) teamArena);
    }

    public void customWinEvent(Object o) {
        Team team = (Team) o;
        DuelArena arena = (DuelArena) MGConstructorAPI.getInstance().getArenaManager().getArenaByPlayer(team.getUuidList().getFirst(), Duel.getInstance().getArenas.getArenaList());
        if (arena.getAlluuids().size() == 2) {
            for(UUID winnerUUID : team.getUuidList()) {
                Player winner = Bukkit.getPlayer(winnerUUID);

                StatsAccount statsAccount = (StatsAccount) memoryProvider.getStatsAccount(Duel.getInstance().mainMap, winnerUUID, AccountType.statsKey(serverName));
                ProfilAccount profilAccount = (ProfilAccount) memoryProvider.getProfilAccount(Duel.getInstance().mainMap, winnerUUID, AccountType.PROFIL.getKeyPrefix());
                profilAccount.setCoins(profilAccount.getCoins() + 10);
                if (arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                    statsAccount.setWins(statsAccount.getWins() + 1);
                } else statsAccount.setKills(statsAccount.getKills() + 1);
                statsAccount.setElos(statsAccount.getElos() + 2);
                dirtyManager.markDirty(winnerUUID, AccountType.statsKey(serverName),statsAccount);
                dirtyManager.markDirty(winnerUUID, AccountType.PROFIL.getKeyPrefix(), profilAccount);
                User user = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(winner);
                winner.sendMessage("§7§l+ §d§l2 elos !");
                winner.sendMessage("§7§l+ §b§l1 win !");
                winner.sendMessage("§7§l+ §6§l10 coins !");
            }

            for (UUID uuid : arena.getAlluuids()) {
                Player pls = Bukkit.getPlayer(uuid);
                pls.sendMessage("§a§lDuel > §l" + " §l§r" + team.getString() + " §b§lRemporte le duel ! §6§lGG");
//                pls.sendMessage("§a§lDuel > §l§3Une nouvelle partie va débuter dans 10s !");
            }
            gameSessionManager.checkWinCoreTeam(team, Duel.getInstance().getArenas.getArenaList());
        }
    }
    
    public void eliminate(Player victim, List<Arena> list) {
        StatsAccount statsAccount = (StatsAccount) memoryProvider.getStatsAccount(Duel.getInstance().mainMap, victim.getUniqueId(), AccountType.statsKey(serverName));
        statsAccount.setElos(statsAccount.getElos() - 1);
        dirtyManager.markDirty(victim.getUniqueId(), AccountType.statsKey(serverName), statsAccount);
        victim.sendMessage("§7§l- §d§l1 elo !");
        gameSessionManager.eliminateTeam(victim.getUniqueId(), list);
    }
}
