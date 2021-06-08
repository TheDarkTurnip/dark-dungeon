package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public enum NPCType {
    FARMER("farmhouse", 4, Material.WHEAT, "Farmer"),
    SMITH("smith", 4, Material.ANVIL, "Item Smith"),
    HEALER("healer", 4, Material.APPLE, "Healer"),
    ALCHEMIST("", 4, Material.POTION, "Alchemist"),
    GUILD_REP("", 3, Material.EMERALD, "Guild Representative"),
    INFUSER("", Material.NETHERITE_INGOT, "Infuser"),
    ADV_ITEM_SMITH("", Material.SMITHING_TABLE, "Advanced Item Smith"),
    ENCHANTER("", Material.ENCHANTING_TABLE, "Enchanter");

    @Getter @Setter String schemName;
    @Getter @Setter int maxLevel;
    @Getter @Setter Material menuMaterial;
    @Getter @Setter String title;

    NPCType(String schem, Material menuMaterial, String title) {
        setSchemName(schem);
        setMaxLevel(0);
        setMenuMaterial(menuMaterial);
        setTitle(title);
    }

    NPCType(String schem, int maxLevel, Material menuMaterial, String title) {
        setSchemName(schem);
        setMaxLevel(maxLevel);
        setMenuMaterial(menuMaterial);
        setTitle(title);
    }

    public boolean isLevel() { return maxLevel > 0; }
}
