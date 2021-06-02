package dev.forbit.items.events;

import dev.forbit.effects.MobEffects;
import dev.forbit.items.DarkItems;
import dev.forbit.items.Holograms;
import dev.forbit.items.events.custom.DarkDamageAction;
import dev.forbit.items.events.custom.DarkDamageEvent;
import dev.forbit.items.events.custom.DarkPlayer;
import dev.forbit.items.items.types.Enchantable;
import dev.forbit.items.items.types.Enchantment;
import dev.forbit.items.items.types.Weapon;
import dev.forbit.items.items.types.WeaponType;
import dev.forbit.library.DamageType;
import dev.forbit.library.Utils;
import dev.forbit.library.effects.EffectType;
import dev.forbit.library.effects.MobEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class DarkDamageListener implements Listener {
    DarkItems main;

    public DarkDamageListener(DarkItems main) {
        this.main = main;
        main.getLogger().info("Registered DarkDamageListener");
    }

    @EventHandler public void onDamage(DarkDamageEvent event) {
        if (event.getWeapon() instanceof Weapon) {
            Weapon weapon = (Weapon) event.getWeapon();
            event.getDamageMap().put(weapon.getDamageType(), weapon.getDamage());
            if (weapon.getWeapon().equals(WeaponType.CLUB)) {
                // slow the mob
                MobEffects.addEffect(event.getDamaged().getEntity(), new MobEffect(((DarkPlayer) event.getDamager()).getPlayer(), EffectType.SLOW, 5));
            }
            else if (weapon.getWeapon().equals(WeaponType.DAGGER)) {
                // dagger calculations
                // 1.5x damage if mob is facing away.
                if (Utils.isBehind(event.getDamaged().getEntity(), event.getDamager().getEntity())) {
                    event.getDamageMap().put(weapon.getDamageType(), weapon.getDamage() * 1.5d);
                    // play crit effect
                    Location loc = Utils.getMidpoint(event.getDamaged().getEntity().getLocation(), event.getDamaged().getEntity().getEyeLocation());
                    DarkItems.effectManager.display(Particle.CRIT_MAGIC, loc, 0f, 0f, 0f, 0.4f, 20, 1f, null, null, (byte) 0, 24.0f, Utils.getNearbyPlayers(loc));
                }
            }
        }
        else {
            event.getDamageMap().put(DamageType.NORMAL, 0.5d);
        }
        if (!(event.getAction().equals(DarkDamageAction.MAGIC))) { Utils.knockback(event.getDamager().getEntity(), event.getDamaged().getEntity(), 0.5f); }
    }

    @EventHandler(priority = EventPriority.HIGHEST) public void armorCalculations(DarkDamageEvent event) {
        HashMap<DamageType, Double> damageMap = event.getDamageMap();
        HashMap<DamageType, Double> newDamageMap = Utils.getBlankDamageMap();
        double rawDamage = 0;
        for (DamageType t : DamageType.values()) {
            rawDamage += damageMap.get(t);
        }
        event.setRawDamage(rawDamage);
        double armor = event.getDamaged().getProtection();
        double piercingValue = event.getPiercingValue();
        double trueDamage = damageMap.get(DamageType.TRUE);
        boolean convertTrue = false;
        double trueConversionAmount = 0d;
        /*if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            convertTrue = TalismanMain.hasTalisman(p, Talisman.TRUE_DAMAGE_1)
                          || TalismanMain.hasTalisman(p, Talisman.TRUE_DAMAGE_2);
            trueConversionAmount += TalismanMain.hasTalisman(p, Talisman.TRUE_DAMAGE_1) ? 0.05 : 0;
            trueConversionAmount += TalismanMain.hasTalisman(p, Talisman.TRUE_DAMAGE_2) ? 0.1 : 0;
        }*/
        for (DamageType t : damageMap.keySet()) {
            if (t.equals(DamageType.TRUE)) { continue; }
            double dmg = damageMap.get(t);
            double dmgAfterArmor = dmg;
            if (dmg > 0) {
                if (convertTrue) {
                    double amount = dmg * trueConversionAmount;
                    trueDamage += amount;
                    dmg -= amount;
                }
                if (event.getDamaged() instanceof Player) {
                    dmgAfterArmor = Utils.calculateArmor(dmg, armor/* + TalismanMain.getAdditonalProtection((Player) event.getDamaged())*/, piercingValue);
                }
                else {
                    dmgAfterArmor = Utils.calculateArmor(dmg, armor, piercingValue);
                }

                switch (t) {
                    case MAGIC:
                        dmgAfterArmor = Utils.calculateArmor(dmg, armor / 3, piercingValue);
                        break;

                    default:
                        break;
                }
                newDamageMap.put(t, dmgAfterArmor);
            }
        }
        newDamageMap.put(DamageType.TRUE, trueDamage);
        event.setDamageMap(newDamageMap);
    }

    @EventHandler(priority = EventPriority.NORMAL) public void enchants(DarkDamageEvent event) {
        if (event.getDamager() == null) { return; }
        if (!(event.getWeapon() instanceof Weapon)) { return; }

        Weapon darkItem = (Weapon) event.getWeapon();

        if (darkItem == null) { return; }
        if (!(darkItem instanceof Enchantable)) { return; }

        Enchantable enchantable = (Enchantable) darkItem;
        Player player = null;
        if (event.getDamager() instanceof DarkPlayer) {
            player = ((DarkPlayer) event.getDamager()).getPlayer();
        }
        // REFACTOR possible?
        HashMap<DamageType, Double> damageMap = event.getDamageMap();
        DamageType type = darkItem.getDamageType();
        for (Enchantment e : enchantable.getEnchantments().keySet()) {
            int level = enchantable.getEnchantment(e);
            switch (e) {
                case BLEED:
                    // set the mob to bleed
                    if (event.getDamaged() instanceof Player) { break; }
                    MobEffects.addEffect(event.getDamaged().getEntity(), new MobEffect(player, EffectType.BLEED, 3 * level));
                    break;
                case FIRE: {
                    // set the mob to take fire damage
                    if (event.getDamaged() instanceof Player) { break; }
                    event.getDamaged().getEntity().setFireTicks((int) (level * 30.0));
                    // System.out.println("fire: "+level*10.0);
                    break;
                }
                case BESERKER: { // deals extra damage based on the missing health
                    // of player
                    double pct = event.getDamager().getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / event.getDamager().getHealth();
                    double damage = darkItem.getDamage();
                    double newDamage = ((damage * pct) / (5.0 + (10.0 / level)));
                    damageMap.put(type, damageMap.get(type) + newDamage);
                    break;
                }
                case CUTDOWN: { // deal extra damage based on max health
                    // modify damage
                    double mobHealth = event.getDamaged().getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double prevDamage = damageMap.get(type);
                    double newDamage = prevDamage + (mobHealth / (5.0 + (10.0 / level)));
                    // System.out.println("old damage: "+prevDamage+" new damage:
                    // "+newDamage);
                    damageMap.put(type, newDamage);
                    break;
                }
                case ENERGISED: { // extra damage while spriting
                    if (player != null) {
                        if (player.isSprinting()) {
                            double prevDamage = damageMap.get(type);
                            double newDamage = prevDamage + (2 * level);
                            damageMap.put(type, newDamage);
                        }
                    }
                    break;
                }
                case PIERCING:
                    // double piercing =
                    // Math.round(-(1.0/(piercingValue+2)+0.5)*100.0) / 100.0;
                    double piercing = Math.min(1.0, Math.log(level + 1) / 4.0);
                    event.setPiercingValue((float) (Math.round(piercing * 100.0) / 100.0));
                    break;
                default:
                    break;
            }
        }
        event.setDamageMap(damageMap);
    }

    @EventHandler(priority = EventPriority.MONITOR) public void hologramEvent(DarkDamageEvent event) {
        Holograms.damageHologram(event.getDamaged().getEntity(), event.getDamageMap());
    }

}
