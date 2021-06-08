package dev.forbit.generator.plugin;

import dev.forbit.generator.generator.Tile;
import dev.forbit.spawners.DarkSpawners;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandManager implements CommandExecutor {
    @Getter
    @Setter
    private Generator main;


    public CommandManager(Generator main) {
        setMain(main);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, @NonNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Requires an argument: /gen <clear|tp|n>");
            return true;
        }
        Player p = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "clear": {
                // removes all generation.
                Generator.getAPI().getWorldEditUtils().clearArea(Generator.getAPI().getCell(p.getUniqueId()));
                getMain().getInstances().get(p.getUniqueId()).setFloors(new HashMap<>());
                getMain().getInstances().get(p.getUniqueId()).invalidateCheckpoint();
                DarkSpawners.getApi().clear(p);
                return true;
            }
            case "tp": {
                // teleports the player to the topcorner of their starting cell.
                Tile t = getMain().getInstances().get(p.getUniqueId()).getFloors().get(0).getStart();
                int grix = Generator.getAPI().getCell(p.getUniqueId()).getGridX();
                int grip = Generator.getAPI().getCell(p.getUniqueId()).getGridY();
                int x = t.getX() * 21 + (grix * 1000);
                int z = t.getY() * 21 + (grip * 1000);
                int y = 132;
                Location loc = new Location(getMain().getPlayerWorld(), x, y, z);
                p.teleport(loc);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Teleport to &ddungeon"));
                return true;
            }
            default: {
                // example: /gen 1
                // generates the level 1 and only level 1.
                try {
                    int i = Integer.parseInt(args[0]);
                    getMain().scheduleGen(p.getUniqueId(), i);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error parsing an argument: /gen <clear|tp|n>");
                    return true;
                }
            }
        }
        return true;

    }

}
