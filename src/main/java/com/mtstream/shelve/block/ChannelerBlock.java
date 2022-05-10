package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ChannelerBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 3);
	
	public ChannelerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CHARGE, 0));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,CHARGE);
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
	               if(state.getValue(CHARGE)>=3) {
	            	   lev.addParticle(ParticleTypes.ELECTRIC_SPARK, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
	               }
	            }
	         }
	      }
	}
	@Override
	public int getAnalogOutputSignal(BlockState state, Level lev, BlockPos pos) {
		return state.getValue(CHARGE)*5;
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		BlockPos leftpos = pos.relative(state.getValue(FACING).getCounterClockWise(Axis.Y));
		BlockPos rightpos = pos.relative(state.getValue(FACING).getClockWise(Axis.Y));
		boolean canCharge = lev.getBlockState(leftpos).is(Blocks.REDSTONE_BLOCK) && lev.getBlockState(rightpos).is(Blocks.GLOWSTONE);
		if(canCharge&&state.getValue(CHARGE)<3&&lev.getBlockState(pos.above()).is(Blocks.LIGHTNING_ROD)) {
			lev.setBlock(pos, state.setValue(CHARGE, state.getValue(CHARGE)+1), 2);
			lev.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.7f, state.getValue(CHARGE));
		}
		this.updateState(lev, pos, state);
	}
	public void updateState(Level lev,BlockPos pos,BlockState state) {
		BlockPos leftpos = pos.relative(state.getValue(FACING).getCounterClockWise(Axis.Y));
		BlockPos rightpos = pos.relative(state.getValue(FACING).getClockWise(Axis.Y));
		boolean canStrike = !lev.getBlockState(leftpos).is(Blocks.REDSTONE_BLOCK) && !lev.getBlockState(rightpos).is(Blocks.GLOWSTONE);
		if(state.getValue(CHARGE)<3) {
			lev.scheduleTick(pos, this, 20);
		}else if(state.getValue(CHARGE)>=3 && canStrike&&lev.getBlockState(pos.above()).is(Blocks.LIGHTNING_ROD)){
			this.strike(lev, pos.above(), state);
			lev.setBlockAndUpdate(pos, state.setValue(CHARGE, 0));
		}
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos p_60513_, boolean p_60514_) {
		if(!lev.isClientSide) {
			this.updateState(lev, pos, state);
		}
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
}
