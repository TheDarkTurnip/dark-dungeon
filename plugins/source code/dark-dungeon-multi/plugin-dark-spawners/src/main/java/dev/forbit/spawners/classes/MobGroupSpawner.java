package dev.forbit.spawners.classes;

import dev.forbit.library.group.MobGroup;
import dev.forbit.spawners.DarkSpawners;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MobGroupSpawner extends Spawner implements GroupSpawner{
    @Getter @Setter MobGroup group;

    public MobGroupSpawner(Location location, MobGroup group, int level) {
        this.setMobName("null");
        this.setGroup(group);
        this.setLocation(location);
        this.setActive(true);
        this.setLevel(level);
    }

    @Override public MobGroup getMobGroup() {
        return getGroup();
    }

    @Override public void spawn() {
        setCurrentCooldown(getBaseCooldown());
        ActiveMob am = MythicMobs.inst().getMobManager().spawnMob(group.getRandomMob(),getLocation());
        am.setLevel(getLevel());
        getActiveMobs().add(am);
        Bukkit.getScheduler().runTaskLater(DarkSpawners.plugin, () -> DarkSpawners.getApi().equip(am), 2L);
    }
}
