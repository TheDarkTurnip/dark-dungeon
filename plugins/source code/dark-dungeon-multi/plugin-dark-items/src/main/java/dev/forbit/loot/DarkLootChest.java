package dev.forbit.loot;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

public @Data class DarkLootChest {
    ItemStack[] contents;
    String title;
    int level;

    public DarkLootChest(float chestLevel) {
        setContents(Loot.getChest(chestLevel).getStorageContents());
        setTitle(Loot.getTitle(chestLevel));

    }
}
