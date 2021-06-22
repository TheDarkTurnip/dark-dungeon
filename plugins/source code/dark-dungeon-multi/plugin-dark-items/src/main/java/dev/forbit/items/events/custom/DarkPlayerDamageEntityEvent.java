package dev.forbit.items.events.custom;

import dev.forbit.library.DamageType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Player damages entity (PvE)
 */
 public class DarkPlayerDamageEntityEvent extends DarkDamageEvent {

    public DarkPlayerDamageEntityEvent(DarkPlayer damager, DarkEntity damaged, ItemStack item, DarkDamageAction action, HashMap<DamageType, Double> damageMap) {
        super(damager, damaged, item, action, damageMap);
        setDamager(damager);
        setDamaged(damaged);
    }
}
