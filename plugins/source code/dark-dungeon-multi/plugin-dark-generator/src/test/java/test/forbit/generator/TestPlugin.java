package test.forbit.generator;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.forbit.generator.plugin.CommandManager;
import dev.forbit.generator.plugin.Generator;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestPlugin {
    private static ServerMock server;
    private static Generator plugin;


    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        PluginDescriptionFile file = new PluginDescriptionFile(
                "dark-generator",
                "test-version",
                "dev.forbit.generator.plugin.Generator"
        );
        plugin = MockBukkit.loadWith(Generator.class, file);

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
        CommandManager manager = plugin.getCommandManager();
        manager.onCommand(sender, null,"gen", new String[] { "2" });
    }
}
