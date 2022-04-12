package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class IgniterBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public IgniterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,POWERED);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(POWERED, Boolean.valueOf(con.getLevel().hasNeighborSignal(con.getClickedPos())))
				.setValue(FACING, con.getNearestLookingDirection().getOpposite());
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		
	}
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo, BlockPos blopos, boolean bln) {
		BlockPos frontpos = pos.relative(state.getValue(FACING));
	      BlockState frontstate = lev.getBlockState(frontpos);
		if (state.getValue(POWERED) && !lev.hasNeighborSignal(pos)) {
	         lev.setBlock(pos, state.setValue(POWERED,false), 2);
	         if(lev.getBlockState(frontpos).getBlock().equals(Blocks.FIRE)||lev.getBlockState(frontpos).getBlock().equals(Blocks.SOUL_FIRE)) {
					lev.setBlockAndUpdate(frontpos, Blocks.AIR.defaultBlockState());
					lev.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.0f);
				}
				if(frontstate.getBlock().equals(Blocks.CAMPFIRE)||frontstate.getBlock().equals(Blocks.SOUL_CAMPFIRE)) {
					lev.setBlockAndUpdate(frontpos,frontstate.setValue(CampfireBlock.LIT, false));
					lev.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.0f);
				}
	      }
		if (!state.getValue(POWERED) && lev.hasNeighborSignal(pos)) {
			lev.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.3f, 1.0f);
	         lev.setBlock(pos, state.setValue(POWERED,true), 2);
	      }
	      
		if(lev.hasNeighborSignal(pos)) {
			if(lev.isEmptyBlock(frontpos) && !lev.isEmptyBlock(frontpos.below())) {
				if(!SoulFireBlock.canSurviveOnBlock(lev.getBlockState(frontpos.below()))) {
					lev.setBlockAndUpdate(frontpos, Blocks.FIRE.defaultBlockState());
				}else {
					lev.setBlockAndUpdate(frontpos, Blocks.SOUL_FIRE.defaultBlockState());
				}
			}
			if(frontstate.getBlock().equals(Blocks.CAMPFIRE)||frontstate.getBlock().equals(Blocks.SOUL_CAMPFIRE)) {
				lev.setBlockAndUpdate(frontpos,frontstate.setValue(CampfireBlock.LIT, true));
			}
			} else {
				
		}
		
		
		super.neighborChanged(state, lev, blopos, blo, blopos, bln);
	}

	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		
	}
	
	public BlockState rotate(BlockState p_54125_, Rotation p_54126_) {
	      return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
	   }

	   @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
	      return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
	      }
}
