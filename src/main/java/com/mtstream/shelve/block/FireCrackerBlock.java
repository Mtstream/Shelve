package com.mtstream.shelve.block;

import java.util.Random;

import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FireCrackerBlock extends Block{
	
	public static final VoxelShape AABB = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);
	
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return AABB;
	}
	@Override
	public boolean canSurvive(BlockState state, LevelReader red, BlockPos pos) {
		return canSupportCenter(red, pos.above(), Direction.DOWN);
	}
	@Override
	public void entityInside(BlockState state, Level lev, BlockPos pos, Entity ent) {
		if(state.getValue(LIT)) {
			ent.hurt(DamageSource.ON_FIRE, 1.0f);
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(pla.getItemInHand(han).getItem().equals(Items.FLINT_AND_STEEL)) {
			if(!lev.isClientSide) {
				light(lev, pos, state);
				lev.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
				pla.getItemInHand(han).hurtAndBreak(1, pla, (c) -> {c.broadcastBreakEvent(han);});
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else {
			return InteractionResult.FAIL;
		}
	}
	public void light(Level lev,BlockPos pos,BlockState state) {
		lev.setBlockAndUpdate(pos, state.setValue(LIT, true));
		lev.scheduleTick(pos, this, 10);
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		lev.updateNeighborsAt(pos, this);
		lev.explode(null, pos.getX(), pos.getY(), pos.getZ(), 1.0f, BlockInteraction.NONE);
		lev.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos pos1, boolean bln) {
		if(lev.getBlockState(pos.below()).getBlock().equals(BlockInit.FIRECRACKER.get()) && !state.getValue(LIT)) {
			if(lev.getBlockState(pos.below()).getValue(FireCrackerBlock.LIT) && !lev.isClientSide) {
				light(lev, pos, state);
			}
		}
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		if(state.getValue(LIT)) {
			double rx = pos.getX() + ran.nextDouble();
			double ry = pos.getY() + ran.nextDouble();
			double rz = pos.getZ() + ran.nextDouble();
			if(ran.nextInt(4) == 0) {
				lev.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 2.0f);
				lev.addParticle(ParticleTypes.EXPLOSION, rx, ry, rz, 0.0d, 0.0d, 0.0d);
			}
			if(ran.nextInt(2) == 0) {
				lev.addParticle(ParticleTypes.FLAME, rx, ry, rz, 0.0d, 0.0d, 0.0d);
			}
		}
	}
	
	public FireCrackerBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

}
