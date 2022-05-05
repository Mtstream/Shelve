package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationPath;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ResonatorBlock extends HorizontalDirectionalBlock{
	public static final VoxelShape AABB = Shapes.box(0, 0, 0, 1, 0.125, 1);
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty INPUT = BooleanProperty.create("input");
	public static final IntegerProperty WAITFORACTIVE = IntegerProperty.create("wait_for_active",0,15);
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return AABB;
	}
	public ResonatorBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(INPUT, false).setValue(FACING, Direction.NORTH).setValue(WAITFORACTIVE, 0));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWER,INPUT,FACING,WAITFORACTIVE);
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(POWER)>0) {
			updateState(lev, state, pos);
		}
		if(state.getValue(INPUT)) {
			int waitPower = state.getValue(WAITFORACTIVE);
			if(state.getValue(WAITFORACTIVE)>0) {
				lev.setBlockAndUpdate(pos, state.setValue(WAITFORACTIVE, 0).setValue(POWER, waitPower));
				updateState(lev, state, pos);
			}else {
				lev.setBlockAndUpdate(pos, state.setValue(POWER, 0));
			}
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!lev.isClientSide) {
			lev.setBlockAndUpdate(pos, state.cycle(INPUT));
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection()).setValue(INPUT, false)
				.setValue(POWER, con.getLevel().getSignal(con.getClickedPos().relative(con.getHorizontalDirection().getOpposite()), con.getHorizontalDirection().getOpposite()));
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block block,
			BlockPos pos1, boolean bln) {
		this.updateState(lev, state, pos);
	}
	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(INPUT);
	}
	@Override
	public int getDirectSignal(BlockState state, BlockGetter lev, BlockPos pos, Direction dir) {
		return state.getSignal(lev, pos, dir);
	}
	@Override
	public int getSignal(BlockState state, BlockGetter get, BlockPos pos, Direction dir) {
		return state.getValue(INPUT)?state.getValue(POWER):0;
	}
	public void updateState(Level lev,BlockState state,BlockPos pos) {
		int pow = lev.getBestNeighborSignal(pos);
		int curPow = state.getValue(POWER);
		if(state.getValue(INPUT)) {
			lev.scheduleTick(pos, this, 10);
		}else {
			if(curPow>0!=pow>0) {
				curPow = pow;
				lev.setBlockAndUpdate(pos, state.setValue(POWER, curPow));
				if(curPow>0) {
					this.emit(lev, pos, state, curPow);
				}
				lev.scheduleTick(pos, this, 10);
			}
		}
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		int x;
		int z;
		if(state.getValue(POWER)>0&&state.getValue(INPUT)) {
			switch(state.getValue(FACING)) {
			case EAST:
				x = 4;
				z = 8;
				break;
			case NORTH:
				x = 8;
				z = 12;
				break;
			case SOUTH:
				x = 8;
				z = 4;
				break;
			case WEST:
				x = 12;
				z = 8;
				break;
			default:
				x = 8;
				z = 12;
				break;
			}
				lev.addParticle(ParticleTypes.NOTE, pos.getX()+x*0.0625, pos.getY()+0.5f, pos.getZ()+z*0.0625, state.getValue(POWER)/15, 1.0f, 0);
		}
	}
	public void emit(Level lev,BlockPos pos,BlockState state,int pow) {
		lev.playSound(null, pos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0f, ((float)pow/5));
		Direction dir = state.getValue(FACING);
		BlockPos curPos = pos.relative(dir);
		BlockState curState = lev.getBlockState(curPos);
		int dis = 0;
		for(int i = 0;i < 30;i++) {
			lev.addAlwaysVisibleParticle(ParticleTypes.NOTE, curPos.getX()+0.5f, curPos.getY()+0.5f, curPos.getZ()+0.5, 0, 1.0f, 0);
			if(!lev.isEmptyBlock(curPos)&&!curState.getCollisionShape(lev, curPos).isEmpty()) {
				break;
			}
			curPos = pos.relative(dir, i+1);
			curState = lev.getBlockState(curPos);
			dis = i+1;
		}
		((ServerLevel)lev).sendVibrationParticle(new VibrationPath(pos, new BlockPositionSource(curPos), dis/2));
		if(!lev.isClientSide) {
			if(curState.getBlock() instanceof ResonatorBlock&&curState.getValue(INPUT)) {
				lev.setBlockAndUpdate(curPos, curState.setValue(WAITFORACTIVE, pow));
				lev.scheduleTick(curPos, curState.getBlock(), dis/2);
			}
		}
	}
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter lev, BlockPos pos, Direction dir) {
		return dir.getOpposite() != state.getValue(FACING);
	}
}
