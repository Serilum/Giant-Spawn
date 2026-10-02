package com.serilum.giantspawn;

import com.natamus.collective.objects.SAMObject;
import com.serilum.giantspawn.config.ConfigHandler;
import net.minecraft.world.entity.EntityType;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		new SAMObject(EntityType.ZOMBIE, EntityType.GIANT, null, ConfigHandler.chanceSurfaceZombieIsGiant, false, false, true);
	}
}