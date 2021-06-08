package dev.forbit.items.events.custom;

import dev.forbit.items.DarkItems;
import dev.forbit.items.items.DarkItem;
import dev.forbit.items.items.types.Armor;
import dev.forbit.library.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class DarkEntity {

    @Getter @Setter transient LivingEntity entity;

    public DarkEntity(LivingEntity le) {
        entity = le;
    }

    // methods for use

    public double getHealth() {
        return getEntity().getHealth();
    }

    public void damage(double amount) {
        if (getHealth()-amount <= 0) {
            /// death
            getEntity().setHealth(0.1);
            getEntity().damage(1000.0);
            Utils.sendDeathPacket(getEntity());
        } else {
            getEntity().setHealth(getHealth()-amount);
            Utils.sendHurtPacket(getEntity());
            System.out.println("hp: "+getEntity().getHealth());
        }
    }

    public double getProtection() {
        EntityEquipment ee = getEntity().getEquipment();
        if (ee == null) return 0.0;
        double protection = 0.0;
        for (ItemStack item : ee.getArmorContents()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;
            DarkItem darkItem = DarkItems.getApi().loadItem(item);
            if (darkItem instanceof Armor) {
                Armor armor = (Armor) darkItem;
                protection += armor.getProtection();
            }
        }
        return protection;
    }
}
