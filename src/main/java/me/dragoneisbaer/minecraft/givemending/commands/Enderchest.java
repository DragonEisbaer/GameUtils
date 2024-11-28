package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class Enderchest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.Enderchest")) {
                if (strings.length == 0) {
                    player.openInventory(player.getEnderChest());
                } else if (strings.length == 1) {
                    if (Bukkit.getPlayerExact(strings[0]) != null) {
                        Player target = Bukkit.getPlayerExact(strings[0]);
                        assert target != null;
                        player.openInventory(target.getEnderChest());
                    }
                }
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dazu hast du keine Berechtigung!");
            }
        }
        return false;
    }
}
