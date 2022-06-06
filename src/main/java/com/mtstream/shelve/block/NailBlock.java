package com.mtstream.shelve.block;

import com.mtstream.shelve.init.BlockInit;
import com.mtstream.shelve.util.ItemShrinker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NailBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock{
	
	public static final VoxelShape NAABB = Shapes.box(0.4375, 0.40625, 0.01, 0.5625, 0.53125, 0.1875);
	public static final VoxelShape SAABB = Shapes.box(0.4375, 0.40625, 0.8125, 0.5625, 0.53125, 0.99);
	public static final VoxelShape EAABB = Shapes.box(0.8125, 0.40625, 0.4375, 0.99, 0.53125, 0.5625);
	public static final VoxelShape WAABB = Shapes.box(0.01, 0.40625, 0.4375, 0.1875, 0.53125, 0.5625);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter lev, BlockPos pos,
			CollisionContext con) {
		switch(state.getValue(FACING)) {
		case EAST:
			return EAABB;
		case NORTH:
			return NAABB;
		case SOUTH:
			return SAABB;
		case WEST:
			return WAABB;
		default:
			return NAABB;
		}
	}
	
	public NailBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getClickedFace()!=Direction.DOWN&&con.getClickedFace()!=Direction.UP?con.getClickedFace().getOpposite():con.getHorizontalDirection())
				.setValue(WATERLOGGED, con.getLevel().getFluidState(con.getClickedPos()).is(FluidTags.WATER)&&con.getLevel().getFluidState(con.getClickedPos()).isSource());
	}
	@Override
	public boolean canSurvive(BlockState state, LevelReader lev, BlockPos pos) {
		return lev.getBlockState(pos.relative(state.getValue(FACING))).getMaterial().isSolid();
	}
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED)?Fluids.WATER.getSource(false):super.getFluidState(state);
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor lev,
			BlockPos pos, BlockPos pos1) {
		return !state.canSurvive(lev, pos)?Blocks.AIR.defaultBlockState():super.updateShape(state, dir, state1, lev, pos, pos1);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemShrinker shr = new ItemShrinker();
		ItemStack stack = pla.getItemInHand(han);
		if(!lev.isClientSide) {
			if(stack.is(Items.CLOCK)) {
				lev.setBlockAndUpdate(pos, BlockInit.HANGING_CLOCK.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
				lev.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
				shr.ShrinkItem(pla, stack);
			}
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,WATERLOGGED);
	}
}
