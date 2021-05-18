package dev.forbit.library;

import net.md_5.bungee.api.ChatColor;

public enum DamageType {
    NORMAL, MAGIC, THORNS, TRUE, DARK, LIGHT, INFUSED, GALAXY;

    public ChatColor getColor() {
        switch (this) {
            case NORMAL:
                return ChatColor.RED;
            case MAGIC:
                return ChatColor.LIGHT_PURPLE;
            case THORNS:
                return ChatColor.DARK_GREEN;
            case TRUE:
                return ChatColor.YELLOW;
            case DARK:
                return ChatColor.DARK_GRAY;
            case LIGHT:
                return ChatColor.WHITE;
            case INFUSED:
                return ChatColor.AQUA;
            case GALAXY:
                return ChatColor.DARK_PURPLE;
            default:
                return ChatColor.GRAY;
        }
    }
}
