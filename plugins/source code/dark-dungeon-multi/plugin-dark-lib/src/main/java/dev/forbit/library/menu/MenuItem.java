package dev.forbit.library.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface MenuItem {
    Material getMaterial();
    String getName();
    List<String> getLore();
    Runnable getClickAction();

    default ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
        if (getLore() != null) meta.setLore(getLore());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

}
