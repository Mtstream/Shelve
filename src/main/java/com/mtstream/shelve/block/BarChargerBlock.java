package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BarChargerBlock extends HorizontalDirectionalBlock{
	
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	
	public BarChargerBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(FACING, Direction.NORTH));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED,FACING);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection());
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blk,
			BlockPos pos1, boolean bln) {
		this.updateState(lev, pos, state);
	}
	public void updateState(Level lev,BlockPos pos,BlockState state) {
		boolean powered = lev.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise(Axis.Y))).is(Blocks.REDSTONE_BLOCK)&&lev.getBlockState(pos.relative(state.getValue(FACING).getClockWise(Axis.Y))).is(Blocks.GLOWSTONE);
		if(state.getValue(POWERED) != powered) {
			lev.playSound(null, pos, powered?SoundEvents.RESPAWN_ANCHOR_CHARGE:SoundEvents.SHULKER_SHOOT, SoundSource.BLOCKS, 1.0f, 1.0f);
			lev.setBlockAndUpdate(pos, state.setValue(POWERED, powered));
			lev.updateNeighborsAt(pos, this);
		}
	}
}
