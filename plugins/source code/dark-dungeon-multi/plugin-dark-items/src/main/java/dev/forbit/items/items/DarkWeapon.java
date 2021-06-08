package dev.forbit.items.items;

import dev.forbit.items.items.types.*;
import dev.forbit.library.Utils;
import dev.forbit.library.rarity.Rarity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.HashMap;

public class DarkWeapon extends Weapon implements Enchantable, Modifiable, Repairable, Typeable, Raritable, Sellable, Levellable {
    @Getter @Setter HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    @Getter @Setter Modifier modifier = Modifier.GODLY;
    @Getter @Setter int uses;
    @Getter @Setter int repair;
    @Getter @Setter int XP;

    public DarkWeapon() {
        setMaterial(Material.NETHERITE_SWORD);
        setWeapon(WeaponType.SWORD);
        setItemGroup(ItemGroup.WEAPON);
    }

    @Override public double getDamage() {
        DamageProperties properties = new DamageProperties(this);
        return properties.getDamage();
    }

    @Override public String getDisplayName() {
        String mod = Utils.namify(getModifier().name());
        String typ = Utils.namify(getType().name());
        String wep = Utils.namify(getWeapon().getName());
        return getRarity().getColor() + mod + " " + typ + " " + wep;

    }

    @Override public Rarity getRarity() {
        return getModifier().getRarity();
    }

    @Override public int getLevel() {
        return (int) Math.max(0.0, Utils.log2(getXP()));
    }

    private int getAddition(ItemType type) {
        switch (type) {
            case WOOD:
            case STONE:
            case IRON:
            case GOLD:
            case DIAMOND:
            case MOLTEN:
                return 0;
            case BONE:
            case FLINT:
            case COPPER:
            case TUNGSTEN:
            case COBALT:
            case MAGMA:
                return 1;
            case TIN:
            case BRONZE:
            case PLATINUM:
            case TITANIUM:
                return 2;
            case SILVER:
            case STEEL:
            case URANIUM:
                return 3;
            case OBSIDIAN:
            case PLUTONIUM:
                return 4;
            case RAINBOWROCK:
                return 5;
            case TURNIP:
                return 6;
        }
        return 0;
    }

    @Override public int getCustomModelData() {
        switch (this.getWeapon()) {
            case SWORD:
                return getAddition(this.getType());
            case CLUB:
                return 10 + getAddition(this.getType());
            case DAGGER:
                return 20 + getAddition(this.getType());
            case SLING:
                return 30 + getAddition(this.getType());
            case DART_SHOOTER:
                return 40 + getAddition(this.getType());
            case STAFF:
                return 50 + getAddition(this.getType());
            case BOW:
                if (!this.getType().equals(ItemType.WOOD)) { return this.getType().getIdentifier() + 59; }
        }
        return super.getCustomModelData();
    }

    @Override public int getWorth() {
        return (int) ((this.getRarity().getValue()+2) * Math.pow(this.getType().getIdentifier(), 2));
    }
}

