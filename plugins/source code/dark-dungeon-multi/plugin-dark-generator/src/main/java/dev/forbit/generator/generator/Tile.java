package dev.forbit.generator.generator;
import lombok.Getter;
import lombok.Setter;
public class Tile {
    @Getter @Setter int x;
    @Getter @Setter int y;
    @Getter @Setter int bitwise = 0;
    @Getter @Setter boolean value = false;
    @Getter @Setter boolean loot = false;
    @Getter @Setter boolean end = false;
    @Getter @Setter boolean start = false;
    @Getter @Setter boolean explored = false;
    @Getter @Setter boolean stair = false;

    public Tile(int x, int y) {
        setX(x);
        setY(y);
    }
}
