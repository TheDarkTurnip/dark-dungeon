package dev.forbit.library.rarity;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.Random;

/**
 * Rarity class
 */
public enum Rarity {
    JUNK(ChatColor.DARK_GRAY, 0, 0),
    COMMON(ChatColor.DARK_GRAY, 1, 50),
    UNCOMMON(ChatColor.GRAY, 2, 40),
    UNIQUE(ChatColor.WHITE, 4, 30),
    RARE(ChatColor.LIGHT_PURPLE, 8, 10),
    EPIC(ChatColor.BLUE, 16, 5),
    SUPER(ChatColor.AQUA, 32, 3),
    MASTERFUL(ChatColor.GREEN, 64, 2),
    LEGENDARY(ChatColor.YELLOW, 128, 1),
    INSANE(ChatColor.RED, 256, 0),
    EXOTIC(ChatColor.of("#ab375d"), 512, 0);

    /**
     * The Color of this Rarity
     */
    @Getter @Setter ChatColor color;
    /**
     * The numerical value (2^n) of the index of the rarity
     */
    @Getter @Setter int value;
    /**
     * The weight used for randomly generating a rarity.
     */
    @Getter @Setter int weight;

    Rarity(ChatColor color, int value, int weight) {
        setColor(color);
        setValue(value);
        setWeight(weight);

    }

    /**
     * Gets a rarity with the highest value underneath the given value
     *
     * @param value
     *
     * @return
     */
    public static Rarity getFromValue(int value) {
        Rarity rarity = Rarity.COMMON;
        for (Rarity r : Rarity.values()) {
            if (value >= r.getValue()) {
                rarity = r;
            }
        }
        return rarity;
    }

    /**
     * Gets a random rarity
     *
     * @return Will not return INSANE or EXOTIC
     */
    public static Rarity getRandomWeighted() {
        int total = 0;
        for (Rarity r : Rarity.values()) {
            total += r.getWeight();
        }
        Random random = new Random();
        int val = random.nextInt(total - 1) + 1;
        for (Rarity r : Rarity.values()) {
            val -= r.getWeight();
            if (val <= 0) {
                return r;
            }
        }
        return Rarity.JUNK;
    }

    /**
     * Gets the secondary color.
     *
     * @return
     */
    public String getSecondaryColor() {
        return ChatColor.of(this.getColor().getColor().darker())+"";
    }

    /**
     * Gets the rarity underneath
     *
     * @return
     */
    public Rarity getLower() {
        switch (this) {
            case UNCOMMON:
                return COMMON;
            case UNIQUE:
                return UNCOMMON;
            case RARE:
                return UNIQUE;
            case EPIC:
                return RARE;
            case SUPER:
                return EPIC;
            case MASTERFUL:
                return SUPER;
            case LEGENDARY:
                return MASTERFUL;
            case INSANE:
                return LEGENDARY;
            case EXOTIC:
                return INSANE;
            case JUNK:
            case COMMON:
            default:
                return JUNK;
        }
    }

    /**
     * Gets the rarity above
     *
     * @return
     */
    public Rarity getUpper() {
        switch (this) {
            case COMMON:
                return UNCOMMON;
            case UNCOMMON:
                return UNIQUE;
            case UNIQUE:
                return RARE;
            case RARE:
                return EPIC;
            case EPIC:
                return SUPER;
            case SUPER:
                return MASTERFUL;
            case MASTERFUL:
                return LEGENDARY;
            case LEGENDARY:
                return INSANE;
            case INSANE:
            case EXOTIC:
                return EXOTIC;
            case JUNK:
            default:
                return COMMON;
        }
    }
}
