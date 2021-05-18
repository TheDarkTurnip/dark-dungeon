package dev.forbit.items.items.types;

import dev.forbit.items.items.DarkItem;

public enum ModifierType {
    MELEE,
    RANGED,
    MAGIC,
    ARMOR,
    UNIVERSAL,
    WEAPON;

    public boolean compatible(DarkItem item) {
        /*switch (this) {
            case UNIVERSAL:
                return true;
            case MELEE:
                if (DarkItems.isSword(mat)) {
                    return true;
                }
                return false;
            case RANGED:
                if (DarkItems.isRanged(mat)) {
                    return true;
                }
                return false;
            case MAGIC:
                if (DarkItems.isMagic(mat)) {
                    return true;
                }
                return false;
            case ARMOR:
                if (DarkItem.isBoots(mat) || DarkItem.isChestplate(mat) || DarkItem.isLeggings(mat) || DarkItem.isHelmet(mat)) {
                    return true;
                }
                return false;
            case WEAPON:
                if (DarkItems.isSword(mat) || DarkItems.isRanged(mat) || DarkItems.isMagic(mat)) {
                    return true;
                }
                return false;
            default: return false;
        }*/
        switch (this) {
            case UNIVERSAL:
                return true;
            case MELEE:
                if (item instanceof Weapon) {
                    return ((Weapon) item).getWeapon().getType().equals(WeaponGroup.MELEE);
                } else {
                    return false;
                }
            case RANGED:
                if (item instanceof Weapon) {
                    return ((Weapon) item).getWeapon().getType().equals(WeaponGroup.RANGED);
                } else {
                    return false;
                }
            case MAGIC:
                if (item instanceof Weapon) {
                    return ((Weapon) item).getWeapon().getType().equals(WeaponGroup.MAGIC);
                } else {
                    return false;
                }
            case WEAPON:
                return item instanceof Weapon;
            case ARMOR:
                return item instanceof Armor;
        }
        return false;
    }

}
