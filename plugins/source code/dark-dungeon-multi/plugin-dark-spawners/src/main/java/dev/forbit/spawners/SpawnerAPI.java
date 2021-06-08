package dev.forbit.spawners;

import dev.forbit.items.DarkItemFactory;
import dev.forbit.items.items.types.WeaponType;
import dev.forbit.library.ArmorType;
import dev.forbit.spawners.classes.Spawner;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class SpawnerAPI {
    DarkSpawners plugin;

    public SpawnerAPI(DarkSpawners plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the nearest player of a given location in a given radius.
     *
     * @param location
     * @param radius
     *
     * @return
     */
    @Nullable public static Player getClosestPlayer(Location location, int radius) {
        double closestDist = radius * radius;
        Player closest = null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().getWorld().equals(location.getWorld())) {
                double distance = p.getLocation().distanceSquared(location);
                if (distance < closestDist) {
                    closestDist = distance;
                    closest = p;
                }
            }
        }
        return closest;
    }


    public void addPlayerSpawner(Spawner s, UUID player) {
        PlayerSpawners pSpawners = getPlayerSpawner(player);
        pSpawners.getSpawners().add(s);
    }

    private PlayerSpawners getPlayerSpawner(UUID player) {
        for (PlayerSpawners pSpawners : plugin.getPlayerSpawners()) {
            if (pSpawners.getOwner().equals(player)) { return pSpawners; }
        }
        PlayerSpawners pSpawner = new PlayerSpawners(player);
        plugin.getPlayerSpawners().add(pSpawner);
        return pSpawner;
    }


    /**
     * Gets the parent spawner of an active mob.
     *
     * @param mob
     *
     * @return
     */
    @Nullable public Spawner getParentSpawner(ActiveMob mob) {
        for (PlayerSpawners playerSpawners : plugin.getPlayerSpawners()) {
            for (Spawner spawner : playerSpawners.getSpawners()) {
                if (spawner != null && spawner.getActiveMobs() != null) {
                    if (spawner.getActiveMobs().contains(mob)) {
                        return spawner;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Returns the spawner at a block's location
     *
     * @param clickedBlock
     *
     * @return
     */
    public Spawner getSpawner(Block clickedBlock) {
        for (PlayerSpawners pSpawner : plugin.getPlayerSpawners()) {
            for (Spawner spawner : pSpawner.getSpawners()) {
                if (spawner.getLocation().clone().add(0, -0.5, 0).getBlock().equals(clickedBlock)) {
                    return spawner;
                }
            }
        }
        return null;
    }

    public void equip(ActiveMob am)  {
        float level = (float) am.getLevel() / 20.0f;
        Entity entity = am.getEntity().getBukkitEntity();
        if (!(entity instanceof LivingEntity)) { return; }
        LivingEntity le = (LivingEntity) entity;
        EntityEquipment ee = le.getEquipment();
        if (ee == null) { return; }
        Random random = new Random();
        for (ArmorType type : ArmorType.values()) {
            switch (type) {
                case HELMET:
                    ee.setHelmet(DarkItemFactory.getArmorPiece(type, level));
                    break;
                case CHESTPLATE:
                    ee.setChestplate(DarkItemFactory.getArmorPiece(type, level));
                    break;
                case LEGGINGS:
                    ee.setLeggings(DarkItemFactory.getArmorPiece(type, level));
                    break;
                case BOOTS:
                    ee.setBoots(DarkItemFactory.getArmorPiece(type, level));
                    break;
            }
        }
        ee.setItemInMainHand(DarkItemFactory.getLevelledWeapon(WeaponType.SWORD, level));

    }

    public void clear(Player player) {
        PlayerSpawners playerSpawners = getPlayerSpawner(player.getUniqueId());
        playerSpawners.clear();
    }
}
