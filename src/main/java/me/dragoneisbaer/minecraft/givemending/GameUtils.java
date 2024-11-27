package me.dragoneisbaer.minecraft.givemending;

import me.dragoneisbaer.minecraft.givemending.Listeners.EnchantGUI;
import me.dragoneisbaer.minecraft.givemending.Listeners.SpawnFlight;
import me.dragoneisbaer.minecraft.givemending.Memory.PlayerUtility;
import me.dragoneisbaer.minecraft.givemending.TabComplete.TCWarp;
import me.dragoneisbaer.minecraft.givemending.commands.*;
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
    public List<Player> dispic = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("mending").setExecutor(new MendingCommand());
        getCommand("removemending").setExecutor(new RemoveMending());
        getCommand("warp").setExecutor(new Warp());
        getCommand("warp").setTabCompleter(new TCWarp());
        getCommand("enchantgui").setExecutor(new EnchantGUICommand());
        getCommand("setspawn").setExecutor(new SetSpawn());
        getCommand("dispic").setExecutor(new DisablePickup(this));

        getServer().getPluginManager().registerEvents(new EnchantGUI(), this);
        getServer().getPluginManager().registerEvents(new SpawnFlight(), this);
        getServer().getPluginManager().registerEvents(new me.dragoneisbaer.minecraft.givemending.Listeners.DisablePickup(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HashMap<Player,ItemStack> getItemstacks() {
        return itemstacks;
    }
    public void setItemstacks(HashMap<Player,ItemStack> itemstacks) {
        this.itemstacks = itemstacks;
    }

    public List<Player> getDispic() {
        return dispic;
    }
}
