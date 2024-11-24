package me.dragoneisbaer.minecraft.givemending.commands;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("GameUtils.SetSpawn")) {
                Location playerloc = player.getLocation();
                File f = new File(Bukkit.getUpdateFolder() + "/Locations/spawnloc.yml");
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                cfg.set("Anfang.World", playerloc.getWorld().getName());
                cfg.set("Anfang.Y", playerloc.getY());
                cfg.set("Anfang.X", playerloc.getX());
                cfg.set("Anfang.Z", playerloc.getZ());
                try {
                    cfg.save(f);
                    player.sendMessage(ChatColor.DARK_GREEN + "Spawn gesetzt.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
