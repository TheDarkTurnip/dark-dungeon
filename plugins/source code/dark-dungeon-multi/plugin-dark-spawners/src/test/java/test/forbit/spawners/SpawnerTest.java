package test.forbit.spawners;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.library.DarkWorld;
import dev.forbit.spawners.DarkSpawners;
import org.junit.jupiter.api.*;


@Disabled
public class SpawnerTest {


    private static ServerMock server;
    private static DarkSpawners plugin;


    @BeforeAll
    static void setUp()
    {
        server = MockBukkit.mock();
        plugin = (DarkSpawners) MockBukkit.load(DarkSpawners.class);
    }

    @AfterAll
    static void tearDown()
    {
        MockBukkit.unmock();
    }


    @Test
    void testPlugin() {
        Assertions.assertNotNull(DarkSpawners.getApi());
        Assertions.assertNotNull(plugin.getEffectManager());
    }
}
