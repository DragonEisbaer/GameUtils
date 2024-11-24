package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MendingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.giveMending")) {
                player.getInventory().getItemInMainHand().addEnchantment(Enchantment.MENDING, 1);
                player.sendMessage(ChatColor.DARK_GREEN + "Mending wurde auf deinem Item hinzugefügt!");
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dazu hast du keine Berechtigung!");
            }
        }
        return false;
    }
}
