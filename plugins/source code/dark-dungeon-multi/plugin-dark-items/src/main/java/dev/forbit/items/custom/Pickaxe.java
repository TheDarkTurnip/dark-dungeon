package dev.forbit.items.custom;

import dev.forbit.items.items.DarkItem;
import dev.forbit.items.items.types.ItemGroup;
import dev.forbit.items.items.types.Loreable;
import dev.forbit.items.items.types.Nameable;
import dev.forbit.items.items.types.Raritable;
import dev.forbit.library.Utils;
import dev.forbit.library.rarity.Rarity;
import org.bukkit.Material;

public class Pickaxe extends DarkItem implements Nameable, Loreable, Raritable {

    public Pickaxe(Material material) {
        setMaterial(material);
        setItemGroup(ItemGroup.UTILITY);
        setCustomModelData(0);
    }

    @Override public String getCustomLore() {
        return "Used to disable Spawners and break Ores";
    }

    @Override public String getDisplayName() {
        return getRarity().getColor() + Utils.namify(getMaterial().name().split("_")[0]) + " Pickaxe";
    }

    @Override public Rarity getRarity() {
        switch (this.getMaterial()) {
            case WOODEN_PICKAXE:
                return Rarity.COMMON;
            case STONE_PICKAXE:
                return Rarity.UNCOMMON;
            case GOLDEN_PICKAXE:
                return Rarity.UNIQUE;
            case IRON_PICKAXE:
                return Rarity.RARE;
            case DIAMOND_PICKAXE:
                return Rarity.EPIC;
            case NETHERITE_PICKAXE:
                return Rarity.EXOTIC;
            default:
                return Rarity.JUNK;
        }
    }
}
