package dev.forbit.generator.plugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;

/**
 * Class that handles all WorldEdit-related activities.<br>
 * Should be called using <code>getAPI().getWorldEditUtils()</code>
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 * @see GeneratorAPI
 */
public class WorldEditUtils {
    @Getter @Setter private Generator gen;

    public WorldEditUtils(Generator gen) { setGen(gen); }

    /**
     * Pastes a schematic cell at a given location
     *
     * @param string    The schematic name, .schem is auto-appenended to the end.
     * @param topCorner Origin point of paste.
     */
    public void pasteCell(String string, Location topCorner) {
        File file = new File("plugins/FastAsyncWorldEdit/schematics/" + string + ".schem"); // load the schematic.
        BukkitWorld world = new BukkitWorld(getGen().getPlayerWorld()); // get the worldedit version of the world we're pasting into.
        ClipboardFormat format = ClipboardFormats.findByFile(file); // get the format
        if (format == null) { return; }
        Clipboard clipboard = null;
        // read the file into the clipboard.
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (Exception e) {
            System.err.println("Error Reading File: " + string + ".schem");
            e.printStackTrace();
        }
        if (clipboard == null) { return; }
        // create the editsession and paste.
        try (@SuppressWarnings("deprecation") EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            // create paste
            Operation operation = holder.createPaste(editSession).to(BlockVector3.at(topCorner.getX(), topCorner.getY(), topCorner.getZ())).ignoreAirBlocks(false).build();
            Operations.complete(operation); // complte operation
        } catch (Exception e) {
            System.err.println("Error Pasting Cell: " + string + ".schem");
            e.printStackTrace();
        }
    }

    /**
     * Pastes the player base schematic at a point.
     *
     * @param point Anchor Point.
     */
    public void pasteArea(Location point) {
        if (getGen().isTest()) return;
        File file = new File("plugins/FastAsyncWorldEdit/schematics/player_base_2.schem"); // load schematic
        BukkitWorld world = new BukkitWorld(getGen().getPlayerWorld()); // load world.
        ClipboardFormat format = ClipboardFormats.findByFile(file); // load clipboard format
        Clipboard clipboard = null;
        if (format == null) { return; }

        // read file into clipboard
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (clipboard == null) { return; }
        // prepare editsesison for pasting
        try (@SuppressWarnings("deprecation") EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            Operation operation = holder.createPaste(editSession)
                                        .to(BlockVector3.at(point.getX(), point.getY(), point.getZ()))
                                        .ignoreAirBlocks(false)
                                        .copyBiomes(false)
                                        .build(); // BUG copy biomes doesnt work.
            Operations.complete(operation); // paste.
        } catch (Exception e) {
            e.printStackTrace();
        }
        // after pasting, fix the lighting
        // again, a couple lighting bugs.
        // BUG lighting.
        /*if (Generator.getAPI().isFixLighting()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getGen(), () -> {
                CuboidRegion region = new CuboidRegion(BlockVector3.at(point.getX() - 150, point.getY() - 50, point.getZ() - 100), BlockVector3.at(point.getX() + 100, point.getY() + 120, point
                        .getZ() + 100));
                FaweAPI.fixLighting(world, region, null, RelightMode.ALL);
            }, 50L);
        }*/
    }

    /**
     * Clears the generated dungeon area of a given cell
     *
     * @param cell Position to clear.
     */
    public void clearArea(Position cell) {
        /*
         * TODO Optimization
         *
         * We probably don't have to be clearing the ENTIRE area each time.
         * Try to get the size of the currently generated cells and only clear that box,
         * or alternatively loop through each cell and only clear those.
         */
        int start_x = cell.getGridX() * 1000;
        int start_z = cell.getGridY() * 1000;
        int end_x = start_x + 232;
        int end_z = start_z + 232;
        Region region = new CuboidRegion(BlockVector3.at(start_x, 1, start_z), BlockVector3.at(end_x, 140, end_z));
        getGen().getGeneratorTasks().addTask(() -> {
            try (@SuppressWarnings("deprecation") EditSession editSession = WorldEdit.getInstance()
                                                                                     .getEditSessionFactory()
                                                                                     .getEditSession(new BukkitWorld(getGen().getPlayerWorld()), -1)) {
                editSession.setBlocks(region, BukkitAdapter.adapt(Material.AIR.createBlockData())); // SERIOUSLY LAGS
                editSession.flushQueue();
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        });
    }


    public void clearLootRoom(Location location) {

        final Material spawnerBlock = Material.REDSTONE_BLOCK;
        Region region = new CuboidRegion(BlockVector3.at(location.getBlockX(), location.getBlockX(), location.getBlockZ()), BlockVector3.at(location.getBlockX() + 20, location.getBlockY() + 9, location
                .getBlockZ() + 20));
        try (@SuppressWarnings("deprecation") EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(gen.getPlayerWorld()), -1)) {
            BaseBlock[] blocks = new BaseBlock[2];
            blocks[0] = BukkitAdapter.adapt(Material.BLACK_CONCRETE.createBlockData()).toBaseBlock();
            blocks[1] = BukkitAdapter.adapt(Material.GOLD_BLOCK.createBlockData()).toBaseBlock();
            Mask mask = new BlockMask(editSession.getExtent(), blocks);
            editSession.replaceBlocks(region, mask, BukkitAdapter.adapt(Material.AIR.createBlockData()));
            editSession.flushQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
