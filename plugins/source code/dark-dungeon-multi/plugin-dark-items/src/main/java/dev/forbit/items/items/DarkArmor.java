package dev.forbit.items.items;

import dev.forbit.items.items.types.*;
import dev.forbit.library.Utils;
import dev.forbit.library.rarity.Rarity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class DarkArmor extends Armor implements Enchantable, Modifiable, Repairable, Typeable, Raritable, Sellable {
    @Getter @Setter HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    @Getter @Setter Modifier modifier = Modifier.GODLY;
    @Getter @Setter int uses;
    @Getter @Setter int repair;

    public DarkArmor() {
        setItemGroup(ItemGroup.ARMOR);
    }

    @Override public double getProtection() {
        return getType().getProtectionModifier() * getModifier().getProtectionModifier();
    }

    @Override public String getDisplayName() {
        String mod = Utils.namify(getModifier().name());
        String typ = Utils.namify(getType().name());
        String wep = Utils.namify(getArmorType().name());
        return getRarity().getColor() + mod + " " + typ + " " + wep;
    }

    @Override public Rarity getRarity() {
        return getModifier().getRarity();
    }

    @Override public int getWorth() {
        return (int) ((this.getRarity().getValue()+2) * Math.pow(this.getType().getIdentifier(), 2));
    }

}
