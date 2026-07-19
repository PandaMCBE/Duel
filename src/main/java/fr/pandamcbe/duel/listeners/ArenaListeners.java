package fr.pandamcbe.duel.listeners;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.Arena;
import fr.pandamcbe.duel.arenas.DuelArenaManager;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;

import java.util.stream.Stream;

public class ArenaListeners implements Listener {

    DuelArenaManager arenaManager = Duel.getInstance().arenaManager;

    @EventHandler
    void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            //MLGRUSH
            Player player = (Player) event.getEntity();
            Arena arena = arenaManager.getArenaByPlayer(player);
            if (arena != null) {
                if (arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                    // Annule le changement de niveau de faim du joueur
                    event.setCancelled(true);
                    // Définit le niveau de faim du joueur à 20 (maximum)
                    player.setFoodLevel(20);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        //GLOBAL
        if (player.getGameMode() == GameMode.CREATIVE)
            return;
        if (Duel.getInstance().frozens.contains(player)) {
            // Empêche le mouvement en annulant l'event
            event.setTo(event.getFrom());
        }
        //MLGRUSH
        Arena arena = arenaManager.getArenaByPlayer(player);
        if (arena != null) {
            if (arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                Location location = player.getLocation();

                if (location.getY() <= 2) {
                    arenaManager.onDeathMLGRush(player, arena);
                }
            }
        }
    }

    @EventHandler
    void onBlockPlacing(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlockPlaced();
        Block block2 = event.getBlockAgainst();
        if (p.getGameMode() == GameMode.CREATIVE)
            return;
        Arena arena = arenaManager.getArenaByPlayer(p);
        arena.getBlocksPlaced().add(block);

        if (arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName()))
            if (block2.getType() != Material.SANDSTONE)
                event.setCancelled(true);

    }

    @EventHandler
    void onDestroyingBlock(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (p.getGameMode() == GameMode.CREATIVE)
            return;
        Arena arena = arenaManager.getArenaByPlayer(p);

        if (arena != null) {
            switch (arena.getDuelMode()) {
                case "BuildUHC":
                    break;
                case "MLGRush":
                    if (block.getType().equals(Material.BED_BLOCK)) {
                        if (arenaManager.red.contains(p.getUniqueId())) {
                            if (block.getLocation().equals(arena.getBlue_bed()) || block.getLocation().equals(arena.getBlue_bed().clone().add(0, 0, 1))) {
                                arena.nextRound(p);
                                System.out.println("BLUE BED!!");
                            } else {
                                event.setCancelled(true);
                                System.out.println("Breaking Blue Cancelled");
                            }
                        }
                        if (arenaManager.blue.contains(p.getUniqueId())) {
                            if (block.getLocation().equals(arena.getRed_bed()) || block.getLocation().equals(arena.getRed_bed().clone().add(0, 0, -1))) {

                                arena.nextRound(p);
                                System.out.println("RED BED!!");
                            } else {
                                System.out.println("Breaking Red Cancelled");
                                event.setCancelled(true);
                            }
                        }
                        Stream<Entity> beds = block.getWorld().getEntities().stream().filter(entity -> entity.isOnGround() && entity instanceof Bed);
                        beds.forEach(Entity::remove);
                    } else {
                        if (block.getType() == Material.SANDSTONE && block.getData() != 2 || block.getType() != Material.SANDSTONE)
                            event.setCancelled(true);
                    }
                    break;
                case "SG":
                        event.setCancelled(true);
                    break;
                default:
                    break;

            }
        }
    }
    @EventHandler
    void onSleepling(PlayerBedEnterEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void ProjectileHit(ProjectileHitEvent event){
        if (event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            arrow.remove();
        }
    }

    @EventHandler
    void onBlockUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (p.getGameMode() == GameMode.CREATIVE)
            return;

        ItemStack item = event.getItem();

        Arena arena = arenaManager.getArenaByPlayer(p);

        if (arena != null) {
            switch (arena.getDuelMode()) {
                case "BuildUHC":
                    break;
                case "SG":
                    if (item != null && item.getType() == Material.FLINT_AND_STEEL)
                        return;
                    if (block.getType() == Material.STAINED_CLAY || block.getType() == Material.MYCEL)
                        event.setCancelled(true);
                    break;
                default:
                    break;

            }
        }
    }

    //on annule le drop des items
    @EventHandler
    void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
     void onEntityDamage(EntityDamageEvent event) {
        // Vérifiez si les dégâts sont causés par une chute
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            // Annulez les dégâts de chute
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onWeatherChange (WeatherChangeEvent event){
        event.setCancelled(true);
    }


    @EventHandler
    void onDamage(EntityDamageByEntityEvent event){

       if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){

           Player victim = (Player) event.getEntity();
           Player damager = (Player) event.getDamager();

           Arena victimArena = arenaManager.getArenaByPlayer(victim);

           if(victimArena == null || !victimArena.getUUIDs().contains(damager.getUniqueId()))
           {
               event.setCancelled(true);
           }
       }

   }

    @EventHandler
    void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player Killer = victim.getKiller();
        Arena arena = arenaManager.getArenaByPlayer(Killer);

        User user1 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(victim);

            if (Killer != null) {
                User user2 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(Killer);

                if (victim != Killer) {
                    //GLOBAL
                    if (!arena.getDuelMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                            arena.eliminate(victim);
                            arena.checkWin(Killer);

                            StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getAccount(Duel.getInstance().mainMap, Killer.getUniqueId(), AllNames.statsPojo(AllNames.DUELS.getName()));
                            Main.getInstance().gameMechanics.applyKillStreak(statsAccount, Killer, "§a§lDuel > §l");

                            StatsAccount statsAccount2 = (StatsAccount) Main.getInstance().manipulation.getAccount(Duel.getInstance().mainMap, victim.getUniqueId(), AllNames.statsPojo(AllNames.DUELS.getName()));
                            if(statsAccount2.getKillstreaks() > 1) {
                            victim.sendMessage("§c§lVous avez perdu vos KillStreaks!");
                            statsAccount2.setKillstreaks(0);
                        }
                        //MLGRUSH
                    } else arenaManager.onDeathMLGRush(victim, arena);

                    Bukkit.broadcastMessage("§a§lDuel > §l" + user1.getCachedData().getMetaData().getPrefix() + " §l§r" + victim.getName() + "§6§l s'est fait retourné par §r§l" + user2.getCachedData().getMetaData().getPrefix() + " §l" + Killer.getName());
                }

            }

        e.getDrops().clear();
        victim.spigot().respawn();
    }
}