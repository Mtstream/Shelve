package com.mtstream.shelve.block;

import java.util.Random;

import com.mtstream.shelve.init.ItemInit;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CreepshroomBlock extends CropBlock{

	public CreepshroomBlock(Properties prop) {
		super(prop);
	}
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter get, BlockPos pos) {
		return state.is(Blocks.MYCELIUM);
	}
	@Override
	public void randomTick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(AGE) >= 7) {
			if(!lev.getBlockState(pos.below(2)).is(Blocks.TORCH)||lev.getBlockState(pos.below(2)).is(Blocks.WALL_TORCH)) {
				Creeper cre = EntityType.CREEPER.create(lev);
				cre.setPos(pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f);
				lev.addFreshEntity(cre);
				lev.setBlockAndUpdate(pos, state.setValue(AGE, 3));
				lev.playSound(null, pos, SoundEvents.SOUL_SAND_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
			}
		}else if(ran.nextInt(5) == 0){
			int res = state.getValue(AGE) + 1;
			if(!(res > 7)) {
				lev.setBlockAndUpdate(pos, state.setValue(AGE, res));
			}
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemStack stack = pla.getItemInHand(han);
		if(state.getValue(AGE)==7&&!stack.is(Items.BONE_MEAL)) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(AGE, 3));
				lev.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 1.0f);
				Block.popResource(lev, pos, new ItemStack(ItemInit.CREEPER_BUD.get(),1));
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else if(state.getValue(AGE)>=5&&state.getValue(AGE)<7&&!stack.is(Items.BONE_MEAL)){
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(AGE, 4));
				lev.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 1.0f);
				Block.popResource(lev, pos, new ItemStack(Items.GUNPOWDER,3));
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else {
			return InteractionResult.FAIL;
		}
		
	}
	@Override
	protected ItemLike getBaseSeedId() {
		return ItemInit.CREEPER_BUD.get();
	}
	@Override
	public boolean isRandomlyTicking(BlockState p_52288_) {
		return true;
	}
	@Override
	public void performBonemeal(ServerLevel lev, Random ran, BlockPos pos, BlockState state) {
		int res = state.getValue(AGE) + 1;
		if(!(res > 7)) {
			lev.setBlockAndUpdate(pos, state.setValue(AGE, res));
		}
	}

}
