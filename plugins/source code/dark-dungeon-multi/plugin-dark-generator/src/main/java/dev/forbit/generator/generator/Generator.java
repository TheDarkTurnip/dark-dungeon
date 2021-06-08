package dev.forbit.generator.generator;

import java.util.Random;

/**
 * Class to handle generation of random dungeon floors.
 *
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class Generator {
    /**
     * Generates a new floor for the specified level.
     *
     * @param level must be below 1000
     *
     * @return
     */
    public Floor generate(int level) {
        Random random = new Random();
        return makeFloor(random, level);
    }

    /**
     *
     * Generates a new floor with the start tile directly under #startBelow
     *
     * @param level must be < 1000
     * @param startBelow
     * @return
     */
    public Floor generate(int level, Tile startBelow) {
        return makeFloor(new Random(), level, startBelow);
    }

    /***
     * Makes a floor based on the given level
     *
     * @param level must be < 1000
     * @return
     */
    public Floor makeFloor(Random random, int level) {
        Floor floor;
        long seed;
        int req_deadends = 2+(level/3);
        int req_fifteens = (((4+level) * (4+level)) / 5);
        do {
            seed = random.nextLong();
            random.setSeed(seed);
            floor = new Floor(random, level);
            floor.generateTiles(random.nextInt(floor.getSize() - 1) + 1, random.nextInt(floor.getSize() - 1) + 1);
            makeDeadEnds(random, floor);

        } while (!floor.getStart().isEnd() || countDeadends(floor) != req_deadends || count15(floor) > req_fifteens);
        return floor;
    }

    public Floor makeFloor(Random random, int level, Tile startBelow) {
        Floor floor;
        long seed;
        do {
            seed = random.nextLong();
            random.setSeed(seed);
            floor = new Floor(random, level);
            floor.generateTiles(startBelow.getX(), startBelow.getY());
            makeDeadEnds(random, floor);
        } while (!floor.getStart().isEnd() || countDeadends(floor) != 2 + (level / 3) || count15(floor) > (((4 + level) * (4 + level)) / 5));
        return floor;

    }

    /**
     * Counts the amount of deadends of a given floor.
     *
     * @param f floor
     *
     * @return
     */
    public int countDeadends(Floor f) {
        int sum = 0;
        for (Tile t : f.getTiles()) { if (t.isEnd()) { sum++; } }
        return sum;
    }

    /**
     * Counts the amount of tiles that have surrounding tiles on all sides.
     *
     * @param f floor
     *
     * @return
     */
    public int count15(Floor f) {
        int sum = 0;
        for (Tile t : f.getTiles()) {
			if (t == null) { continue; }
            if (Integer.bitCount(t.getBitwise()) == 15) { sum++; }
        }
        return sum;
    }

    /**
     * Generates dead ends.
     *
     * @param random
     * @param f      floor
     */
    public void makeDeadEnds(Random random, Floor f) {
        f.getTiles().forEach((Tile t) -> t.setEnd(Integer.bitCount(t.getBitwise()) == 1));
    }
}
