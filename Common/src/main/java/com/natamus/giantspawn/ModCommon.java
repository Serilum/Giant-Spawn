package com.natamus.giantspawn;

import com.natamus.collective.objects.SAMObject;
import com.natamus.giantspawn.config.ConfigHandler;
import net.minecraft.world.entity.EntityTypes;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		new SAMObject(EntityTypes.ZOMBIE, EntityTypes.GIANT, null, ConfigHandler.chanceSurfaceZombieIsGiant, false, false, true);
	}
}