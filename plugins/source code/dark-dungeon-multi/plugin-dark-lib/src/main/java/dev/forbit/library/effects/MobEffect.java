package dev.forbit.library.effects;

import lombok.Data;
import org.bukkit.entity.Player;

public @Data class MobEffect {
    EffectType type;
    int duration;
    Player committer;

    public MobEffect(Player player, EffectType type, int duration) {
        setCommitter(player);
        setType(type);
        setDuration(duration);
    }
}
