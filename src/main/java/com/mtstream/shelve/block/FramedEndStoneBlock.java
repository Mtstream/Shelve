package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class FramedEndStoneBlock extends Block{
	
	public static final IntegerProperty TEARING = IntegerProperty.create("tearing", 0, 4);
	
	public FramedEndStoneBlock(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(this.stateDefinition.any().setValue(TEARING, 0));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(TEARING);
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		BlockPos curpos = pos;
		while(!lev.isEmptyBlock(curpos)) {
			curpos = curpos.above();
		}
		lev.addParticle(ParticleTypes.PORTAL, curpos.getX()+ran.nextDouble(), curpos.getY(), curpos.getZ()+ran.nextDouble(), 0, 1, 0);
		if(state.getValue(TEARING)>0) {
			         for(int j = 0; j < 5; ++j) {
			            double d0 = (double)pos.getX() + ran.nextDouble();
			            double d1 = (double)pos.getY() + ran.nextDouble();
			            double d2 = (double)pos.getZ() + ran.nextDouble();
			            double d3 = (ran.nextDouble() - 0.5D) * 0.5D;
			            double d4 = (ran.nextDouble() - 0.5D) * 0.5D;
			            double d5 = (ran.nextDouble() - 0.5D) * 0.5D;
			            int k = ran.nextInt(2) * 2 - 1;
			            if (ran.nextBoolean()) {
			               d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
			               d5 = (double)(ran.nextFloat() * 2.0F * (float)k);
			            } else {
			               d0 = (double)pos.getX() + 0.5D + 0.25D * (double)k;
			               d3 = (double)(ran.nextFloat() * 2.0F * (float)k);
			            }
			            lev.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
			         }
		}
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(TEARING)<3) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(TEARING, state.getValue(TEARING)+1));
				lev.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.scheduleTick(pos, this, 20);
			}
		}else {
			if(!lev.isClientSide)this.tear(lev, pos);
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult p_60508_) {
		if(pla.getItemInHand(han).is(Items.ENDER_PEARL)&&state.getValue(TEARING)==0) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(TEARING, 1));
				lev.scheduleTick(pos, this, 20);
				lev.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else if(pla.getItemInHand(han).is(Items.ENDER_EYE)&&state.getValue(TEARING)==0&&lev.dimensionType().respawnAnchorWorks()||lev.dimensionType().bedWorks()) {
			if(!lev.isClientSide) {
				this.tear(lev, pos, ((ServerPlayer)pla).getRespawnPosition());
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;}
		}else {
			return InteractionResult.FAIL;
		}
	}
	public void tear(Level lev,BlockPos pos) {
		this.tear(lev, pos, new BlockPos(0, 70, 0));
	}
	public void tear(Level lev,BlockPos pos,BlockPos pos1) {
		lev.explode(null, pos.getX(), pos.getY(), pos.getZ(), 3.0f, BlockInteraction.BREAK);
		lev.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
		lev.setBlockAndUpdate(pos, Blocks.END_GATEWAY.defaultBlockState());
		if(lev.getBlockEntity(pos) instanceof TheEndGatewayBlockEntity blkent) {
			blkent.setExitPosition(pos1, false);
		}
	}
}
