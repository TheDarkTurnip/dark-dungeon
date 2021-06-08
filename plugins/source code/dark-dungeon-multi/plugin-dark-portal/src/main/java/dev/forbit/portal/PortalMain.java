package dev.forbit.portal;

import dev.forbit.generator.plugin.Generator;
import dev.forbit.generator.plugin.Position;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PortalMain extends JavaPlugin implements Listener {
    List<Porter> portals = new ArrayList<>();

    @Override public void onEnable() {
        World world = Bukkit.getWorld("world");
        World playerWorld = Bukkit.getWorld(Generator.PLAYER_DUNGEON_WORLD_NAME);

        /* PORTAL TO PLAYER ISLAND FROM SPAWN */
        portals.add(new Porter(world, Material.OBSIDIAN, new PorterRunnable() {
            @Override public void run() {
                Position pos = Generator.getAPI().getCell(getPlayer().getUniqueId());
                Location anchor = Generator.getAPI().getAnchorPoint(pos);
                getPlayer().teleport(anchor);
            }
        }));
        /* PORTAL TO COMPETITION AREA FROM SPAWN */
        portals.add(new Porter(world, Material.GOLD_BLOCK, new PorterRunnable() {
            @Override public void run() {
                Location location = new Location(Bukkit.getWorld("world"), -1842.5f, 124.1f, 184757.5f);
                location.setYaw(180);
                location.setPitch(0);
                getPlayer().teleport(location);

            }
        }));
        /* PORTAL TO GUILD AREA FROM SPAWN */
        portals.add(new Porter(world, Material.EMERALD_BLOCK, new PorterRunnable() {
            @Override public void run() {
                // teleport to guild area
                // TODO GUILDS
            }
        }));
        /* PORTAL TO FISHING AREA FROM SPAWN */
        portals.add(new Porter(world, Material.LAPIS_BLOCK, new PorterRunnable() {
            @Override public void run() {
                // teleport to fishing area
                Location location = new Location(Bukkit.getWorld("world"), 140217.5f, 86.1f, 18351.5f);
                location.setYaw(0);
                location.setPitch(0);
                getPlayer().teleport(location);
            }
        }));

        /* PORTAL TO PLAYER DUNGEON FROM PLAYER ISLAND */
        portals.add(new Porter(playerWorld, Material.POLISHED_BLACKSTONE_BRICKS, new PorterRunnable() {
            @Override public void run() {
                /*
                 * Teleport to dungeon area
                 */
                // BUG is null when generating first level.
                // Possible fix IDEA have first level generated all the time (on player join)
                getPlayer().teleport(Generator.getAPI().getCheckPoint(getPlayer().getUniqueId()));
            }
        }));


        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler public void onTeleport(PlayerPortalEvent event) {
        event.setCancelled(true);
        Block standing = event.getPlayer().getLocation().add(0, -1, 0).getBlock();
        System.out.println("block: " + standing); // OUTPUT block:
        for (Porter port : portals) {
            if (port.getBlockType().equals(standing.getType()) && port.getOriginWorld().equals(event.getPlayer().getWorld())) {
                port.getRunnable().setPlayer(event.getPlayer());
                port.getRunnable().run();
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.1f, 0f);
            }
        }
    }
}
