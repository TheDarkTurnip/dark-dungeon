package dev.forbit.darknpc.areas;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
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


    // WORLD EDIT PASTING STUFF
    public void pasteArea(Area area, int areaID) {
        AreaOffset offset = AreaOffset.getOffset(areaID + 1);
        String schematic = area.getSchematic();
        Location location = getLocation().clone().add(offset.getXOffset(), offset.getYOffset(), offset.getZOffset());
        String fileName = ("plugins/FastAsyncWorldEdit/schematics/areas/" + schematic + ".schem");
        AreaAPI.pasteArea(fileName, location);
    }

    public void clearArea(int areaID) {
        AreaOffset offset = AreaOffset.getOffset(areaID + 1);
        Location location = getLocation().clone().add(offset.getXOffset(), offset.getYOffset(), offset.getZOffset());
        AreaAPI.pasteArea("plugins/FastAsyncWorldEdit/schematics/areas/blank_area.schem", location);
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

    public int getPosition(Area area) {
        for (int i : getCurrentAreas().keySet()) {
            if (getCurrentAreas().get(i).equals(area.getType())) return i;
        }
        throw new ArrayIndexOutOfBoundsException("Could not find current area index for area "+area+" (NPCTYPE: "+area.getType().name());
    }

    public int getPosition(NPCType type) {
        for (int i : getCurrentAreas().keySet()) {
            if (getCurrentAreas().get(i).equals(type)) return i;
        }
        throw new ArrayIndexOutOfBoundsException("Could not find current area index for NPC type: "+type.name());
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

    /**
     * Unlocks an npc
     *
     * @param t
     */
    public void unlockNPC(NPCType t) {
        getArea(t).setLevel(1);
    }

    /**
     * Upgrades an NPC's level
     *
     * @param t
     */
    public boolean upgrade(NPCType t) {
        if (getArea(t) == null || getArea(t).getLevel() <= 0) { return false; }
        getArea(t).setLevel(getArea(t).getLevel() + 1);

        //update if being used currently.
        if (getCurrentAreas().containsValue(t)) {
            int position = getPosition(t);
            pasteArea(getArea(t), position);
        }
        return true;
    }

    /**
     * Sets the npc at area #id to type type
     *
     * @param id
     * @param type
     */
    public void setArea(int id, NPCType type) {
        getCurrentAreas().put(id, type);
        pasteArea(getArea(type), id);
    }

    /**
     * Clear the area at #id
     *
     * @param id
     */
    public void clear(int id) {
        getCurrentAreas().remove(id);
        clearArea(id);
    }
}
