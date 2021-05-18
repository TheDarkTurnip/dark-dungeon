package dev.forbit.generator.plugin;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Stores grid information for a specific cell in the
 * {@link Generator#playerWorld}
 * 
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class Position {
	@Getter @Setter public int gridX;
	@Getter @Setter public int gridY;
	@Getter @Setter public UUID owner;
	@Getter @Setter public double anchorX;
	@Getter @Setter public double anchorY;
	@Getter @Setter public double anchorZ;

	Position() {}

	Position(int x, int y) {
		setGridX(x);
		setGridY(y);
	}

	public String toJson() {
		return ((new GsonBuilder()).setPrettyPrinting().create()).toJson(this);
	}
}
