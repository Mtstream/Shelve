package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RheostatBlock extends DiodeBlock{
	
	public static final IntegerProperty RESISTANCE = IntegerProperty.create("resistance", 0, 7);
	

	public RheostatBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(RESISTANCE, 0).setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,RESISTANCE,POWERED);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite()).setValue(RESISTANCE, 0);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!lev.isClientSide) {
			lev.setBlock(pos, state.cycle(RESISTANCE), 2);
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
	@Override
	protected int getOutputSignal(BlockGetter lev, BlockPos pos, BlockState state) {
		int signal = getInputSignal((Level)lev, pos, state) - (state.getValue(RESISTANCE)*2);
		return signal < 0 ? 0 : signal;
	}
	@Override
	public int getSignal(BlockState state, BlockGetter lev, BlockPos pos, Direction dir) {
		return dir == state.getValue(FACING) ? this.getOutputSignal(lev, pos, state) : 0;
	}
	@Override
	protected int getDelay(BlockState state) {
		return 1;
	}
	
}
