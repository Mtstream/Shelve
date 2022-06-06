package com.mtstream.shelve.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;

public class RippingForkItem extends Item{

	public RippingForkItem(Properties prop) {
		super(prop);
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return !(stack.getTag()==null||!stack.getTag().getBoolean("record"));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext con) {
		Level lev = con.getLevel();
		Player pla = con.getPlayer();
		BlockPos pos = con.getClickedPos();
		InteractionHand han = con.getHand();
		ItemStack stack = pla.getItemInHand(han);
		CompoundTag orcpt = stack.getTag();
		CompoundTag cpt = new CompoundTag();
		if(stack.getTag()==null||!orcpt.getBoolean("record")) {
			cpt.putDouble("x", pos.getX());
			cpt.putDouble("y", pos.getY());
			cpt.putDouble("z", pos.getZ());
			cpt.putBoolean("record", true);
			if(!lev.isClientSide) {
				stack.setTag(cpt);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else {
			BlockPos pos1 = new BlockPos(orcpt.getDouble("x"), orcpt.getDouble("y"), orcpt.getDouble("z"));
			cpt.putBoolean("record", false);
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos1, Blocks.END_GATEWAY.defaultBlockState());
				((TheEndGatewayBlockEntity)lev.getBlockEntity(pos1)).setExitPosition(pos, true);
				lev.playSound(null, pos1, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.setBlockAndUpdate(pos, Blocks.END_GATEWAY.defaultBlockState());
				((TheEndGatewayBlockEntity)lev.getBlockEntity(pos)).setExitPosition(pos1, true);
				lev.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
				stack.setTag(cpt);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}
	}
	
}
