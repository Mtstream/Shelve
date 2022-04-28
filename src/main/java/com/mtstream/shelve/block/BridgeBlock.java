package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BridgeBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public static final VoxelShape ZAABB = Shapes.or(Shapes.box(0.0625, 0, 0, 0.1875, 0.0625, 1), 
			Shapes.box(0.8125, 0, 0, 0.9375, 0.0625, 1), Shapes.box(0, 0.0625, 0.0625, 1, 0.125, 0.3125), 
			Shapes.box(0, 0.0625, 0.375, 1, 0.125, 0.625), Shapes.box(0, 0.0625, 0.6875, 1, 0.125, 0.9375));
	public static final VoxelShape XAABB = Shapes.or(Shapes.box(0, 0, 0.8125, 1, 0.0625, 0.9375), 
			Shapes.box(0, 0, 0.0625, 1, 0.0625, 0.1875), Shapes.box(0.0625, 0.0625, 0, 0.3125, 0.125, 1), 
			Shapes.box(0.375, 0.0625, 0, 0.625, 0.125, 1), Shapes.box(0.6875, 0.0625, 0, 0.9375, 0.125, 1));
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		if(state.getValue(FACING)==Direction.NORTH||state.getValue(FACING)==Direction.SOUTH) {
			return ZAABB;
		}else {
			return XAABB;
		}
	}
	
	
	
	
	public BridgeBlock(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
	
}
