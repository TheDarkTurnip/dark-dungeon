package test.forbit.generator;

import dev.forbit.generator.generator.Floor;
import dev.forbit.generator.generator.Generator;
import dev.forbit.generator.generator.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

public class TestDungeon {
    static Generator generator = new Generator();
    static List<Floor> dungeon = new ArrayList<>();

    TestDungeon() {
        dungeon.add(generator.generate(0));
        explore(dungeon.get(0));
    }


    @ParameterizedTest(name = "Generating floor {0}") @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}) void generateFloor(int level) {
        Floor above = dungeon.get(level - 1);
        explore(above);
        Assertions.assertTrue(above.getTiles().size() >= (int) Math.pow(above.getLevel(), 2));
        Assertions.assertNotNull(above);
        Floor newFloor = generator.generate(level, above.getEnd());
        Assertions.assertNotNull(newFloor);

        dungeon.add(newFloor);
    }

    void explore(Floor f) {
        for (Tile t : f.getTiles()) {
            if (t.isEnd() && !t.isExplored()) {
                if (f.getUnexplored() <= 1) {
                    t.setStair(true);
                }
                t.setExplored(true);
            }
        }
    }


}
