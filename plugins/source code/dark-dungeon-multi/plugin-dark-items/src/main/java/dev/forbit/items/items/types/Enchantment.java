package dev.forbit.items.items.types;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Enchantment {
    SHARPNESS(ChatColor.RED+"Sharpness"),
    FIRE(ChatColor.RED+"Fire"),
    LOOTING(ChatColor.BLUE+"Looting"),
    BLEED(ChatColor.DARK_RED+"Bleed", ChatColor.DARK_RED, false),
    ENERGISED(ChatColor.GREEN+"Energised", ChatColor.DARK_GREEN),
    BESERKER(ChatColor.RED+"Beserker", ChatColor.DARK_RED),
    CUTDOWN(ChatColor.DARK_RED+"Cut Down", ChatColor.DARK_RED, false),
    PIERCING(ChatColor.AQUA+"Piercing", ChatColor.DARK_AQUA),
    STRENGTH(ChatColor.RED+"Strength"),
    RANGE(ChatColor.AQUA+"Range", ChatColor.DARK_AQUA),
    PROTECTION(ChatColor.RED+"Protection"),
    THORNS(ChatColor.DARK_GREEN+"Thorns"),
    POWER(ChatColor.RED+"Power");

    @Getter @Setter private boolean obtainable;
    @Getter @Setter private String title;
    @Getter @Setter private ChatColor color;

    Enchantment(String title, ChatColor color, boolean isObtainable) {
        setObtainable(isObtainable);
        setTitle(title);
        setColor(color);
    }

    Enchantment(String title) {
        setObtainable(true);
        setTitle(title);
        setColor(ChatColor.GRAY);
    }
    Enchantment(String title, ChatColor color) {
        setObtainable(true);
        setTitle(title);
        setColor(color);
    }

    public static Enchantment getFromTitle(String name) {
        for (Enchantment e : Enchantment.values()) {
            if (ChatColor.stripColor(name).equals(ChatColor.stripColor(e.getTitle()))) {
                return e;
            }
        }
        return null;

    }

    /***
     * get a random enchantment
     *
     * @param include un-obtainable enchantments
     * @return enchantment
     */
    public static Enchantment pickRandom(boolean includeUnObtainable) {
        Random rand = new Random();
        List<Enchantment> list = getEnchants(includeUnObtainable);
        int val = rand.nextInt(list.size());
        for (Enchantment ench : list) {
            if (val == list.indexOf(ench)) {
                return ench;
            }
        }
        return null;
    }

    private static List<Enchantment> getEnchants(boolean includeUnObtainable) {
        List<Enchantment> list = new ArrayList<Enchantment>();
        for (Enchantment ench : Enchantment.values()) {
            if (ench.isObtainable()) {
                list.add(ench);
            }
            else if (includeUnObtainable) {
                list.add(ench);
            }
        }
        return list;
    }

    /*public String getTitle() {
        switch (this) {
            case SHARPNESS:
                return ChatColor.RED + "Sharpness";
            case FIRE:
                return ChatColor.RED + "Fire";
            case LOOTING:
                return ChatColor.BLUE + "Looting";
            case BLEED:
                return ChatColor.DARK_RED + "Bleed";
            case ENERGISED:
                return ChatColor.GREEN + "Energised";
            case BESERKER:
                return ChatColor.RED + "Berserker";
            case CUTDOWN:
                return ChatColor.DARK_RED + "Cut Down";
            case PIERCING:
                return ChatColor.AQUA + "Piercing";
            case PROTECTION:
                return ChatColor.RED + "Protection";
            case RANGE:
                return ChatColor.AQUA + "Range";
            case STRENGTH:
                return ChatColor.RED + "Strength";
            case THORNS:
                return ChatColor.DARK_GREEN + "Thorns";
            case POWER:
                return ChatColor.RED + "Power";
        }
        return "";
    }

    public ChatColor getColour() {
        switch (this) {
            case BLEED:
                return ChatColor.DARK_RED;
            case BESERKER:
                return ChatColor.DARK_RED;
            case ENERGISED:
                return ChatColor.DARK_GREEN;
            case PIERCING:
                return ChatColor.DARK_AQUA;
            case CUTDOWN:
                return ChatColor.DARK_RED;
            case RANGE:
                return ChatColor.DARK_AQUA;
            default:
                return ChatColor.GRAY;
        }
    }

    public boolean isObtainable() {
        return obtainable;
    }

    public void setObtainable(boolean obtainable) {
        this.obtainable = obtainable;
    }*/

}
