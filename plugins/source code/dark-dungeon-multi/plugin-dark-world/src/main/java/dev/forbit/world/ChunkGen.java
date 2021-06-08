package dev.forbit.world;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * General Chunk Generator for World <br>
 * Made for DarkDungeon
 *
 * @author Forbit
 */
public class ChunkGen extends ChunkGenerator {
    /**
     * World generator where every chunk that has x < -10 and y < -10 as stone, and everything else is air.
     */
    @Override @NonNull public ChunkData generateChunkData(@NonNull World world, @NonNull Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = this.createChunkData(world);
        // set biome
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 255; y++) {
                    if (biome != null) biome.setBiome(x, y, z, Biome.FOREST);
                }
                chunk.setBlock(x, 0, z, Material.BEDROCK);
            }
        }
        if (chunkX < -10 && chunkZ < -10) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 1; y < 128; y++) { chunk.setBlock(x, y, z, Material.STONE); }
                }
            }
        }


        return chunk;
    }
}
