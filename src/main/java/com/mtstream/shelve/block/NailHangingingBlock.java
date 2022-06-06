package com.mtstream.shelve.block;

import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class NailHangingingBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock{
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public NailHangingingBlock(Properties prop) {
		super(prop);
	}
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED)?Fluids.WATER.getSource(false):super.getFluidState(state);
	}
	@Override
	public boolean canSurvive(BlockState state, LevelReader lev, BlockPos pos) {
		return lev.getBlockState(pos.relative(state.getValue(FACING))).getMaterial().isSolid();
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor lev,
			BlockPos pos, BlockPos pos1) {
		return !state.canSurvive(lev, pos)?Blocks.AIR.defaultBlockState():super.updateShape(state, dir, state1, lev, pos, pos1);
	}
	@Override
	public void onRemove(BlockState state, Level lev, BlockPos pos, BlockState state1,
			boolean bln) {
		if(!(state1.getBlock() instanceof NailHangingingBlock)) {
			if(!lev.isClientSide)lev.setBlock(pos, BlockInit.NAIL.get().defaultBlockState().setValue(FACING, state.getValue(FACING)), 2);
		}
	}
}
