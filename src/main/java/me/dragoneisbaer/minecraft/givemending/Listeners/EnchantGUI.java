package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EnchantGUI implements Listener {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    @EventHandler
    public void onGiveItem(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            if (event.getCurrentItem() != null) {
                if ((event.getCurrentItem().getType().equals(Material.OAK_SIGN) || (event.getCurrentItem().getType().equals(Material.BOOK)) || event.getCurrentItem().getType().equals(Material.BOOKSHELF) || event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE))) {
                    event.setCancelled(true);
                    return;
                }else if (event.getCurrentItem().getMaxStackSize() > 1) {
                    event.setCancelled(true);
                    return;
                }
                HumanEntity human = event.getWhoClicked();
                if (human instanceof Player) {
                    Player player = (Player) human;
                    ItemStack item = event.getCurrentItem();
                    if (!Objects.equals(item, new ItemStack(Material.AIR))) {
                        ItemStack citem = event.getCurrentItem();
                        ItemMeta  citemmeta = citem.getItemMeta();
                        if (citemmeta.getLore() == null) {
                            //citemmeta.setLore(List.of(HiddenStringUtils.encodeString()));
                            citem.setItemMeta(citemmeta);
                            plugin.getItemstacks().put(player, citem);
                        }else if (citemmeta.getLore() != null){
                            String uuid = citemmeta.getLore().get(0);
                        }
                        player.sendMessage(plugin.getItemstacks().get(player).getItemMeta().getLore().get(0));
                    }
                }
            }
        }
    }


    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                ItemStack storeditem = plugin.getItemstacks().get(player);
                if (storeditem != null) {
                    if (!player.getInventory().contains(storeditem)) {
                        player.getInventory().addItem(storeditem);
                        plugin.getItemstacks().remove(player);
                    }
                }
            }
        }
    }


    @EventHandler
    public void openChest(InventoryOpenEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_BLUE + "EnchantGUI")) {
            Block block = Objects.requireNonNull(e.getInventory().getLocation()).getBlock();
            if (block.getType() == Material.CHEST) {
                e.setCancelled(true);
                Player player = (Player) e.getPlayer();
                player.sendMessage(ChatColor.RED + "Bitte nutze den Befehl: /enchantgui!");
            }
        }
    }
}
