package com.mtstream.shelve.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HandheldPistonItem extends Item{

	public HandheldPistonItem(Properties prop) {
		super(prop);
	}
	@Override
	public InteractionResult useOn(UseOnContext con) {
		Level lev = con.getLevel();
		Direction dir = con.getClickedFace();
		BlockState state = lev.getBlockState(con.getClickedPos().relative(dir));
		BlockState state1 = lev.getBlockState(con.getClickedPos().relative(dir, 2));
		BlockPos pistonPos = con.getClickedPos().relative(dir);
		if(!lev.isClientSide) {
			lev.setBlock(pistonPos, Blocks.PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, dir.getOpposite()), 2);
			lev.blockEvent(pistonPos, lev.getBlockState(pistonPos).getBlock(), 0, dir.getOpposite().get3DDataValue());
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
}
