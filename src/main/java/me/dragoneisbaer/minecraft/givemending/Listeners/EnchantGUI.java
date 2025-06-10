package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EnchantGUI implements Listener {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    ArrayList<ItemStack> alloweditems = new ArrayList<>();
    private boolean openingNewInventory = false;

    private Inventory inv;

    @EventHandler
    public void onGiveItem(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            return;
        }
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        setAllowedItems();
        if (!alloweditems.contains(event.getCurrentItem())) {
            event.setCancelled(true);
            return;
        }

        if (plugin.getStoredEnchantItem().containsKey(event.getWhoClicked())) {
            if (event.getCurrentItem().equals(plugin.getStoredEnchantItem().get(event.getWhoClicked()))) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getSlot() == 13 && event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            openingNewInventory = true;
            if (!plugin.getStoredEnchantItem().containsKey(player)) {
                plugin.getStoredEnchantItem().put(player, event.getCurrentItem());
                plugin.getLogger().info("Set Item: " + event.getCurrentItem().getType() + " for " + player.getName());
            }
            setInv(player);
            player.openInventory(inv);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            if (event.getPlayer() instanceof Player && !openingNewInventory) {
                Player player = (Player) event.getPlayer();
                if (plugin.getStoredEnchantItem().containsKey(player)) {
                    player.getInventory().addItem(plugin.getStoredEnchantItem().get(player));
                    plugin.getStoredEnchantItem().remove(player);
                } else {
                    ItemStack item = event.getInventory().getItem(13);
                    if (item != null) {
                        player.getInventory().addItem(item);
                    }
                }
            }
            openingNewInventory = false;
        }
    }


    @EventHandler
    public void openChest(InventoryOpenEvent e) {
        if (e.getInventory().getLocation() == null) {
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            Block block = Objects.requireNonNull(e.getInventory().getLocation()).getBlock();
            if (block.getType() == Material.CHEST) {
                e.setCancelled(true);
                Player player = (Player) e.getPlayer();
                player.sendMessage(ChatColor.RED + "Bitte nutze den Befehl: /enchantgui!");
            }
        }
    }

    private void setAllowedItems() {
        alloweditems.add(new ItemStack(Material.BOOK));
        alloweditems.add(new ItemStack(Material.ENCHANTED_BOOK));
        alloweditems.add(new ItemStack(Material.NETHERITE_SWORD));
        alloweditems.add(new ItemStack(Material.DIAMOND_SWORD));
        alloweditems.add(new ItemStack(Material.IRON_SWORD));
        alloweditems.add(new ItemStack(Material.GOLDEN_SWORD));
        alloweditems.add(new ItemStack(Material.STONE_SWORD));
        alloweditems.add(new ItemStack(Material.WOODEN_SWORD));
        alloweditems.add(new ItemStack(Material.WOODEN_AXE));
        alloweditems.add(new ItemStack(Material.IRON_AXE));
        alloweditems.add(new ItemStack(Material.GOLDEN_AXE));
        alloweditems.add(new ItemStack(Material.DIAMOND_AXE));
        alloweditems.add(new ItemStack(Material.STONE_AXE));
        alloweditems.add(new ItemStack(Material.WOODEN_PICKAXE));
        alloweditems.add(new ItemStack(Material.IRON_PICKAXE));
        alloweditems.add(new ItemStack(Material.GOLDEN_PICKAXE));
        alloweditems.add(new ItemStack(Material.DIAMOND_PICKAXE));
        alloweditems.add(new ItemStack(Material.STONE_PICKAXE));
        alloweditems.add(new ItemStack(Material.WOODEN_SHOVEL));
        alloweditems.add(new ItemStack(Material.IRON_SHOVEL));
        alloweditems.add(new ItemStack(Material.GOLDEN_SHOVEL));
        alloweditems.add(new ItemStack(Material.DIAMOND_SHOVEL));
        alloweditems.add(new ItemStack(Material.STONE_SHOVEL));
        alloweditems.add(new ItemStack(Material.WOODEN_HOE));
        alloweditems.add(new ItemStack(Material.IRON_HOE));
        alloweditems.add(new ItemStack(Material.GOLDEN_HOE));
        alloweditems.add(new ItemStack(Material.DIAMOND_HOE));
        alloweditems.add(new ItemStack(Material.STONE_HOE));
        alloweditems.add(new ItemStack(Material.BOW));
        alloweditems.add(new ItemStack(Material.CROSSBOW));
        alloweditems.add(new ItemStack(Material.FISHING_ROD));
        alloweditems.add(new ItemStack(Material.TRIDENT));
        alloweditems.add(new ItemStack(Material.SHIELD));
        alloweditems.add(new ItemStack(Material.ELYTRA));
        alloweditems.add(new ItemStack(Material.LEATHER_HELMET));
        alloweditems.add(new ItemStack(Material.LEATHER_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.LEATHER_LEGGINGS));
        alloweditems.add(new ItemStack(Material.LEATHER_BOOTS));
        alloweditems.add(new ItemStack(Material.CHAINMAIL_HELMET));
        alloweditems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        alloweditems.add(new ItemStack(Material.CHAINMAIL_BOOTS));
        alloweditems.add(new ItemStack(Material.IRON_HELMET));
        alloweditems.add(new ItemStack(Material.IRON_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.IRON_LEGGINGS));
        alloweditems.add(new ItemStack(Material.IRON_BOOTS));
        alloweditems.add(new ItemStack(Material.GOLDEN_HELMET));
        alloweditems.add(new ItemStack(Material.GOLDEN_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.GOLDEN_LEGGINGS));
        alloweditems.add(new ItemStack(Material.GOLDEN_BOOTS));
        alloweditems.add(new ItemStack(Material.DIAMOND_HELMET));
        alloweditems.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        alloweditems.add(new ItemStack(Material.DIAMOND_BOOTS));
        alloweditems.add(new ItemStack(Material.NETHERITE_HELMET));
        alloweditems.add(new ItemStack(Material.NETHERITE_CHESTPLATE));
        alloweditems.add(new ItemStack(Material.NETHERITE_LEGGINGS));
        alloweditems.add(new ItemStack(Material.NETHERITE_BOOTS));
        alloweditems.add(new ItemStack(Material.TURTLE_HELMET));
    }
    private void setInv(Player player) {
        inv = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "EnchantGUI");

        //SetPanes
        for (int i = 0;inv.getSize()>i;i++) {
            inv.setItem(i,new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        //SetItemDisplay
        inv.setItem(10, plugin.getStoredEnchantItem().get(player));

        //SetBookshelfs
        inv.setItem(3, new ItemStack(Material.BOOKSHELF));
        inv.setItem(12, new ItemStack(Material.BOOKSHELF));
        inv.setItem(21, new ItemStack(Material.BOOKSHELF));

        //Check all Possible Enchants
        ArrayList<Enchantment> allPossibleEnchantments = new ArrayList<>();
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(plugin.getStoredEnchantItem().get(player))) {
                allPossibleEnchantments.add(enchantment);
            }
        }
        plugin.getLogger().info("Allench: " + allPossibleEnchantments + " for " + player.getName() + " (" + allPossibleEnchantments.size() + ")");

        //SetEnchs
        if (allPossibleEnchantments.size() <= 15) {
            List<Integer> safeslots = Arrays.asList(0,1,2,3,9,10,11,12,18,19,20,21);
            int enchindex = 0;
            for (int i = 0;i<inv.getSize();i++) {
                plugin.getLogger().info("Slot: " + i + " for " + player.getName());
                if (safeslots.contains(i+1)) {
                    if (enchindex >= allPossibleEnchantments.size()) {
                        plugin.getLogger().info("All Ench done!");
                    }else {
                        plugin.getLogger().info("Skip Ench: " + allPossibleEnchantments.get(enchindex) + " for " + player.getName() + " (" + enchindex + ")");
                    }
                }else {
                    if (enchindex >= allPossibleEnchantments.size()) {
                        plugin.getLogger().info("All Ench done!");
                        break;
                    }else {
                        ItemStack ench = new ItemStack(Material.ENCHANTED_BOOK);
                        ench.addUnsafeEnchantment(allPossibleEnchantments.get(enchindex),1);
                        enchindex++;
                        inv.setItem(i+1,ench);
                        plugin.getLogger().info("Set Ench: " + allPossibleEnchantments.get(enchindex-1) + " for " + player.getName() + " (" + enchindex + ")");
                    }
                }
            }
        } else {

        }

        //DisableShifting
        inv.setMaxStackSize(1);
    }
}