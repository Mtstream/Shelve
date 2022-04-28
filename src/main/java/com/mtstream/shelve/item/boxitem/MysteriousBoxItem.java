package com.mtstream.shelve.item.boxitem;

import java.util.Random;
import java.util.List;

import com.mtstream.shelve.init.ItemInit;
import com.mtstream.shelve.item.ItemShrinker;
import com.mtstream.shelve.loot.ShelveLootTable;

import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;



public class MysteriousBoxItem extends Item{
	

	public MysteriousBoxItem(Properties properties) {
		super(properties);
	}
	public InteractionResultHolder<ItemStack> use(Level lev,Player pla,InteractionHand han){
			Random random = new Random();
			int randomEvent = random.nextInt(4);
			ItemStack stack = pla.getItemInHand(han);
			ItemShrinker shr = new ItemShrinker();
		if(!lev.isClientSide()) {
			if(randomEvent == 3||randomEvent == 4) {
				shr.ShrinkItem(pla, stack);
				pla.addItem(new ItemStack(ItemInit.EMPTY_BOX.get()));
			}
			switch(randomEvent) {
			case 1:
				pla.sendMessage(new TranslatableComponent("text.openbutnot.message"), Util.NIL_UUID);
				pla.getCooldowns().addCooldown(this, 100);
				lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_OPEN, SoundSource.PLAYERS, 0.5f, 1.0f);
				lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_CLOSE, SoundSource.PLAYERS, 0.5f, 1.0f);
				break;
			case 2:
				int randomEvent2 = random.nextInt(25);
				if(randomEvent2 >= 24) {
				lev.explode(null, DamageSource.GENERIC, null, pla.getX(), pla.getY(), pla.getZ(), 1.0f, false, BlockInteraction.DESTROY);
				shr.ShrinkItem(pla, stack);
				pla.sendMessage(new TranslatableComponent("text.boomnothing.message"), Util.NIL_UUID);
				}else {
					pla.sendMessage(new TranslatableComponent("text.openbutnot.message"), Util.NIL_UUID);
					pla.getCooldowns().addCooldown(this, 100);
					lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_OPEN, SoundSource.PLAYERS, 0.5f, 1.0f);
					lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_CLOSE, SoundSource.PLAYERS, 0.5f, 1.0f);
				}
				break;
			case 3:
				LootTable trash = lev.getServer().getLootTables().get(ShelveLootTable.MYSTERIOUS_TRASH);
				LootContext trashcon = (new LootContext.Builder((ServerLevel) lev))
				.withLuck(pla.getLuck()).withParameter(LootContextParams.THIS_ENTITY, pla)
				.withParameter(LootContextParams.ORIGIN, pla.position()).create(LootContextParamSets.GIFT);
				List<ItemStack> trashLoot = trash.getRandomItems(trashcon);
				if(pla.getInventory().getFreeSlot() >= 0){
					pla.addItem(trashLoot.get(0));
				}else {
					pla.drop(trashLoot.get(0), true);
				}
				lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_OPEN, SoundSource.PLAYERS, 0.5f, 1.0f);
				break;
			case 4:
				LootTable tresure = lev.getServer().getLootTables().get(ShelveLootTable.MYSTERIOUS_TRESURE);
				LootContext tresurecon = (new LootContext.Builder((ServerLevel) lev))
				.withLuck(pla.getLuck()).withParameter(LootContextParams.THIS_ENTITY, pla)
				.withParameter(LootContextParams.ORIGIN, pla.position()).create(LootContextParamSets.GIFT);
				List<ItemStack> tresureLoot = tresure.getRandomItems(tresurecon);
				if(pla.getInventory().getFreeSlot() >= 0){
					pla.addItem(tresureLoot.get(0));
				}else {
					pla.drop(tresureLoot.get(0), true);
				}
				lev.playSound(null, pla.getX(), pla.getY(), pla.getZ(), SoundEvents.BARREL_OPEN, SoundSource.PLAYERS, 0.5f, 1.0f);
				break;
			}
			
			
		}
		return InteractionResultHolder.sidedSuccess(stack, lev.isClientSide());
	}
	
	
}
