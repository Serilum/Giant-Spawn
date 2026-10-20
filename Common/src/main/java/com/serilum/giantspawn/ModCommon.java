package com.serilum.giantspawn;

import com.natamus.collective.globalcallbacks.ConfigCallback;
import com.natamus.collective.objects.SAMObject;
import com.serilum.giantspawn.config.ConfigHandler;
import com.serilum.giantspawn.util.Reference;
import net.minecraft.world.entity.EntityTypes;

public class ModCommon {
	private static SAMObject samObject;

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		samObject = new SAMObject(EntityTypes.ZOMBIE, EntityTypes.GIANT, null, ConfigHandler.chanceSurfaceZombieIsGiant, false, false, ConfigHandler.onlySpawnGiantOnSurface, false, false, Integer.MAX_VALUE);
		samObject.dimensionIds = ConfigHandler.dimensionsToSpawnIn;
		samObject.biomeIds = ConfigHandler.biomesToSpawnIn;

		ConfigCallback.CONFIG_CHANGED.register((String modid) -> {
			if (!modid.equals(Reference.MOD_ID)) {
				return;
			}

			samObject.changeChance = ConfigHandler.chanceSurfaceZombieIsGiant;
			samObject.onlyOnSurface = ConfigHandler.onlySpawnGiantOnSurface;
			samObject.dimensionIds = ConfigHandler.dimensionsToSpawnIn;
			samObject.biomeIds = ConfigHandler.biomesToSpawnIn;
		});
	}
}