package fr.pandamcbe.duel.arenas;

import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.duelmanager.GiveKitsUtils;
import fr.pandamcbe.duel.duelmanager.runnables.DuelsRunnable;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.arenas.State;
import fr.pandamcbe.mGConstructorAPI.arenas.Team;
import fr.pandamcbe.mGConstructorAPI.managers.ArenaManager;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DuelArenaManager extends ArenaManager {
    //La classe est appelée dans l'onEnable donc tout ce qui est hors des méthodes est exécuté au démarrage du plugin

    @Override
    public void onPlayersJoin(List<UUID> list, List<Arena> list1, Player p, String s) {

    }



    public void JoinArena(UUID uuid, DuelArena nextArena){
        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().clear();
        if (nextArena != null) {
                nextArena.getInGamePlayers().clear();
                if (!nextArena.getAlluuids().contains(uuid)) {
                    nextArena.getAlluuids().add(uuid);
                    System.out.println("ListPlayer: " + nextArena.getAlluuids().size() + " : " + nextArena.getAlluuids());
                }
            Team team = getNextTeam(nextArena, nextArena.getTotalTeams());
            team.getUuidList().add(uuid);

                if (!nextArena.isStarted() && nextArena.getAlluuids().size() == 2) {
                    Teleport(nextArena);
                    DuelsRunnable start = new DuelsRunnable(nextArena, Duel.getInstance());
                    nextArena.setState(State.PLAYING);
                    start.runTaskTimer(Duel.getInstance(), 0, 20);
                }
                nextArena.getWonRounds().put(uuid, 0);
        } else {
            Bukkit.getPlayer(uuid).sendMessage("§b§lMG-Land > §c§lAucune arène n'est disponible :( Revenez plus-tard !");
        }
    }
    //MLGRUSH


    public void placingBedInTeamsLoc(DuelArena arena){
        System.out.println("Blue bed: "+ arena.getBlue_bed() + " Red bed: "+ arena.getRed_bed());
        if(arena.getRed_bed().getBlock() != null) {
            BlockState bedFoot = arena.getBlue_bed().getBlock().getRelative(arena.getBlue_bed().getBlock().getFace(arena.getBlue_bed().getBlock())).getState();
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
        if(arena.getBlue_bed().getBlock() != null) {
            BlockState bedHead2 = arena.getRed_bed().getBlock().getRelative(arena.getRed_bed().getBlock().getFace(arena.getRed_bed().getBlock())).getState();
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

    public DuelArena getNextArena(String playersDuelMode) { //Récupérer une arène non démarrée
        for (Arena arena : Duel.getInstance().getArenas.getArenaList()) { //Pour chaque objet Arena qui sont dans la liste définie de toutes les arènes existantes
            DuelArena duelArena = (DuelArena) arena;
            if(duelArena.getDuelMode().equalsIgnoreCase(playersDuelMode)) { //Si le nom du mode de duel de l'arène correspond à celui que le joueur est en train de jouer
                if (!duelArena.isStarted() && duelArena.getAlluuids().isEmpty()) { //Si l'arène n'est pas démarrée
                    return duelArena; //retourner l'arène une arène non démarrée qui correspond au mode de duel du joueur
                }
            }
        }
        return null; //retourner null par défaut si aucune arène n'est disponible
    }

    @Override
    public void onDeath(List<UUID> list, List<Arena> list1, Arena arena, GameMode mode, int i, int i1, Player player) {
        DuelArena duelArena = (DuelArena) arena;
        if (duelArena != null) {
            player.teleport(arena.getMiddlespawn());
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();

            new BukkitRunnable() {
                int timer = i;

                @Override
                public void run() {

                    if(!player.isOnline()){
                        cancel();
                        Duel.getInstance().arenaManager.whenPlayerDisconnects(player, duelArena, list1, list);
                        return;
                    }
                    if (timer >= 1) {
                        player.sendMessage("§cRespawn dans :" + timer);

                    } else {

                        if (getSpecificTeam(duelArena, ChatColor.RED).getUuidList().contains(player.getUniqueId())) {
                            player.teleport(duelArena.getLocByTeam().get(ChatColor.RED));
                        }
                        if (getSpecificTeam(duelArena, ChatColor.BLUE).getUuidList().contains(player.getUniqueId())) {
                            player.teleport(duelArena.getLocByTeam().get(ChatColor.BLUE));
                        }
                        player.setGameMode(mode);
                        new GiveKitsUtils(duelArena, player);

                        cancel();
                    }
                    timer--;
                }

            }.runTaskTimer(Duel.getInstance(), 20L, 20L);
        } else System.out.println("arena est null");
    }

    @Override
    public void whenPlayerDisconnects(Player leaver, Arena arena, List<Arena> list, List<UUID> list1) {
        DuelArena duelArena = (DuelArena) arena;
        Duel.getInstance().getScoreboardManager().onLogout(leaver);
        if (duelArena != null) {
            duelArena.getAlluuids().remove(leaver.getUniqueId());
            duelArena.getInGamePlayers().remove(leaver.getUniqueId());
            if (duelArena.getAlluuids().size() == 1) {
                UUID winnerUUID = duelArena.getAlluuids().get(0);
                Player winner = Bukkit.getPlayer(winnerUUID);
                if (winner.isOnline()) {
                    Team team = getTeamByPlayer(winner, duelArena);
                    team.getUuidList().remove(winnerUUID);
                    ByteArrayOutputStream a = new ByteArrayOutputStream();
                    DataOutputStream out3 = new DataOutputStream(a);
                    try {
                        out3.writeUTF("Connect");
                        out3.writeUTF("Lobby");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    winner.sendPluginMessage(Duel.getInstance(), "BungeeCord", a.toByteArray());

                }
                breakingPlacedBlocks(duelArena);
                getTeamByPlayer(leaver, duelArena).getUuidList().remove(leaver.getUniqueId());
                if (duelArena.isStarted()) {
                    duelArena.restart();
                }
            }
        }
    }
}