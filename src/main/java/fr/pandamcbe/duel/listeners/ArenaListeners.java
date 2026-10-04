package fr.pandamcbe.duel.listeners;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.duel.arenas.DuelArenaManager;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.managers.GameSessionManager;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AccountType;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import net.luckperms.api.model.user.User;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaListeners implements Listener {

    private final DuelArenaManager arenaManager = Duel.getInstance().getArenaManager();
    private final GameSessionManager gameSessionManager = MGConstructorAPI.getInstance().getGameManager();
    public List<Arena> arenaList = Duel.getInstance().getArenas.getArenaList();
    private final Team team = new Team();
    private final Set<Location> recentBreaks = new HashSet<>();


    @EventHandler
    void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            //MLGRUSH
            Player player = (Player) event.getEntity();
            Arena arena = arenaManager.getArenaByPlayer(player.getUniqueId(), arenaList);
            if (arena != null) {
                if (arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
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
        Arena arena = arenaManager.getArenaByPlayer(player.getUniqueId(), arenaList);
        if (arena != null) {
            if (arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                Location location = player.getLocation();

                if (location.getY() <= 2 && player.getGameMode() != GameMode.SPECTATOR) {
                    gameSessionManager.onDeath(arenaManager, arena, GameMode.SURVIVAL, 2, 0, player);
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
        Arena arena = arenaManager.getArenaByPlayer(p.getUniqueId(), arenaList);
        arena.getBlocks().put(block.getLocation(), block.getType());

        if (arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName()))
            if (block2.getType() != Material.SANDSTONE)
                event.setCancelled(true);

    }

    @EventHandler
    void onDestroyingBlock(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (p.getGameMode() == GameMode.CREATIVE)
            return;
        event.setCancelled(true);
        DuelArena arena = (DuelArena) arenaManager.getArenaByPlayer(p.getUniqueId(), arenaList);
        Team playerTeam = team.getTeamByPlayer(p, arena);
        AllNames allNames = AllNames.fromName(arena.getMode());
        if(allNames != null) {
            switch (allNames) {
                case BUILDUHC:
                    break;
                case MLGRUSH:
                    if (block.getType().equals(Material.BED_BLOCK)) {
                        if (team.getTeamByPlayer(p, arena).getColor().equals(ChatColor.BLUE)) {
//                            System.out.println("Block destroyed : "+block.getLocation() + " | goal block : "+ arena.getGoalByTeam().get(ChatColor.RED));

                            if (block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.RED)) || block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.RED).clone().add(0, 0, -1))) {
                                arena.nextRound(arena, playerTeam);
                            } else {
                                event.setCancelled(true);
                            }
                        }
                        if (team.getTeamByPlayer(p, arena).getColor().equals(ChatColor.RED)) {

                            if (block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.BLUE)) || block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.BLUE).clone().add(0, 0, 1))) {

                                arena.nextRound(arena, playerTeam);
                            } else {
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        if (block.getType() == Material.SANDSTONE && block.getData() != 2 ||block.getType() != Material.SANDSTONE) {
                            event.setCancelled(true);
                        }
                    }
//                    block.setType(Material.AIR);
                    recentBreaks.add(event.getBlock().getLocation());

                    break;
                case SG:
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Location loc = event.getLocation().getBlock().getLocation();
        if (recentBreaks.remove(loc)) {
            event.setCancelled(true);
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

        Arena arena = arenaManager.getArenaByPlayer(p.getUniqueId(), Duel.getInstance().getArenas.getArenaList());

        if (arena != null) {
            switch (arena.getMode()) {
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

           Arena victimArena = arenaManager.getArenaByPlayer(victim.getUniqueId(), arenaList);

           if(victimArena == null || !victimArena.getAlluuids().contains(damager.getUniqueId()))
           {
               event.setCancelled(true);
           }
       }

   }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player Killer = victim.getKiller();
        System.out.println("Killer : " + Killer + " | victim : " +victim);

        if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) victim.getLastDamageCause();
            Entity damager = damageEvent.getDamager();
            // If the damager is a projectile (arrow), get the shooter
            if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                if (projectile.getShooter() instanceof Player) {
                    Killer = (Player) projectile.getShooter();
                }
            }
            if (Killer != null) {
                DuelArena arena = (DuelArena) arenaManager.getArenaByPlayer(Killer.getUniqueId(), arenaList);
                Team killerTeam = team.getTeamByPlayer(Killer, arena);
                User user1 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(victim);

                User user2 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(Killer);

                //GLOBAL
                System.out.println(arena.getMode());
                if (!arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                    System.out.println("eliminate and win");
                    arena.eliminate(victim, arenaList);
                    arena.customWinEvent(killerTeam);

                    StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, Killer.getUniqueId(), AccountType.statsKey(AllNames.DUELS.getName()));
                    Main.getInstance().gameMechanics.applyKillStreak(statsAccount, Killer, "§a§lDuel > §l");

                    StatsAccount statsAccount2 = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, victim.getUniqueId(), AccountType.statsKey(AllNames.DUELS.getName()));
                    if (statsAccount2.getKillstreaks() > 1) {
                        victim.sendMessage("§c§lVous avez perdu vos KillStreaks!");
                        statsAccount2.setKillstreaks(0);
                    }
                    //MLGRUSH
                } else gameSessionManager.onDeath(arenaManager, arena, GameMode.SURVIVAL, 2, 0, victim);

                Bukkit.broadcastMessage("§a§lDuel > §l" + user1.getCachedData().getMetaData().getPrefix() + " §l§r" + victim.getName() + "§6§l s'est fait retourné par §r§l" + user2.getCachedData().getMetaData().getPrefix() + " §l" + Killer.getName());
            }
            victim.spigot().respawn();
        }
    }
}