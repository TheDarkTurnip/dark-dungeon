package dev.forbit.generator.plugin;

import com.google.gson.GsonBuilder;
import dev.forbit.generator.generator.Floor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * Class that stores information regarding a player's dungeon layout and floors
 * etc.
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 * @see Floor
 */
public class PlayerDungeon {
    @Getter @Setter public HashMap<Integer, Floor> floors = new HashMap<>();
    @Getter @Setter int checkpointX;
    @Getter @Setter int checkpointY;
    @Getter @Setter int checkpointZ;

    public PlayerDungeon() {}

    public static PlayerDungeon fromJson(String string) {
        return ((new GsonBuilder()).setPrettyPrinting().create()).fromJson(string, PlayerDungeon.class);
    }

    public String toJson() {
        return ((new GsonBuilder()).setPrettyPrinting().create()).toJson(this);
    }

    public Location getCheckPoint() {
        return new Location(Generator.getInstance().getPlayerWorld(), getCheckpointX(), getCheckpointY(), getCheckpointZ());
    }

    public void invalidateCheckpoint() {
        setCheckpointY(-1);
        setCheckpointZ(-1);
        setCheckpointX(-1);
    }
}
