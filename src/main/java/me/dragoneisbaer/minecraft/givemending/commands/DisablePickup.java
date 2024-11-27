package me.dragoneisbaer.minecraft.givemending.commands;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DisablePickup implements CommandExecutor {

    private final GameUtils plugin;
    public DisablePickup(GameUtils plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.DisablePickup")) {
                if (plugin.getDispic().contains(player)) {
                    plugin.getDispic().remove(player);
                    player.sendMessage(ChatColor.RED + "Du kannst nun wieder Items aufheben.");
                } else if (!plugin.getDispic().contains(player)) {
                    plugin.getDispic().add(player);
                    player.sendMessage(ChatColor.GREEN + "Du kannst nun keine Items aufheben.");
                }
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dafür hast du keine Berechtigung!");
            }
        }else {
            commandSender.sendMessage(ChatColor.DARK_RED + "Du musst ein Spieler sein!");
        }

        return false;
    }
}
