package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChimneyBlock extends Block{
	
	public static final VoxelShape P1 = Shapes.box(0, 0.001, 0, 1, 0.999, 0.1875);
	public static final VoxelShape P2 = Shapes.box(0, 0.001, 0.8125, 1, 0.999, 1);
	public static final VoxelShape P3 = Shapes.box(0, 0.001, 0.1875, 0.1875, 0.999, 0.8125);
	public static final VoxelShape P4 = Shapes.box(0.8125, 0.001, 0.1875, 1, 0.999, 0.8125);
	public static final VoxelShape AABB1 = Shapes.or(P1, P2, P3, P4);
	public static final VoxelShape P5 = Shapes.box(0.0625, 0.001, 0.0625, 0.9375, 0.999, 0.1875);
	public static final VoxelShape P6 = Shapes.box(0.0625, 0.001, 0.8125, 0.9375, 0.999, 0.9375);
	public static final VoxelShape P7 = Shapes.box(0.0625, 0.001, 0.1875, 0.1875, 0.999, 0.8125);
	public static final VoxelShape P8 = Shapes.box(0.8125, 0.001, 0.1875, 0.9375, 0.999, 0.8125);
	public static final VoxelShape AABB2 = Shapes.or(P5, P6, P7, P8);
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return state.getValue(FULL)?AABB1:AABB2;
	}
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty FULL = BooleanProperty.create("full");
	
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		BlockState bottomState = getBottomBlock(lev, state, pos);
		if(state.getValue(TOP)) {
			if(bottomState.getBlock() instanceof AbstractFurnaceBlock || bottomState.getBlock() instanceof CampfireBlock) {
				if(bottomState.getValue(AbstractFurnaceBlock.LIT)||bottomState.getValue(CampfireBlock.LIT)) {
					lev.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)pos.getX() + 0.5D + ran.nextDouble() / 3.0D * (double)(ran.nextBoolean() ? 1 : -1), (double)pos.getY() + ran.nextDouble() + ran.nextDouble(), (double)pos.getZ() + 0.5D + ran.nextDouble() / 3.0D * (double)(ran.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
				}
			}
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!pla.isCrouching()) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.cycle(FULL));
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else {
			return InteractionResult.FAIL;
		}
		
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blk,
			BlockPos pos1, boolean bln) {
		lev.setBlockAndUpdate(pos, updateState(lev, state, pos));
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return updateState(con.getLevel(), this.defaultBlockState(), con.getClickedPos());
	}
	public BlockState updateState(Level lev,BlockState state,BlockPos pos) {
		return state.setValue(TOP, !(lev.getBlockState(pos.above()).getBlock() instanceof ChimneyBlock));
		
	}
	public BlockState getBottomBlock(Level lev,BlockState state,BlockPos pos) {
		BlockPos curPos = pos.below();
		BlockState curState = lev.getBlockState(curPos);
		for(int i = 0; i < 65; i++) {
			if(!(curState.getBlock() instanceof ChimneyBlock)) break;
			curPos = pos.below(i);
			curState = lev.getBlockState(curPos);
		}
		return curState;
	}
	
	public ChimneyBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(TOP, false).setValue(FULL, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(TOP,FULL);
	}

}
