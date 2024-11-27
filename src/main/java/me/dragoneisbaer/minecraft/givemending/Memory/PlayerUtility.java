package me.dragoneisbaer.minecraft.givemending.Memory;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerUtility {
    private static final Map<String, PlayerMemory> playerMemory = new HashMap<>();
    public static PlayerMemory getPlayerMemory(Player player) {
        if (!playerMemory.containsKey(player.getUniqueId().toString())) {
            PlayerMemory m = new PlayerMemory();
            playerMemory.put(player.getUniqueId().toString(), m);
            return m;
        }
        return playerMemory.get(player.getUniqueId().toString());
    }

    public static void setPlayerMemory(Player player, PlayerMemory memory) {
        if (memory == null) playerMemory.remove(player.getUniqueId().toString());
        else playerMemory.put(player.getUniqueId().toString(), memory);
    }
}
