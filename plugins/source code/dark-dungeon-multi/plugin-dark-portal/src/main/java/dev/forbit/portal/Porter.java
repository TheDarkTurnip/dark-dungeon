package dev.forbit.portal;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.World;

public @Data class Porter {
	World originWorld;
	Material blockType;
	PorterRunnable runnable;
	
	public Porter(World world, Material block, PorterRunnable runnable) {
		setOriginWorld(world);
		setBlockType(block);
		setRunnable(runnable);
	}
}
