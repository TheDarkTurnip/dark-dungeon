package dev.forbit.items.events.custom;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class DarkPlayer extends DarkEntity {
    @Getter @Setter transient Player player;

    public DarkPlayer(Player le) {
        super(le);
        setPlayer(le);
    }


}

