package dev.forbit.generator.plugin;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle multi-threading aspects of {@link Generator}
 * 
 * @author <a href="https://forbit.dev">Forbit</a>
 */
public class GenThread extends Thread {
	List<Runnable> tasks = new ArrayList<>();
	Generator plugin;

	public GenThread(Generator plugin) { this.plugin = plugin; }

	@Override public void run() {
		while (this.isAlive()) {
			if (tasks.size() > 0) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, tasks.get(0));
				tasks.remove(0);
			}
		}
	}

	public void addTask(Runnable task) { tasks.add(task); }
}
