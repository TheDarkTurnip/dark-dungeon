package dev.forbit.generator.plugin.Tiles;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TileData {

    // IDEA weighted random tile data.
    //  - can sort of be achieved by repeating schematic names,
    //  - but ideally would be accompilshed with a HashMap<String, Integer>
    @Getter HashMap<Integer, List<String>> data = new HashMap<>();

    public TileData(HashMap<Integer, String[]> map) {
        for (int i : map.keySet()) {
            set(i, map.get(i));
        }
    }

    /**
     * Sets the tile data of a bitwise index
     *
     * @param bitwise    index
     * @param schematics array of schematic names.
     */
    public void set(int bitwise, String... schematics) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : schematics) {
            list.add(s);
        }
        data.put(bitwise, list);
    }

    public String get(int bitwise) {
        return get(bitwise, 0);
    }

    public String get(int bitwise, int index) {
        return index < data.get(bitwise).size() ? data.get(bitwise).get(index) : null;
    }

    /**
     * Returns a random schematic name
     *
     * @param bitwise
     *
     * @return
     */
    public String getRandom(int bitwise) {
        Random random = new Random(); // TODO seed generated
        int max = data.get(bitwise).size();
        return get(bitwise, random.nextInt(max));
    }

}
