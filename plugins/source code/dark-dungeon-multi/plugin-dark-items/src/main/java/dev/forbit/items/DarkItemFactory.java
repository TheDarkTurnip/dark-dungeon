package dev.forbit.items;

import dev.forbit.items.items.DarkArmor;
import dev.forbit.items.items.DarkWeapon;
import dev.forbit.items.items.types.ItemType;
import dev.forbit.items.items.types.Modifier;
import dev.forbit.items.items.types.WeaponType;
import dev.forbit.library.ArmorType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Factory for generating Dark Items
 */
public class DarkItemFactory {

    public static ItemStack getRandomItem() {
        Random random = new Random();
        return random.nextBoolean() ? getRandomWeapon() : getRandomArmor();
    }

    public static ItemStack getRandomItem(float chestLevel) {
        Random random = new Random();
        return random.nextBoolean() ? getArmorPiece(pickRandomArmor(), chestLevel / 30.0f) : getLevelledWeapon(
                pickRandomWeapon(),
                chestLevel / 30.0f
        );

    }

    /**
     * @param armorType
     * @param level     0-1 (0 is basic wood item, 1 is plutonium)
     * @return
     */
    public static ItemStack getArmorPiece(ArmorType armorType, float level) {
        return getArmor(armorType, getItemType(level));
    }

    /**
     * @param weapon
     * @param level  0-1 (0 is basic wood item, 1 is plutonium)
     * @return
     */
    public static ItemStack getLevelledWeapon(WeaponType weapon, float level) {
        return getWeapon(weapon, getItemType(level));
    }

    /**
     * @param level 0-1 (0 is basic wood item, 1 is plutonium)
     * @return
     */
    public static ItemType getItemType(float level) {
        int max = ItemType.PLUTONIUM.getIdentifier();
        float value = level * max;
        Random random = new Random();
        float variation = (random.nextInt(2) - 1) / 10.0f;
        float n = value + variation;
        for (ItemType t : ItemType.values()) {
            if (t.getIdentifier() < n) continue;
            return t;
        }
        return ItemType.WOOD;
    }

    public static ItemStack getRandomWeapon() {
        ItemType type = ItemType.pickRandom();
        WeaponType weapon = pickRandomWeapon();
        DarkWeapon darkWeapon = new DarkWeapon();
        darkWeapon.setType(type);
        darkWeapon.setWeapon(weapon);
        darkWeapon.setMaterial(generateMaterial(weapon, type));
        do {
            darkWeapon.setModifier(Modifier.pickWeightedRandom());
        } while (!darkWeapon.getModifier().compatible(darkWeapon));
        return DarkItems.getApi().infuseItem(darkWeapon);
    }

    public static ItemStack getRandomWeapon(WeaponType weapon) {
        ItemType type = ItemType.pickRandom();
        DarkWeapon darkWeapon = new DarkWeapon();
        darkWeapon.setType(type);
        darkWeapon.setWeapon(weapon);
        darkWeapon.setMaterial(generateMaterial(weapon, type));
        do {
            darkWeapon.setModifier(Modifier.pickWeightedRandom());
        } while (!darkWeapon.getModifier().compatible(darkWeapon));
        return DarkItems.getApi().infuseItem(darkWeapon);
    }

    public static ItemStack getRandomWeapon(ItemType type) {
        WeaponType weapon = pickRandomWeapon();
        DarkWeapon darkWeapon = new DarkWeapon();
        darkWeapon.setType(type);
        darkWeapon.setWeapon(weapon);
        darkWeapon.setMaterial(generateMaterial(weapon, type));
        do {
            darkWeapon.setModifier(Modifier.pickWeightedRandom());
        } while (!darkWeapon.getModifier().compatible(darkWeapon));
        return DarkItems.getApi().infuseItem(darkWeapon);
    }

    public static ItemStack getWeapon(WeaponType weapon, ItemType type) {
        DarkWeapon darkWeapon = new DarkWeapon();
        darkWeapon.setType(type);
        darkWeapon.setWeapon(weapon);
        darkWeapon.setMaterial(generateMaterial(weapon, type));
        do {
            darkWeapon.setModifier(Modifier.pickWeightedRandom());
        } while (!darkWeapon.getModifier().compatible(darkWeapon));
        return DarkItems.getApi().infuseItem(darkWeapon);
    }

    public static ItemStack getRandomArmor() {
        DarkArmor darkArmor = new DarkArmor();
        darkArmor.setType(ItemType.pickRandom());
        darkArmor.setArmorType(pickRandomArmor());
        darkArmor.setMaterial(generateMaterial(darkArmor.getArmorType(), darkArmor.getType()));
        do {
            darkArmor.setModifier(Modifier.pickWeightedRandom());
        } while (!darkArmor.getModifier().compatible(darkArmor));
        return DarkItems.getApi().infuseItem(darkArmor);
    }

    public static ItemStack getRandomArmor(ArmorType armorType) {
        DarkArmor darkArmor = new DarkArmor();
        darkArmor.setType(ItemType.pickRandom());
        darkArmor.setArmorType(armorType);
        darkArmor.setMaterial(generateMaterial(armorType, darkArmor.getType()));
        do {
            darkArmor.setModifier(Modifier.pickWeightedRandom());
        } while (!darkArmor.getModifier().compatible(darkArmor));
        return DarkItems.getApi().infuseItem(darkArmor);
    }

    public static ItemStack getRandomArmor(ItemType type) {
        DarkArmor darkArmor = new DarkArmor();
        darkArmor.setType(type);
        darkArmor.setArmorType(pickRandomArmor());
        darkArmor.setMaterial(generateMaterial(darkArmor.getArmorType(), type));
        do {
            darkArmor.setModifier(Modifier.pickWeightedRandom());
        } while (!darkArmor.getModifier().compatible(darkArmor));
        return DarkItems.getApi().infuseItem(darkArmor);
    }

    public static ItemStack getArmor(ArmorType armorType, ItemType type) {
        DarkArmor darkArmor = new DarkArmor();
        darkArmor.setType(type);
        darkArmor.setArmorType(armorType);
        darkArmor.setMaterial(generateMaterial(armorType, type));
        do {
            darkArmor.setModifier(Modifier.pickWeightedRandom());
        } while (!darkArmor.getModifier().compatible(darkArmor));
        return DarkItems.getApi().infuseItem(darkArmor);
    }

    public static Material generateMaterial(WeaponType weapon, ItemType item) {
        if (weapon.equals(WeaponType.BOW)) {
            return Material.BOW;
        }
        String tool = weapon.getTool();
        String material = item.getMaterial();
        try {
            return Material.valueOf(material + "_" + tool);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Material generateMaterial(ArmorType armorType, ItemType type) {
        String piece = armorType.name();
        String material = type.getArmorMaterial();
        try {
            return Material.valueOf(material + "_" + piece);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    public static WeaponType pickRandomWeapon() {
        Random random = new Random();
        int val = random.nextInt(WeaponType.values().length);
        for (WeaponType type : WeaponType.values()) {
            if (val-- <= 0) return type;
        }
        return WeaponType.SWORD;
    }

    public static ArmorType pickRandomArmor() {
        Random random = new Random();
        int val = random.nextInt(ArmorType.values().length);
        for (ArmorType type : ArmorType.values()) {
            if (val-- <= 0) return type;
        }
        return ArmorType.CHESTPLATE;
    }

}
