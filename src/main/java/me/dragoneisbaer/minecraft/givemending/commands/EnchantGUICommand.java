package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantGUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

    if (commandSender instanceof Player) {
        Player player = (Player) commandSender;
        if (player.hasPermission("Utils.EnchantGUI")) {
            int levelint = 0;
            String guiName = ChatColor.DARK_BLUE + "EnchantGUI";
            Inventory gui = Bukkit.createInventory(player, 27, guiName);

            //Items
            ItemStack search = new ItemStack(Material.OAK_SIGN);
            ItemStack level = new ItemStack(Material.BOOK);
            ItemStack allench = new ItemStack(Material.BOOKSHELF);

            //SearchConf
            ItemMeta search_meta = search.getItemMeta();
            search_meta.setDisplayName(ChatColor.BLUE + "Suche");
            search.setItemMeta(search_meta);

            //LevelConf
            ItemMeta level_meta = level.getItemMeta();
            level_meta.setDisplayName(ChatColor.DARK_GREEN + "Level: " + levelint);
            level.setItemMeta(level_meta);

            //AllenchConf
            ItemMeta allench_meta = allench.getItemMeta();
            allench_meta.setDisplayName(ChatColor.RED + "Alle Enchantments");
            allench.setItemMeta(allench_meta);

            //SetPanes
            for (int i = 0;gui.getSize()>i;i++) {
                gui.setItem(i,new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }

            //SetItems
            gui.setItem(10, new ItemStack(Material.AIR));
            gui.setItem(12, search);
            gui.setItem(14, level);
            gui.setItem(16, allench);

            //GuiConf
            gui.setMaxStackSize(1);

            player.openInventory(gui);

        }else {
            player.sendMessage(ChatColor.DARK_RED + "Du hast keine Berechtigung dafür! Sprich mit deinem Admin.");
        }
    }else {
        commandSender.sendMessage(ChatColor.RED + "Du musst ein Spieler sein!");
    }
        return false;
    }
}
