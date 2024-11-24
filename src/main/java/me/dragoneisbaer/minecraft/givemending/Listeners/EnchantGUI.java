package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import me.dragoneisbaer.minecraft.givemending.Memory.PlayerMemory;
import me.dragoneisbaer.minecraft.givemending.Memory.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantGUI implements Listener {

    private final GameUtils plugin;
    public EnchantGUI(GameUtils plugin) {this.plugin = plugin;}

    @EventHandler
    public void onGiveItem(InventoryClickEvent event) {
        String title = ChatColor.stripColor(event.getView().getTitle());
        if (title.equalsIgnoreCase("EnchantGUI")) {
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
                    if (plugin.itemstacks.get(player) == null) {
                        plugin.itemstacks.put(player, event.getCurrentItem());
                    }
                    Bukkit.getServer().broadcastMessage("SetI: " + plugin.itemstacks.get(player).toString());
                    //player.getInventory().addItem(memory.getItem());
                }
            }
        }
    }
    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        String title = ChatColor.stripColor(event.getView().getTitle());
        if (title.equalsIgnoreCase("EnchantGUI")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();

                if (plugin.itemstacks.get(player) != null) {
                    ItemStack playerItem = plugin.itemstacks.get(player);
                    Bukkit.getServer().broadcastMessage("ReadItem: " + playerItem.toString());
                    player.getInventory().addItem(plugin.itemstacks.get(player));
                    plugin.itemstacks.put(player, null);
                    //PlayerUtility.setPlayerMemory(player, memory);
                }
            }
        }
    }
}
