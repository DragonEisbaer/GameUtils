package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.Memory.PlayerMemory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SpawnFlight implements Listener {

    private final List<Player> flying = new ArrayList<>();

    @EventHandler
    public void onSpawnLoc(PlayerMoveEvent e) {
        File f = new File(Bukkit.getUpdateFolder() + "/Locations/spawnloc.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        Location anfanglocation = new Location(Bukkit.getWorlds().get(0), cfg.getDouble("Anfang.X"), cfg.getDouble("Anfang.Y"), cfg.getDouble("Anfang.Z"));
        Player player = e.getPlayer();
        Location playerloc = player.getLocation();
        if (playerloc.distance(anfanglocation) <= 100) {
            Block block = playerloc.add(0,-2,0).getBlock();
            if (block.getType() == Material.AIR) {
                player.setGliding(true);
            }
            if (!flying.contains(player)) {
                flying.add(player);
            }
        }else {
            if (flying.contains(player)) {
                player.setGliding(true);
                if (player.isOnGround()) {
                    flying.remove(player);
                }
            }
        }
    }
}
