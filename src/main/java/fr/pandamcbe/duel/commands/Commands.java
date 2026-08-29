package fr.pandamcbe.duel.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("report")) {
                player.sendMessage("§b§lMG-Land §3§l> §6§lVoulez-vous §e§lreport §d§l§oquelqu'un §6§l?");

                TextComponent component = new TextComponent(TextComponent.fromLegacyText("§a§l§nCliquez ici"));
                // Add a click event to the component.
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://docs.google.com/forms/d/e/1FAIpQLSfFTdxrUsv0d9dzn69yCkE6MxgdgMegjUoU21dyY93KmHpQAA/viewform?usp=pp_url"));

                // Send it!
                player.spigot().sendMessage(component);
                return true;

            }

//            if (label.equalsIgnoreCase("duel")) {
//                if (args.length == 0) {
//                    return true;
//                }
//                if (args[0].equalsIgnoreCase("createarena")) {
//                    if (args.length < 4) {
//                        player.sendMessage("§lTapez la commande §e§l/duel createarena §d§l <loc1> <loc2>");
//                        return true;
//                    }
//                    Location loc1 = Duel.getInstance().parseStringToLoc(args[1]);
//                    Location loc2 = Duel.getInstance().parseStringToLoc(args[2]);
//                    Arena arena = new Arena(loc1, loc2);
//                    String arenaName = "arena-" + new Random().nextInt(9999);
//                    this.arenaConfig.set("arenas. " + arenaName + ".loc1", args[1]);
//                    this.arenaConfig.set("arenas. " + arenaName + ".loc2", args[2]);
//                    Duel.getInstance().saveArenaConfig();
//                    this.arenaManager.addArena(arena);
//                    player.sendMessage("Vous venez de créer l'arène §a§l" + arenaName + " !");
//
//                }
//
//                return true;
//
//            }
        }
        return false;
    }
}