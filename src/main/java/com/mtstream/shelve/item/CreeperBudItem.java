package com.mtstream.shelve.item;

import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class CreeperBudItem extends Item{

	public CreeperBudItem(Properties prop) {
		super(prop);
	}
	@Override
	public InteractionResult useOn(UseOnContext con) {
		Level lev = con.getLevel();
		BlockPos pos = con.getClickedPos();
		Direction dir = con.getClickedFace();
		BlockPos relatedPos = pos.relative(dir);
		ItemStack stack = con.getItemInHand();
			if(!lev.isClientSide) {
				if(lev.getBlockState(relatedPos.below()).is(Blocks.MYCELIUM)) {
					lev.setBlockAndUpdate(relatedPos, BlockInit.CREEPSHROOM.get().defaultBlockState());
					lev.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
				}else {
					Creeper cre = EntityType.CREEPER.create(lev);
					cre.setPos(relatedPos.getX()+0.5f, relatedPos.getY(), relatedPos.getZ()+0.5f);
					lev.addFreshEntity(cre);
					lev.playSound(null, relatedPos, SoundEvents.CREEPER_HURT, SoundSource.HOSTILE, 1.0f, 1.0f);
				}
				stack.shrink(1);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
	}

}
