package dev.forbit.items.events.custom;

import dev.forbit.library.DamageType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DarkPlayerDamagePlayerEvent extends DarkDamageEvent {

    public DarkPlayerDamagePlayerEvent(DarkPlayer damager, DarkPlayer damaged, ItemStack item, DarkDamageAction action, HashMap<DamageType, Double> damageMap) {
        super(damager, damaged, item, action, damageMap);
        setDamager(damager);
        setDamaged(damaged);

    }
}
