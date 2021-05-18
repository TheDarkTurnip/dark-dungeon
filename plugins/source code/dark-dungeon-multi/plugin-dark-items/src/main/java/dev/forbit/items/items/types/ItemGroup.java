package dev.forbit.items.items.types;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public enum ItemGroup {
    WEAPON("Weapon"),
    ARMOR("Armor"),
    CONSUMABLE("Consumable"),
    UTILITY("Utility"),
    MATERIAL("Material"),
    CRATE_KEY("Crate Key"),
    ENCHANT_BOOK("Enchantment"),
    DOOR_KEY("Key"),
    FISHING_ROD("Fishing Rod"),
    FISH("Fish"),
    NULL("null"),
    TALISMAN("Talisman");

    @Getter private static final ChatColor color = ChatColor.DARK_GRAY;
    @Getter @Setter String title;

    ItemGroup(String s) {
        setTitle(s);
    }
}
