package dev.forbit.projectile;

import dev.forbit.items.DarkItems;
import dev.forbit.items.events.BukkitListener;
import dev.forbit.items.events.custom.DarkDamageAction;
import dev.forbit.items.events.custom.DarkEntity;
import dev.forbit.items.events.custom.DarkPlayer;
import dev.forbit.items.events.custom.DarkPlayerDamageEntityEvent;
import dev.forbit.library.DamageType;
import lombok.Data;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public @Data class DarkProjectile {
    List<Location> locations;
    Location startLocation;
    World world;
    Player owner;
    double damage;
    double range;
    HashMap<DamageType, Double> damageMap;

    public DarkProjectile(Location start, Location end, double velocity, Player player, double range, HashMap<DamageType, Double> damageMap) {
        this.startLocation = start;
        this.world = start.getWorld();
        this.owner = player;
        this.damageMap = damageMap;
        this.range = range;
        locations = new ArrayList<Location>();
        Vector a = start.toVector();
        Vector b = end.toVector();
        Vector d = (new Vector().copy(a)).subtract(new Vector().copy(b));
        for (float i = 0; i < 1.0; i += velocity) {
            Vector p = new Vector();
            p = (new Vector().copy(a).subtract(new Vector().copy(d).multiply(i)));
            locations.add(p.toLocation(world));
        }
    }

    public boolean displayParticle(int num) {
        if (num >= locations.size()) {
            return false;
        } else {
            List<Player> players = new ArrayList<Player>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().distance(locations.get(num)) < 128) {
                    players.add(p);
                }
            }
            Location loc = locations.get(num);
            float speed = 0f;
            int amount = 8;
            DarkItems.effectManager.display(Particle.SPELL_WITCH, locations.get(num).add(0.0, -0.5, 0.0), 0f, 0f, 0f, speed, amount,
                    1f, Color.BLACK, Material.AIR, (byte) 31, 0.0, players);
            // check for damage
            for (Entity e : loc.getChunk().getEntities()) {
                if (e instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) e;
                    if (le instanceof Player || le instanceof ArmorStand || (le.isDead()))
                        continue;
                    if (le.getLocation().distance(loc) <= 1.5) {
                        // System.out.println("damage event!");
                        if (!(le.equals(this.owner))) {
                            return damage(le);
                        }
                    } else {
                        if (le.getEyeLocation().distance(loc) <= 1.5) {
                            // System.out.println("damage event!");
                            if (!(le.equals(this.owner))) {
                                return damage(le);
                            }
                        }
                    }
                }
            }
            return !loc.getBlock().getType().equals(Material.STONE);
        }
    }

    private boolean damage(LivingEntity le) {
        //BukkitListener.callEvent(owner, le, owner.getEquipment().getItemInMainHand(), DarkDamageAction.MAGIC, damageMap);
        BukkitListener.callEvent(new DarkPlayerDamageEntityEvent(new DarkPlayer(owner), new DarkEntity(le), owner.getEquipment().getItemInMainHand(), DarkDamageAction.MAGIC, damageMap));
        return false;

    }
}
