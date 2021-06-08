package test.forbit.portal;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.portal.PortalMain;
import org.bukkit.Material;
import org.junit.jupiter.api.*;

public class TestPlugin {


    private static ServerMock server;
    private static PortalMain plugin;


    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = (PortalMain) MockBukkit.load(PortalMain.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }


    @Test
    @DisplayName("Test Portal Main")
    void testPlugin() {
        Assertions.assertNotNull(plugin.getPortals());
        Assertions.assertEquals(plugin.getPortals().get(0).getBlockType(), Material.OBSIDIAN);
    }
}
