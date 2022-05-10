package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FuriousObsidianBlock extends Block{

	public FuriousObsidianBlock(Properties prop) {
		super(prop);
	}
	@Override
	public void stepOn(Level lev, BlockPos pos, BlockState state, Entity ent) {
		ent.hurt(DamageSource.HOT_FLOOR, 1);
	}
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
	      if (ran.nextInt(5) == 0) {
	         Direction direction = Direction.getRandom(ran);
	         if (direction != Direction.UP) {
	            BlockPos blockpos = pos.relative(direction);
	            BlockState blockstate = lev.getBlockState(blockpos);
	            if (!state.canOcclude() || !blockstate.isFaceSturdy(lev, blockpos, direction.getOpposite())) {
	               double d0 = direction.getStepX() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepX() * 0.6D;
	               double d1 = direction.getStepY() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepY() * 0.6D;
	               double d2 = direction.getStepZ() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.6D;
	               lev.addParticle(ParticleTypes.DRIPPING_LAVA, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
	            }
	         }
	      }
	   }
}
