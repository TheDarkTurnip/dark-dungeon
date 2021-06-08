package dev.forbit.items.items.types;

import lombok.Getter;
import lombok.Setter;

public enum WeaponType {
    STAFF(WeaponGroup.MAGIC, "HOE"),
    BOW(WeaponGroup.RANGED, "BOW"),
    DART_SHOOTER(WeaponGroup.RANGED, "SHOVEL"),
    SLING(WeaponGroup.RANGED,"SHOVEL"),
    CLUB(WeaponGroup.MELEE, "SWORD"),
    DAGGER(WeaponGroup.MELEE, "SWORD"),
    SWORD(WeaponGroup.MELEE, "SWORD");

    @Getter @Setter WeaponGroup type;
    @Getter @Setter String tool;
    WeaponType(WeaponGroup type, String tool) {
        setType(type);
        setTool(tool);
    }

    public String getName() {
        switch (this) {
            case STAFF:
                return "Staff";
            case BOW:
                return "Bow";
            case DART_SHOOTER:
                return "DartShooter";
            case SLING:
                return "Sling";
            case CLUB:
                return "Club";
            case DAGGER:
                return "Dagger";
            case SWORD:
                return "Sword";
        }
        return "null";
    }
}
