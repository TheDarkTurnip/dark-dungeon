package dev.forbit.items.events.custom;

import dev.forbit.items.DarkItems;
import dev.forbit.items.items.DarkItem;
import dev.forbit.library.DamageType;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class DarkDamageEvent extends Event {

    @Getter @Setter boolean cancelled = false;
    @Getter @Setter DarkEntity damaged = null; // can be player or mob
    @Getter @Setter DarkEntity damager = null; // can be player or mob
    @Getter @Setter double rawDamage = 0.0;
    @Getter @Setter HashMap<DamageType, Double> damageMap;
    @Getter @Setter @Nullable DarkItem weapon = null;
    @Getter @Setter float piercingValue = 0.0f;
    @Getter @Setter HashMap<DamageType, Float> changePercent;
    @Getter @Setter DarkDamageAction action = DarkDamageAction.MELEE;

    public DarkDamageEvent() {}


    public DarkDamageEvent(DarkEntity damager, DarkEntity damaged, @Nullable ItemStack item, DarkDamageAction action, HashMap<DamageType, Double> damageMap) {
        setDamaged(damaged);
        setDamager(damager);
        setWeapon(DarkItems.getApi().loadItem(item));
        setAction(action);
        setDamageMap(damageMap);
        setChangePercent(new HashMap<>());
        for (DamageType type : DamageType.values()) {
            getChangePercent().put(type, 0.0F);
        }
        setPiercingValue(0.0f);
        setRawDamage(0.0d);
        setCancelled(false);
    }


    public double getDamage() {
        double damage = 0.0;
        for (DamageType type : DamageType.values()) {
            damage += getDamageMap().get(type) * (1+getChangePercent().get(type));
        }
        return damage;
    }


    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
