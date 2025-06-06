package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class RemoveMending implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if (player.hasPermission("GameUtils.RemoveMending")) {
                player.getInventory().getItemInMainHand().removeEnchantment(Enchantment.MENDING);
                player.sendMessage(ChatColor.DARK_GREEN + "Mending wurde von deinem Item entfernt!");
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dazu hast du keine Berechtigung!");
            }
        }else {
            commandSender.sendMessage(ChatColor.DARK_RED + "Du musst ein Spieler sein!");
        }

        return true;
    }
}
