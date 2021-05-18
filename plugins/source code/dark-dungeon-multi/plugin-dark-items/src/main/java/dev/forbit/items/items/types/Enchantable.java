package dev.forbit.items.items.types;


import java.util.HashMap;

public interface Enchantable {

    default boolean hasEnchantment(Enchantment enchantment) {
        return getEnchantments() != null && getEnchantments().containsKey(enchantment);
    }

    default int getEnchantment(Enchantment enchantment) {
        return hasEnchantment(enchantment) ? getEnchantments().get(enchantment) : 0;
    }

    default void addEnchantment(Enchantment enchantment, int level) {
        if (hasEnchantment(enchantment)) {
            int oldLevel = getEnchantment(enchantment);
            if (oldLevel == level) {
                getEnchantments().put(enchantment,level+1);
            } else if (level > oldLevel) {
                getEnchantments().put(enchantment, level);
            }
        } else {
            getEnchantments().put(enchantment, level);
        }
    }

    HashMap<Enchantment, Integer> getEnchantments();



}
