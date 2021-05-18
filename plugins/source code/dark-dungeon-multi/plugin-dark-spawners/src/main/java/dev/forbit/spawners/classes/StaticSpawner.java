package dev.forbit.spawners.classes;

import org.bukkit.Location;

public class StaticSpawner extends Spawner {

    public StaticSpawner(Location location, String mobName, int level) {
        this.setMobName(mobName);
        this.setLocation(location);
        this.setActive(true);
        this.setLevel(level);
    }

}
