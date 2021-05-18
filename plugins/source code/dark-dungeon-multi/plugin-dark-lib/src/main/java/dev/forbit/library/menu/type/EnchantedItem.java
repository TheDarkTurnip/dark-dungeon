package dev.forbit.library.menu.type;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantedItem extends BasicItem {

    public EnchantedItem(Material material, String itemName, String lore, Runnable action) {
        super(material, itemName, lore, action);
    }

    @Override public ItemStack getItemStack() {
        ItemStack item = super.getItemStack();
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        item.setItemMeta(meta);
        return item;
    }
}
