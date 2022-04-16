package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CheeseCakeBlock extends Block{
	
	public static final IntegerProperty BITES = BlockStateProperties.BITES;
	
	public static final VoxelShape shape(int i) {
		return Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.375, 0.9375 - (0.125 * i));	
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!pla.canEat(false)) {
			return InteractionResult.PASS;
		}else {
			if(!lev.isClientSide) {
				int remain = state.getValue(BITES) + 1;
				pla.getFoodData().eat(3, 0.3f);
				lev.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);	
				if(remain != 7) {
					lev.setBlockAndUpdate(pos, state.setValue(BITES, remain));
				}else {
					lev.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				}
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
			
		}
		
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		return shape(state.getValue(BITES));
	}
	public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor lev, BlockPos pos, BlockPos pos1) {
	    return dir == Direction.DOWN && !state.canSurvive(lev, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, state1, lev, pos, pos1);
	}

	public boolean canSurvive(BlockState p_51209_, LevelReader p_51210_, BlockPos p_51211_) {
	    return p_51210_.getBlockState(p_51211_.below()).getMaterial().isSolid();
	}
	public CheeseCakeBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}

}
