package test.forbit.darknpc;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.darknpc.DarkNpc;
import org.junit.jupiter.api.*;

@Disabled
public class TestNPCPlugin {


    private static ServerMock server;
    private static DarkNpc plugin;

    @BeforeAll
    static void setUp()
    {
        server = MockBukkit.mock();
        plugin = (DarkNpc) MockBukkit.load(DarkNpc.class);
    }

    @AfterAll
    static void tearDown()
    {
        MockBukkit.unmock();
    }
    @Test
    void testPlugin() {
        Assertions.assertNotNull(plugin.getLibrary());

    }
}
