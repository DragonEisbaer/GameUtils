package me.dragoneisbaer.minecraft.givemending;

import me.dragoneisbaer.minecraft.givemending.Listeners.EnchantGUI;
import me.dragoneisbaer.minecraft.givemending.Memory.PlayerUtility;
import me.dragoneisbaer.minecraft.givemending.TabComplete.TCWarp;
import me.dragoneisbaer.minecraft.givemending.commands.EnchantGUICommand;
import me.dragoneisbaer.minecraft.givemending.commands.MendingCommand;
import me.dragoneisbaer.minecraft.givemending.commands.RemoveMending;
import me.dragoneisbaer.minecraft.givemending.commands.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class GameUtils extends JavaPlugin {

    public HashMap<Player,ItemStack> itemstacks = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("mending").setExecutor(new MendingCommand());
        getCommand("removemending").setExecutor(new RemoveMending());
        getCommand("warp").setExecutor(new Warp());
        getCommand("warp").setTabCompleter(new TCWarp());
        getCommand("enchantgui").setExecutor(new EnchantGUICommand());

        getServer().getPluginManager().registerEvents(new EnchantGUI(this), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage(itemstacks.get(Bukkit.getPlayer("DragonEisbaer")).toString());
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
