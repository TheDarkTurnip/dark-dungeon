package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public enum NPCType {
    FARMER("farmhouse", true, Material.WHEAT, "Farmer"),
    SMITH("", true, Material.ANVIL, "Item Smith"),
    HEALER("", true, Material.APPLE, "Healer"),
    ALCHEMIST("", true, Material.POTION, "Alchemist"),
    GUILD_REP("", true, Material.EMERALD, "Guild Representative"),
    INFUSER("", true, Material.NETHERITE_INGOT, "Infuser"),
    ADV_ITEM_SMITH("", false, Material.SMITHING_TABLE, "Advanced Item Smith"),
    ENCHANTER("", false, Material.ENCHANTING_TABLE, "Enchanter");

    @Getter @Setter String schemName;
    @Getter @Setter boolean level;
    @Getter @Setter Material menuMaterial;
    @Getter @Setter String title;
    NPCType(String schem, boolean Level, Material menuMaterial, String title) {
        setSchemName(schem);
        setLevel(level);
        setMenuMaterial(menuMaterial);
        setTitle(title);
    }
}
