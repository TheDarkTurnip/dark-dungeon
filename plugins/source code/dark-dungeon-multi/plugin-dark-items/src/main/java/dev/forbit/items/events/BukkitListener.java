package dev.forbit.items.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.items.DarkItems;
import dev.forbit.items.events.custom.*;
import dev.forbit.items.items.DarkItem;
import dev.forbit.items.items.types.*;
import dev.forbit.library.DamageType;
import dev.forbit.library.Symbols;
import dev.forbit.library.Utils;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BukkitListener implements Listener {
    static DarkItems main;

    public BukkitListener(DarkItems m) {
        main = m;
        m.getLogger().info("Registered BukkitListener");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            ArrayList<Player> remove = new ArrayList<Player>();
            // cooldown
            for (Player p : cooldown.keySet()) {
                if (cooldown.get(p) <= 1) {
                    remove.add(p);
                } else {
                    cooldown.put(p, cooldown.get(p) - 1);
                }
            }
            for (Player p : remove) {
                cooldown.remove(p);
            }
            // notify cooldown
            remove = new ArrayList<Player>();
            for (Player p : notifyCooldown.keySet()) {
                if (notifyCooldown.get(p) <= 1) {
                    remove.add(p);
                } else {
                    notifyCooldown.put(p, notifyCooldown.get(p) - 1);
                }
            }
            for (Player p : remove) {
                notifyCooldown.remove(p);
            }
        }, 0L, 1L);
    }

    @Getter private final HashMap<Player, Integer> cooldown = new HashMap<>();
    private final HashMap<Player, Integer> notifyCooldown = new HashMap<>();
    private final HashMap<WeaponType, Player> weaponCooldown = new HashMap<>();

    private boolean onCooldown(Player player, WeaponType type) {
        for (WeaponType w : weaponCooldown.keySet()) {
            if (w.equals(type)) { return true; }
        }
        return false;
    }

    private void addCooldown(Player player, WeaponType type) {
        weaponCooldown.put(type, player);
    }

    private void removePlayer(Player player, WeaponType type) {
        weaponCooldown.remove(type, player);
        // DarkItems.plugin.getLogger().info("weapon cooldown size:" +
        // weaponCooldown.size());
    }

    private void cooldown(Player player, WeaponType weapon) {
        addCooldown(player, weapon);
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> removePlayer(player, weapon), 10L);
    }

    // IDEA could be used for longer range weapons (like scythes)
    /*@EventHandler public void onPvE(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) { return; }
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            // attack

            LivingEntity closestEntity = Utils.getNearestLivingEntity(event.getPlayer(), event.getPlayer().getEyeLocation().getDirection(), 3.5);
            if (closestEntity == null || closestEntity.isDead()) { return; }
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            callEvent(new DarkPlayerDamageEntityEvent(new DarkPlayer(player), new DarkEntity(closestEntity), item, DarkDamageAction.MELEE, Utils.getBlankDamageMap()));

        }
    }*/

    public static void callEvent(DarkDamageEvent event) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        main.getLogger().info("called event: " + event.getClass().getName());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) { return; }
        event.getDamaged().damage(event.getDamage());

        if (event.getDamager() instanceof DarkPlayer) {
            Player p = (Player) event.getDamager().getEntity();
            double roundedRaw = Math.round(event.getRawDamage() * 100.0) / 100.0;
            String bracketString = "&8";
            String tagString = "&c";
            String neutralString = "&f";
            boolean useSymbols = true;
            boolean colons = false;
            String health = "";
            if (event.getDamaged().getEntity().getHealth() <= 0) {
                health = "&c&lDEAD";
            }
            else {
                health = "&c" + ((int) event.getDamaged().getEntity().getHealth()) + neutralString + "/&4" +
                         ((int) event.getDamaged().getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }

            Utils.sendActionBar(p, ChatColor.translateAlternateColorCodes('&',
                    "" + bracketString + "[" + tagString + (useSymbols ? Symbols.SHORT_SWORD : "Damage") + bracketString + "]" + neutralString + (colons ? ":" : "") + " &c" +
                    roundedRaw + " " + neutralString + "| " + bracketString + "[" + tagString + (useSymbols ? Symbols.HEART : "Health") + bracketString + "] " + health));
        }

    }

    @EventHandler public void onHit(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        ItemStack item = null;
        System.out.println("portal cooldown: "+damager.getPortalCooldown());
        if (damager.getPortalCooldown() > 1) return;
        if (damager instanceof LivingEntity) {
            EntityEquipment ee = ((LivingEntity) damager).getEquipment();
            assert ee != null;
            item = ee.getItemInMainHand();
        }
        HashMap<DamageType, Double> damageMap = Utils.getBlankDamageMap();
        if (damager instanceof LivingEntity) {
            if (damager instanceof Player) {
                if (damaged instanceof Player) {
                    // pvp
                    callEvent(new DarkPlayerDamagePlayerEvent(new DarkPlayer((Player) damager), new DarkPlayer((Player) damaged), item, DarkDamageAction.MELEE, damageMap));
                }
                else {
                    // pve
                    callEvent(new DarkPlayerDamageEntityEvent(new DarkPlayer((Player) damager), new DarkEntity((LivingEntity) damaged), item, DarkDamageAction.MELEE, damageMap));

                }
            }
            else {
                // TODO mob damages mob
                // eve
                if (damaged instanceof Player) {
                    // evp
                    callEvent(new DarkEntityDamagePlayerEvent(new DarkEntity((LivingEntity) damager), new DarkPlayer((Player) damaged), item, DarkDamageAction.MELEE, damageMap));
                }
            }
        }
        damager.setPortalCooldown(10);
    }

    // magic staff go shoot shoot
    @EventHandler public void magicStaff(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) { return; }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            DarkItem darkItem = DarkItems.getApi().loadItem(event.getItem());
            if (darkItem == null) { return; }
            if (darkItem instanceof Weapon) {
                Weapon weapon = (Weapon) darkItem;
                if (weapon.getWeapon().equals(WeaponType.STAFF)) {
                    // magic
                    event.setCancelled(true);

                    if (onCooldown(event.getPlayer(), WeaponType.STAFF)) { return; }
                    double range = 15.0;
                    if (darkItem instanceof Enchantable) {
                        Enchantable enchantable = (Enchantable) darkItem;
                        range += enchantable.getEnchantment(Enchantment.RANGE)*2.0;
                    }
                    HashMap<DamageType, Double> damageMap = Utils.getBlankDamageMap();
                    DamageType damageType = weapon.getDamageType();
                    if (damageType.equals(DamageType.NORMAL)) { damageType = DamageType.MAGIC; }
                    damageMap.put(damageType, weapon.getDamage());
                    DarkItems.projectileManager.addProjectile(event.getPlayer(), range, 1.0, damageMap);
                    if (darkItem instanceof Repairable) {
                        Repairable repairable = (Repairable) darkItem;
                        repairable.addUse(1);
                    }
                    event.getPlayer().getInventory().setItemInMainHand(DarkItems.getApi().infuseItem(darkItem));
                    cooldown(event.getPlayer(), WeaponType.STAFF);
                }
            }
        }
    }

    @EventHandler public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getView().getBottomInventory();
        InventoryHolder holder = inv.getHolder();
        if (!(holder instanceof Player)) return;
        Player player = (Player) holder;
        main.updateInventory(player);
        player.updateInventory();
    }

    @EventHandler public void onHotBarChange(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> main.updateInventory(event.getPlayer()), 1L);
    }

}
