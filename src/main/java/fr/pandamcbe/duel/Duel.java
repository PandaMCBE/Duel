package fr.pandamcbe.duel;


import fr.pandamcbe.duel.arenas.DuelArenaManager;
import fr.pandamcbe.duel.arenas.getArenas;
import fr.pandamcbe.duel.listeners.ArenaListeners;
import fr.pandamcbe.duel.listeners.GameJoinListener;
import fr.pandamcbe.duel.rediscommunication.RedisSubCatch;
import fr.pandamcbe.mgapi.serverutilapi.scoreboards.ScoreboardManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Duel extends JavaPlugin {
    private static Duel instance;
    public DuelArenaManager arenaManager;
    public LuckPerms luckPerms;
    public getArenas getArenas;
//    public List<Arena> SGarenas = new ArrayList<>();public List<Arena> BUHCarenas = new ArrayList<>();public List<Arena> SUMOarenas = new ArrayList<>();public List<Arena> MLGRarenas = new ArrayList<>();
    public Set<Player> frozens = new HashSet<>();
    public Map<UUID, Map<String, Object>> mainMap;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;
    private ScoreboardManager scoreboardManager;
    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
    public void onEnable() {
        instance = this;
        System.out.println("§2[DEBUG] Duel chargé !");

        getArenas = new getArenas();
        arenaManager = new DuelArenaManager();

        getServer().getPluginManager().registerEvents(new ArenaListeners(), this);
        getServer().getPluginManager().registerEvents(new GameJoinListener(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().setIdleTimeout(180);
        getServer().setDefaultGameMode(GameMode.SURVIVAL);

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);
        this.scoreboardManager = new ScoreboardManager();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }

        mainMap = new HashMap<>();

        new RedisSubCatch().subChannels();


    }

    public void onDisable(){
        this.scoreboardManager.onDisable();
    }

    public static Duel getInstance() {
        return instance;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

   public DuelArenaManager getArenaManager() {
        return this.arenaManager;
    }
}