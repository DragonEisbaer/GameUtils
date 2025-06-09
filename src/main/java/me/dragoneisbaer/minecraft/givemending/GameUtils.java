package me.dragoneisbaer.minecraft.givemending;

import me.dragoneisbaer.minecraft.givemending.Listeners.EnchantGUI;
import me.dragoneisbaer.minecraft.givemending.Listeners.SpawnFlightElytra;
import me.dragoneisbaer.minecraft.givemending.TabComplete.TCWarp;
import me.dragoneisbaer.minecraft.givemending.commands.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class GameUtils extends JavaPlugin {


    private final HashMap<Player,ItemStack> itemstacks = new HashMap<>();
    private final List<Player> dispic = new ArrayList<>();

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
        getCommand("ec").setExecutor(new Enderchest());
        getCommand("restart").setExecutor(new RestartServer());
        getCommand("spawn").setExecutor(new Spawn());

        getServer().getPluginManager().registerEvents(new EnchantGUI(), this);
        getServer().getPluginManager().registerEvents(new SpawnFlightElytra(), this);
        getServer().getPluginManager().registerEvents(new me.dragoneisbaer.minecraft.givemending.Listeners.DisablePickup(), this);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getLogger().info("Config: GameUtils geladen!");

        getLogger().info("Plugin: GameUtils geladen!");
    }


    public HashMap<Player,ItemStack> getItemstacks() {
        return itemstacks;
    }
    public List<Player> getDispic() {
        return dispic;
    }
}
