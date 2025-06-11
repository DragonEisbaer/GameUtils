package me.dragoneisbaer.minecraft.givemending.Listeners;

import me.dragoneisbaer.minecraft.givemending.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnFlightElytra implements Listener {

    Plugin plugin = GameUtils.getPlugin(GameUtils.class);

    private final List<Player> flying = new ArrayList<>();
    private final HashMap<Player,ItemStack> itemssave = new HashMap<>();

    @EventHandler
    public void onSpawnFlight(PlayerMoveEvent e) {

        if (!plugin.getConfig().getBoolean("FlightEnabled")) {
            return;
        }

        Player player = e.getPlayer();

        File f = new File(Bukkit.getUpdateFolder() + "/Locations/spawnloc.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        Location anfanglocation = new Location(Bukkit.getWorlds().get(0), cfg.getDouble("Anfang.X"), cfg.getDouble("Anfang.Y"), cfg.getDouble("Anfang.Z"));
        Location playerloc = player.getLocation();

        ItemStack Elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = Elytra.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "SpawnElytra");
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        meta.addEnchant(Enchantment.BINDING_CURSE,1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setEnchantmentGlintOverride(false);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Diese Elytra ist nur geliehen :)");
        meta.setLore(lore);
        Elytra.setItemMeta(meta);

        if (!player.getWorld().equals(Bukkit.getWorlds().get(0))) {
            return;
        }

        if (playerloc.distance(anfanglocation) <= plugin.getConfig().getInt("FlightRadius")) {
            if (player.isGliding()) {
                return;
            }
            if (player.isOnGround()) {
                return;
            }
            if (player.getInventory().getChestplate() != null) {
                if (player.getInventory().getChestplate() != Elytra) {
                    if (!itemssave.containsKey(player)) {
                        if (!player.getInventory().getChestplate().equals(Elytra)) {
                            if (player.getInventory().getChestplate().hasItemMeta()) {
                                if (player.getInventory().getChestplate().getItemMeta().getDisplayName().equals(Elytra.getItemMeta().getDisplayName())) {
                                    return;
                                }
                            }
                            itemssave.put(player, player.getInventory().getChestplate());
                            if  (player.getInventory().getChestplate().getItemMeta().hasDisplayName()) {
                                player.sendMessage(ChatColor.DARK_GREEN + player.getInventory().getChestplate().getItemMeta().getDisplayName() + " gespeichert!");
                            }else {
                                player.sendMessage(ChatColor.DARK_GREEN + "Deine Chestplate wurde gespeichert!");
                            }
                        }
                    }

                    player.getInventory().setChestplate(Elytra);
                    if (!flying.contains(player)) {
                        flying.add(player);
                    }
                }
            }else if (player.getInventory().getChestplate() == null) {
                player.getInventory().setChestplate(Elytra);
                if (!flying.contains(player)) {
                    flying.add(player);
                }
            }

        } else {
            if (player.isOnGround() && flying.contains(player)) {
                player.setFallDistance(0);
                player.getInventory().setChestplate(null);
                if (itemssave.containsKey(player)) {
                    player.getInventory().setChestplate(itemssave.get(player));
                    if  (itemssave.get(player).getItemMeta().hasDisplayName()) {
                        player.sendMessage(ChatColor.GREEN + itemssave.get(player).getItemMeta().getDisplayName() + " wieder hergestellt!");
                    }else {
                        player.sendMessage(ChatColor.GREEN + "Deine Chestplate wurde wieder hergestellt!");
                    }
                    itemssave.remove(player);
                }
                flying.remove(player);
            }
        }
    }
}
