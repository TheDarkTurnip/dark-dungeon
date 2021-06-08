package dev.forbit.items.items.types;

import dev.forbit.library.rarity.Rarity;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public enum ItemType {
    WOOD(1, 1, 1, "WOODEN", "LEATHER"),
    BONE(2, 1.7, 2, "WOODEN", "LEATHER"),
    FLINT(3, 2.4, 3, "STONE", "CHAINMAIL"),
    STONE(4, 3.1, 4, "STONE", "CHAINMAIL"),
    TIN(5, 3.9, 5, "STONE", "CHAINMAIL"),
    SILVER(6, 4.7, 6, "STONE", "CHAINMAIL"),
    COPPER(7, 5.5, 7, "GOLDEN"),
    BRONZE(8, 6.3, 8, "GOLDEN"),
    GOLD(9, 7.2, 9, "GOLDEN"),
    TUNGSTEN(10, 8.1, 10, "IRON"),
    PLATINUM(11, 9.0, 11, "IRON"),
    IRON(12, 10.0, 12, "IRON"),
    STEEL(13, 11.0, 13, "IRON"),
    OBSIDIAN(14, 12.0, 14, "IRON"),
    COBALT(15, 13.1, 15, "DIAMOND"),
    DIAMOND(16, 14.2, 16, "DIAMOND"),
    TITANIUM(17, 15.3, 17, "DIAMOND"),
    URANIUM(18, 16.6, 18, "DIAMOND"),
    PLUTONIUM(19, 18.0, 19, "DIAMOND"),
    MOLTEN(20, 19.5, 20, "NETHERITE"),
    MAGMA(21, 21.1, 21, "NETHERITE"),
    STARLIGHT(22, 23.0, 22, "NETHERITE"),
    RAINBOWROCK(23, 25.0, 23, "DIAMOND"),
    TURNIP(24, 30.0, 25, "DIAMOND");

    @Getter @Setter private int identifier;
    @Getter @Setter private double damageModifier;
    @Getter @Setter private double protectionModifier;
    @Getter @Setter private String material;
    @Getter @Setter private String armorMaterial;

    ItemType(int i, double damage, double protection, String material, String armorMaterial) {
        setIdentifier(i);
        setDamageModifier(damage);
        setProtectionModifier(protection);
        setMaterial(material);
        setArmorMaterial(armorMaterial);

    }
    ItemType(int i, double damage, double protection, String material) {
        setIdentifier(i);
        setDamageModifier(damage);
        setProtectionModifier(protection);
        setMaterial(material);
        setArmorMaterial(material);
    }

    @Nullable public static ItemType pickRandom() {
        int amount = ItemType.values().length-5; // up to plutonium.
        Random rand = new Random();
        int val = rand.nextInt(amount) + 1;
        for (ItemType type : ItemType.values()) {
            if (val == type.getIdentifier()) { return type; }
        }
        return null;
    }

    public Rarity getRarity() {
        switch (this) {
            case WOOD:
            case BONE:
            default:
                return Rarity.COMMON;
            case FLINT:
            case STONE:
            case TIN:
            case SILVER:
                return Rarity.UNCOMMON;
            case COPPER:
            case BRONZE:
            case GOLD:
            case TUNGSTEN:
                return Rarity.UNIQUE;
            case PLATINUM:
            case IRON:
            case STEEL:
            case OBSIDIAN:
                return Rarity.RARE;
            case DIAMOND:
            case TITANIUM:
                return Rarity.EPIC;
            case URANIUM:
                return Rarity.SUPER;
            case PLUTONIUM:
            case RAINBOWROCK:
                return Rarity.MASTERFUL;
            case TURNIP:
            case MOLTEN:
                return Rarity.LEGENDARY;
        }
    }
}
