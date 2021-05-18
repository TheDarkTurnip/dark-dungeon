package dev.forbit.library.player;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

public @Data class DarkInventory {
    ItemStack[] items = new ItemStack[35];
}
