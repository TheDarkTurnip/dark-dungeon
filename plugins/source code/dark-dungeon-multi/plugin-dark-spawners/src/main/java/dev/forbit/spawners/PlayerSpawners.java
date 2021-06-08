package dev.forbit.spawners;

import dev.forbit.spawners.classes.SaveSpawnerWrapper;
import dev.forbit.spawners.classes.Spawner;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public @Data class PlayerSpawners {
    List<Spawner> spawners = new ArrayList<>();
    UUID owner = null;

    public PlayerSpawners(UUID id) {
        setOwner(id);
    }

    public void save(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Spawner s : spawners) {
            SaveSpawnerWrapper wrapper = new SaveSpawnerWrapper(s);
            writer.write(wrapper.toJSON()+"\n");

        }
        writer.close();
    }

    public void fromFile(File file) throws IOException, ClassNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                spawners.add(SaveSpawnerWrapper.fromJSON(line.substring(0, line.length())));
            }
            // line is not visible here.
        }


    }

    public void clear() {
        spawners = new ArrayList<>();
    }
}
