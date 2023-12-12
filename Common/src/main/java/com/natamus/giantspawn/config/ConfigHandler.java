package com.natamus.giantspawn.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.giantspawn.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(min = 0, max = 1.0) public static double chanceSurfaceZombieIsGiant = 0.05;
	@Entry public static boolean shouldBurnGiantsInDaylight = true;
	@Entry public static boolean onlySpawnGiantOnSurface = true;
	@Entry(min = 0, max = 20.0) public static double giantMovementSpeedModifier = 1.0;
	@Entry(min = 0, max = 20.0) public static double giantAttackDamageModifier = 2.0;

	public static void initConfig() {
		configMetaData.put("chanceSurfaceZombieIsGiant", Arrays.asList(
			"The chance a zombie that has spawned on the surface is a giant."
		));
		configMetaData.put("shouldBurnGiantsInDaylight", Arrays.asList(
			"If enabled, burns giants when daylight shines upon them."
		));
		configMetaData.put("onlySpawnGiantOnSurface", Arrays.asList(
			"If enabled, a giant will only spawn on the surface."
		));
		configMetaData.put("giantMovementSpeedModifier", Arrays.asList(
			"The giant movement speed modifier relative to the base zombie movement speed."
		));
		configMetaData.put("giantAttackDamageModifier", Arrays.asList(
			"The giant attack damage modifier relative to the base zombie attack damage."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}