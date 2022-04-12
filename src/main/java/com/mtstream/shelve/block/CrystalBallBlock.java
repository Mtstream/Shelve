package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
	@Override
	public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
		return 5;
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		Direction direction = Direction.getRandom(ran);
		double d0 = direction.getStepX() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepX() * 0.6D;
        double d1 = direction.getStepY() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepY() * 0.6D;
        double d2 = direction.getStepZ() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.6D;
        if(ran.nextInt(4) == 0)
     	lev.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
	}
	public CrystalBallBlock(Properties prop) {
		super(prop);
	}

}
