package com.mtstream.shelve.item;

import java.lang.reflect.Field;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ChaoticStewItem extends Item{

	public ChaoticStewItem(Properties p_41383_) {
		super(p_41383_);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level lev, LivingEntity ent) {
		if(!lev.isClientSide) {
			ent.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.JUMP, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.POISON, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
			ent.addEffect(new MobEffectInstance(MobEffects.WITHER, 600));
		}
		return new ItemStack(Items.BOWL);
	}
}
