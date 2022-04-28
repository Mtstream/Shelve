package com.mtstream.shelve.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class HandheldDispenserItem extends Item{
	public HandheldDispenserItem(Properties properties) {
		super(properties);
	}
	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {
		return UseAnim.NONE;
	}
	@Override
	public InteractionResultHolder<ItemStack> use(Level lev, Player pla, InteractionHand han) {
		ItemStack stack = pla.getItemInHand(han);
		ItemShrinker shr = new ItemShrinker();
		if(getAnotherHand(han, pla).equals(Items.ARROW)||getAnotherHand(han, pla).equals(Items.SPECTRAL_ARROW)||getAnotherHand(han, pla).equals(Items.TIPPED_ARROW)){
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.ARROW));
			if(!lev.isClientSide) {
				ArrowItem ari = (ArrowItem)(proInHand.getItem() instanceof ArrowItem ? proInHand.getItem() : Items.ARROW);
				AbstractArrow arr = ari.createArrow(lev, proInHand, pla);
				arr.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), 0.0F,2.5F, 1.0F);
				lev.playSound(null, pla.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(arr);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.SNOWBALL)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.SNOWBALL));
			if(!lev.isClientSide) {
				Snowball sno = new Snowball(lev, pla);
				sno.setItem(proInHand);
				sno.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), 0.0F, 2.0F, 1.0F);
				lev.playSound(null, pla.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(sno);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.FIRE_CHARGE)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.FIRE_CHARGE));
			if(!lev.isClientSide) {
				Vec3 target = pla.getEyePosition().add(pla.getViewVector(1).scale(10));
				ClipContext clc = new ClipContext(pla.getEyePosition(), target, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, pla);
				BlockHitResult res = lev.clip(clc);
				double dt = Math.sqrt(Math.sqrt(pla.distanceToSqr(target))) * 0.5D;
				double xv = res.getBlockPos().getX() - pla.getX();
				double yv = res.getBlockPos().getY() - pla.getY();
				double zv = res.getBlockPos().getZ() - pla.getZ();
				SmallFireball sfb = new SmallFireball(lev, pla, xv*dt, yv, zv*dt);
				sfb.setItem(proInHand);
				sfb.setPos(pla.getX(), pla.getY(0.5D) + 0.5D, pla.getZ());
				sfb.setNoGravity(true);
				lev.playSound(null, pla.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(sfb);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.SPLASH_POTION)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.SPLASH_POTION));
			if(!lev.isClientSide) {
				ThrownPotion tpo = new ThrownPotion(lev, pla);
				tpo.setItem(proInHand);
				tpo.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), -20.0f, 1.0f, 1.0f);
				lev.playSound(null, pla.blockPosition(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(tpo);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.LINGERING_POTION)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.LINGERING_POTION));
			if(!lev.isClientSide) {
				ThrownPotion tpo = new ThrownPotion(lev, pla);
				tpo.setItem(proInHand);
				tpo.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), -20.0f, 1.0f, 1.0f);
				lev.playSound(null, pla.blockPosition(), SoundEvents.LINGERING_POTION_THROW, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(tpo);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.EGG)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.EGG));
			if(!lev.isClientSide) {
				ThrownEgg egg = new ThrownEgg(lev, pla);
				egg.setItem(proInHand);
				egg.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), 0.0f, 2.0f, 1.0f);
				lev.playSound(null, pla.blockPosition(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(egg);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		if(getAnotherHand(han, pla).equals(Items.EXPERIENCE_BOTTLE)) {
			final ItemStack proInHand = ProjectileWeaponItem.getHeldProjectile(pla, st->st.getItem().equals(Items.EXPERIENCE_BOTTLE));
			if(!lev.isClientSide) {
				ThrownExperienceBottle exp = new ThrownExperienceBottle(lev, pla);
				exp.setItem(proInHand);
				exp.shootFromRotation(pla, pla.getXRot(), pla.getYRot(), -20.0f, 1.0f, 1.0f);
				lev.playSound(null, pla.blockPosition(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.addFreshEntity(exp);
				if(!pla.getAbilities().instabuild) {
					shr.ShrinkItem(pla, proInHand);
				}
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, lev.isClientSide);
	}
	public Item getAnotherHand(InteractionHand han,Player pla) {
		return(han.equals(InteractionHand.MAIN_HAND)) ?pla.getItemInHand(InteractionHand.OFF_HAND).getItem():pla.getItemInHand(InteractionHand.MAIN_HAND).getItem();
	}
	public ItemStack getAnotherHandStack(InteractionHand han,Player pla) {
		return(han.equals(InteractionHand.MAIN_HAND)) ?pla.getItemInHand(InteractionHand.OFF_HAND):pla.getItemInHand(InteractionHand.MAIN_HAND);
	}
}



