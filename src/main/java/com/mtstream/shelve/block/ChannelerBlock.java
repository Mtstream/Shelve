package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class ChannelerBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty ENABLED = BooleanProperty.create("enable");
	
	public ChannelerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ENABLED, Boolean.valueOf(false)));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,ENABLED);
	}
	public BlockState rotate(BlockState p_54125_, Rotation p_54126_) {
	      return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
	   }

	   @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
	      return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
	   }
	
	public void strike(Level lev,BlockPos pos,BlockState state) {
		LightningBolt lig = EntityType.LIGHTNING_BOLT.create(lev);
		lig.setPos(pos.getX(), pos.getY(), pos.getZ());
		lev.addFreshEntity(lig);
		lev.playSound(null, pos, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 1.0f, 1.0f);
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		if (ran.nextInt(1) == 0) {
	         Direction direction = Direction.getRandom(ran);
	         if (direction != Direction.UP) {
	            BlockPos blockpos = pos.relative(direction);
	            BlockState blockstate = lev.getBlockState(blockpos);
	            if (!state.canOcclude() || !blockstate.isFaceSturdy(lev, blockpos, direction.getOpposite())) {
	               double d0 = direction.getStepX() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepX() * 0.6D;
	               double d1 = direction.getStepY() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepY() * 0.6D;
	               double d2 = direction.getStepZ() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.6D;
	               if(state.getValue(ENABLED)) {
	            	   lev.addParticle(ParticleTypes.ELECTRIC_SPARK, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
	               }
	            }
	         }
	      }
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos p_60513_, boolean p_60514_) {
		if(!lev.isClientSide) {
			BlockPos leftpos = pos.relative(state.getValue(FACING).getCounterClockWise(Axis.Y));
			BlockPos rightpos = pos.relative(state.getValue(FACING).getClockWise(Axis.Y));
			if(lev.getBlockState(pos.above()).equals(Blocks.LIGHTNING_ROD.defaultBlockState())) {
				if(lev.getBlockState(leftpos).equals(Blocks.REDSTONE_BLOCK.defaultBlockState()) && 
						lev.getBlockState(rightpos).equals(Blocks.GLOWSTONE.defaultBlockState()) && !state.getValue(ENABLED)) {
					lev.setBlock(pos, state.setValue(ENABLED, true), 2);
					lev.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.7f, 1.0f);
				}
				if(!lev.getBlockState(leftpos).equals(Blocks.REDSTONE_BLOCK.defaultBlockState()) && 
						!lev.getBlockState(rightpos).equals(Blocks.GLOWSTONE.defaultBlockState()) && state.getValue(ENABLED)) {
					lev.setBlock(pos, state.setValue(ENABLED, false), 2);
					strike(lev, pos.above(), state);
				}
			}
		}
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
}
