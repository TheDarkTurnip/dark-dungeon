package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerArea {
    @Getter @Setter HashMap<Integer, NPCType> currentAreas = new HashMap<>();
    @Getter @Setter List<Area> areas = new ArrayList<Area>() {
        {
            add(new Area(NPCType.FARMER, 1));
            add(new Area(NPCType.SMITH, 0));
            add(new Area(NPCType.HEALER, 0));
            add(new Area(NPCType.ALCHEMIST, 0));
            add(new Area(NPCType.GUILD_REP, 0));
            add(new Area(NPCType.INFUSER, 0));
            add(new Area(NPCType.ADV_ITEM_SMITH, 0));
            add(new Area(NPCType.ENCHANTER, 0));
        }
    };
    @Getter @Setter UUID owner;
    @Getter @Setter int unlockedAreas;
    @Getter @Setter double centerX;
    @Getter @Setter double centerY;
    @Getter @Setter double centerZ;

    public Location getLocation() {
        return new Location(Bukkit.getWorld("player_world"), getCenterX(), getCenterY(), getCenterZ());
    }

    public Location getNPCManagerLocation() {
        Location loc = getLocation().clone();
        loc.add(-6.5, -1, -3.5);
        return loc;
    }

    /**
     * @param type NPCType the type of npc
     *
     * @return reference to the area class of NPC type
     *
     * @throws IllegalArgumentException if type is found to not have a corresponding area inside {#areas}
     */
    public Area getArea(@NonNull NPCType type) {
        for (Area area : areas) {
            if (area.getType().equals(type)) {
                return area;
            }
        }
        throw new IllegalArgumentException("type was not found to have a corresponding area");
    }

    /**
     * Gets a list of NPC's that haven't been added
     *
     * @return
     */
    public List<NPCType> getAvailableNPCS() {
        List<NPCType> list = new ArrayList<>(List.of(NPCType.values()));
        for (Area area : getAreas()) {
            NPCType type = area.getType();
            if (getCurrentAreas().containsValue(type) || area.getLevel() <= 0) { list.remove(type); }
        }
        return list;
    }
}
