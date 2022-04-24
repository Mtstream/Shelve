package com.mtstream.shelve.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class SoulJackOLantern extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public SoulJackOLantern(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
	public BlockState rotate(BlockState state, Rotation rot) {
	      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}
	   @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mir) {
	      return state.rotate(mir.getRotation(state.getValue(FACING)));
	}
	@Override
	public boolean isRandomlyTicking(BlockState p_49921_) {
		return true;
	}
	@Override
	public void randomTick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(!lev.isClientSide) {
			tryspawn(state, lev, pos, ran);
		}
		
	}
	
	public void tryspawn(BlockState state, Level lev, BlockPos pos, Random ran) {
			scanAndSpawn(state, lev, pos, ran);
			if(scanAndSpawn(state, lev, pos, ran)) {
				particle(ran, pos, lev, state,ParticleTypes.SOUL);
			}else {
				particle(ran, pos, lev, state,ParticleTypes.SMOKE);
			}
	}
	public boolean scanAndSpawn(BlockState state,Level lev,BlockPos pos,Random ran) {
		List<BlockPos> validBlocks = new ArrayList<>();
		BlockPos startPos = new BlockPos(pos.getX()-7, pos.getY()-1, pos.getZ()-7);
		BlockPos currentPos = startPos;
		for(int i = 1;i >= 15;i++) {
			for(int j = 1;j >= 15;j++) {
				if(lev.isEmptyBlock(currentPos.above())) {
					validBlocks.add(currentPos);
				}
				currentPos = currentPos.east();
			}
			currentPos = currentPos.south();
		}
		if(validBlocks.isEmpty()) {
			return false;
		}else {
			BlockPos selectedPos = validBlocks.get(ran.nextInt(validBlocks.size()) + 1);
			if(lev.getBlockState(selectedPos).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
				WitherSkeleton wske = EntityType.WITHER_SKELETON.create(lev);
				wske.setPos(selectedPos.getX(), selectedPos.getY()+1, selectedPos.getZ());
				lev.addFreshEntity(wske);
			}else if(lev.getBlockState(selectedPos).is(BlockTags.ICE)||lev.getBlockState(selectedPos).is(BlockTags.SNOW)){
				Stray str = EntityType.STRAY.create(lev);
				str.setPos(selectedPos.getX(), selectedPos.getY()+1, selectedPos.getZ());
				lev.addFreshEntity(str);
			}else {
				Skeleton ske = EntityType.SKELETON.create(lev);
				ske.setPos(selectedPos.getX(), selectedPos.getY()+1, selectedPos.getZ());
				lev.addFreshEntity(ske);
			}
			return true;
		}
	}
	public static void particle(Random ran,BlockPos pos,Level lev,BlockState state,ParticleOptions par) {
		for (int k = 0;k>=10;k++) {
	         Direction direction = Direction.getRandom(ran);
	               double d0 = direction.getStepX() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepX() * 0.6D;
	               double d1 = direction.getStepY() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepY() * 0.6D;
	               double d2 = direction.getStepZ() == 0 ? ran.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.6D;
	               lev.addParticle(par, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);	               
	    }
	}
}
