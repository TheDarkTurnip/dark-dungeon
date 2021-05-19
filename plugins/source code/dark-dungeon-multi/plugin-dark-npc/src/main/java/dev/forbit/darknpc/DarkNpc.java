package dev.forbit.darknpc;

import dev.forbit.darknpc.areas.AreaAPI;
import dev.forbit.darknpc.areas.PlayerArea;
import dev.forbit.generator.plugin.Generator;
import lombok.Getter;
import lombok.Setter;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class DarkNpc extends JavaPlugin implements Listener {

    @Getter @Setter private static AreaAPI API;
    @Getter @Setter private NPCLib library;
    @Getter private HashMap<UUID, PlayerArea> playerAreas = new HashMap<>();

    @Override public void onEnable() {
        setLibrary(new NPCLib(this));
        setAPI(new AreaAPI(this));
        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override public void run() {
                // loop through every npc and get them to look at player
                for (NPC npc : getAPI().getNpcs().keySet()) {
                    OfflinePlayer offlinePlayer = getAPI().getNpcs().get(npc);
                    if (!offlinePlayer.isOnline()) { continue; }
                    Player player = offlinePlayer.getPlayer();
                    if (player.getLocation().distanceSquared(npc.getLocation()) < 500) {
                        npc.lookAt(player.getEyeLocation().add(0,-1.25,0));
                    } else {
                        npc.lookAt(Generator.getAPI().getAnchorPoint(Generator.getAPI().getCell(player.getUniqueId())));
                    }
                }
            }
        }, 100L, 1L);
    }

    @Override public void onDisable() {
        for (NPC npc : getAPI().getNpcs().keySet()) {
            npc.destroy();
        }
    }

    @EventHandler public void onJoin(PlayerJoinEvent event) {
        getAPI().initArea(event.getPlayer().getUniqueId());
        //getLogger().info("made npcs for "+event.getPlayer().getName());
    }

    @EventHandler public void onLeave(PlayerQuitEvent event) {
        List<NPC> toRemove = new ArrayList<>();
        for (NPC npc : getAPI().getNpcs().keySet()) {
            OfflinePlayer offlinePlayer = getAPI().getNpcs().get(npc);
            if (offlinePlayer.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                npc.destroy();
                toRemove.add(npc);
            }
        }
        toRemove.forEach((npc -> {
            getAPI().getNpcs().remove(npc);
        }));
        getLogger().info("removed " + toRemove.size()+" npcs.");
    }

    @EventHandler public void onInteract(NPCInteractEvent event) {
        if (getAPI().getNpcs().containsKey(event.getNPC())) {
            NPC npc = event.getNPC();
            Player player = event.getWhoClicked();
            if (ChatColor.stripColor(npc.getText().get(0)).equals("NPC Manager")) {
                getAPI().getManagerMenu(getAPI().getArea(event.getWhoClicked())).show(player);
            }
        }
    }
}


