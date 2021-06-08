package dev.forbit.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.slikey.effectlib.EffectManager;
import de.tr7zw.nbtapi.NBTItem;
import dev.forbit.effects.MobEffects;
import dev.forbit.items.custom.Pickaxe;
import dev.forbit.items.events.BukkitListener;
import dev.forbit.items.events.DarkDamageListener;
import dev.forbit.items.items.DamageProperties;
import dev.forbit.items.items.DarkItem;
import dev.forbit.items.items.DarkItemAPI;
import dev.forbit.items.items.DarkWeapon;
import dev.forbit.items.items.types.Weapon;
import dev.forbit.loot.Loot;
import dev.forbit.projectile.ProjectileManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class DarkItems extends JavaPlugin implements CommandExecutor {

    public static EffectManager effectManager;
    public static ProjectileManager projectileManager;
    public static MobEffects effects;
    public static Loot loot;
    @Getter private static DarkItemAPI api;
    @Getter private static Holograms holograms;

    public DarkItems() {
        super();
    }

    protected DarkItems(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }


    @Override public void onEnable() {
        // Plugin startup logic
        DarkWeapon weapon = new DarkWeapon();
        holograms = new Holograms(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(weapon));

        api = new DarkItemAPI();
        projectileManager = new ProjectileManager(this);
        effectManager = new EffectManager(this);
        effects = new MobEffects(this);
        loot = new Loot(this);

        this.getServer().getPluginManager().registerEvents(new BukkitListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DarkDamageListener(this), this);


    }

    @Override public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (label) {
            case "infuse": {
                Player player = (Player) sender;
                player.getInventory().setItemInMainHand(api.infuseItem(new DarkWeapon()));
                return true;
            }
            case "load": {
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();
                DarkItem darkItem = api.loadItem(item);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Item: &b" + darkItem));
                return true;
            }
            case "loot": {
                Player player = (Player) sender;
                Inventory inv = Bukkit.createInventory(null, 27, "items");
                for (int i = 0; i < 27; i++) {
                    inv.setItem(i, DarkItemFactory.getRandomItem());
                }
                inv.setItem(0, getApi().infuseItem(new Pickaxe(Material.WOODEN_PICKAXE)));
                inv.setItem(1, getApi().infuseItem(new Pickaxe(Material.STONE_PICKAXE)));
                inv.setItem(2, getApi().infuseItem(new Pickaxe(Material.GOLDEN_PICKAXE)));
                inv.setItem(3, getApi().infuseItem(new Pickaxe(Material.IRON_PICKAXE)));
                inv.setItem(4, getApi().infuseItem(new Pickaxe(Material.DIAMOND_PICKAXE)));
                inv.setItem(5, getApi().infuseItem(new Pickaxe(Material.NETHERITE_PICKAXE)));
                player.openInventory(inv);
                return true;
            }
            case "damage": {
                Player player = (Player) sender;
                DarkItem darkItem = api.loadItem(player.getInventory().getItemInMainHand());
                if (darkItem instanceof Weapon) {
                    DamageProperties properties = new DamageProperties((Weapon) darkItem);
                    for (String s : properties.getDamageString()) {
                        player.sendMessage(s);
                    }
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED+"You must be holding a weapon!");
                }
                return true;
            }
        }
        return false;
    }

    public void updateInventory(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("class")) {
                DarkItem darkItem = getApi().loadItem(item);
                inventory.setItem(i, getApi().infuseItem(darkItem));
            } else {
                continue;
            }
        }
    }

}
