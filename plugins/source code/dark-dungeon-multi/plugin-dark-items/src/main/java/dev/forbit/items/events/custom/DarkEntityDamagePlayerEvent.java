package dev.forbit.items.events.custom;

import dev.forbit.library.DamageType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Entity damage player (EvP)
 */
public class DarkEntityDamagePlayerEvent extends DarkDamageEvent {

    public DarkEntityDamagePlayerEvent(DarkEntity damager, DarkPlayer damaged, ItemStack item, DarkDamageAction action, HashMap<DamageType, Double> damageMap) {
        super(damager, damaged, item, action, damageMap);
        setDamager(damager);
        setDamaged(damaged);
    }
}
