package dev.forbit.items.items.types;

import dev.forbit.items.items.DarkItem;
import dev.forbit.library.DamageType;
import lombok.Getter;
import lombok.Setter;

public abstract class Weapon extends DarkItem implements Nameable {
    @Getter @Setter private WeaponType weapon;
    @Getter @Setter private ItemType type;

    abstract public double getDamage();

    public double getBaseDamage() {
        switch (this.getWeapon()) {
            case STAFF:
            case BOW:
            case DAGGER:
                return 1.0;
            case DART_SHOOTER:
            case SLING:
            case SWORD:
                return 1.1;
            case CLUB:
                return 0.9;
        }
        return 0.5;
    }

    public DamageType getDamageType() {
        switch (this.getWeapon()) {
            case STAFF:
                return DamageType.MAGIC;
            default:
                return DamageType.NORMAL;
        }
    }

}
