package com.mtstream.shelve.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaterCageBlock extends WaterLoggableBlock{

	public static VoxelShape SHAPE = Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999);

	public WaterCageBlock(Properties properties) {
		super(properties);
	}
	public boolean skipRendering(BlockState sta, BlockState state, Direction dir) {
	      return state.is(this) ? true : super.skipRendering(sta, state, dir);
	}
	

}
