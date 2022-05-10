package com.mtstream.shelve.block;

import java.util.Random;

import com.mtstream.shelve.util.FacingTransformaer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FlameThrowerBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty LOADING = IntegerProperty.create("loading", 0, 3);
	public static final BooleanProperty ACTIVED = BooleanProperty.create("actived");

	public FlameThrowerBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LOADING, 0).setValue(ACTIVED, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,LOADING,ACTIVED);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
	@Override
	public void onPlace(BlockState p_60566_, Level lev, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
		lev.scheduleTick(pos, this, 40);
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(ACTIVED)) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(ACTIVED, false));
			}
		}else {
			if(state.getValue(LOADING)<3) {
				if(!lev.isClientSide) {
					lev.setBlockAndUpdate(pos, state.setValue(LOADING, state.getValue(LOADING)+1));
					lev.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0f, 1.0f);
				}
				lev.addParticle(ParticleTypes.LAVA, pos.getZ()+ran.nextDouble(), pos.getY()+1, pos.getZ()+ran.nextDouble(), 0, 0.5, 0);
			}
		}
		
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		double d = ran.nextDouble();
		int x;
		int z;
		switch(state.getValue(FACING)) {
		case EAST:
			x=1;z=0;
			break;
		case NORTH:
			x=0;z=-1;
			break;
		case SOUTH:
			x=0;z=1;
			break;
		case WEST:
			x=-1;z=0;
			break;
		default:
			x=0;z=-1;
			break;
		}
		for(int j=0;j<=10;j++) {
			if(state.getValue(ACTIVED))lev.addParticle(ParticleTypes.LAVA, pos.getX()+d, pos.getY()+d, pos.getZ()+d, x, 0, z);
		}
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blk,
			BlockPos pos1, boolean bln) {
		this.updateState(lev, pos, state);
	}
	public void updateState(Level lev,BlockPos pos,BlockState state) {
		if(!lev.isClientSide) {
			if(state.getValue(ACTIVED)) {
				lev.scheduleTick(pos, this, 2);
			}else if(lev.hasNeighborSignal(pos)&&state.getValue(LOADING)==3){
				lev.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.setBlockAndUpdate(pos, state.setValue(ACTIVED, true));
				lev.setBlockAndUpdate(pos, state.setValue(LOADING, 0));
				this.shoot(lev, pos, state);
			}
		}
	}
	public void shoot(Level lev,BlockPos pos,BlockState state) {
		BlockPos pos1 = pos.relative(state.getValue(FACING));
		SmallFireball sfb = EntityType.SMALL_FIREBALL.create(lev);
		sfb.setPos(pos1.getX()+0.5, pos1.getY()+0.5, pos1.getZ()+0.5);
		sfb.xPower = (double)FacingTransformaer.FacingToPosRelativeX(state)/2.0d;
		sfb.yPower = 0;
		sfb.zPower = (double)FacingTransformaer.FacingToPosRelativeZ(state)/2.0d;
		lev.addFreshEntity(sfb);
	}
}
