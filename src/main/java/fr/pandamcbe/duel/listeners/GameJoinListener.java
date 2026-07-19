package fr.pandamcbe.duel.listeners;

import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.mgapi.Main;
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

            p.getInventory().setArmorContents(null); //nettoyer le contenu des slots d'armure
            //on régéne le poto
            p.setHealth(20.0);
//                    p.setGameMode(GameMode.SPECTATOR);
//                    p.sendMessage("Vous en êtes spectateur. ");
//                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

            Duel.getInstance().arenaManager.onPlayersJoin(Duel.getInstance().waitingPlayers, Duel.getInstance().getArenas.getArenaList(), p, "§a§lDuel"); //Appel de la méthode onPlayersJoin de ArenaManager sur le joueur qui se connecte

    }

    @EventHandler
    void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null); //Annulation du message par défaut
        Player leaver = event.getPlayer();
        DuelArena arena = (DuelArena) Duel.getInstance().getArenaManager().getArenaByPlayer(leaver.getUniqueId(), Duel.getInstance().getArenas.getArenaList()); //Récupération de l'arène où se trouve le joueur qui quitte
        Duel.getInstance().getArenaManager().whenPlayerDisconnects(leaver, arena, Duel.getInstance().getArenas.getArenaList(), Duel.getInstance().waitingPlayers); //Appel de la méthode qui va gérer la déconnexion du joueur
        // pour l'arène ou le reste des joueurs s'y trouvant

        Main.getInstance().redisPbSb.saveDatasToCache(leaver.getUniqueId(), Duel.getInstance().mainMap);
        Main.getInstance().manipulation.deleteAccount(leaver.getUniqueId(), Duel.getInstance().mainMap);

    }
}