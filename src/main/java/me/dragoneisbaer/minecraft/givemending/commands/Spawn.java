package me.dragoneisbaer.minecraft.givemending.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission("GameUtils.spawn")) {
            if (commandSender instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) commandSender;
                File f = new File(Bukkit.getUpdateFolder() + "/Locations/spawnloc.yml");
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

                Location spawnloacation = new Location(Bukkit.getWorlds().get(0), cfg.getDouble("Anfang.X"), cfg.getDouble("Anfang.Y"), cfg.getDouble("Anfang.Z"));

                player.teleport(spawnloacation);
                player.sendMessage(ChatColor.GOLD + "Du wurdest zum Spawn teleportiert!");
            }
        }
        return false;
    }
}
