package dev.forbit.loot;

import dev.forbit.items.DarkItemFactory;
import dev.forbit.items.DarkItems;
import dev.forbit.library.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Random;

public class Loot {
    DarkItems main;

    public Loot(DarkItems main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(new LootEvents(main), main);
    }

    public static Inventory getChest(float chestLevel) {
        Inventory inventory = Bukkit.createInventory(null, 27, getTitle(chestLevel));
        Random random = new Random();
        // fill contents
        int items = 5 + (random.nextInt(2)-1);
        for (int i = 0; i < items; i++) {
            inventory.setItem(getRandomEmtptySlot(inventory), DarkItemFactory.getRandomItem(chestLevel));
        }
        return inventory;
    }

    private static int getRandomEmtptySlot(Inventory inventory) {
        if (inventory.firstEmpty() < 0) return -1;
        Random random = new Random();
        int slot = random.nextInt(inventory.getSize());
        while (!(inventory.getItem(slot) == null || inventory.getItem(slot).getType().equals(Material.AIR))) {
            slot = random.nextInt(inventory.getSize());
        }
        return slot;
    }

    public static String getTitle(float chestLevel) {
        return ChatColor.GRAY + "Level " + ChatColor.AQUA + Utils.toRoman((int) chestLevel);
    }
}
