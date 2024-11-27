package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DisablePickup implements Listener {

    GameUtils plugin = JavaPlugin.getPlugin(GameUtils.class);

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (plugin.getDispic().contains(player)) {
            e.setCancelled(true);
        }
    }
}
