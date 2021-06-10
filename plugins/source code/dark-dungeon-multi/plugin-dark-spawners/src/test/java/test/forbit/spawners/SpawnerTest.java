package test.forbit.spawners;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import dev.forbit.library.DarkWorld;
import dev.forbit.spawners.DarkSpawners;
import dev.forbit.spawners.classes.StaticSpawner;
import org.bukkit.Location;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.*;

import java.util.UUID;


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
        System.out.println("all good in the hood?");
        DarkSpawners.getApi().addPlayerSpawner(new StaticSpawner(new Location(new WorldMock(), 1, 1, 1), "Testmob", 1), UUID.randomUUID());
    }

}
