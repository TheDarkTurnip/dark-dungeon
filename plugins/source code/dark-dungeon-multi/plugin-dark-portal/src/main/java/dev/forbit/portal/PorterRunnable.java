package dev.forbit.portal;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public abstract class PorterRunnable implements Runnable {
    @Getter @Setter Player player;
}
