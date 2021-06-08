package test.forbit.generator;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.generator.plugin.Generator;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestPlugin {
    private static ServerMock server;
    private static Generator plugin;



    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = (Generator) MockBukkit.load(Generator.class);
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    void testFields() {
        Assertions.assertNotNull(Generator.getAPI());
        Assertions.assertNotNull(Generator.getInstance());

    }

    @Test
    void testCommands() {
        CommandSender sender = server.addPlayer();
        Assertions.assertAll(() -> {
            Assertions.assertTrue(server.dispatchCommand(sender, "gen 0"));
        }, () -> {
            Assertions.assertTrue(server.dispatchCommand(sender, "gen 2"));
        }, () -> {
            Assertions.assertTrue(server.dispatchCommand(sender, "gen clear"));
        }, () -> {
            Assertions.assertTrue(server.dispatchCommand(sender, "gen 0"));
        }, () -> {
            Assertions.assertTrue(server.dispatchCommand(sender, "gen tp"));
        });
    }
}
