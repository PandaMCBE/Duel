package fr.pandamcbe.duel.commands;

import fr.pandamcbe.commons.StatsAccount;
import fr.pandamcbe.duel.Duel;
import fr.pandamcbe.mgapi.Main;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AccountType;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatCommand implements CommandExecutor {

    private boolean verifyName(String serverName){
        for(String s : Main.getInstance().getProxyMessaging().askServersName()){
            if(!s.equalsIgnoreCase(AllNames.LOBBY.getName())) {
                if(serverName.equalsIgnoreCase(s))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (sender.hasPermission("pandafun.commands.stats")) {
            if (cmd.getName().equalsIgnoreCase("stats")) {


                if (args.length == 2) {
                    final String serverName = args[0];
                    if(verifyName(serverName)) {
                        StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, uuid, AccountType.statsKey(serverName));

                        player.sendMessage("§b§lMG-Land> §r§f" + args[1] + " §fa actuellement au " + serverName + ":\n§e" + statsAccount.getCoins() + " §6MGCoins\n§d" + statsAccount.getElos() + " §dMGElos\n§c" + statsAccount.getKills() + " §cMGKills\n§a" + statsAccount.getKillstreaks() + " §aMGKillStreaks\n§b" + statsAccount.getWins() + " §bMGWins");
                    } else {
                        player.sendMessage("§cNom de serveur incorrècte, veuillez réessayer.");
                    }
                } else if (args.length < 2) {
                    player.sendMessage("§b§lMG-Land §r§c/stats [serverName] [playerName] [stat(coins, elos...)] [add/remove] [amount]");
                }if (args.length >= 3) {
                    final String serverName = args[0];
                    if(verifyName(serverName)) {
                        StatsAccount statsAccount = (StatsAccount) Main.getInstance().manipulation.getStatsAccount(Duel.getInstance().mainMap, uuid, AccountType.statsKey(serverName));


                        //stats CJFFA[0] PlayerName[1] coins[2] add[3] amount[4]= 5 args

                        Player target = Bukkit.getPlayer(args[1]);
                        if (args[2].equalsIgnoreCase("coins")) {
                            if (args[3].equalsIgnoreCase("add")) {
                                if (args.length == 5) {

                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setCoins(statsAccount.getCoins() + amount);
                                        player.sendMessage("§b§lMG-Land> §r§f" + target.getName() + " §fa reçu §e" + amount + " §6MGCoins");
                                    }

                                }
                                // /coins remove <le nombre de coins> <le joueur>
                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setCoins(statsAccount.getCoins() - amount);
                                        player.sendMessage("§b§lMG-Land>: §r§f" + target.getName() + " §fa perdu §e" + amount + " §6MGCoins");
                                    }

                                }
                            }


                        }

                        if (args[2].equalsIgnoreCase("elos")) {
                            if (args[3].equalsIgnoreCase("add")) {
                                if (args.length == 5) {

                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setElos(statsAccount.getElos() + amount);
                                        player.sendMessage("§b§lMG-Land> §r§f" + target.getName() + " §fa reçu §e" + amount + " §dMGElos");
                                    }

                                }

                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (args.length == 5) {

                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setElos(statsAccount.getElos() - amount);
                                        player.sendMessage("§b§lMG-Land>: §r§f" + target.getName() + " §fa perdu §e" + amount + " §dMGElos");

                                    }

                                }
                            }

                        }

                        // /coins add <le nombre de kills> <le joueur>
                        if (args[2].equalsIgnoreCase("kills")) {
                            if (args[3].equalsIgnoreCase("add")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setKills(statsAccount.getKills() + amount);
                                        player.sendMessage("§b§lMG-Land> §r§f" + target.getName() + " §fa reçu §e" + amount + " §cMGKills");
                                    }

                                }

                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.parseInt(args[4]);
                                        statsAccount.setKills(statsAccount.getKills() - amount);
                                        player.sendMessage("§b§lMG-Land>: §r§f" + target.getName() + " §fa perdu §e" + amount + " §cMGKills");
                                    }

                                }
                            }

                        }


                        // /coins add <le nombre de wins> <le joueur>
                        if (args[2].equalsIgnoreCase("wins")) {
                            if (args[3].equalsIgnoreCase("add")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.valueOf(args[4]);
                                        statsAccount.setWins(statsAccount.getWins() + amount);
                                        player.sendMessage("§b§lMG-Land> §r§f" + target.getName() + " §fa reçu §e" + amount + " §bMGWins");
                                    }
                                }

                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.valueOf(args[4]);
                                        statsAccount.setWins(statsAccount.getWins() - amount);
                                        player.sendMessage("§b§lMG-Land>: §r§f" + target.getName() + " §fa perdu §e" + amount + " §bMGWins");
                                    }
                                }
                            }

                        }


                        if (args[2].equalsIgnoreCase("killstreaks")) {
                            if (args[3].equalsIgnoreCase("add")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.valueOf(args[4]);
                                        statsAccount.setKillstreaks(statsAccount.getKillstreaks() + amount);
                                        player.sendMessage("§b§lMG-Land> §r§f" + target.getName() + " §fa reçu §e" + amount + " §bMGKillStreak");
                                    }

                                }

                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (args.length == 5) {
                                    if (target != null) {
                                        int amount = Integer.valueOf(args[4]);
                                        statsAccount.setKillstreaks(statsAccount.getKillstreaks() - amount);
                                        player.sendMessage("§b§lMG-Land>: §r§f" + target.getName() + " §fa perdu §e" + amount + " §bMGKillStreak");
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage("§cNom de serveur incorrècte, veuillez réessayer.");
                    }
                }
            }
        }
        return true;
    }
}