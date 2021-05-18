package dev.forbit.loot;

import dev.forbit.items.DarkItems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class LootEvents implements Listener {
    private static final int topY = 140; // IMPORTANT
    DarkItems main;

    public LootEvents(DarkItems main) {
        this.main = main;
    }

    @EventHandler public void onChestOpen(PlayerInteractEvent event) {
        if (event.getHand() == null) { return; }
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) { return; }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (event.getClickedBlock() == null) { return; }
        Block b = event.getClickedBlock();
        if (!b.getType().equals(Material.CHEST)) { return; }
        Chest chest = (Chest) b.getState();
        // get the floor level of the chest.
        int floorLevel = Math.abs((topY - b.getY()) / 9);
        float chestLevel = ((floorLevel / 13.0f) * 20.0f); // assumes 13 is hell?
        if (chest.getCustomName() != null && (chest.getCustomName().equals("0") || chest.getCustomName().equals("1"))) {
            if (Objects.equals(chest.getCustomName(), "1")) {
                chestLevel = (floorLevel / 13.0f) * 30.0f;
            }
            DarkLootChest loot = new DarkLootChest(chestLevel);
            chest.setCustomName(loot.getTitle());
            chest.update();
            chest.getBlockInventory().setStorageContents(loot.getContents());
        }
    }

    @EventHandler public void onFlowerPotBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.FLOWER_POT)) return;
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();
        // flower pot loot
        // calculate loot drop
        LootDrop drop = LootDrop.getDrop(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', drop.award(player)));
        event.setDropItems(false);
        event.setExpToDrop(0);
    }
}
