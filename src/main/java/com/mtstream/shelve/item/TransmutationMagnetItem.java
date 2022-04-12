package com.mtstream.shelve.item;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TransmutationMagnetItem extends Item{

	public TransmutationMagnetItem(Properties properties) {
		super(properties);
	}
	@Override
	public InteractionResult useOn(UseOnContext con) {
		BlockPos pos = con.getClickedPos();
		Level lev = con.getLevel();
		if(!lev.isClientSide) {
			transmutate(lev, pos, Blocks.SHROOMLIGHT, Items.GLOWSTONE_DUST, 2, 1,Blocks.RED_MUSHROOM_BLOCK);
			transmutate(lev, pos, Blocks.REDSTONE_BLOCK, Items.REDSTONE, 4, 1, Blocks.GLOWSTONE);
			transmutate(lev, pos, Blocks.GLOW_LICHEN, Items.GLOWSTONE_DUST, 1, 6, Blocks.AIR);
			transmutate(lev, pos, Blocks.RED_SAND, Items.REDSTONE, 1, 20, Blocks.SAND);
			transmutate(lev, pos, Blocks.SOUL_SAND, Items.GHAST_TEAR, 1, 50, Blocks.SAND);
			transmutate(lev, pos, Blocks.COARSE_DIRT, Items.GRAVEL, 1, 4, Blocks.DIRT);
			transmutate(lev, pos, Blocks.SOUL_SAND, Items.GHAST_TEAR, 1, 50, Blocks.SAND);
			transmutate(lev, pos, Blocks.MAGMA_BLOCK, Items.MAGMA_CREAM, 1, 10, Blocks.BASALT);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.SUCCESS;
		}
		
	}
	public void transmutate(Level lev,BlockPos pos,Block block,Item item,int count,int rand,Block resblock) {
		Random ran = new Random();
		int i = ran.nextInt(rand);
		BlockState state = lev.getBlockState(pos);
		if(state.equals(block.defaultBlockState())) {
			lev.destroyBlock(pos, false);
			if(i == 0) Block.popResource(lev, pos, new ItemStack(item,count));
			lev.setBlockAndUpdate(pos, resblock.defaultBlockState());
		}
	}

}
