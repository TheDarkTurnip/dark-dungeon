package test.forbit.darknpc;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.darknpc.DarkNpc;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.*;

import java.util.UUID;


@Disabled
public class TestNPCPlugin {


    private static ServerMock server;
    private static DarkNpc plugin;

    @BeforeAll
    static void setUp()
    {
        server = MockBukkit.mock();
        System.out.println("poackage: "+server.getClass().getPackage().getName());
        PluginDescriptionFile file = new PluginDescriptionFile(
                "dark-npc",
                "test-version",
                "dev.forbit.darknpc.DarkNPC"
        );
        plugin = (DarkNpc) MockBukkit.loadWith(DarkNpc.class, file);
    }

    @AfterAll
    static void tearDown()
    {
        MockBukkit.unmock();
    }
    @Test
    void testPlugin() {
        Assertions.assertNotNull(plugin.getLibrary());
        DarkNpc.getAPI().initArea(UUID.randomUUID());

    }
}
