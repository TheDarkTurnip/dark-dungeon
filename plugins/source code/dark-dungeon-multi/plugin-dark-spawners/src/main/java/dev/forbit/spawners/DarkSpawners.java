package dev.forbit.spawners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class DarkSpawners extends JavaPlugin {

    public static final String PLAYER_SPAWNER_FILE_DIRECTORY = "player-spawners";
    public static Plugin plugin;

    @Getter private static SpawnerAPI api;
    // TODO only acitvate player spawners when they're connected!
    @Getter private SpawnerScheduler scheduler;
    @Getter private EffectManager effectManager;
    @Getter private List<PlayerSpawners> playerSpawners = new ArrayList<>();


    public DarkSpawners() {
        super();
    }

    protected DarkSpawners(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override public void onEnable() {
        // TODO load spawners
        plugin = this;
        api = new SpawnerAPI(this);
        scheduler = new SpawnerScheduler(this);
        effectManager = new EffectManager(this);

        try {
            loadSpawners();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadSpawners() throws IOException, ClassNotFoundException {
        // load spawners from file structure
        File directory = new File(this.getDataFolder(), PLAYER_SPAWNER_FILE_DIRECTORY);
        if (!directory.exists() || directory.listFiles().length <= 0) { return; }
        Gson gson = ((new GsonBuilder()).setPrettyPrinting()).create();
        for (File file : directory.listFiles()) {
            // load spawners from file
            // file has name UUID.json
            UUID id = UUID.fromString(file.getName().substring(0, file.getName().length()-5)); // removes the .json
            /*BufferedReader reader = new BufferedReader(new FileReader(file));
            PlayerSpawners pSpawner = gson.fromJson(reader, PlayerSpawners.class);*/
            PlayerSpawners pSpawner = new PlayerSpawners(id);
            pSpawner.fromFile(file);
            playerSpawners.add(pSpawner);
        }
        getLogger().info("Loaded "+getPlayerSpawners().size()+" player spawner libraries.");
    }

    @Override public void onDisable() {
        try {
            saveSpawners();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveSpawners() throws IOException {
        File directory = new File(this.getDataFolder(), PLAYER_SPAWNER_FILE_DIRECTORY);
        if (!(directory.exists())) directory.mkdirs();
        Gson gson = ((new GsonBuilder()).setPrettyPrinting()).create();
        int count = 0;
        for (PlayerSpawners pSpawner : getPlayerSpawners()) {
            File f = new File(directory, pSpawner.getOwner().toString()+".json");
            if (f.exists()) f.delete();
            f.createNewFile();
            /*BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(gson.toJson(pSpawner));
            writer.close();*/
            pSpawner.save(f);
            count++;
        }
        getLogger().info("Saved "+count+" spawners");
    }
}
