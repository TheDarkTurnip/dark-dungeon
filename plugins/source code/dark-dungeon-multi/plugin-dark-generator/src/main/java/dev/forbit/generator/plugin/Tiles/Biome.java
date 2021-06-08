package dev.forbit.generator.plugin.Tiles;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public enum Biome {
    // REFACTOR surely a better way to do this.
    WOOL(new HashMap<Integer, String[]>() {{
        put(1, new String[]{"wool_1"});
        put(2, new String[]{"wool_2"});
        put(3, new String[]{"wool_3"});
        put(4, new String[]{"wool_4"});
        put(5, new String[]{"wool_5"});
        put(6, new String[]{"wool_6"});
        put(7, new String[]{"wool_7"});
        put(8, new String[]{"wool_8"});
        put(9, new String[]{"wool_9"});
        put(10, new String[]{"wool_10"});
        put(11, new String[]{"wool_11"});
        put(12, new String[]{"wool_12"});
        put(13, new String[]{"wool_13"});
        put(14, new String[]{"wool_14"});
        put(15, new String[]{"wool_15"});
    }}), NORMAL(new HashMap<Integer, String[]>() {{
        put(1, new String[]{"stone_1","grass_1"});
        put(2, new String[]{"stone_2","grass_2"});
        put(3, new String[]{"stone_3","grass_3"});
        put(4, new String[]{"stone_4","grass_4"});
        put(5, new String[]{"stone_5","grass_5"});
        put(6, new String[]{"stone_6","grass_6"});
        put(7, new String[]{"stone_7","grass_7"});
        put(8, new String[]{"stone_8","grass_8"});
        put(9, new String[]{"stone_9","grass_9"});
        put(10, new String[]{"stone_10","grass_10"});
        put(11, new String[]{"stone_11","grass_11"});
        put(12, new String[]{"stone_12","grass_12"});
        put(13, new String[]{"stone_13","grass_13"});
        put(14, new String[]{"stone_14","grass_14"});
        put(15, new String[]{"stone_15","stone_15_1","grass_15"});
    }});

    @Getter @Setter TileData data;

    Biome(HashMap<Integer, String[]> tileData) {
        setData(new TileData(tileData));
    }

    public static Biome pickRandom() {
        List<Biome> randomBiomes = new ArrayList<Biome>() {{
            add(Biome.NORMAL);
        }};
        int length = randomBiomes.size();
        Random random = new Random(); // TODO seed generation
        return randomBiomes.get(random.nextInt(length));
    }


}
