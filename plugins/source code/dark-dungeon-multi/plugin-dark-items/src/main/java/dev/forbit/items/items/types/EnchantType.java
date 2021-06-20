package dev.forbit.items.items.types;

import dev.forbit.items.items.DarkItem;

import java.util.ArrayList;
import java.util.List;

public enum EnchantType {
    MAGIC(Enchantment.STRENGTH, Enchantment.LOOTING, Enchantment.RANGE, Enchantment.BLEED, Enchantment.PIERCING,
          Enchantment.ENERGISED
    ),
    RANGED(Enchantment.POWER, Enchantment.LOOTING, Enchantment.FIRE, Enchantment.PIERCING, Enchantment.ENERGISED),
    MELEE(Enchantment.SHARPNESS, Enchantment.LOOTING, Enchantment.FIRE, Enchantment.PIERCING, Enchantment.BLEED,
          Enchantment.BESERKER, Enchantment.ENERGISED, Enchantment.CUTDOWN
    ),
    ARMOR(Enchantment.PROTECTION, Enchantment.THORNS);

    private List<Enchantment> enchantments = new ArrayList<Enchantment>();

    EnchantType(Enchantment... enchants) {
        for (Enchantment e : enchants) {
            enchantments.add(e);
        }
    }

    public static boolean isCompatible(Enchantment e, DarkItem dItem) {
        /*if (dItem.isArmor()) {
            return ARMOR.getEnchantments().contains(e);
        }
        Weapon w = dItem.getWeapon();
        if (w instanceof RangedWeapon) {
            return RANGED.getEnchantments().contains(e);
        } else if (w instanceof MagicWeapon) {
            return MAGIC.getEnchantments().contains(e);
        } else if (w instanceof MeleeWeapon) {
            return MELEE.getEnchantments().contains(e);
        }
        return false;*/
        return true;
    }

    public List<Enchantment> getEnchantments() {
        return enchantments;
    }
}
