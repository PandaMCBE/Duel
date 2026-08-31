package fr.pandamcbe.duel.listeners;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mGConstructorAPI.arenas.Arena;
import fr.pandamcbe.mGConstructorAPI.managers.ArenaManager;
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

import java.util.List;

public class ArenaListeners implements Listener {

    private final ArenaManager arenaManager = MGConstructorAPI.getInstance().getArenaManager();
    private final GameSessionManager gameSessionManager = MGConstructorAPI.getInstance().getGameManager();
    public List<Arena> arenaList = Duel.getInstance().getArenas.getArenaList();
    private final Team team = new Team();

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

                if (location.getY() <= 2) {
                    gameSessionManager.onDeath(arena, GameMode.ADVENTURE, 2, 0, player);
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
        DuelArena arena = (DuelArena) arenaManager.getArenaByPlayer(p.getUniqueId(), arenaList);
        Team playerTeam = team.getTeamByPlayer(p, arena);
        AllNames allNames = AllNames.fromName(arena.getMode());
        if(allNames != null) {
            switch (allNames) {
                case BUILDUHC:
                    break;
                case MLGRUSH:
                    if (block.getType().equals(Material.BED_BLOCK)) {
                        if (team.getSpecificTeam(arena, ChatColor.BLUE).getUuidList().contains(p.getUniqueId())) {
                            if (block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.BLUE)) || block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.BLUE).clone().add(0, 0, 1))) {
                                arena.nextRound(arena, playerTeam);
                                System.out.println("BLUE BED!!");
                            } else {
                                event.setCancelled(true);
                                System.out.println("Breaking Blue Cancelled");
                            }
                        }
                        if (team.getSpecificTeam(arena, ChatColor.RED).getUuidList().contains(p.getUniqueId())) {
                            if (block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.RED)) || block.getLocation().equals(arena.getGoalByTeam().get(ChatColor.RED).clone().add(0, 0, -1))) {

                                arena.nextRound(arena, playerTeam);
                                System.out.println("RED BED!!");
                            } else {
                                System.out.println("Breaking Red Cancelled");
                                event.setCancelled(true);
                            }
                        }
//                        Stream<Entity> beds = block.getWorld().getEntities().stream().filter(entity -> entity.isOnGround() && entity instanceof Bed);
//                        beds.forEach(Entity::remove);
                    } else {
                        if (block.getType() == Material.SANDSTONE && block.getData() != 2 || block.getType() != Material.SANDSTONE)
                            event.setCancelled(true);
                    }
                    break;
                case SG:
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        }

        for (Entity entity : block.getWorld().getEntities()) {
            if (entity.getLocation().getBlockX() == block.getLocation().getBlockX() &&
                    entity.getLocation().getBlockZ() == block.getLocation().getBlockZ() &&
                    entity.getLocation().getBlockY() == block.getLocation().getBlockY()) {
                entity.remove();
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
    void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player Killer = victim.getKiller();
        DuelArena arena = (DuelArena) arenaManager.getArenaByPlayer(Killer.getUniqueId(),arenaList);
        Team killerTeam = team.getTeamByPlayer(Killer, arena);
        User user1 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(victim);

        User user2 = Duel.getInstance().luckPerms.getPlayerAdapter(Player.class).getUser(Killer);

        if (victim != Killer) {
            //GLOBAL
            if (!arena.getMode().equalsIgnoreCase(AllNames.MLGRUSH.getName())) {
                    arena.eliminate(victim, arenaList);
                    arena.customWinEvent(killerTeam);

                    StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, Killer.getUniqueId(), AccountType.statsKey(AllNames.DUELS.getName()));
                    Main.getInstance().gameMechanics.applyKillStreak(statsAccount, Killer, "§a§lDuel > §l");

                    StatsAccount statsAccount2 = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, victim.getUniqueId(), AccountType.statsKey(AllNames.DUELS.getName()));
                    if(statsAccount2.getKillstreaks() > 1) {
                    victim.sendMessage("§c§lVous avez perdu vos KillStreaks!");
                    statsAccount2.setKillstreaks(0);
                }
                //MLGRUSH
            } else gameSessionManager.onDeath(arena, GameMode.ADVENTURE, 2, 0, victim);

            Bukkit.broadcastMessage("§a§lDuel > §l" + user1.getCachedData().getMetaData().getPrefix() + " §l§r" + victim.getName() + "§6§l s'est fait retourné par §r§l" + user2.getCachedData().getMetaData().getPrefix() + " §l" + Killer.getName());
        }

        e.getDrops().clear();
        victim.spigot().respawn();
    }
}