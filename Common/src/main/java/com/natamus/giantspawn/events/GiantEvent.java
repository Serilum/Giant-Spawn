package com.natamus.giantspawn.events;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.HashMapFunctions;
import com.natamus.giantspawn.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GiantEvent {
	private static final HashMap<Level, CopyOnWriteArrayList<Entity>> giants_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static final HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	public static void onEntityJoin(Level level, Entity entity) {
		if (level.isClientSide) {
			return;
		}
		
		if (!(entity instanceof Giant)) {
			return;
		}
		
		if (!HashMapFunctions.computeIfAbsent(giants_per_world, level, k -> new CopyOnWriteArrayList<Entity>()).contains(entity)) {
			giants_per_world.get(level).add(entity);
		}

		Giant giant = (Giant)entity;
		
		giant.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0D); // FOLLOW_RANGE
		giant.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.23F * ConfigHandler.giantMovementSpeedModifier); // MOVEMENT_SPEED
		giant.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D * ConfigHandler.giantAttackDamageModifier); // ATTACK_DAMAGE
		giant.getAttribute(Attributes.ARMOR).setBaseValue(2.0D); // ARMOR
	}
	
	public static void onWorldTick(ServerLevel level) {
		int ticks = HashMapFunctions.computeIfAbsent(tickdelay_per_world, level, k -> 1);
		if (ticks % 20 != 0) {
			tickdelay_per_world.put(level, ticks + 1);
			return;
		}
		tickdelay_per_world.put(level, 1);
		
		if (!ConfigHandler.shouldBurnGiantsInDaylight) {
			return;
		}
		
		if (!level.isDay()) {
			return;
		}
		
		for (Entity giant : HashMapFunctions.computeIfAbsent(giants_per_world, level, k -> new CopyOnWriteArrayList<Entity>())) {
			if (giant.isAlive()) {
				if (!giant.isInWaterRainOrBubble()) {
					BlockPos epos = giant.blockPosition();
					if (BlockPosFunctions.isOnSurface(level, epos)) {
						giant.setSecondsOnFire(3);
					}
				}	
			}
			else {
				giants_per_world.get(level).remove(giant);
			}		
		}
	}
}
