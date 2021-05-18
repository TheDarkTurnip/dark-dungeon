package dev.forbit.generator.plugin;

import dev.forbit.generator.generator.Floor;
import dev.forbit.generator.generator.Tile;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * API class for DungeonGenerator<br>
 * Should be obtained using {@link Generator#getAPI()}
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class GeneratorAPI {
    @Getter @Setter private static Generator gen;
    @Getter @Setter private WorldEditUtils worldEditUtils;

    public GeneratorAPI(Generator gen) {
        setGen(gen);
        setWorldEditUtils(new WorldEditUtils(gen));
    }

    /**
     * Looks through all cells <i>(O(n))</i> and attempts to find the first cell
     * with a matching UUID.
     * <p>
     * If it can't find a cell, then it will get the next available cell and
     * generate the starting area for it.
     * </p>
     *
     * @param id The UUID of the player to search for.
     *
     * @return Position of cell with <code> owner == id </code>, or the
     * {@link #getNextAvailablePosition()}
     *
     * @throws NullPointerException throws null pointer exception if {@link #getNextAvailablePosition()} returns null.
     */
    public Position getCell(UUID id) throws NullPointerException {
        // search through all positions in current memory
        for (Position p : getGen().getCells()) {
            if (p == null) { continue; }
            if (p.getOwner().toString().equals(id.toString())) { return p; }
        }
        // else, generate a new cell
        Position cell = getNextAvailablePosition();
        if (cell == null) { throw new NullPointerException("Cell is null!"); }
        cell.setOwner(id); // set the owner to the player we originally searched for.
        getGen().getCells().add(cell); // plop it into memory.
        /* Generate the new area
         * This uses FAWE, and lags the server.
         * REFACTOR Run Async */
        makeArea(cell);
        getGen().getLogger().info("GENERATING NEW AREA FOR " + id); // OUTPUT GENERATING NEW AREA
        return cell;
    }

    /**
     * Looks through each cell and finds the first one that isnt occupied
     * <p>
     * The algorithm will attempt to fill the square in, with size maxRow^2 before
     * increasing the size of the square.
     * </p>
     *
     * @return The first cell that isn't occupied (in a square pattern)
     */
    @Nullable public Position getNextAvailablePosition() {
        // start at (0,0)
        /* increase max_x by 1 loop through x, every y value until max_x, increase
         * max_x */
        Position pos;
        for (int max_x = 0; max_x <= getGen().getMaxRow(); max_x++) {
            for (int x = 0; x < max_x; x++) {
                for (int y = 0; y < max_x; y++) {
                    if (getGen().getCell(x, y) == null) {
                        pos = new Position(x, y);
                        return pos;
                    }
                }
            }
            getGen().setMaxRow(max_x + 1);
        }
        return null;
    }

    /**
     * Gets the anchor point based on a cell position.
     *
     * @param pos The cell
     *
     * @return Anchor Point Location
     */
    public Location getAnchorPoint(Position pos) {
        return new Location(getGen().getPlayerWorld(), pos.getAnchorX(), pos.getAnchorY(), pos.getAnchorZ());
    }

    /**
     * Pastes the area at the cell.
     *
     * @param pos Position to make area
     */
    public void makeArea(Position pos) {
        // sets the anchor point to the middle of the player's 1kx1k cell.
        Location anchorPoint = new Location(getGen().getPlayerWorld(), pos.getGridX() * 1000 + 500, getGen().getTopY() + 7, pos.getGridY() * 1000 + 500);
        // sets the anchor point data
        pos.setAnchorX(anchorPoint.getX());
        pos.setAnchorY(anchorPoint.getY());
        pos.setAnchorZ(anchorPoint.getZ());
        // REFACTOR run async
        // pasting the area lags!
        getWorldEditUtils().pasteArea(anchorPoint);
        // generate the first level!
        getGen().scheduleGen(pos.getOwner(), 0);
    }

    /**
     * Prints out tile data to the player.
     *
     * @param t      Tile
     * @param player Player
     */
    public void printTile(Tile t, Player player) {
        if (t == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNo Tile Here!"));
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Tile at &a(" + t.getX() + "," + t.getY() + ")&7:"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7bitwise: &a" + t.getBitwise()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7isEnd: &a" + t.isEnd()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7isLoot: &a" + t.isLoot()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7isStart: &a" + t.isStart()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7isValue: &a" + t.isValue()));
        player.sendMessage(ChatColor.GRAY + "--------------------------");
    }

    /**
     * Generates a Floor using the {@link dev.forbit.generator.generator.Generator}
     * algorithm.
     *
     * @param id The ID of the player we're generating the dungeon for.
     * @param i  The level
     *
     * @return Floor.
     */
    public Floor generateLevel(UUID id, int i) {
        dev.forbit.generator.generator.Generator gen = new dev.forbit.generator.generator.Generator();
        Floor f;
        if (i > 0) {
            f = gen.generate(i+1, getDungeon(id).getFloors().get(i-1).getEnd());
        } else {
            f = gen.generate(i + 1);
        }
        if (getGen().getInstances().get(id) == null) { getGen().getInstances().put(id, new PlayerDungeon()); }
        getGen().getInstances().get(id).getFloors().put(i, f);
        return f;
    }

    /***
     * Returns the PlayerDungeon instance of a given UUID.
     * @param id id of the player.
     * @return Player's Dungeon
     */
    @Nullable public PlayerDungeon getDungeon(UUID id) {
        return getGen().getInstances().get(id);
    }

    public Location getCheckPoint(UUID id) {
        PlayerDungeon dungeon = getDungeon(id);
        if (dungeon == null) {
            // create new dungeon instance
            getGen().scheduleGen(id, 0);

        }
        else if (dungeon.getFloors().isEmpty()) {
            // generate new floor @ 0
            getGen().scheduleGen(id, 0);
        }
        // REFACTOR temp
        if (dungeon.getCheckpointY() <= 0) {
            Floor startFloor = dungeon.getFloors().get(0);
            Tile startTile = startFloor.getStart();
            int tile_x = startTile.getX();
            int tile_y = startTile.getY();
            Position cell = getCell(id);
            int rel_x = (cell.getGridX() * 1000) + (tile_x * 21);
            int rel_y = (Generator.getInstance().getTopY());
            int rel_z = (cell.getGridY() * 1000) + (tile_y * 21);
            dungeon.setCheckpointX(rel_x+10);
            dungeon.setCheckpointY(rel_y-4);
            dungeon.setCheckpointZ(rel_z+10);
        }
        // there should now definitely be a dungeon at 0
        return dungeon.getCheckPoint();
    }


    /**
     * Retires a tile from a location
     * @param location
     * @return
     */
    @Nullable public Tile getTile(Location location) {
        Floor f = getFloor(location);
        if (f == null) { return null; }
        int rel_x = (location.getBlockX() % 1000) / 21;
        int rel_z = (location.getBlockZ() % 1000) / 21;
        return f.getTile(rel_x, rel_z);
    }

    /**
     * Retrieves a floor from a location
     * @param location
     * @return
     */
    @Nullable public Floor getFloor(Location location) {
        Position pos = getPosition(location);
        if (pos == null) { return null; }
        // pos is the cell, from this we can get the owner
        UUID owner = pos.getOwner();
        // with the owner we can now get the floor data
        PlayerDungeon dungeon = getGen().getInstances().get(owner);
        // with the dungeon we can now get the tile data at x,y,z
        int level = Math.abs((getGen().getTopY() - location.getBlockY()) / 9);
        //Floor f = dungeon.getFloors().get(level);
        if (dungeon.getFloors().isEmpty()) { return null; }
        return dungeon.getFloors().get(level);

    }

    /**
     * Retrivies the postion cell from a location
     * @param location
     * @return
     */
    @Nullable public Position getPosition(Location location) {
        return getGen().getCell(location.getBlockX() / 1000, location.getBlockY() / 1000);
    }
}
