package me.dragoneisbaer.minecraft.givemending.commands;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Timer;

public class RestartServer implements CommandExecutor {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.Restart")) {
                if (strings.length == 1) {
                    int tmp;
                    try {
                        tmp = Integer.parseInt(strings[0]);
                    }catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.DARK_RED + "Das ist keine Zahl!");
                        e.printStackTrace();
                        return false;
                    }
                    final int[] countdown = {tmp};
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (countdown[0] != 0){
                                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Restart in " + countdown[0] + " Sekunden!");
                                countdown[0] = countdown[0] - 1;
                            }else {
                                Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Restart!");
                                Bukkit.spigot().restart();
                            }
                        }
                    }.runTaskTimerAsynchronously(plugin, 0, 20);
                }else {
                    player.sendMessage(ChatColor.RED + "Gib einen Countdown an!");
                }
            }else {
                player.sendMessage(ChatColor.DARK_RED + "Dazu hast du keine Berechtigung!");
            }
        }
        return false;
    }
}
