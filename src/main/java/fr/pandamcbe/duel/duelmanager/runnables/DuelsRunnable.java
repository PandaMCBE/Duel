package fr.pandamcbe.duel.duelmanager.runnables;

import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.duel.duelmanager.GiveKitsUtils;
import fr.pandamcbe.duel.scoreboards.samaboardsclasses.PersonalScoreboard;
import fr.pandamcbe.mGConstructorAPI.managers.CountDownsManager;
import fr.pandamcbe.mGConstructorAPI.managers.GameSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DuelsRunnable extends BukkitRunnable {

    private int timer = 5;

    public int getTimer() {
        return timer;
    }

    private final DuelArena arena;
    private final Duel duel;
    private final GameSessionManager gameSessionManager = new GameSessionManager();
    public DuelsRunnable(DuelArena arena, Duel main){
        this.arena = arena;
        duel = main;
    }

    @Override
    public void run() {

            new CountDownsManager(arena, duel.getArenaManager()).runNewRound(this, timer, Sound.CLICK, Sound.NOTE_PLING);

//            if(timer != 0) Bukkit.getScheduler().runTask(Duel.getInstance(), ()->ArenaManager.Teleport(GameArena));
                if(timer == 5) {
                    gameSessionManager.teleport(arena);
                    for (UUID uuid : arena.getAlluuids()) {
                        Player p = Bukkit.getPlayer(uuid);
                        Duel.getInstance().getScoreboardManager().onLogin(new PersonalScoreboard(p), p);
                        gameSessionManager.clearStuffandHealth(p);
                        p.setGameMode(GameMode.SURVIVAL);
                        Duel.getInstance().frozens.add(p);
                        p.sendTitle("§6§lLancement dans:", "§c§l5");
                    }
                } else if (timer == 0) {
                    Duel.getInstance().getArenaManager().placingBedInTeamsLoc(arena);
                    for (UUID uuid : arena.getAlluuids()) {
                        Player p = Bukkit.getPlayer(uuid);
                        p.sendMessage("§b§lGO!!");
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                        new GiveKitsUtils(arena, p);
                        Duel.getInstance().frozens.remove(p);
                    }
                    System.out.println("Arena-Name : "+arena.getName());
                }

            timer--;
        }
}
