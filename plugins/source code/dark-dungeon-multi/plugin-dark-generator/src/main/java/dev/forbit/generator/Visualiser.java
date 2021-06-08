package dev.forbit.generator;

import dev.forbit.generator.generator.Floor;
import dev.forbit.generator.generator.Generator;
import dev.forbit.generator.generator.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A utility class that helps to visualise a dungeon's layout
 * Contains a main() function.
 * 
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class Visualiser {
	public static void main(String[] args) {
		int level = 10;
		if (args.length >= 1) { level = Integer.parseInt(args[0]); }
		Generator gen = new Generator();
		Floor f = gen.generate(level);
		BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.getColor("#151515"));
		graphics.fillRect(-1, -1, 402, 402);
		draw(f, graphics);
		JOptionPane.showMessageDialog(null, new ImageIcon(image));
	}

	private static void draw(Floor f, Graphics2D graphics) {
		for (Tile t : f.getTiles()) {
			if (t.isValue()) {
				if (t.isStart()) {
					graphics.setPaint(Color.MAGENTA);
				} else if (t.isEnd()) {
					graphics.setPaint(Color.RED);
				} else if (t.isLoot()) {
					graphics.setPaint(Color.ORANGE);
				} else {
					graphics.setPaint(Color.GREEN);
				}
				graphics.fillRect(t.getX() * 32, t.getY() * 32, 32, 32);
			} else {
				graphics.setPaint(Color.GRAY);
				graphics.fillRect(t.getX() * 32, t.getY() * 32, 32, 32);
			}
		}
	}
}
