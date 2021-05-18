package dev.forbit.generator.generator;

import lombok.Data;

import java.util.List;
import java.util.Random;

/**
 * An implementation of the Drunkard Walker algorithm
 * 
 * @author <a href="https://forbit.dev">Forbit</a>
 * @see <a href=
 *      "https://blog.bitrageous.io/drunken-walk/">blog.gitrageous.io</a>
 */
public @Data class Walker {
	int x;
	int y;
	int walkStartX, walkStartY;
	List<Tile> tiles;
	Random random;
	int lastDirection = 2;

	public Walker(Random random, List<Tile> list) {
		setRandom(random);
		setTiles(list);
	}

	public void walk(int x, int y, int length) {
		setWalkStartX(x);
		setWalkStartY(y);
		getTile(x, y).setValue(true);
		getTile(x,y).setExplored(true);
		setX(x);
		setY(y);
		for (int i = 0; i < length; i++) { Tile t = getNextTile(); t.setValue(true); }
	}

	public Tile getNextTile() {
		int start_x = getX();
		int start_y = getY();
		do {
			setX(start_x); setY(start_y); randomDirection();
		} while ((getTile(getX(), getY()) == null) || ((getX() == getWalkStartX() && getY() == getWalkStartY())));
		return getTile(getX(), getY());
	}

	public void randomDirection() {
		int newDirection = getRandom().nextInt(4);
		if (getRandom().nextInt(4) < 3) { newDirection = lastDirection; }
		lastDirection = newDirection;
		switch (newDirection) {
			case 0: // up
				setY(getY() - 1);
				break;
			case 1: // down
				setY(getY() + 1);
				break;
			case 2: // left
				setX(getX() - 1);
				break;
			case 3: // right
				setX(getX() + 1);
				break;
		}
	}

	public Tile getTile(int x, int y) {
		for (Tile t : getTiles()) {
			if (t == null)
				continue;
			if (t.getX() == x && t.getY() == y) { return t; }
		}
		return null;
	}
}
