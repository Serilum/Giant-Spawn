package com.natamus.giantspawn.neoforge.events;

import com.natamus.giantspawn.events.GiantEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeGiantEvent {
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent e) {
		GiantEvent.onEntityJoin(e.getLevel(), e.getEntity());
	}
	
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Pre e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}
		
		GiantEvent.onWorldTick((ServerLevel)level);
	}
}
