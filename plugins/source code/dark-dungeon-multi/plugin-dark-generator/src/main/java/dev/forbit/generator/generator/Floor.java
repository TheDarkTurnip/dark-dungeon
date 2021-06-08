package dev.forbit.generator.generator;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Handles all things floor generation related.
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class Floor {
    @Getter @Setter int size;
    @Getter @Setter List<Tile> tiles;
    @Getter @Setter Random random;
    @Getter @Setter int level;

    /***
     * Makes a floor, and fills it with tiles equal to 0.
     *
     * @param level level of floor, must be < 1000
     */
    public Floor(Random random, int level) {
        assert(level < 1000);
        setLevel(level);
        setRandom(random);
        setSize((int) (4 + (level / 2f)));
        clearTiles();
    }

    /**
     * Clears tiles with new, blank tiles.
     */
    public void clearTiles() {
        List<Tile> t = new ArrayList<>();
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                t.add(new Tile(x, y));
            }
        }
        setTiles(t);
    }

    /***
     * fills List<Tile> tiles with generated tiles. Tile at startX and startY will
     * be guaranteed to have at least 2 corridors.
     *
     * @param startX
     * @param startY
     */
    public void generateTiles(int startX, int startY) {
        do {
            clearTiles();
            while (countTiles() < ((size * size) / 5)) { // must be reasonably sized
                (new Walker(getRandom(), getTiles())).walk(startX, startY, size * size);
            }
            // generate bitwise values
            generateBitwise(getTiles());
        } while (!generateLoot((level + 1) / 2));
        getTile(startX, startY).setStart(true);
    }

    /**
     * Generates loot rooms.
     *
     * @param i Amoutn of loot rooms.
     *
     * @return false if couldn't generate rooms.
     */
    private boolean generateLoot(int i) {
        List<Tile> possibles = new ArrayList<>();
        for (Tile t : getTiles()) {
            if (t.isValue() && (isStraightPath(t))) {
                possibles.add(t);
            }
        }
        if (possibles.size() < i) { return false; }
        // pick random i from possibles
        for (int n = 0; n < i; n++) {
            int r = getRandom().nextInt(possibles.size());
            Tile t = possibles.get(r);
            t.setLoot(true);
            possibles.remove(r);
        }
        return true;
    }

    private boolean isStraightPath(Tile t) {
        return t.getBitwise() == 3 || t.getBitwise() == 12;
    }

    /**
     * Sets the bitwise values of each tile.
     *
     * @param tiles
     */
    private void generateBitwise(List<Tile> tiles) {
        for (Tile t : tiles) {
            if (t == null || !t.isValue()) { continue; }
            int n = 0;
            Tile left = getTile(t.getX() - 1, t.getY());
            if (left != null) { n += (left.isValue()) ? 8 : 0; }
            Tile right = getTile(t.getX() + 1, t.getY());
            if (right != null) { n += (right.isValue()) ? 4 : 0; }
            Tile up = getTile(t.getX(), t.getY() - 1);
            if (up != null) { n += (up.isValue()) ? 2 : 0; }
            Tile down = getTile(t.getX(), t.getY() + 1);
            if (down != null) { n += (down.isValue()) ? 1 : 0; }
            t.setBitwise(n);
        }
    }


    /**
     * Counts the total tiles.
     *
     * @return
     */
    public int countTiles() {
        int sum = 0;
        for (Tile t : getTiles()) {
            if (t == null) { continue; }
            if (t.isValue()) { sum++; }
        }
        return sum;
    }

    /**
     * Gets a tile at a given position
     *
     * @param x
     * @param y
     *
     * @return Tile
     */
    public Tile getTile(int x, int y) {
        for (Tile t : getTiles()) {
            if (t == null) { continue; }
            if (t.getX() == x && t.getY() == y) { return t; }
        }
        return null;
    }

    /**
     * Gets the start tile
     *
     * @return
     */
    public Tile getStart() {
        for (Tile t : getTiles()) {
            if (t.isStart()) { return t; }
        }
        throw new ArrayIndexOutOfBoundsException("Cant find start tile.");
    }

    /**
     * Returns the sum of tiles that are end and are also unexplored.
     *
     * @return
     */
    public int getUnexplored() {
        int sum = 0;
        for (Tile t : getTiles()) {
            if (t.isEnd() && !t.isExplored()) { sum++; }
        }
        return sum;
    }

    /**
     * Returns the first tile with Tile#isStair() is true
     *
     * @return
     */
    @Nullable public Tile getEnd() {
        for (Tile t : getTiles()) {
            if (t.isStair()) { return t; }
        }
        return null;
    }
}
