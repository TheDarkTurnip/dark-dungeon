package dev.forbit.items.items;

import dev.forbit.items.items.types.*;
import dev.forbit.library.Utils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class DamageProperties {
    // finals
    @Getter private final double sharpnessExponent = 1.75;
    @Getter private final double levelExponent = 1.3;
    // item
    @Getter @Setter private Weapon item;
    // doubles
    @Getter @Setter private double baseDamage;
    @Getter @Setter private double modifierMultiple;
    @Getter @Setter private double repairMultiple;

    public DamageProperties(Weapon item) {
        setItem(item);
        setBaseDamage(item.getBaseDamage() * item.getType().getDamageModifier());

        if (item instanceof Modifiable) {
            Modifiable modifiable = (Modifiable) item;
            this.modifierMultiple = (modifiable.getModifier() != null) ? modifiable.getModifier().getDamageModifier() : 1.0;
        }
        if (item instanceof Repairable) {
            Repairable repairable = (Repairable) item;
            this.repairMultiple = (repairable.isRepaired() ? 1.4 : repairable.isBlunt() ? 0.7 : 1.0);
        }
    }

    public double getDamage() {
        double damage = this.getBaseDamage();
        damage += this.getModifierDamage();
        damage += this.getSharpnessDamage();
        damage += this.getRepairDamage();
        damage += this.getLevelsDamage();
        return Utils.round(damage);
    }

    private double getLevelsDamage() {
        if (getItem() instanceof Levellable) {
            Levellable levellable = (Levellable) getItem();
            double multiple = Math.pow(Math.floor(levellable.getLevel() / 5.0) + 1, 0.5);
            return (this.getBaseDamage() * multiple) - this.getBaseDamage();
        }
        return 0.0;
    }


    private double getRepairDamage() {
        return (this.getModifierDamage() * this.getRepairMultiple()) - this.getModifierDamage();
    }

    private double getSharpnessDamage() {
        if (getItem() instanceof Enchantable) {
            Enchantable enchantable = (Enchantable) getItem();
            double sharpnessLevel =
                    enchantable.getEnchantment(Enchantment.SHARPNESS) + enchantable.getEnchantment(Enchantment.POWER) + enchantable.getEnchantment(Enchantment.STRENGTH);
            return Math.pow(sharpnessLevel, getSharpnessExponent());
        }
        return 0.0;
    }

    private double getModifierDamage() {
        return this.getBaseDamage() * getModifierMultiple();
    }

    public List<String> getDamageString() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(ChatColor.GRAY + "Base Damage: " + ChatColor.WHITE + Utils.round(this.getBaseDamage()));
        if (this.getModifierDamage() > 0) list.add(ChatColor.GRAY + "Modifier Damage: " + ChatColor.WHITE + Utils.round(this.getModifierDamage())) ;
        if (this.getSharpnessDamage() > 0)list.add(ChatColor.GRAY + "Sharpness Damage: " + ChatColor.WHITE + Utils.round(this.getSharpnessDamage()));
        if (this.getRepairDamage() > 0)list.add(ChatColor.GRAY + "Repair Damage: " + ChatColor.WHITE + Utils.round(this.getRepairDamage()));
        if (this.getLevelsDamage() > 0)list.add(ChatColor.GRAY + "Level Damage: " + ChatColor.WHITE + Utils.round(this.getLevelsDamage()));

        list.add(ChatColor.WHITE + "Total Damage: " + ChatColor.WHITE + ChatColor.BOLD + this.getDamage());
        return list;
    }
}
