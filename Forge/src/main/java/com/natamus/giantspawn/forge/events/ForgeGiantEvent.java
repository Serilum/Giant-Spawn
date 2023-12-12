package com.natamus.giantspawn.forge.events;

import com.natamus.giantspawn.events.GiantEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeGiantEvent {
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		GiantEvent.onEntityJoin(e.getWorld(), e.getEntity());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level level = e.world;
		if (level.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		GiantEvent.onWorldTick((ServerLevel)level);
	}
}
