package dev.forbit.world;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * Flat Earth Generator
 * Made for DarkDungeon.
 *
 * @author Forbit
 */
public class FlatGen extends ChunkGenerator {
    @Getter @Setter Material block;
    @Getter @Setter int y;

    /**
     * Chunk Generator
     *
     * @param block Block to fill floor with
     * @param y     Any block underneath this value will be filled with the specified material.
     */
    public FlatGen(Material block, int y) {
        setBlock(block);
        setY(y);
    }

    @Override @NonNull public ChunkData generateChunkData(@NonNull World world, @NonNull Random random, int chunkX, int chunkZ, @NonNull BiomeGrid biome) {
        ChunkData chunk = this.createChunkData(world);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) { if (y < getY()) { chunk.setBlock(x, y, z, getBlock()); } }
            }
        }
        return chunk;
    }
}
