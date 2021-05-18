package dev.forbit.library.effects;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public enum EffectType {
    POISON(ChatColor.DARK_GREEN, "POISONED"),
    BLEED(ChatColor.DARK_RED, "BLEEDING"),
    SLOW(ChatColor.GRAY, "SLOWED"),
    FROZEN(ChatColor.AQUA, "FROZEN");

    @Getter private final ChatColor color;
    @Getter private final String effect;

    EffectType(ChatColor color, String effect) {
        this.color = color;
        this.effect = effect;
    }

}
