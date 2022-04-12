package com.mtstream.shelve.block;

import javax.annotation.Nullable;

import com.mtstream.shelve.blockEntity.HumidityDetectorBlockEntity;
import com.mtstream.shelve.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HumidityDetectorBlock extends BaseEntityBlock{
	
	public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
	public static final BooleanProperty WATER_DETECTING = BooleanProperty.create("water_detecting");
	
	public static final VoxelShape BASE = Shapes.box(0, 0, 0, 1, 0.375, 1);
	public static final VoxelShape SPONGEBASE = Shapes.box(0.375, 0.4375, 0.375, 0.625, 0.5625, 0.625);
	public static final VoxelShape SPONGE = Shapes.box(0.3125, 0.375, 0.3125, 0.6875, 0.4375, 0.6875);
	public static final VoxelShape AABB = Shapes.or(SPONGE, BASE, SPONGEBASE);
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		return AABB;
	}
	public RenderShape getRenderShape(BlockState state) {
	      return RenderShape.MODEL;
	   }
	public HumidityDetectorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(WATER_DETECTING, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWER,WATER_DETECTING);
	}
	@Override
	public boolean useShapeForLightOcclusion(BlockState p_60576_) {
		return true;
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityInit.HUMIDITY_DETECTOR.get().create(pos, state);
	}
	@Override
	public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction dir) {
		return state.getValue(POWER);
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos pos1, boolean bln) {
		updateSignal(lev, pos, state);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!lev.isClientSide) {
			BlockState state1 = state.cycle(WATER_DETECTING);
			lev.setBlock(pos, state1, 4);
			updateSignal(lev, pos, state1);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.SUCCESS;
		}
		
	}
	public static void updateSignal(Level lev,BlockPos pos,BlockState state) {
		int signal = 0;
		if(state.getValue(WATER_DETECTING) == true) {
			int water = detectwater(lev, pos);
			if(water > 0) {
				signal = 3 + (water * 2);
			}
		} else {
			if(lev.isRaining()||lev.isThundering()) {
				signal = 12;
			}
		}
		lev.setBlock(pos, state.setValue(POWER, signal), 3);
	}
	public static int detectwater(Level lev,BlockPos pos) {
		int water = 0;
		for(Direction dir : Direction.values()) {
			BlockPos pos1 = pos.relative(dir);
			FluidState fstate = lev.getFluidState(pos1);
			if(fstate.is(FluidTags.WATER)) {
				water++;
			}
		}
		return water;
	}
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lev, BlockState state, BlockEntityType<T> type) { 
		return lev.isClientSide ? null : createTickerHelper(type, BlockEntityInit.HUMIDITY_DETECTOR.get(), HumidityDetectorBlock::tickEntity);
	}
	public static void tickEntity(Level lev,BlockPos pos,BlockState state,HumidityDetectorBlockEntity entity) {
		if(lev.getGameTime()%20L == 0L) {
			updateSignal(lev, pos, state);
		}
	}
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
}
