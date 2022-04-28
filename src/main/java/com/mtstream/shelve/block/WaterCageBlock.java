package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaterCageBlock extends WaterLoggableBlock{

	public static final VoxelShape SHAPE = Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999);

	public WaterCageBlock(Properties properties) {
		super(properties);
	}
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return SHAPE;
	}
	

}
