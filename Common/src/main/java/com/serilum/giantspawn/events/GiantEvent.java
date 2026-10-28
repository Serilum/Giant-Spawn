package com.serilum.giantspawn.events;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.HashMapFunctions;
import com.natamus.collective.objects.IdAmount;
import com.serilum.giantspawn.config.ConfigHandler;
import com.serilum.giantspawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GiantEvent {
	private static final HashMap<Level, CopyOnWriteArrayList<Giant>> giants_per_world = new HashMap<Level, CopyOnWriteArrayList<Giant>>();
	private static final HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	public static void onEntityJoin(Level level, Entity entity) {
		if (level.isClientSide()) {
			return;
		}
		
		if (!(entity instanceof Giant)) {
			return;
		}
		
		if (!HashMapFunctions.computeIfAbsent(giants_per_world, level, k -> new CopyOnWriteArrayList<Giant>()).contains(entity)) {
			giants_per_world.get(level).add((Giant)entity);
		}

		Giant giant = (Giant)entity;
		
		giant.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(ConfigHandler.giantFollowRange);
		giant.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.23F * ConfigHandler.giantMovementSpeedModifier);
		giant.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D * ConfigHandler.giantAttackDamageModifier);
		giant.getAttribute(Attributes.ARMOR).setBaseValue(2.0D);
		giant.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D * ConfigHandler.giantMaxHealthModifier);
		giant.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(ConfigHandler.giantKnockbackResistance);

		if (ConfigHandler.preventGiantDespawning) {
			giant.setPersistenceRequired();
		}

		if (giant.entityTags().contains(Reference.MOD_ID + ".spawned")) {
			return;
		}
		giant.addTag(Reference.MOD_ID + ".spawned");

		giant.setHealth(giant.getMaxHealth());

		if (giant.getRandom().nextDouble() < ConfigHandler.chanceGiantSpawnsWithEquipment) {
			equipGiant(giant);
		}
	}

	private static void equipGiant(Giant giant) {
		RandomSource random = giant.getRandom();

		List<Item> armorPieces = getArmorPieces(random.nextInt(100));
		List<EquipmentSlot> armorSlots = Arrays.asList(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
		for (int i = 0; i < armorSlots.size(); i++) {
			EquipmentSlot armorSlot = armorSlots.get(i);
			if (giant.getItemBySlot(armorSlot).isEmpty()) {
				giant.setItemSlot(armorSlot, new ItemStack(armorPieces.get(i)));
			}
		}

		if (giant.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
			Item weapon = Items.IRON_SHOVEL;
			if (random.nextBoolean()) {
				weapon = Items.IRON_SWORD;
			}
			giant.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(weapon));
		}
	}

	private static List<Item> getArmorPieces(int tierRoll) {
		if (tierRoll < 40) {
			return Arrays.asList(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
		}
		if (tierRoll < 65) {
			return Arrays.asList(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
		}
		if (tierRoll < 85) {
			return Arrays.asList(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS);
		}
		if (tierRoll < 97) {
			return Arrays.asList(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
		}
		return Arrays.asList(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
	}

	public static void onEntityDropLoot(Level level, Entity entity) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		if (!(entity instanceof Giant)) {
			return;
		}

		if (!serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
			return;
		}

		Registry<Item> itemRegistry = level.registryAccess().lookupOrThrow(Registries.ITEM);
		for (IdAmount idAmount : ConfigFunctions.getIdAmounts(ConfigHandler.giantDropItems)) {
			Identifier identifier = Identifier.tryParse(idAmount.id);
			if (identifier == null || !itemRegistry.containsKey(identifier)) {
				continue;
			}

			Item item = itemRegistry.getValue(identifier);
			int amount = idAmount.getRandomAmount(level.getRandom());
			while (amount > 0) {
				int stackSize = Math.min(amount, new ItemStack(item).getMaxStackSize());
				level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY() + 1, entity.getZ(), new ItemStack(item, stackSize)));
				amount -= stackSize;
			}
		}
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
		
		if (!level.isBrightOutside()) {
			return;
		}
		
		for (Giant giant : HashMapFunctions.computeIfAbsent(giants_per_world, level, k -> new CopyOnWriteArrayList<Giant>())) {
			if (giant.isAlive()) {
				if (!giant.isInWaterOrRain()) {
					if (giant.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
						BlockPos epos = giant.blockPosition();
						if (BlockPosFunctions.isOnSurface(level, epos)) {
							giant.setRemainingFireTicks(60);
						}
					}
				}	
			}
			else {
				giants_per_world.get(level).remove(giant);
			}		
		}
	}
}
