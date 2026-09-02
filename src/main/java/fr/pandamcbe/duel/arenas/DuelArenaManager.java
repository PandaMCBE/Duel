package fr.pandamcbe.duel.arenas;

import fr.pandamcbe.commons.DuelAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.duelmanager.GiveKitsUtils;
import fr.pandamcbe.duel.duelmanager.runnables.DuelsRunnable;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.managers.ArenaManager;
import fr.pandamcbe.mGConstructorAPI.managers.DuelPlayerManager;
import fr.pandamcbe.mGConstructorAPI.managers.GameSessionManager;
import fr.pandamcbe.mgapi.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.List;

public class DuelArenaManager extends DuelPlayerManager {
    //La classe est appelée dans l'onEnable donc tout ce qui est hors des méthodes est exécuté au démarrage du plugin

    private final Team team = MGConstructorAPI.getInstance().getTeam();
    private final GameSessionManager gameSessionManager = MGConstructorAPI.getInstance().getGameManager();
    private final ArenaManager arenaManager = MGConstructorAPI.getInstance().getArenaManager();


    public void customJoinEvent(Player player){
        gameSessionManager.clearStuffandHealth(player);
        List<Arena> arenaList = Duel.getInstance().getArenas.getArenaList();
        DuelAccount duelAccount = (DuelAccount) Main.getInstance().manipulation.getAccountFromMemory(Duel.getInstance().mainMap, player.getUniqueId(), DuelAccount.class.getName());
        if(duelAccount != null) {
            System.out.println(duelAccount.getPlayer1UUID());
            arenaManager.lookForArena(arenaList, duelAccount.getGamemode(), Bukkit.getPlayer(duelAccount.getPlayer1UUID()));
            System.out.println(duelAccount.getPlayer2UUID());
            arenaManager.lookForArena(arenaList, duelAccount.getGamemode(), Bukkit.getPlayer(duelAccount.getPlayer2UUID()));
        }
    }

    @Override
    public void startCountDown(Arena arena) {
        arena.getAlluuids().forEach(uuid -> arena.getWonRounds().put(uuid, 0));
        DuelsRunnable start = new DuelsRunnable((DuelArena) arena, Duel.getInstance());
        start.runTaskTimer(Duel.getInstance(), 0, 20);
    }

    @Override
    public String getPrefix(){
        return "§a§lDuel";
    }

    //MLGRUSH

    public void placingBedInTeamsLoc(DuelArena arena){
        if(arena.getGoalByTeam().get(ChatColor.BLUE).getBlock() != null) {
            Block blue_bed = arena.goalByTeam.get(ChatColor.BLUE).getBlock();
            BlockState bedFoot = blue_bed.getRelative(blue_bed.getFace(blue_bed)).getState();
            BlockState bedHead = bedFoot.getBlock().getRelative(BlockFace.SOUTH).getState();
            bedFoot.setType(Material.BED_BLOCK);
            bedHead.setType(Material.BED_BLOCK);
            bedFoot.setRawData((byte) 0x0);
            bedHead.setRawData((byte) 0x8);
            bedFoot.update(true, false);
            bedHead.update(true, true);
        }

//        BlockState bedFoot2 = arena.getRed_bed().getBlock().getRelative(arena.getRed_bed().getBlock().getFace(arena.getRed_bed().getBlock())).getState();
//        BlockState bedHead2 = bedFoot.getBlock().getRelative(BlockFace.SOUTH).getState();
        if(arena.getGoalByTeam().get(ChatColor.RED).getBlock() != null) {
            Block red_bed = arena.goalByTeam.get(ChatColor.RED).getBlock();
            BlockState bedHead2 = red_bed.getRelative(red_bed.getFace(red_bed)).getState();
            BlockState bedFoot2 = bedHead2.getBlock().getRelative(BlockFace.NORTH).getState();
            bedFoot2.setType(Material.BED_BLOCK);
            bedHead2.setType(Material.BED_BLOCK);
            bedFoot2.setRawData((byte) 0x0);
            bedHead2.setRawData((byte) 0x8);
            bedFoot2.update(true, false);
            bedHead2.update(true, true);
        }
    }

    //---------------------------<MLGRUSH

    public void respawning(Arena arena, Player player){
        DuelArena duelArena = (DuelArena) arena;
        if (team.getSpecificTeam(duelArena, ChatColor.RED).getUuidList().contains(player.getUniqueId())) {
            player.teleport(duelArena.getSpawnByTeam().get(ChatColor.RED));
        }
        if (team.getSpecificTeam(duelArena, ChatColor.BLUE).getUuidList().contains(player.getUniqueId())) {
            player.teleport(duelArena.getSpawnByTeam().get(ChatColor.BLUE));
        }

        new GiveKitsUtils(duelArena, player);
    }

    public void playerQuits(Arena arena, Player player){
        List<Arena> arenaList = Duel.getInstance().getArenas.getArenaList();
        whenPlayerDisconnects(player, arena, arenaList);
    }

    public void breakOrPlace(Arena arena) {
        MGConstructorAPI.getInstance().getArenaManager().breakingPlacedBlocks(arena);
    }
}