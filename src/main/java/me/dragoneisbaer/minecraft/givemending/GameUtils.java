package me.dragoneisbaer.minecraft.givemending;

import me.dragoneisbaer.minecraft.givemending.Listeners.EnchantGUI;
import me.dragoneisbaer.minecraft.givemending.Listeners.SpawnFlight;
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
    private final List<Player> playerclicks = new ArrayList<>();

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

        getServer().getPluginManager().registerEvents(new EnchantGUI(), this);
        getServer().getPluginManager().registerEvents(new SpawnFlight(), this);
        getServer().getPluginManager().registerEvents(new me.dragoneisbaer.minecraft.givemending.Listeners.DisablePickup(), this);
        /*
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().log(Level.INFO, "DD: " + getItemstacks().values());
            }
        }.runTaskTimerAsynchronously(this, 0, 10);
         */
    }


    public HashMap<Player,ItemStack> getItemstacks() {
        return itemstacks;
    }
    public List<Player> getDispic() {
        return dispic;
    }
    public List<Player> getPlayerClicks() {
        return playerclicks;
    }
}
