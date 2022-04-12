package com.mtstream.shelve.block;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaterCageBlock extends WaterLoggableBlock{

	public static VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 1, 1);

	public WaterCageBlock(Properties properties) {
		super(properties);
	}
	

}
