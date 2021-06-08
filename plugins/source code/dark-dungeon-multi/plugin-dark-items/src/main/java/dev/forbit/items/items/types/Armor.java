package dev.forbit.items.items.types;

import dev.forbit.items.items.DarkItem;
import dev.forbit.library.ArmorType;
import lombok.Getter;
import lombok.Setter;

public abstract class Armor extends DarkItem implements Nameable {
    @Getter @Setter private ArmorType armorType;
    @Getter @Setter private ItemType type;

    abstract public double getProtection();

    public double getBaseProtection() {
        return type.getProtectionModifier();
    }



}
