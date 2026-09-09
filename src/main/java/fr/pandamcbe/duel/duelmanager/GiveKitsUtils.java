package fr.pandamcbe.duel.duelmanager;

import fr.pandamcbe.duel.arenas.DuelArena;
import fr.pandamcbe.mGConstructorAPI.MGConstructorAPI;
import fr.pandamcbe.mGConstructorAPI.Team;
import fr.pandamcbe.mgapi.serverutilapi.enums_strings.AllNames;
import fr.pandamcbe.mgapi.serverutilapi.utils.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveKitsUtils {

    public GiveKitsUtils (DuelArena arena, Player pls) {
        AllNames allNames = AllNames.fromName(arena.getMode());
        if(allNames != null) {
            ItemStack casque;ItemStack plastron;ItemStack jambieres;ItemStack bottes;
            switch (allNames){
                case BUILDUHC:
                    pls.getInventory().addItem(new ItemBuilder(Material.STONE_SWORD).makeUnbreakable().toItemStack());
                    pls.getInventory().setItem(8, new ItemBuilder(Material.GOLDEN_APPLE, 1).toItemStack());
                    casque = new ItemBuilder(Material.LEATHER_HELMET).makeUnbreakable().toItemStack();
                    plastron = new ItemBuilder(Material.LEATHER_CHESTPLATE).makeUnbreakable().toItemStack();
                    jambieres = new ItemBuilder(Material.LEATHER_LEGGINGS).makeUnbreakable().toItemStack();
                    bottes = new ItemBuilder(Material.LEATHER_BOOTS).makeUnbreakable().toItemStack();

                    pls.getInventory().setHelmet(casque);
                    pls.getInventory().setChestplate(plastron);
                    pls.getInventory().setLeggings(jambieres);
                    pls.getInventory().setBoots(bottes);
                    break;
                case SG:
                    pls.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).makeUnbreakable().toItemStack());
                    pls.getInventory().setItem(1, new ItemBuilder(Material.FISHING_ROD).makeUnbreakable().toItemStack());
                    pls.getInventory().setItem(2, new ItemBuilder(Material.BOW).makeUnbreakable().toItemStack());
                    pls.getInventory().setItem(3, new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());
                    pls.getInventory().setItem(4, new ItemBuilder(Material.PUMPKIN_PIE, 2).toItemStack());
                    pls.getInventory().setItem(5, new ItemBuilder(Material.GOLDEN_CARROT).toItemStack());
                    pls.getInventory().setItem(6, new ItemBuilder(Material.MELON, 2).toItemStack());
                    pls.getInventory().setItem(7, new ItemBuilder(Material.BREAD).toItemStack());
                    pls.getInventory().setItem(8, new ItemBuilder(Material.FLINT_AND_STEEL).setDurability((short) 61).toItemStack());
                    pls.getInventory().addItem(new ItemBuilder(Material.ARROW, 8).toItemStack());

                    casque = new ItemBuilder(Material.GOLD_HELMET).makeUnbreakable().toItemStack();
                    plastron = new ItemBuilder(Material.IRON_CHESTPLATE).makeUnbreakable().toItemStack();
                    jambieres = new ItemBuilder(Material.CHAINMAIL_LEGGINGS).makeUnbreakable().toItemStack();
                    bottes = new ItemBuilder(Material.IRON_BOOTS).makeUnbreakable().toItemStack();

                    pls.getInventory().setHelmet(casque);
                    pls.getInventory().setChestplate(plastron);
                    pls.getInventory().setLeggings(jambieres);
                    pls.getInventory().setBoots(bottes);
                    break;

                case SUMO:
                    pls.getInventory().setItem(6, new ItemBuilder(Material.STICK).setName("§cOne Punch Stick").setLore("§7Repousse tes énemies vers l'infini", "§7et l'au-delà !").addEnchant(Enchantment.KNOCKBACK, 4).toItemStack());
                    pls.getInventory().setItem(1, new ItemBuilder(Material.FISHING_ROD).setName("§dUltimate PvP Item").setLore("§7Seuls les pros arrivent à faire", "§7qu'un avec leur canne à pêche,", "§7pour nous défoncer, nous,", "§7les noobs xd").makeUnbreakable().toItemStack());
                    pls.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 2).toItemStack());
                    casque = new ItemBuilder(Material.DIAMOND_HELMET).makeUnbreakable().toItemStack();
                    plastron = new ItemBuilder(Material.IRON_CHESTPLATE).makeUnbreakable().toItemStack();
                    jambieres = new ItemBuilder(Material.IRON_LEGGINGS).makeUnbreakable().toItemStack();
                    bottes = new ItemBuilder(Material.GOLD_BOOTS).makeUnbreakable().toItemStack();

                    pls.getInventory().setHelmet(casque);
                    pls.getInventory().setChestplate(plastron);
                    pls.getInventory().setLeggings(jambieres);
                    pls.getInventory().setBoots(bottes);
                    break;
                case MLGRUSH:
                    pls.getInventory().setItem(0, new ItemBuilder(Material.STICK).setName("§cOne Punch Stick").setLore("§7Repousse tes énemies vers l'infini", "§7et l'au-delà !").addEnchant(Enchantment.KNOCKBACK, 1).toItemStack());
                    pls.getInventory().setItem(1, new ItemBuilder(Material.IRON_PICKAXE).setName("§bDemolition Tool").setLore("§7Détruis les blocks (sérieux)", "§7pas d'originalité dsl...").makeUnbreakable().toItemStack());
//                        pls.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setLore("§7Allez, prends cette arme", "§7et défonce tout le monde.").toItemStack());
                    pls.getInventory().setItem(2, new ItemBuilder(Material.SANDSTONE, 64).setDurability((byte) 2).toItemStack());  //Lore :"§7Fais des bridges pour ", "§7enquiquiner ton adversaire"
                    pls.getInventory().setItem(6, new ItemBuilder(Material.GOLDEN_APPLE, 3).toItemStack());
//                        pls.getInventory().setItem(8, new ItemBuilder(Material.COOKED_BEEF, 64).toItemStack());

                    casque = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(Color.RED).makeUnbreakable().toItemStack();
                    plastron = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.RED).makeUnbreakable().toItemStack();
                    jambieres = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.RED).makeUnbreakable().toItemStack();
                    bottes = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.RED).makeUnbreakable().toItemStack();

                    ItemStack casque2 = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(Color.BLUE).makeUnbreakable().toItemStack();
                    ItemStack plastron2 = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.BLUE).makeUnbreakable().toItemStack();
                    ItemStack jambieres2 = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.BLUE).makeUnbreakable().toItemStack();
                    ItemStack bottes2 = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.BLUE).makeUnbreakable().toItemStack();

                    Team team = MGConstructorAPI.getInstance().getTeam();
                    if (team.getTeamByPlayer(pls, arena).getColor().equals(ChatColor.RED)) {
                        pls.getInventory().setHelmet(casque);
                        pls.getInventory().setChestplate(plastron);
                        pls.getInventory().setLeggings(jambieres);
                        pls.getInventory().setBoots(bottes);
                    }
                    if (team.getTeamByPlayer(pls, arena).getColor().equals(ChatColor.BLUE)) {
                        pls.getInventory().setHelmet(casque2);
                        pls.getInventory().setChestplate(plastron2);
                        pls.getInventory().setLeggings(jambieres2);
                        pls.getInventory().setBoots(bottes2);
                    }
                    break;
            }
        }
    }
}
