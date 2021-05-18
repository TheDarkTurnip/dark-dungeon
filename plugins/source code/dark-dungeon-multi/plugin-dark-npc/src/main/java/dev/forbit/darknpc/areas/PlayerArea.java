package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class PlayerArea {
    @Getter @Setter HashMap<Integer, Area> areas;
    @Getter @Setter UUID owner;
    @Getter @Setter double centerX;
    @Getter @Setter double centerY;
    @Getter @Setter double centerZ;

    public Location getLocation() {
        return new Location(Bukkit.getWorld("player_world"), getCenterX(), getCenterY(), getCenterZ());
    }

    public Location getNPCManagerLocation() {
        Location loc = getLocation().clone();
        loc.add(-6.5, -1, -3.5);
        return loc;
    }
}
