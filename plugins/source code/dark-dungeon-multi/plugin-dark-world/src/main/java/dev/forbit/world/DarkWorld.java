package dev.forbit.world;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Dark World plugin
 * Made for mc.darkdungeon.co
 *
 * @author Forbit
 */
public class DarkWorld extends JavaPlugin {
    @Override public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if (worldName.equals("schem_world")) { return new FlatGen(Material.BLACK_CONCRETE, 100); }
        return new ChunkGen();
    }
}
