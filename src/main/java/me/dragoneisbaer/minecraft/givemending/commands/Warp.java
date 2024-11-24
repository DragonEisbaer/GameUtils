package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.warp")) {
                if (strings.length == 1) {
                    switch (strings[0]) {
                        case "end":
                            player.teleport(new Location(Bukkit.getWorld("world_the_end"),100,50,0));
                            break;
                        case "nether":
                            player.teleport(new Location(Bukkit.getWorld("world_nether"),81,92,-234));
                            break;
                        case "world":
                            player.teleport(new Location(Bukkit.getWorld("world"),743,103,-1824));
                            break;
                        default:
                            player.sendMessage("Error");
                            return false;
                    }
                }else if (strings.length == 0) {
                    player.sendMessage(ChatColor.RED + "Du musst eine Destination angeben. (End/Nether/World)");
                } else{
                    player.sendMessage(ChatColor.RED + "Zuviele Arguments.");
                }
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dazu hast du keine Berechtigung!");
            }
        }else {
            commandSender.sendMessage(ChatColor.DARK_RED + "Du musst ein Spieler sein!");
        }
        return false;
    }
}
