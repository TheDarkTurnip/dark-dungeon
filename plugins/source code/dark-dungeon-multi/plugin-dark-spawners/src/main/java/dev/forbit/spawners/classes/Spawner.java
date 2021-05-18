package dev.forbit.spawners.classes;

import dev.forbit.spawners.DarkSpawners;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Spawner {
    public static final int WARMUP = 5;
    @Getter @Setter boolean active;
    //@Getter @Setter private transient Location location;
    @Getter @Setter double x;
    @Getter @Setter double y;
    @Getter @Setter double z;
    @Getter @Setter @NonNull String world = "world";
    @Getter @Setter String mobName;
    @Getter @Setter int level;
    @Getter @Setter int baseCooldown = 30;
    @Getter @Setter private transient int currentCooldown = WARMUP;
    @Getter @Setter private transient List<ActiveMob> activeMobs = new ArrayList<>();
    @Getter @Setter int maxMobs = 2;

    public Spawner() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
        level = 1;
        active = false;
        mobName = "BasicZombie";
    }

    public void tick() {
        if (getActiveMobs() == null) setActiveMobs(new ArrayList<>());
        if (getActiveMobs().size() < maxMobs) {
            if ((getCurrentCooldown() <= 0)) {
                spawn();
                setCurrentCooldown(getBaseCooldown());
            }
            else {
                setCurrentCooldown(getCurrentCooldown()-1);
            }
        }
    }

    public void spawn() {
        setCurrentCooldown(getBaseCooldown());
        ActiveMob am = MythicMobs.inst().getMobManager().spawnMob(getMobName(),getLocation());
        am.setLevel(getLevel());
        getActiveMobs().add(am);
        Bukkit.getScheduler().runTaskLater(DarkSpawners.plugin, () -> DarkSpawners.getApi().equip(am), 2L);

    }


    // ** getters and setters ** \\
    public Location getLocation() {
        return new Location(Bukkit.getWorld(getWorld()),getX(),getY(),getZ());
    }
    public void setLocation(@NonNull Location location) {
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
        setWorld(Objects.requireNonNull(location.getWorld()).getName());
    }


}
