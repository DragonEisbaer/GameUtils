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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EnchantGUI implements Listener {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    HashMap<Player, Integer> page = new HashMap<>();
    private final Set<Material> alloweditems = new HashSet<>();
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

        setAllowedMaterials();
        if (!alloweditems.contains(event.getCurrentItem().getType())) {
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
        if (title.equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI") || title.equalsIgnoreCase(ChatColor.DARK_BLUE + "Enchant Level Selector")) {
            if (event.getPlayer() instanceof Player && !openingNewInventory) {
                Player player = (Player) event.getPlayer();
                if (plugin.getStoredEnchantItem().containsKey(player)) {
                    player.getInventory().addItem(plugin.getStoredEnchantItem().get(player));
                    plugin.getLogger().info("Removed Item: " + plugin.getStoredEnchantItem().get(player).getType() + " for " + player.getName());
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
        int currentPage = page.getOrDefault(player, 1);
        int maxPage = (int) Math.ceil((double) getAllPossibleEnchantments(player).size() / 15.0);

        if (e.getClick() == ClickType.LEFT && currentPage < maxPage) {
            setInv(player, currentPage * 15);
            page.put(player, currentPage + 1);
        } else if (e.getClick() == ClickType.RIGHT && currentPage > 1) {
            setInv(player, (currentPage - 2) * 15);
            page.put(player, currentPage - 1);
        } else {
            e.setCancelled(true);
            return;
        }
        plugin.getLogger().info("Set Page: " + page.get(player) + " for " + player.getName());
        openingNewInventory = true;
        player.openInventory(inv);
        e.setCancelled(true);
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

    @EventHandler
    public void selectBook(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            return;
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() != Material.ENCHANTED_BOOK) {
            return;
        }
        if (e.getSlot() < 1 || e.getSlot() > 26) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        ItemStack selectedBook = e.getCurrentItem();

        if (plugin.getStoredEnchantItem().containsKey(player)) {
            openingNewInventory = true;
            openEnchLevelSelector(player, selectedBook);
        } else {
            player.sendMessage(ChatColor.RED + "Du hast kein Item ausgewählt, welches du verzaubern möchtest!");
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void addEnchantToItem(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "Enchant Level Selector")) {
            return;
        }
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() != Material.ENCHANTED_BOOK) {
            return;
        }
        if (e.getSlot() < 0 || e.getSlot() > 26) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        ItemStack selectedBook = e.getCurrentItem();

        if (!(selectedBook.getItemMeta() instanceof EnchantmentStorageMeta)) {
            player.sendMessage(ChatColor.RED + "Du hast kein Buch ausgewählt, welches du verzaubern möchtest!");
            e.setCancelled(true);
            return;
        }
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) selectedBook.getItemMeta();

        if (enchantmentStorageMeta.getStoredEnchants().isEmpty()) {
            player.sendMessage(ChatColor.RED + "Du hast kein Buch ausgewählt, welches du verzaubern möchtest!");
            e.setCancelled(true);
            return;
        }
        Enchantment bookench = enchantmentStorageMeta.getStoredEnchants().keySet().iterator().next();
        int bookenchlevel = enchantmentStorageMeta.getStoredEnchantLevel(bookench);

        if (plugin.getStoredEnchantItem().containsKey(player)) {

            ItemStack itemToEnchant;

            if (plugin.getStoredEnchantItem().get(player).getType() == Material.BOOK) {
                itemToEnchant = new ItemStack(Material.ENCHANTED_BOOK);
            }else {
                itemToEnchant = plugin.getStoredEnchantItem().get(player);
            }

            if (itemToEnchant.getType() == Material.ENCHANTED_BOOK) {
                ItemMeta meta = itemToEnchant.getItemMeta();
                if (meta instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) meta;
                    storageMeta.addStoredEnchant(bookench, bookenchlevel, true);
                    itemToEnchant.setItemMeta(storageMeta);
                }
            }else {
                itemToEnchant.addUnsafeEnchantment(bookench, bookenchlevel);
            }
            plugin.getStoredEnchantItem().put(player, itemToEnchant);
            openingNewInventory = true;
            page.put(player, 1);
            setInv(player, 0);
            player.openInventory(inv);
        } else {
            player.sendMessage(ChatColor.RED + "Du hast kein Item ausgewählt, welches du verzaubern möchtest!");
        }
    }

    private void setInv(Player player, int startIndex) {

        inv = getBasicInv(player, "EnchantGUI");

        int enchindex = startIndex;

        ArrayList<Enchantment> allPossibleEnchantments = getAllPossibleEnchantments(player);

        //SetEnchs
        List<Integer> safeslots = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21));
        if (allPossibleEnchantments.size() > 15 || plugin.getStoredEnchantItem().get(player).getType() == Material.BOOK) {
            safeslots.add(27);
        }
        for (int i = 0; i < inv.getSize(); i++) {
            if (!safeslots.contains(i + 1)) {
                if (enchindex >= allPossibleEnchantments.size()) {
                    page.put(player, 99);
                    break;
                } else {
                    ItemStack ench = new ItemStack(Material.ENCHANTED_BOOK);
                    ench.addUnsafeEnchantment(allPossibleEnchantments.get(enchindex), 1);
                    enchindex++;
                    inv.setItem(i + 1, ench);
                }
            }
        }
        if (safeslots.contains(27)) {
            page.putIfAbsent(player, 1);
            int currentPage = page.getOrDefault(player, 1);
            int maxPage = (int) Math.ceil((double) allPossibleEnchantments.size() / 15.0);

            if (maxPage > 1) { // Nur anzeigen, wenn Blättern möglich ist
                ItemStack next = new ItemStack(Material.ARROW);
                ItemMeta meta = next.getItemMeta();
                assert meta != null;
                meta.setDisplayName(ChatColor.GREEN + "Seitenwechsel");
                List<String> lore = new ArrayList<>();
                if (currentPage == 1) {
                    lore.add(ChatColor.DARK_GREEN + "Linksklick: Nächste Seite");
                } else if (currentPage == maxPage) {
                    lore.add(ChatColor.DARK_RED + "Rechtsklick: Vorherige Seite");
                } else {
                    lore.add(ChatColor.DARK_GREEN + "Linksklick: Nächste Seite");
                    lore.add(ChatColor.DARK_RED + "Rechtsklick: Vorherige Seite");
                }
                meta.setLore(lore);
                next.setItemMeta(meta);
                inv.setItem(26, next);
            }
        }
        //DisableShifting
        inv.setMaxStackSize(1);
    }

    private void openEnchLevelSelector(Player player, ItemStack selectedBook) {
        Inventory enchLevelInv = getBasicInv(player, "Enchant Level Selector");

        if (selectedBook.getItemMeta().getEnchants().size() == 1) {
            Enchantment enchantment = selectedBook.getItemMeta().getEnchants().keySet().iterator().next();
            int maxLevel = enchantment.getMaxLevel();

            // Slot-Positionen je nach MaxLevel
            int[] slots;
            switch (maxLevel) {
                case 1:
                    slots = new int[]{15};
                    break;
                case 2:
                    slots = new int[]{14, 16};
                    break;
                case 3:
                    slots = new int[]{14, 15, 16};
                    break;
                case 4:
                    slots = new int[]{13, 14, 16, 17};
                    break;
                case 5:
                    slots = new int[]{13, 14, 15, 16, 17};
                    break;
                default:
                    slots = new int[]{13, 14, 15, 16, 17};
                    break;
            }

            for (int level = 1; level <= maxLevel; level++) {
                ItemStack levelBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) levelBook.getItemMeta();
                meta.addStoredEnchant(enchantment, level, true);
                levelBook.setItemMeta(meta);
                enchLevelInv.setItem(slots[level - 1], levelBook);
            }
        }
        player.openInventory(enchLevelInv);
        openingNewInventory = true;
    }

    private Inventory getBasicInv(Player player, String title) {
        Inventory basicInv = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + title);

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
        if (plugin.getStoredEnchantItem().get(player).getType() == Material.BOOK || plugin.getStoredEnchantItem().get(player).getType() == Material.ENCHANTED_BOOK) {
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

    private void setAllowedMaterials() {
        alloweditems.clear();
        for (Material material : Material.values()) {
            if (material.isItem()) {
                ItemStack testItem = new ItemStack(material);
                for (Enchantment ench : Enchantment.values()) {
                    if (ench.canEnchantItem(testItem)) {
                        alloweditems.add(material);
                        break;
                    }
                }
            }
        }
        alloweditems.add(Material.BOOK);
        alloweditems.add(Material.ENCHANTED_BOOK);
    }
}