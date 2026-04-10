package com.natamus.giantspawn.ai;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GiantAttackTurtleEggGoal extends RemoveBlockGoal {
	public GiantAttackTurtleEggGoal(PathfinderMob pathfinderMob, double d, int i) {
		super(Blocks.TURTLE_EGG, pathfinderMob, d, i);
	}
	
	public void playDestroyProgressSound(LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
		levelAccessor.playSound((Player)null, blockPos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + GlobalVariables.random.nextFloat() * 0.2F);
	}
	
	public void playBreakSound(Level level, @NotNull BlockPos blockPos) {
		level.playSound((Player)null, blockPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
	}
	
	public double acceptedDistance() {
		return 1.14D;
	}
}
