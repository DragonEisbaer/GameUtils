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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EnchantGUI implements Listener {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    HashMap<Player, Integer> page = new HashMap<>();
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
            setInv(player, 0);
            page.put(player, 1);
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
    public void handleNextArrow(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            return;
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() != Material.ARROW) {
            return;
        }
        if (e.getSlot() != 26) {
            return;
        }
        Player player = (Player) e.getWhoClicked();

        if (page.containsKey(player)) {
            switch (page.get(player)) {
                case 1:
                    setInv(player, 15);
                    page.put(player, 2);
                    break;
                case 2:
                    setInv(player, 30);
                    page.put(player, 3);
                    break;
                case 3:
                    setInv(player, 45);
                    page.put(player, 4);
                    break;
            }
            plugin.getLogger().info("Set Page: " + page.get(player) + " for " + player.getName());
        }
        openingNewInventory = true;

        player.openInventory(inv);
    }

    @EventHandler
    public void preventRenameChestExploit(InventoryOpenEvent e) {
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

    private void setInv(Player player, int startIndex) {

        inv = getBasicInv(player);

        int enchindex = startIndex;

        ArrayList<Enchantment> allPossibleEnchantments = getAllPossibleEnchantments(player);

        //SetEnchs
        List<Integer> safeslots = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21));
        if (allPossibleEnchantments.size() > 15 || plugin.getStoredEnchantItem().get(player).getType() == Material.BOOK) {
            safeslots.add(27);
        }
        for (int i = 0; i < inv.getSize(); i++) {
            plugin.getLogger().info("Slot: " + i + " for " + player.getName());
            if (safeslots.contains(i + 1)) {
                if (enchindex >= allPossibleEnchantments.size()) {
                    plugin.getLogger().info("All Ench done!");
                } else {
                    plugin.getLogger().info("Skip Ench: " + allPossibleEnchantments.get(enchindex) + " for " + player.getName() + " (" + enchindex + ")");
                }
            } else {
                if (enchindex >= allPossibleEnchantments.size()) {
                    plugin.getLogger().info("All Ench done!");
                    page.put(player, 99);
                    break;
                } else {
                    ItemStack ench = new ItemStack(Material.ENCHANTED_BOOK);
                    ench.addUnsafeEnchantment(allPossibleEnchantments.get(enchindex), 1);
                    enchindex++;
                    inv.setItem(i + 1, ench);
                    plugin.getLogger().info("Set Ench: " + allPossibleEnchantments.get(enchindex - 1) + " for " + player.getName() + " (" + enchindex + ")");
                }
            }
        }
        if (safeslots.contains(27)) {
            page.putIfAbsent(player, 1);
            if (page.get(player) != 99) {
                ItemStack next = new ItemStack(Material.ARROW);
                ItemMeta meta = next.getItemMeta();
                assert meta != null;
                meta.setDisplayName(ChatColor.GREEN + "Next");
                next.setItemMeta(meta);
                inv.setItem(26, next);
            }
        }
        //DisableShifting
        inv.setMaxStackSize(1);
    }

    private Inventory getBasicInv(Player player) {
        Inventory basicInv = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "EnchantGUI");

        //SetPanes
        for (int i = 0; basicInv.getSize() > i; i++) {
            basicInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        //SetBookshelfs
        basicInv.setItem(3, new ItemStack(Material.BOOKSHELF));
        basicInv.setItem(12, new ItemStack(Material.BOOKSHELF));
        basicInv.setItem(21, new ItemStack(Material.BOOKSHELF));

        basicInv.setItem(10, plugin.getStoredEnchantItem().get(player));

        return basicInv;
    }

    private ArrayList<Enchantment> getAllPossibleEnchantments(Player player) {
        //Check all Possible Enchants
        ArrayList<Enchantment> allPossibleEnchantments = new ArrayList<>();
        if (plugin.getStoredEnchantItem().get(player).getType() == Material.BOOK) {
            allPossibleEnchantments.addAll(List.of(Enchantment.values()));
        }else {
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.canEnchantItem(plugin.getStoredEnchantItem().get(player))) {
                    allPossibleEnchantments.add(enchantment);
                }
            }
        }
        return allPossibleEnchantments;
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
}