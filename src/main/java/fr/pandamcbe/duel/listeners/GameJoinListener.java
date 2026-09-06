package fr.pandamcbe.duel.listeners;

import fr.pandamcbe.commons.DuelAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.core.localmemory.DirtyManager;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();
         //Association du joueur à son scoreboard
        event.setJoinMessage(null); //Annulation du message par défaut
        p.setGameMode(GameMode.SPECTATOR);

        Duel.getInstance().arenaManager.customJoinEvent(p); //Appel de la méthode onPlayersJoin de ArenaManager sur le joueur qui se connecte

    }

    @EventHandler
    void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null); //Annulation du message par défaut
        Player leaver = event.getPlayer();
        DuelArena arena = (DuelArena) MGConstructorAPI.getInstance().getArenaManager().getArenaByPlayer(leaver.getUniqueId(), Duel.getInstance().getArenas.getArenaList()); //Récupération de l'arène où se trouve le joueur qui quitte
        Duel.getInstance().getArenaManager().playerQuits(arena, leaver); //Appel de la méthode qui va gérer la déconnexion du joueur
        DuelAccount duelAccount = (DuelAccount) Main.getInstance().manipulation.getAccountFromMemory(Duel.getInstance().mainMap, leaver.getUniqueId(), DuelAccount.class.getName());
        Main.getInstance().getProxyMessaging().connectToAnotherServer(AllNames.LOBBY.getName(), Bukkit.getPlayer(Duel.getInstance().arenaManager.findOpponent(duelAccount, leaver)));
        // pour l'arène ou le reste des joueurs s'y trouvant

        new DirtyManager().forceSave(Duel.getInstance().mainMap, leaver.getUniqueId());
        Main.getInstance().manipulation.deleteAccount(leaver.getUniqueId(), Duel.getInstance().mainMap);
    }
}