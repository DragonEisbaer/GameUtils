package me.dragoneisbaer.minecraft.givemending.Memory;

import org.bukkit.inventory.ItemStack;

public class PlayerMemory {
    private ItemStack itemstack;

    public ItemStack getItem() {
        return itemstack;
    }

    public void setItem(ItemStack item) {
        this.itemstack = item;
    }
}
