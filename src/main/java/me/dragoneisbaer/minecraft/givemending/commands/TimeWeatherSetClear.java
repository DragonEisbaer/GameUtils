package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimeWeatherSetClear implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission("GameUtils.timeweather.clear")) {
            commandSender.getServer().getWorld("world").setTime(1000);
            commandSender.getServer().getWorld("world").setStorm(false);
            commandSender.getServer().getWorld("world").setThundering(false);
            commandSender.sendMessage("§aZeit und Wetter wurden auf day und clear gesetzt.");
            return true;
        } else {
            commandSender.sendMessage("§cDu hast keine Berechtigung, diesen Befehl auszuführen.");
        }
        return false;
    }
}
