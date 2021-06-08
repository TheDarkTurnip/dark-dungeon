package dev.forbit.projectile;

import dev.forbit.items.DarkItems;
import dev.forbit.library.DamageType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProjectileManager {
    DarkItems main;

    public ProjectileManager(DarkItems main) {
        this.main = main;
    }
    public void addProjectile(Player player, double range, double velocityModifier, HashMap<DamageType, Double> damageMap) {
        double velocity = 0.08*velocityModifier;
        Location end = player.getEyeLocation().add((player.getEyeLocation().getDirection()).multiply(range));
        DarkProjectile proj = new DarkProjectile(player.getEyeLocation(),end,velocity, player, range, damageMap);
        drawProjectile( proj);
    }

    public static Set<Material> getTransparentBlocks() {
        Set<Material> mats = new HashSet<Material>();
        mats.add(Material.AIR);
        mats.add(Material.VINE);
        mats.add(Material.TALL_GRASS);
        mats.add(Material.GRASS);
        mats.add(Material.TORCH);
        return mats;
    }

    private void drawProjectile(final DarkProjectile	proj) {
        new BukkitRunnable() {
            int counter = 0;
            public void run() {
                if (!proj.displayParticle(counter++)) {
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 1);
    }

}
