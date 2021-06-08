package dev.forbit.items;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import dev.forbit.library.DamageType;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Holograms {
    @Getter static DarkItems plugin;

    public Holograms(DarkItems main) {
        plugin = main;
    }

    public int dab() {
        return 1+1;
    }

    public static void damageHologram(LivingEntity le, HashMap<DamageType, Double> damageMap) {
        Hologram holo = HologramsAPI.createHologram(plugin, le.getEyeLocation().add(0.0, 1.0, 0.0));
        for (DamageType t : DamageType.values()) {
            if (!damageMap.containsKey(t)) continue;
            double dmg = damageMap.get(t);
            if (dmg > 0.0) { holo.appendTextLine(t.getColor() + "- " + (Math.round(dmg * 100.0) / 100.0)); }
        }
        runnableHologram(holo, 2);
    }

    public static void deathHolo(LivingEntity entity, double xp, double silver) {
        Hologram holo = HologramsAPI.createHologram(plugin, entity.getEyeLocation().add(0, -0.5, 0));
        if (xp > 0) { holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', "&3+ &b" + (int) xp + " XP")); }
        if (silver > 0) {
            holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', "&8+ &7" + (int) silver + " Silver"));
        }

        runnableHologram(holo, 2);
    }

    public static void runnableHologram(final Hologram holo, int seconds) {
        new BukkitRunnable() {
            int counter = seconds;

            public void run() {
                counter--;
                if (counter == 0) {
                    holo.delete();
                    cancel();

                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
