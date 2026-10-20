package com.serilum.giantspawn.mixin;

import com.serilum.giantspawn.ai.GiantAttackGoal;
import com.serilum.giantspawn.ai.GiantAttackTurtleEggGoal;
import com.serilum.giantspawn.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Giant.class, priority = 1001)
public class GiantMixin extends Mob {
	protected GiantMixin(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		Giant giant = (Giant)(Object)this;
		this.goalSelector.addGoal(4, new GiantAttackTurtleEggGoal(giant, 1.0D, 3));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(giant, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(giant));
		this.addBehaviourGoals(giant);
	}

	protected void addBehaviourGoals(Giant giant) {
		this.goalSelector.addGoal(2, new GiantAttackGoal(giant, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(giant, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(giant, new Class[0])).setAlertOthers(ZombifiedPiglin.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(giant, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<AbstractVillager>(giant, AbstractVillager.class, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolem>(giant, IronGolem.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<Turtle>(giant, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
	}

	@Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "TAIL"))
	public void Giant(EntityType<?> entityType, Level level, CallbackInfo ci) {
		this.xpReward = ConfigHandler.giantExperienceReward;
	}

	protected SoundEvent getAmbientSound() {
		if (!ConfigHandler.giantsUseZombieSounds) {
			return super.getAmbientSound();
		}
		return SoundEvents.ZOMBIE_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSource) {
		if (!ConfigHandler.giantsUseZombieSounds) {
			return super.getHurtSound(damageSource);
		}
		return SoundEvents.ZOMBIE_HURT;
	}

	protected SoundEvent getDeathSound() {
		if (!ConfigHandler.giantsUseZombieSounds) {
			return super.getDeathSound();
		}
		return SoundEvents.ZOMBIE_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		if (!ConfigHandler.giantsUseZombieSounds) {
			super.playStepSound(pos, state);
			return;
		}
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.6F, 0.5F);
	}

	public float getVoicePitch() {
		if (!ConfigHandler.giantsUseZombieSounds) {
			return super.getVoicePitch();
		}
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 0.5F;
	}
}
