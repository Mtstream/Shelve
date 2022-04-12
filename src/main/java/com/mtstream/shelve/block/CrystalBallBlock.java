package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalBallBlock extends Block{

	public static final VoxelShape BASE1 = Shapes.box(0.125, 0, 0.125, 0.875, 0.0625, 0.875);
	public static final VoxelShape BASE2 = Shapes.box(0.1875, 0.0625, 0.1875, 0.8125, 0.125, 0.8125);
	public static final VoxelShape BASE3 = Shapes.box(0.25, 0.125, 0.25, 0.75, 0.1875, 0.75);
	public static final VoxelShape BALL = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125);
	public static final VoxelShape AABB = Shapes.or(BASE1, BASE2, BASE3, BALL);
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		return AABB;
	}
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}
	public CrystalBallBlock(Properties prop) {
		super(prop);
	}

}
