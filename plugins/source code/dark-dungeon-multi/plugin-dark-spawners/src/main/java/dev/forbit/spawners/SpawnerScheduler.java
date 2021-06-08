package dev.forbit.spawners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.spawners.classes.Spawner;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnerScheduler implements Listener {
    DarkSpawners plugin;


    public SpawnerScheduler(DarkSpawners spawners) {
        plugin = spawners;
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::tick, 20L, 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::display, 20L, 5L);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    private static String format(String string) {
        string = string.replaceAll("\"", "&7\"&b").replaceAll("\\{", "&8{&b").replaceAll("\\}", "&8}&b").replaceAll(":", "&8:");
        //System.out.println(string);
        return string;
    }

    public void tick() {
        // loop through every single spawner
        if (Bukkit.getOnlinePlayers().size() <= 0) { return; }
        for (PlayerSpawners pSpawners : plugin.getPlayerSpawners()) {
            OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(pSpawners.getOwner());
            if (oPlayer.isOnline()) {
                for (Spawner s : pSpawners.getSpawners()) {
                    if (s.isActive()) {
                        // IDEA could add a check to see if players are in world, might reduce lag a bit?
                        Player player = SpawnerAPI.getClosestPlayer(s.getLocation(), 40);
                        if (player != null) {
                            task(s::tick);
                        }
                    }
                }
            }
        }
    }

    public void display() {
        final Material spawnerMat = Material.WITHER_ROSE;
        List<Player> players = new ArrayList<Player>();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getInventory().getItemInMainHand().getType().equals(spawnerMat) || p.getInventory().getItemInOffHand().getType().equals(spawnerMat)) {
                if (Objects.equals(p.getLocation().getWorld(), Bukkit.getWorld("player_world"))) players.add(p);
            }
        }
        // show effect
        for (PlayerSpawners playerSpawners : plugin.getPlayerSpawners()) {
            for (Spawner s : playerSpawners.getSpawners()) {
                plugin.getEffectManager().display(Particle.FLAME, s.getLocation(), 0, 0, 0, (float) 0.02, 15, 1, Color.WHITE, null, (byte) 31, 0.0, players);

            }
        }
    }

    @EventHandler public void onDeath(MythicMobDeathEvent event) {
        Spawner spawner = DarkSpawners.getApi().getParentSpawner(event.getMob());
        if (spawner == null) { return; }
        spawner.getActiveMobs().remove(event.getMob());
    }

    @EventHandler public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (event.getHand() == null) { return; }
        if (!event.getHand().equals(EquipmentSlot.HAND)) { return; }
        if (event.getClickedBlock() == null) { return; }
        if (event.getClickedBlock().getType().equals(Material.GRAY_CONCRETE)) {
            // print spawner at block
            Spawner spawner = DarkSpawners.getApi().getSpawner(event.getClickedBlock());
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String string = gson.toJson(spawner);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', format(string)));
        }
    }

    @EventHandler public void onBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.GRAY_CONCRETE)) { return; }
        Spawner spawner = DarkSpawners.getApi().getSpawner(event.getBlock());
        if (spawner == null) { return; }
        spawner.setActive(false);
        event.setCancelled(true);
        event.getBlock().setType(Material.LIGHT_GRAY_CONCRETE);
        System.out.println("spawner borken!");

    }

    public void task(Runnable r) {
        Bukkit.getScheduler().runTask(plugin, r);
    }
}
