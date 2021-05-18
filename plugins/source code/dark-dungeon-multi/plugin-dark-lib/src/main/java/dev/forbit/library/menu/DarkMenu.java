package dev.forbit.library.menu;

import dev.forbit.library.DarkLib;
import dev.forbit.library.menu.type.MenuNavItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class DarkMenu implements Listener {
    @Getter HashMap<Integer, MenuItem> items = new HashMap<>();
    @Getter @Setter String title;

    public DarkMenu(String title) {
        setTitle(title);
    }

    public Inventory getInventory() {
        // TODO auto size inventory
        Inventory inventory = Bukkit.createInventory(null, getSize(), ChatColor.translateAlternateColorCodes('&', title));

        for (int pos : items.keySet()) {
            inventory.setItem(pos, items.get(pos).getItemStack());
        }
        // register inventory
        return inventory;
    }

    public int getSize() {
        // get the last item
        int max = 0;
        for (int i : items.keySet()) {
            if (i > max) max = i;
        }
        return 9 + (max - max%9);

    }

    public void addItem(MenuItem m, int pos) {
        items.put(pos, m);
    }

    public void show(Player player) {
        player.openInventory(this.getInventory());
        Bukkit.getPluginManager().registerEvents(this, DarkLib.getInstance());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(ChatColor.translateAlternateColorCodes('&', getTitle()).equals(event.getView().getTitle()))) return;

        int clicked = event.getSlot();
        if (clicked < 0) return;
        if (items.containsKey(clicked)) {
            MenuItem item = items.get(clicked);
            if (item instanceof MenuNavItem) {
                MenuNavItem menuNavItem = (MenuNavItem) item;
                menuNavItem.setPlayer((Player) event.getWhoClicked());
            }

            Runnable runnable = items.get(clicked).getClickAction();
            if (runnable != null) runnable.run();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(ChatColor.translateAlternateColorCodes('&', getTitle()).equals(event.getView().getTitle()))) return;
        HandlerList.unregisterAll(this);

    }





}
