package dev.forbit.items.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.tr7zw.nbtapi.NBTItem;
import dev.forbit.items.custom.Pickaxe;
import dev.forbit.items.items.types.*;
import dev.forbit.library.Utils;
import jdk.internal.jline.internal.Nullable;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * API Class for dealing with DarkItems and ItemStacks
 */
public class DarkItemAPI {

    /* NBT API WRAPPERS */
    public ItemStack NBTInfuse(DarkItem darkItem, ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(darkItem);
        nbtItem.setString("data", json);
        nbtItem.setString("class", darkItem.getClass().getName());
        return nbtItem.getItem();
    }

    public DarkItem loadItem(@Nullable ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasKey("class")) return null;
        String className = nbtItem.getString("class");
        try {
            Class<?> clazz = Class.forName(className);
            Gson gson = new GsonBuilder().serializeNulls().create();
            return (DarkItem) gson.fromJson(nbtItem.getString("data"), clazz);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error creating darkItem!");
        }
        return null;
    }

    /**
     * Infuses an item
     *
     * @param darkItem
     *
     * @return
     */
    public ItemStack infuseItem(DarkItem darkItem) {
        ItemStack item = new ItemStack(darkItem.getMaterial());
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
        if (darkItem instanceof Nameable) {
            Nameable nameableItem = (Nameable) darkItem;
            meta.setDisplayName(nameableItem.getDisplayName());
        }

        meta.setLore(getLore(darkItem));
        meta.setCustomModelData(darkItem.getCustomModelData());
        item.setItemMeta(meta);
        if (darkItem instanceof Enchantable) {
            Enchantable enchantable = (Enchantable) darkItem;
            if (enchantable.getEnchantments().isEmpty()) {
                item.removeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY);
            } else {
                item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 10);
            }
        }
        if (darkItem instanceof Pickaxe) {
            return NBTInfuse(darkItem, setCanDestroy(setFlags(item), "gray_concrete", "gold_ore", "diamond_ore"));
        } else {
            return NBTInfuse(darkItem, setCanDestroy(setFlags(item), "flower_pot", "#leaves"));
        }
    }

    private List<String> getLore(DarkItem darkItem) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + darkItem.getItemGroup().getTitle());
        boolean data = false;
        lore.add(ChatColor.RESET + "");
        if (darkItem instanceof Restrictable) {
            data = true;
            Restrictable restrictable = (Restrictable) darkItem;
            lore.add(ChatColor.RED + "| " + ChatColor.GRAY + "Min. Level: " + ChatColor.RED + restrictable.getRequiredLevel());
        }
        if (darkItem instanceof Weapon) {
            // damage
            Weapon weapon = (Weapon) darkItem;
            String damage = ChatColor.RED + "| " + ChatColor.GRAY + "Damage: " + ChatColor.RED + Utils.round(weapon.getDamage());
            data = true;
            if (darkItem instanceof Repairable) {
                Repairable repairable = (Repairable) darkItem;
                if (repairable.isRepaired()) {
                    damage += ChatColor.translateAlternateColorCodes('&', "&8 (&fRepaired&8)");
                }
                else if (repairable.isBlunt()) {
                    damage += ChatColor.translateAlternateColorCodes('&', "&8 (&fBlunt&8)");
                }
            }
            lore.add(damage);
        } else if (darkItem instanceof Armor) {
            // armor
            data = true;
            Armor armor = (Armor) darkItem;
            String protection = ChatColor.RED+"| "+ChatColor.GRAY+"Protection: "+ChatColor.RED+Utils.round(armor.getProtection());
            if (darkItem instanceof Repairable) {
                Repairable repairable = (Repairable) darkItem;
                if (repairable.isRepaired()) {
                    protection += ChatColor.translateAlternateColorCodes('&', "&8 (&fRepaired&8)");
                }
                else if (repairable.isBlunt()) {
                    protection += ChatColor.translateAlternateColorCodes('&', "&8 (&fBlunt&8)");
                }
            }
            lore.add(protection);
        }

        if (darkItem instanceof Levellable) {
            data = true;
            Levellable levellable = (Levellable) darkItem;
            String xp = ChatColor.AQUA + "| " + ChatColor.GRAY + "XP: " + ChatColor.AQUA + levellable.getXP() + "/" + (int) Math.pow(2, levellable.getLevel() + 1);
            xp += ChatColor.DARK_AQUA + " (Level " + levellable.getLevel() + ")";
            lore.add(xp);
        }

        if (darkItem instanceof Sellable) {
            data = true;
            Sellable sellable = (Sellable) darkItem;
            lore.add(ChatColor.GRAY+"| Value: $"+sellable.getWorth());
        }

        if (darkItem instanceof Enchantable) {
            Enchantable enchantable = (Enchantable) darkItem;
            if (!enchantable.getEnchantments().isEmpty()) {
                if (data) lore.add(ChatColor.RESET+"");
                data = true;
                lore.add(ChatColor.of("#3f71ab")+"| "+ChatColor.UNDERLINE+"Enchantments");
                for (Enchantment e : enchantable.getEnchantments().keySet()) {
                    int level = enchantable.getEnchantment(e);
                    lore.add(e.getColor() + "- " + ChatColor.stripColor(e.getTitle()) +" "+ Utils.toRoman(level));
                }
            }


        }

        if (darkItem instanceof Loreable) {
            if (data) lore.add(ChatColor.RESET+"");
            data = true;
            Loreable loreable = (Loreable) darkItem;
            int size = 15;
            if (darkItem instanceof Nameable) {
                Nameable nameable = (Nameable) darkItem;
                size = getMaxLength(ChatColor.stripColor(nameable.getDisplayName()), lore);
            }
            lore.addAll(getCustomLoreList(Math.max(18,size + 3), null, loreable));
        }


        if (darkItem instanceof Raritable) {
            if (data) lore.add(ChatColor.RESET + " ");
            data = true;
            Raritable raritable = (Raritable) darkItem;
            lore.add(raritable.getRarity().getSecondaryColor() + ChatColor.BOLD + raritable.getRarity().name());
        }
        return lore;
    }

    private ItemStack setFlags(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) { return item; }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setCanDestroy(ItemStack item, String... tags) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

        NBTTagList idsTags = new NBTTagList();
        for (String s : tags) {
            if (s.startsWith("#")) {
                idsTags.add(NBTTagString.a("#minecraft:"+s.substring(1)));
            } else {
                idsTags.add(NBTTagString.a("minecraft:" + s));
            }
        }
        net.minecraft.server.v1_16_R3.NBTTagCompound tag = itemStack.hasTag() ? itemStack.getTag() : new net.minecraft.server.v1_16_R3.NBTTagCompound();
        assert tag != null;
        tag.set("CanDestroy", idsTags);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    private int getMaxLength(String name, List<String> lore) {
        int length = ChatColor.stripColor(name).length() - 1;
        for (String s : lore) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = ChatColor.stripColor(s);
            if (s.length() > length) { length = s.length() - 1; }
        }
        return length;
    }

    private List<String> getCustomLoreList(int wrapMax, Player player, Loreable item) {
        List<String> lore = new ArrayList<>();
        String str = getCustomLore(item, player);
        if (str != null && str.length() > 1) {
            int arrayMax = 12;
            for (String s : splitString(str, wrapMax, arrayMax)) {
                if (s != null) { lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + s); }
            }
        }
        return lore;
    }

    public String getCustomLore(Loreable item, Player player) {
        if (player != null) {
            return item.getCustomLore().replaceAll("\\{player\\}", player.getName());
        }
        else {
            return item.getCustomLore();
        }
    }

    public String[] splitString(String str, int wrapMax, int arrayMax) {
        String[] realLoreArray = str.split(" ");
        int currentLength = 0;
        int currentIndex = 0;
        String[] currentLoreArray = new String[arrayMax];
        for (String s : realLoreArray) {
            currentLength += (s + " ").length();
            if (currentLength > wrapMax) {
                currentLength = (s + " ").length();
                currentIndex++;
            }
            if (currentLoreArray[currentIndex] == null) {
                currentLoreArray[currentIndex] = s + " ";
            } else {
                currentLoreArray[currentIndex] += s + " ";
            }
        }
        return currentLoreArray;
    }
}
