package com.mtstream.shelve.item;


import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;

public class TurfItem extends Item{

	public TurfItem(Properties properties) {
		super(properties);
	}
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level lev = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState sta = lev.getBlockState(pos);
		Player pla = context.getPlayer();
		InteractionHand han = context.getHand();
		ItemStack stack = pla.getItemInHand(han);
		ItemShrinker shr = new ItemShrinker();
		
		if(!lev.isClientSide()) {
			if(sta.getBlock().equals(Blocks.DIRT)) {
				lev.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
				lev.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
				shr.ShrinkItem(pla, stack);
				
			}
			return InteractionResult.CONSUME;
		}
		return super.useOn(context);
	}
}
