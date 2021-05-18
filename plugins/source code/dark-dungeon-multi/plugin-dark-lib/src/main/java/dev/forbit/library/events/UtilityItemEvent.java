package dev.forbit.library.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class UtilityItemEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    @Getter @Setter private String utilityIdentifier;

    public UtilityItemEvent(Player who, String utilityIdentifier) {
        super(who);
        setUtilityIdentifier(utilityIdentifier);
    }

    @Override @NonNull public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
