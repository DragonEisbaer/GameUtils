package me.dragoneisbaer.minecraft.givemending.Listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SpawnFlight implements Listener {

    private final List<Player> flying = new ArrayList<>();

    @EventHandler
    public void onSpawnLoc(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        File f = new File(Bukkit.getUpdateFolder() + "/Locations/spawnloc.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        Location anfanglocation = new Location(Bukkit.getWorlds().get(0), cfg.getDouble("Anfang.X"), cfg.getDouble("Anfang.Y"), cfg.getDouble("Anfang.Z"));
        Location playerloc = player.getLocation();
        if (!player.isFlying()) {
            if (playerloc.distance(anfanglocation) <= 100) {
                player.setGliding(!(player.getVelocity().getY() <= 1.25));
                Block block = playerloc.add(0,-1,0).getBlock();
                Block block1 = playerloc.add(0, -2, 0).getBlock();
                player.setGliding(block.getType() == Material.AIR && block1.getType() == Material.AIR /*&& block2.getType() == Material.AIR && block3.getType() == Material.AIR*/);
                if (!flying.contains(player)) {
                    flying.add(player);
                }
            } else {
                if (flying.contains(player)) {
                    player.setGliding(true);
                    if (player.isOnGround()) {
                        flying.remove(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = ((Player) e.getEntity()).getPlayer();
            if (flying.contains(player) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }
    }
}
