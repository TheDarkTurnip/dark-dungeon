package dev.forbit.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.library.player.DarkPlayer;
import dev.forbit.library.player.PlayerAPI;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class DarkLib extends JavaPlugin implements Listener, CommandExecutor {
    public static final String PLAYER_DIRECTORY_NAME = "players";
    @Getter private static PlayerAPI playerAPI;
    @Getter private static DarkLib instance;
    // TODO load players
    @Getter List<DarkPlayer> players = new ArrayList<>();

    public DarkLib() {
        super();
    }

    protected DarkLib(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("Started Dark Library!");
        playerAPI = new PlayerAPI(this);
        this.getServer().getPluginManager().registerEvents(this, this);

        try {
            loadPlayers(new File(this.getDataFolder(), PLAYER_DIRECTORY_NAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayers(File file) throws FileNotFoundException {
        if (!file.exists()) { return; }
        Gson gson = ((new GsonBuilder()).setPrettyPrinting()).create();
        int count = 0;
        for (File f : file.listFiles()) {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            DarkPlayer player = gson.fromJson(reader, DarkPlayer.class);
            players.add(player);
            count++;
        }
        getLogger().info("Loaded " + count + " player's data!");
    }

    @Override public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Shutting down Dark Library!");

        try {
            savePlayers(new File(this.getDataFolder(), PLAYER_DIRECTORY_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayers(File file) throws IOException {
        if (!file.exists()) {
            file.mkdirs();
        }
        Gson gson = ((new GsonBuilder()).setPrettyPrinting()).create();
        int count = 0;
        for (DarkPlayer player : players) {
            File f = new File(file, player.getId().toString() + ".json");
            if (f.exists()) { f.delete(); }
            f.createNewFile();
            // save player
            String json = gson.toJson(player);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(json);
            count++;
            writer.close();
        }
        getLogger().info("Saved " + count + " player's data!");

    }

    @EventHandler public void onJoin(PlayerJoinEvent event) {
        DarkPlayer p = getPlayerAPI().getPlayerOrCreate(event.getPlayer().getUniqueId());
        p.setLastIP(event.getPlayer().getAddress());
        getLogger().info("Dark Player: " + p);
    }


    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (args.length < 1) { return false; }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        getPlayerAPI().printData(player, sender);
        return true;
    }
}
