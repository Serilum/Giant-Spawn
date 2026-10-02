package com.serilum.giantspawn;

import com.natamus.collective.objects.SAMObject;
import com.serilum.giantspawn.config.ConfigHandler;
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