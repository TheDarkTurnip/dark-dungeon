package dev.forbit.effects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import dev.forbit.items.DarkItems;
import dev.forbit.items.events.BukkitListener;
import dev.forbit.items.events.custom.DarkDamageAction;
import dev.forbit.items.events.custom.DarkEntity;
import dev.forbit.items.events.custom.DarkPlayer;
import dev.forbit.items.events.custom.DarkPlayerDamageEntityEvent;
import dev.forbit.library.DamageType;
import dev.forbit.library.Utils;
import dev.forbit.library.effects.EffectType;
import dev.forbit.library.effects.MobEffect;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class MobEffects implements Listener {
    private static final HashMap<LivingEntity, ArrayList<MobEffect>> effectMap = new HashMap<>();
    private static final HashMap<LivingEntity, Hologram> hologramMap = new HashMap<>();
    private static final List<Hologram> holograms = new ArrayList<>();
    private static final boolean debug = false;
    private final AttributeModifier frozenModifier = new AttributeModifier("frozen", 0, AttributeModifier.Operation.ADD_SCALAR);
    private final DarkItems main;

    public MobEffects(DarkItems main) {
        this.main = main;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::updateMobEffects, 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::updateHolograms, 20L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::cleanHolograms, 20L, 5L);
    }

    private static HashMap<LivingEntity, ArrayList<MobEffect>> getEffectMap() {
        return effectMap;
    }

    public static void addEffect(LivingEntity le, MobEffect me) {
        ArrayList<MobEffect> mobEffects = getMobEffects(le);
        mobEffects.add(me);
        effectMap.put(le, mobEffects);
        if (debug) { System.out.println("[MobEffects] Added mob effect " + me.getType() + " to " + le.getCustomName()); }
    }

    private static ArrayList<MobEffect> getMobEffects(LivingEntity le) {
        ArrayList<MobEffect> mobEffects;
        if (getEffectMap().containsKey(le)) {
            mobEffects = getEffectMap().get(le);
        }
        else {
            mobEffects = new ArrayList<>();
        }
        return mobEffects;
    }

    public static <T> Set<T> clone(Set<T> set) {
        return new HashSet<>(set);
    }

    private void cleanHolograms() {
        // remove unlinked holograms
        int count = 0;
        List<Hologram> toRemove = new ArrayList<>();
        for (Hologram holo : holograms) {
            if (hologramMap.containsValue(holo)) { continue; }
            if (!holo.isDeleted()) {
                holo.delete();
            }

            toRemove.add(holo);
            count++;
        }
        holograms.removeAll(toRemove);
        if (debug && count > 0) {
            System.out.println("[MobEffects] Removed " + count + " unlinked holograms");
        }
        if (debug) {
            System.out.println("[MobEffects] " + holograms.size() + " linked holograms. " + HologramsAPI.getHolograms(main)
                                                                                                        .size() + " holograms total. " + hologramMap.size() + " active holograms.");
        }
    }

    private void updateHolograms() {
        // update every mob
        ArrayList<LivingEntity> activeMobsChecked = new ArrayList<>();
        if (debug) { System.out.println("[MobEffects] begin looping"); }
        int count = 0;
        for (ActiveMob am : MythicMobs.inst().getMobManager().getActiveMobs()) {
            count++;
            if (debug) { System.out.println("loop " + count + " am: " + am); }
            if (am == null || am.isDead() || am.getEntity() == null || !am.getEntity().isValid()) {
                if (debug) { System.out.println("am is not nice"); }
                //continue;
            }
            else {
                if (debug) { System.out.println("adapting"); }
                LivingEntity le = (LivingEntity) BukkitAdapter.adapt(am.getEntity());
                if (debug) { System.out.println("adpated."); }
                if (le.isValid() && !le.isDead()) {
                    Hologram holo = getHologram(le);
                    if (debug) { System.out.println("got holo"); }
                    activeMobsChecked.add(le);
                    if (debug) { System.out.println("checked"); }
                    if (am.getEntity() != null) {
                        if (debug) { System.out.println("teleporting holo"); }
                        holo.teleport(getLocation(le.getEyeLocation()));
                        if (debug) { System.out.println("teleported."); }
                    }
                }
            }
        }
        if (debug) { System.out.println("[MobEffects] ran through " + count + " active mobs."); }
        if (debug) { System.out.println("[MobEffects] begin clone"); }
        Set<LivingEntity> inactiveMobs = clone(hologramMap.keySet());
        if (debug) { System.out.println("[MobEffects] inactiveMobs before cull " + inactiveMobs.size()); }
        inactiveMobs.removeAll(activeMobsChecked);
        if (debug) { System.out.println("[MobEffects] inactiveMobs after cull " + inactiveMobs.size()); }
        for (LivingEntity am : inactiveMobs) {
            Hologram holo = hologramMap.get(am);
            if (!holo.isDeleted()) {
                holo.delete();
            }
            hologramMap.remove(am);
        }
        if (debug && inactiveMobs.size() > 0) {
            System.out.println("[MobEffects] inactiveMobs.size(): " + inactiveMobs.size());
        }
    }

    private void updateMobEffects() {
        List<LivingEntity> activeMobsChecked = new ArrayList<>();
        for (LivingEntity le : effectMap.keySet()) {
            ArrayList<MobEffect> mobEffects = getMobEffects(le);
            ArrayList<MobEffect> toRemove = new ArrayList<>();
            List<String> lines = new ArrayList<>();
            if (!le.isValid() || le.isDead()) { continue; }
            activeMobsChecked.add(le);
            List<EffectType> doneEffects = new ArrayList<>();
            for (MobEffect me : mobEffects) {
                me.setDuration(me.getDuration() - 1);
                if (me.getDuration() <= 0) {
                    toRemove.add(me);
                    removeEffects(le);
                }
                else {
                    if (doneEffects.contains(me.getType())) { continue; }
                    doneEffects.add(me.getType());
                    lines.add(me.getType().getColor() + "" + ChatColor.BOLD + me.getType().getEffect());
                    // do effect
                    commitEffect(me, le);
                }
            }
            mobEffects.removeAll(toRemove);
            Hologram holo = getHologram(le);
            holo.clearLines();
            if (lines.size() > 0) {
                for (String s : lines) {
                    holo.appendTextLine(s);
                }
            }
            effectMap.put(le, mobEffects);
        }
        Set<LivingEntity> inactiveMobs = clone(effectMap.keySet());
        inactiveMobs.removeAll(activeMobsChecked);
        for (LivingEntity am : inactiveMobs) {
            effectMap.remove(am);
        }
        if (debug) {
            System.out.println("[MobEffects] " + "effectMap.size(): " + effectMap.keySet()
                                                                                 .size() + " hologramMap.size(): " + hologramMap.size() + " activeMobs.size(): " + MythicMobs.inst()
                                                                                                                                                                             .getMobManager()
                                                                                                                                                                             .getActiveMobs()
                                                                                                                                                                             .size());
        }

    }

    private void commitEffect(MobEffect me, LivingEntity le) {
        switch (me.getType()) {
            case SLOW:
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2));
                break;
            case FROZEN:
                Objects.requireNonNull(le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).addModifier(frozenModifier);
                break;
            case BLEED:
                if (le.isValid() && !le.isDead()) {
                    double damage = Objects.requireNonNull(le.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() / 15.0;
                    BukkitListener.callEvent(new DarkPlayerDamageEntityEvent(new DarkPlayer(me.getCommitter()), new DarkEntity(le), null, DarkDamageAction.BLEED, Utils.getDamageList(DamageType.NORMAL, damage)));
                }
        }
    }

    private void removeEffects(LivingEntity le) {
        if (debug) { System.out.println("[MobEffects] Polling removal of effects of " + le.getCustomName()); }
        if (le != null && le.isValid() && !le.isDead()) {
            AttributeInstance attribute = le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            assert attribute != null;
            attribute.removeModifier(frozenModifier);
            le.removePotionEffect(PotionEffectType.SLOW);
            if (debug) { System.out.println("Removed effects for  " + le.getCustomName()); }
        }
        else {
            if (debug) { System.out.println(le.getCustomName() + " isn't alive"); }
        }
    }

    private @NonNull Hologram getHologram(@NonNull LivingEntity le) {
        if (hologramMap.containsKey(le)) { return hologramMap.get(le); }
        Hologram holo = HologramsAPI.createHologram(main, getLocation(le.getEyeLocation()));
        holograms.add(holo);

        if (debug) { System.out.println("[MobEffects] Created hologram for " + le.getCustomName()); }
        hologramMap.put(le, holo);
        return holo;
    }

    @EventHandler public void onSpawn(MythicMobSpawnEvent event) {
        LivingEntity le = (LivingEntity) event.getEntity();
        if (le == null) { return; }
        getHologram(le); // makes hologram
    }

    @EventHandler public void onDespawn(MythicMobDespawnEvent event) {
        remove(event.getEntity());
    }

    /*@EventHandler
    public void onDarkDeath(DarkDeathEvent event) {
        remove(event.getEntity());
    }*/

    @EventHandler public void onDeath(MythicMobDeathEvent event) {
        remove(event.getEntity());
    }

    private void remove(Entity e) {
        LivingEntity le = (LivingEntity) e;
        if (debug) { System.out.println("[MobEffects] Removing mob " + le.getCustomName()); }
        effectMap.remove(le);
        Hologram holo = hologramMap.get(le);
        if (holo != null) {
            if (!holo.isDeleted()) {
                holo.delete();
                if (debug) { System.out.println("[MobEffects] Delted hologram."); }
            }
        }
        hologramMap.remove(le);
        removeEffects(le);
    }

    private Location getLocation(Location location) {

        Location loc = new Location(location.getWorld(), 0, 0, 0);

        loc.setDirection(location.getDirection());
        loc.setYaw(location.getYaw() + 90f);
        Vector vector = loc.getDirection().normalize().multiply(1d);
        location.add(vector);
        return location;

    }
}
