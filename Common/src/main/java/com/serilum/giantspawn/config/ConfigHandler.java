package com.serilum.giantspawn.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.giantspawn.util.Reference;

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
	@Entry(min = 0.1, max = 10.0) public static double giantMaxHealthModifier = 1.0;
	@Entry(min = 0, max = 1.0) public static double giantKnockbackResistance = 0.0;
	@Entry(min = 0, max = 128.0) public static double giantFollowRange = 35.0;
	@Entry(min = 0, max = 10000) public static int giantExperienceReward = 5;
	@Entry(min = 0, max = 1.0) public static double chanceGiantSpawnsWithEquipment = 0.25;
	@Entry public static boolean preventGiantDespawning = false;
	@Entry public static boolean giantsUseZombieSounds = true;
	@Entry(registry = "item", amounts = true) public static String giantDropItems = "minecraft:rotten_flesh=4-20";
	@Entry(registry = "dimension") public static String dimensionsToSpawnIn = "";
	@Entry(registry = "worldgen/biome") public static String biomesToSpawnIn = "";

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
		configMetaData.put("giantMaxHealthModifier", Arrays.asList(
			"The giant max health modifier relative to the base giant max health of 100."
		));
		configMetaData.put("giantKnockbackResistance", Arrays.asList(
			"How much knockback a giant ignores, from 0 (none) to 1 (all)."
		));
		configMetaData.put("giantFollowRange", Arrays.asList(
			"The range in blocks in which a giant finds and follows its target."
		));
		configMetaData.put("giantExperienceReward", Arrays.asList(
			"The amount of experience a giant drops when it is killed by a player."
		));
		configMetaData.put("chanceGiantSpawnsWithEquipment", Arrays.asList(
			"The chance a giant spawns with a set of armor and a sword or shovel."
		));
		configMetaData.put("preventGiantDespawning", Arrays.asList(
			"If enabled, giants never despawn."
		));
		configMetaData.put("giantsUseZombieSounds", Arrays.asList(
			"If enabled, giants make the sounds of a zombie, at a lower pitch."
		));
		configMetaData.put("giantDropItems", Arrays.asList(
			"The items a giant drops when it dies, each with an amount or a range. For example: 'minecraft:rotten_flesh=4-20,minecraft:iron_ingot=1'."
		));
		configMetaData.put("dimensionsToSpawnIn", Arrays.asList(
			"Dimensions in which zombies can turn into giants, separated by a comma. Leave empty for every dimension. For example: 'minecraft:overworld'."
		));
		configMetaData.put("biomesToSpawnIn", Arrays.asList(
			"Biomes in which zombies can turn into giants, separated by a comma. Biome tags work too, starting with a '#'. Leave empty for every biome. For example: 'minecraft:plains,#minecraft:is_forest'."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}