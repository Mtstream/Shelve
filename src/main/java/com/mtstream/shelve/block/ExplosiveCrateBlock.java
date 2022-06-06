package com.mtstream.shelve.block;

import java.util.Random;

import com.mtstream.shelve.util.DateProvider;
import com.mtstream.shelve.util.ItemShrinker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ExplosiveCrateBlock extends Block{
	
	public static final IntegerProperty GUNPOWDER = IntegerProperty.create("gunpowder", 0, 9);
	public static final BooleanProperty PRIMED = BooleanProperty.create("primed");

	public ExplosiveCrateBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(GUNPOWDER, 0).setValue(PRIMED, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(GUNPOWDER,PRIMED);
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(PRIMED)) {
			lev.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			lev.explode(null, pos.getX(), pos.getY(), pos.getZ(), state.getValue(GUNPOWDER), BlockInteraction.DESTROY);
		}
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		if(state.getValue(PRIMED)) {
			lev.addParticle(ParticleTypes.LAVA, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, (ran.nextDouble()-0.5)/2, 1, (ran.nextDouble()-0.5)/2);
		}
	}
	@Override
	public void onRemove(BlockState state, Level lev, BlockPos pos, BlockState state1,
			boolean bln) {
		if(state.is(Blocks.PISTON_HEAD)&&state.getBlock()!=state1.getBlock()&&!lev.isClientSide)Block.popResource(lev, pos, new ItemStack(Items.GUNPOWDER,state.getValue(GUNPOWDER)));
	}
	@Override
	public void onBlockExploded(BlockState state, Level lev, BlockPos pos, Explosion explosion) {
		lev.setBlockAndUpdate(pos, state.setValue(PRIMED, true));
		lev.scheduleTick(pos, this, 20);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemStack stack = pla.getItemInHand(han);
		ItemShrinker shr = new ItemShrinker();
		if(!lev.isClientSide) {
			if(stack.is(Items.GUNPOWDER)) {
				if(state.getValue(GUNPOWDER)<8) {
					shr.ShrinkItem(pla, stack);
					lev.setBlockAndUpdate(pos, state.setValue(GUNPOWDER, state.getValue(GUNPOWDER) + 1));
				}
			}else if(stack.is(Items.FLINT_AND_STEEL)){
				lev.setBlockAndUpdate(pos, state.setValue(PRIMED, true));
				lev.scheduleTick(pos, this, 60);
				lev.playSound(null, pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
			}else if(state.getValue(GUNPOWDER)>0){
				lev.setBlockAndUpdate(pos, state.setValue(GUNPOWDER, state.getValue(GUNPOWDER) - 1));
				pla.addItem(new ItemStack(Items.GUNPOWDER));
			}else {
				return InteractionResult.FAIL;
			}
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
}
