package dev.forbit.generator.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import de.slikey.effectlib.EffectManager;
import dev.forbit.generator.generator.Floor;
import dev.forbit.generator.generator.Tile;
import dev.forbit.generator.plugin.Tiles.Biome;
import dev.forbit.library.DarkLib;
import dev.forbit.library.DarkWorld;
import dev.forbit.library.group.MobGroup;
import dev.forbit.spawners.DarkSpawners;
import dev.forbit.spawners.classes.MobGroupSpawner;
import dev.forbit.world.ChunkGen;
import dev.forbit.world.FlatGen;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Minecraft plugin for DarkDungeon Any
 * calls to function should be called using the GeneratorAPI
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class Generator extends JavaPlugin implements Listener, CommandExecutor {
    /* ----------------------------------------------------------------------- */
    /* |						GETTERS AND SETTERS 						 | */
    /* ----------------------------------------------------------------------- */
    /**
     * The name of the dungeon world TODO: load from file
     */
    public static final String PLAYER_DUNGEON_WORLD_NAME = "player_world";
    /**
     * Static reference to this instance
     *
     * @see #getInstance()
     * @deprecated
     */
    @Deprecated private static Generator instance;
    /**
     * The GeneratorAPI.
     */
    private static GeneratorAPI API;
    @Getter @Setter private static EffectManager effectManager;
    /**
     * The point where the player base ends, and the player dungeon generation
     * begins.
     */
    @Getter private final int topY = 140;
    /**
     * Stores a list of player cells <a href="#{@link}">{@link Position}</a>
     */
    @Getter List<Position> cells = new ArrayList<>();
    /**
     * A seperate thread to run tasks relating to generating levels, so they don't
     * block the server's main thread.
     */
    @Getter GenThread generatorTasks = new GenThread(this);
    /**
     * Hosts a map of {@link PlayerDungeon}s, which store information regarding
     * dungeon generation.
     */
    @Getter HashMap<UUID, PlayerDungeon> instances = new HashMap<>();
    /**
     * The directory to use to store the player dungeons.
     */
    @Getter File playerDungeonDir;
    /**
     * These worlds are generated {@link #onEnable}
     */
    @Getter @Setter private World playerWorld, schemWorld;
    /**
     * The row that we are currently at, in terms of positional square
     * <p>
     * The size of cells should always be less than maxRow^2
     * </p>
     */
    @Getter @Setter private int maxRow = 0;

    /**
     * Returns the instance of GeneratorAPI
     */
    public static GeneratorAPI getAPI() { return API; }

    /**
     * Method for staticly retrieving the instance of this plugin
     *
     * @return Generator.instance
     */
    public static Generator getInstance() { return instance; }
    /* ----------------------------------------------------------------------- */
    /* |						PLUGIN FUNCTIONS	 						 | */
    /* ----------------------------------------------------------------------- */

    /**
     * Loads directories, registers events, starts the generatorTasks thread, makes
     * the schem_world and player_world, and finally loads playerdungeon data.
     */
    @Override public void onEnable() {
        API = new GeneratorAPI(this);
        instance = this;
        setEffectManager(new EffectManager(this));
        makeDir();
        this.getServer().getPluginManager().registerEvents(this, this);
        generatorTasks.start();
        makeWorld();
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void onDisable() {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Requires an argument: /gen <clear|tp|n>");
            return true;
        }
        Player p = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "clear": {
                // removes all generation.
                getAPI().getWorldEditUtils().clearArea(getAPI().getCell(p.getUniqueId()));
                getInstances().get(p.getUniqueId()).setFloors(new HashMap<>());
                getInstances().get(p.getUniqueId()).invalidateCheckpoint();
                DarkSpawners.getApi().clear(p);
                return true;
            }
            case "tp": {
                // teleports the player to the topcorner of their starting cell.
                Tile t = getInstances().get(p.getUniqueId()).getFloors().get(0).getStart();
                int grix = getAPI().getCell(p.getUniqueId()).getGridX();
                int grip = getAPI().getCell(p.getUniqueId()).getGridY();
                int x = t.getX() * 21 + (grix * 1000);
                int z = t.getY() * 21 + (grip * 1000);
                int y = 132;
                Location loc = new Location(getPlayerWorld(), x, y, z);
                p.teleport(loc);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Teleport to &ddungeon"));
                return true;
            }
            default: {
                // example: /gen 1
                // generates the level 1 and only level 1.
                try {
                    int i = Integer.parseInt(args[0]);
                    scheduleGen(p.getUniqueId(), i);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error parsing an argument: /gen <clear|tp|n>");
                    return true;
                }
            }
        }
        return true;
    }

    @EventHandler public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) { return; }
        if (event.getFrom().getWorld() == null || !event.getFrom().getWorld().equals(getPlayerWorld())) { return; }
        if (event.getTo().getBlock().getType().equals(Material.END_PORTAL)) {
            // travel to boss room
            getLogger().info("travel to boss room");
        }
    }


    /**
     * Event that listens for when a player clicks on a cell block with a Fermented
     * Spider Eye and will print out the details of the tile that was clicked on.
     *
     * @param event Player Interact Event
     */
    @EventHandler public void onClick(PlayerInteractEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) { return; }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        Block b = event.getClickedBlock();
        if (b == null) { return; }
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FERMENTED_SPIDER_EYE)) {
            getAPI().printTile(getAPI().getTile(b.getLocation()), event.getPlayer());
        } else {
            if (b.getType().equals(Material.SHROOMLIGHT) || b.getType().equals(Material.GOLD_BLOCK)) {
                unlockArea(b.getLocation(), event.getPlayer());
            }
        }
    }

    /* ----------------------------------------------------------------------- */
    /* |							OTHER FUNCTIONS	 						 | */
    /* ----------------------------------------------------------------------- */


    /**
     * Unlocks an area (such as a boss room, ladder down or loot room)
     * @param location location of one block inside the room
     * @param player player that is unlocking the tile.
     */
    private void unlockArea(Location location, Player player) {
        // gets data about location
        Position pos = getAPI().getPosition(location);
        Tile t = getAPI().getTile(location);
        Floor f = getAPI().getFloor(location);
        assert(f != null);
        assert(t != null);
        assert(pos != null);
        int keys = DarkLib.getPlayerAPI().getKeys(pos.getOwner(), DarkWorld.DUNGEON); // get the amount of keys the player has
        if (keys > 0) {
            // unlock loot room
            unlockTile(pos, f , t);
            effect(location.add(0.5, -1.0, 0.5), player);
            DarkLib.getPlayerAPI().withdrawKeys(pos.getOwner(), DarkWorld.DUNGEON, 1);
        }
        else {
            // not enough keys
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You &cdon't &7have enough &6keys &7to unlock this room!"));
        }
    }


    private void effect(Location location, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            getEffectManager().display(Particle.VILLAGER_ANGRY, location, 0.0f, 0.0f, 0.0f, 0.1f, 1, 1, Color.YELLOW, null, (byte) 0, 15.0d, new ArrayList<Player>() {{
                add(player);
            }});
            player.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1.0f, 2.0f);
        });
    }

    /**
     * Makes the <code>/cells</code> directory if it doesn't exists
     */
    private void makeDir() {
        playerDungeonDir = new File(this.getDataFolder() + "/cells");
        try {
            if (!playerDungeonDir.exists()) { playerDungeonDir.mkdirs(); }
        } catch (SecurityException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Makes the {@linkplain #playerWorld} and {@link #schemWorld}
     */


    private void makeWorld() {
        setPlayerWorld(Bukkit.getWorld(PLAYER_DUNGEON_WORLD_NAME)); // attempts to load player world
        if (getPlayerWorld() == null) {
            // creates player world if it's not already loaded
            // ! There is no reason for it to not be loaded
            WorldCreator creator = new WorldCreator(PLAYER_DUNGEON_WORLD_NAME);
            creator.generator(new ChunkGen()); // uses a generator which is void for all chunks where x > -16 & z > -16
            setPlayerWorld(creator.createWorld()); // creates the world and stores it in static playerWorld.
            getLogger().info("Created Player World!"); // OUTPUT Created Player World
        }
        setSchemWorld(Bukkit.getWorld("schem_world")); // attempts to load schematic world
        if (getSchemWorld() == null) {
            // creates schematic world if it's not already loaded
            // ! There is no reason for it to not be loaded
            WorldCreator creator = new WorldCreator("schem_world");
            creator.generator(new FlatGen(Material.BLACK_CONCRETE, 100)); // fills it with black_concrete for y < 100
            // for all chunks
            setSchemWorld(creator.createWorld()); // creates the world and stores it in static schemWorld.
            getLogger().info("Created Schematic World!"); // OUTPUT Created Schem World
        }
    }

    /**
     * Saves cell data and then saves player dungeons.
     *
     * @throws IOException saving exception
     */
    private void save() throws IOException {
        // creates config.yml if it doesn't already exist.
        this.saveDefaultConfig();
        getConfig().set("maxRow", getMaxRow()); // sets maxRow
        List<String> list = new ArrayList<>();
        for (Position p : getCells()) { // iterate through all position cells in memory
            if (p != null) {
                System.out.println("saving: " + p); // OUTPUT saving: p
                list.add(p.toJson()); // saves each position as a JSON string storing each data entry
                /* Format of JSON String
                 * {"owner": uuid, "gridX": x, "gridY": y, "anchorX": anchorX, "anchorY":
                 * anchorY, "anchorZ": anchorZ}
                 * Anchor points is where the player spawns. */
                System.out.println("saved: " + p); // OUTPUT saved: p
            }
        }
        getConfig().set("cells", list); // saves the list into the config.yml
        getConfig().save(new File(this.getDataFolder(), "config.yml")); // pushes changes to config.yml file.
        /* Loops through each PlayerDungeon and saves the data inside as a .json file */
        for (UUID id : getInstances().keySet()) {
            String idString = id.toString();
            File file = new File(getPlayerDungeonDir(), idString + ".json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(getInstances().get(id).toJson());
            writer.close();
        }
    }

    /**
     * Loads data from config.yml and then loads all the player dungeons.
     *
     * @throws FileNotFoundException file not loaded
     */
    private void load() throws FileNotFoundException {
        // load cell grid data
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();
        List<String> list = getConfig().getStringList("cells");
        if (!list.isEmpty()) { for (String s : list) { cells.add(g.fromJson(s, Position.class)); } }
        maxRow = getConfig().getInt("maxRow");

        // load dungeons
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        for (File f : playerDungeonDir.listFiles()) {
            if (f != null) {
                /* The .length()-5 removes the ".json" from the file name,
                 * leaving us with just the uuid. */
                UUID id = UUID.fromString(f.getName().substring(0, f.getName().length() - 5));
                BufferedReader reader = new BufferedReader(new FileReader(f));
                PlayerDungeon dungeon = gson.fromJson(reader, PlayerDungeon.class);
                getInstances().put(id, dungeon);
            }
        }
    }

    /**
     * Gets a cell at a position
     *
     * @param x x position
     * @param y y position
     *
     * @return Position if found, or null
     */
    @Nullable public Position getCell(int x, int y) {
        for (Position p : getCells()) {
            if (p == null) { continue; }
            if (p.getGridX() == x && p.getGridY() == y) { return p; }
        }
        return null;
    }

    public void unlockTile(Position cell, Floor f, Tile t) {
        // TODO require keys?
        // check if is last tile

        int bitwise = t.getBitwise();
        int rel_x = t.getX() * 21;
        int rel_z = t.getY() * 21;
        int rel_y = (f.getLevel() - 1) * 9;

        Location location = new Location(getPlayerWorld(), cell.getGridX() * 1000 + rel_x, topY - rel_y, cell.getGridY() * 1000 + rel_z);

        if (!t.isEnd()) {
            // remove black concrete and gold blocks.
            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> getAPI().getWorldEditUtils().clearLootRoom(location));
            getLogger().info("cleared loot room");
        }
        else {
            if (f.getUnexplored() <= 1) {
                // set to staircase down
                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> getAPI().getWorldEditUtils().pasteCell("stair/stair_" + bitwise, location));
                t.setStair(true);
                getLogger().info("pasted stair room");
                // generate next level
                scheduleGen(cell.getOwner(), f.getLevel());
                // i dont know about this!

            }
            else {
                // set to boss room
                // pasteCell(boss_bitwise @ location)
                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> getAPI().getWorldEditUtils().pasteCell("boss_" + bitwise, location));
                getLogger().info("pasted boss room");
            }
        }
        t.setExplored(true);
    }

    /**
     * <!-- REFACTOR scheduleGen() -->
     * This is a mess.
     * It schedules the generation of a floor
     *
     * @param id Owner's id
     * @param i  Floor level
     */
    public void scheduleGen(UUID id, int i) {
        Position cell = getAPI().getCell(id);
        Generator instance = this;
        // DO WE SERIOUSLY NEED 1000 RUNNABLES??
        // Surely it can just be done async in one simple call.
        // IDEA maybe add generatorTasks#addAsyncTask ??
        generatorTasks.addTask(() -> {
            Floor f = getAPI().generateLevel(id, i);
            generatorTasks.addTask(new Runnable() {
                @Override public void run() {
                    int max_x = 0, max_z = 0;
                    int min_x = Integer.MAX_VALUE, min_z = Integer.MAX_VALUE;
                    for (Tile t : f.getTiles()) {
                        if (t.getBitwise() == 0) { continue; }
                        int rel_x = t.getX() * 21;
                        int rel_z = t.getY() * 21;
                        int rel_y = i * 9;
                        if (rel_x > max_x) { max_x = rel_x; }
                        if (rel_z > max_z) { max_z = rel_z; }
                        if (rel_x < min_x) { min_x = rel_x; }
                        if (rel_z < min_z) { min_z = rel_z; }
                        Location location = new Location(getPlayerWorld(), (cell.getGridX() * 1000) + rel_x, topY - rel_y, (cell.getGridY() * 1000) + rel_z);
                        String cell;
                        if (t.isStart()) {
                            if (i > 0) {
                                Tile end = getInstances().get(id).getFloors().get(i - 1).getEnd();
                                if (end == null) {
                                    // sohuldnt be null!
                                    // would happen if floor(0) hasnt been generated.
                                    getLogger().severe("END TILE IS NULL!");
                                    cell = "start" + t.getBitwise();
                                }
                                else {
                                    cell = "land/land_" + t.getBitwise() + "_" + end.getBitwise();
                                }
                            }
                            else {
                                cell = "start_" + t.getBitwise();
                            }
                        }
                        else if (t.isEnd()) {
                            cell = "end_" + t.getBitwise();
                        }
                        else if (t.isLoot()) {
                            cell = "loot_" + t.getBitwise();
                        }
                        else {
                            cell = getRandomCell(Biome.NORMAL, t.getBitwise());
                        }
                        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> getAPI().getWorldEditUtils().pasteCell(cell, location));
                    }
                    max_x += 21;
                    max_z += 21;
                    min_z -= 21;
                    min_x -= 21;
                    makeSpawners(max_x, max_z);
                }

                private String getRandomCell(Biome biome, int bitwise) {
                    return biome.getData().getRandom(bitwise);
                }

                private void makeSpawners(int max_x, int max_z) {
                    /* done makeSpawners
                     * Store the spawner Locations in a list, and then pass through that list to DarkDungeons
                     * which will actually make the spawners for us.
                     * Once these spawners are destroyed, change the block to an "inactive" spawner block, remove
                     * the spawner from the DarkDungeons activeSpawner list and give the player some TODO infusion shards.
                     */
                    Bukkit.getScheduler().runTaskLaterAsynchronously(instance, () -> {
                        int start_x = cell.getGridX() * 1000;
                        int start_y = 140 - i * 9;
                        int start_z = cell.getGridY() * 1000;
                        int end_x = max_x + 21;
                        int end_y = 140 - (i + 1) * 9 + cell.getGridX() * 1000;
                        int end_z = max_z + cell.getGridY() * 1000 + 21;
                        final Material spawnerBlock = Material.REDSTONE_BLOCK;
                        Region region = new CuboidRegion(BlockVector3.at(start_x, start_y, start_z), BlockVector3.at(end_x, end_y, end_z));
                        // TODO run this async
                        List<Location> spawners = new ArrayList<>();
                        for (BlockVector3 b3 : region) {
                            Block block = getPlayerWorld().getBlockAt(b3.getBlockX(), b3.getBlockY(), b3.getBlockZ());
                            if (block.getType().equals(spawnerBlock)) {
                                spawners.add(block.getLocation().add(0.5, 1.0, 0.5));
                            }
                        }
                        spawners.forEach((Location loc) -> {
                            DarkSpawners.getApi().addPlayerSpawner(new MobGroupSpawner(loc, MobGroup.BASIC, (int) (1 + (i * 1.33))), id);
                        });

                        try (@SuppressWarnings("deprecation") EditSession editSession = WorldEdit.getInstance()
                                                                                                 .getEditSessionFactory()
                                                                                                 .getEditSession(new BukkitWorld(getPlayerWorld()), -1)) {
                            Mask mask = new BlockMask(editSession.getExtent(), BukkitAdapter.adapt(spawnerBlock.createBlockData()).toBaseBlock());


                            editSession.replaceBlocks(region, mask, BukkitAdapter.adapt(Material.GRAY_CONCRETE.createBlockData()));
                            editSession.flushQueue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 10L);
                }

            });
        });
    }
}
