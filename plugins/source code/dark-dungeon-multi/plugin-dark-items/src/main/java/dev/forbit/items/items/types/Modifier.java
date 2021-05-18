package dev.forbit.items.items.types;

import dev.forbit.items.items.DarkItem;
import dev.forbit.library.rarity.Rarity;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Random;

public enum Modifier {
    VOID(ModifierType.UNIVERSAL, Rarity.JUNK),
    // sword damage
    POINTY(ModifierType.MELEE, Rarity.COMMON),
    SHARP(ModifierType.MELEE, Rarity.UNCOMMON),
    STRONG(ModifierType.MELEE, Rarity.UNIQUE),
    KEEN(ModifierType.MELEE, Rarity.UNIQUE),
    SUPERIOR(ModifierType.MELEE, Rarity.RARE),
    FORCEFUL(ModifierType.MELEE, Rarity.RARE),
    HURTFUL(ModifierType.MELEE, Rarity.EPIC),
    DANGEROUS(ModifierType.MELEE, Rarity.EPIC),
    SAVAGE(ModifierType.MELEE, Rarity.SUPER),

    // weapon damage
    RUTHLESS(ModifierType.WEAPON, Rarity.RARE),
    NASTY(ModifierType.WEAPON, Rarity.RARE),
    ZEALOUS(ModifierType.WEAPON, Rarity.EPIC),
    DEMONIC(ModifierType.WEAPON, Rarity.SUPER),
    UNREAL(ModifierType.WEAPON, Rarity.SUPER),
    DEADLY(ModifierType.WEAPON, Rarity.SUPER),
    MURDEROUS(ModifierType.WEAPON, Rarity.MASTERFUL),

    // everything
    GODLY(ModifierType.UNIVERSAL, Rarity.MASTERFUL),
    LEGENDARY(ModifierType.UNIVERSAL, Rarity.LEGENDARY),

    // movement speed
    HASTY(ModifierType.ARMOR, Rarity.COMMON),
    QUICK(ModifierType.ARMOR, Rarity.UNCOMMON),
    AGILE(ModifierType.ARMOR, Rarity.UNIQUE),
    NIMBLE(ModifierType.ARMOR, Rarity.EPIC),

    // ranged damage
    SIGHTED(ModifierType.RANGED, Rarity.COMMON),
    RAPID(ModifierType.RANGED, Rarity.UNCOMMON),
    INTIMIDATING(ModifierType.RANGED, Rarity.UNIQUE),
    STAUNCH(ModifierType.RANGED, Rarity.RARE),
    POWERFUL(ModifierType.RANGED, Rarity.EPIC),

    // magic damage
    MYSTIC(ModifierType.MAGIC, Rarity.UNCOMMON),
    ADEPT(ModifierType.MAGIC, Rarity.UNIQUE),
    CELESTIAL(ModifierType.MAGIC, Rarity.RARE),
    MANIC(ModifierType.MAGIC, Rarity.EPIC),
    MYTHICAL(ModifierType.MAGIC, Rarity.MASTERFUL),

    // protection
    LARGE(ModifierType.ARMOR, Rarity.COMMON),
    MASSIVE(ModifierType.ARMOR, Rarity.UNCOMMON),
    HARD(ModifierType.ARMOR, Rarity.UNIQUE),
    STURDY(ModifierType.ARMOR, Rarity.RARE),
    GUARDING(ModifierType.ARMOR, Rarity.EPIC),
    ARMORED(ModifierType.ARMOR, Rarity.SUPER),
    WARDING(ModifierType.ARMOR, Rarity.SUPER);

    @Getter @Setter @NonNull ModifierType type;
    @Getter @Setter @NonNull Rarity rarity;

    Modifier(@NonNull ModifierType type, @NonNull Rarity rarity) {
        setType(type);
        setRarity(rarity);

    }

    @Nullable public static Modifier pickRandom() {
        int amount = Modifier.values().length - 1;
        Random rand = new Random();
        int val = rand.nextInt(amount - 1) + 1;
        int count = 0;
        for (Modifier m : Modifier.values()) {
            if (val == count) {
                return m;
            }
            else {
                count++;
            }
        }
        return null;
    }

    @Nullable public static Modifier pickWeightedRandom() {
        Modifier m;
        Rarity rarity = Rarity.getRandomWeighted();
        int count = 0;
        // get random modifier with rarity (rarity)
        do {
            m = Modifier.pickRandom();
            rarity = Rarity.getRandomWeighted();
            count++;
            if (count > 50000) {
                return pickRandom();
            }
        } while (m == null || !m.getRarity().equals(rarity));
        return m;
    }

    @Deprecated public double getProtectionModifier() {
        switch (this) {
            case LARGE:
                return 1.1;
            case MASSIVE:
                return 1.2;
            case HARD:
            case STURDY:
                return 1.3;
            case GUARDING:
            case ARMORED:
                return 1.4;
            case WARDING:
            case GODLY:
                return 1.5;
            case LEGENDARY:
                return 1.75;
            default:
                return 1;
        }
    }


    @Deprecated public double getVelocityModifier() {
        switch (this) {
            case ADEPT:
            case RAPID:
                return 1.1;
            case INTIMIDATING:
            case CELESTIAL:
                return 1.2;
            case MANIC:
            case STAUNCH:
                return 1.3;
            case POWERFUL:
            case MYTHICAL:
            case GODLY:
                return 1.4;
            case LEGENDARY:
                return 1.5;
            default:
                return 1;
        }
    }


    @Deprecated public double getMovementSpeedModifier() {
        switch (this) {
            case HASTY:
            case POWERFUL:
            case SAVAGE:
                return 0.02;
            case QUICK:
            case ZEALOUS:
            case RUTHLESS:
            case DEMONIC:
                return 0.05;
            case AGILE:
            case GODLY:
                return 0.1;
            case NIMBLE:
                return 0.125;
            case LEGENDARY:
                return 0.15;
            default:
                return 0;
        }
    }

    @Deprecated public double getDamageModifier() {
        switch (this) {
            case POINTY:
            case SHARP:
            case SIGHTED:
                return 1.1;
            case STRONG:
            case KEEN:
            case RAPID:
            case MYSTIC:
                return 1.2;
            case SUPERIOR:
            case FORCEFUL:
            case RUTHLESS:
            case NASTY:
            case INTIMIDATING:
            case ADEPT:
                return 1.3;
            case HURTFUL:
            case DANGEROUS:
            case DEMONIC:
            case ZEALOUS:
            case UNREAL:
            case STAUNCH:
            case CELESTIAL:
            case MANIC:
                return 1.4;
            case SAVAGE:
            case DEADLY:
            case MURDEROUS:
            case GODLY:
            case POWERFUL:
            case MYTHICAL:
                return 1.5;
            case LEGENDARY:
                return 1.75;
            default:
                return 1;
        }
    }


    @Deprecated public double getKnockbackModifier() {
        switch (this) {
            case KEEN:
                return 1.1;
            case FORCEFUL:
            case SUPERIOR:
                return 1.2;
            case HURTFUL:
            case DANGEROUS:
                return 1.3;
            case SAVAGE:
                return 1.4;
            case GODLY:
            case LEGENDARY:
                return 1.5;
            default:
                return 1;
        }
    }

    public boolean compatible(DarkItem item) {
        return this.getType().compatible(item);
    }
}
