package test.forbit.world;

import be.seeseemelk.mockbukkit.ChunkMock;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import dev.forbit.world.ChunkGen;
import dev.forbit.world.DarkWorld;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;
import java.util.Random;

public class ChunkTest {

    private static ServerMock server;
    private static DarkWorld plugin;


    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = (DarkWorld) MockBukkit.load(DarkWorld.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testSomething() {
        Assertions.assertTrue(true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"world", "player_world", "guild_world", "schem_world"})
    public void testDefaultChunkGenerator(String worldName) {
        Assertions.assertNotNull(plugin.getDefaultWorldGenerator(worldName, null));

    }


    @ParameterizedTest(name = "Testing generators at height {0}")
    @ValueSource(ints = {-1, 0, 10, 20, 50, 75, 90, 99, 100, 101, 122, 150, 160})
    void testGenerators(int height) {
        WorldMock world = new WorldMock();

        int[] x_vals = {-1000, -100, 100, 1000};
        int[] y_vals = {-1000, -100, 100, 1000};
        for (int x = 0; x < x_vals.length; x++) {
            for (int z = 0; z < y_vals.length; z++) {
                ChunkMock chunk = world.getChunkAt(x, z);
                ChunkGen gen = new ChunkGen();
                gen.generateChunkData(world, new Random(), chunk.getX(), chunk.getZ(), null);
                if (height == 0) {
                    Assertions.assertEquals(chunk.getBlock(x,height,z).getType(), Material.BEDROCK);
                    continue;
                }
                if (chunk.getX() < -10 && chunk.getZ() < -10) {
                    if (height > 128) {
                        Assertions.assertEquals(chunk.getBlock(x,height,z).getType(), Material.AIR);
                    } else {
                        Assertions.assertEquals(chunk.getBlock(x,height,z).getType(), Material.STONE);
                    }
                }


            }
        }
    }


}
