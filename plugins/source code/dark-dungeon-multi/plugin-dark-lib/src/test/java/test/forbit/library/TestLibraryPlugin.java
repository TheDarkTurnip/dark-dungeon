package test.forbit.library;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.library.DarkLib;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestLibraryPlugin {


    private static ServerMock server;
    private static DarkLib plugin;


    @BeforeAll
    static void setUp()
    {
        server = MockBukkit.mock();
        plugin = (DarkLib) MockBukkit.load(DarkLib.class);
    }

    @AfterAll
    static void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void testPlugin() {
        Assertions.assertNotNull(DarkLib.getInstance());
    }


}
