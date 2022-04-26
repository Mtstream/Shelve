package com.mtstream.shelve.block;


import java.util.Random;

import com.mtstream.shelve.init.ItemInit;
import com.mtstream.shelve.item.ItemShrinker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MilkCauldron extends Block{
	
	public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 1, 3);
	
	private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return SHAPE;
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemShrinker shr = new ItemShrinker();
			if(!lev.isClientSide) {
				switch(state.getValue(PROGRESS)) {
				case 1:
					if(pla.getItemInHand(han).getItem().equals(Items.BUCKET)) {
						lev.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
						shr.ShrinkItem(pla, pla.getItemInHand(han));
						lev.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
						pla.addItem(new ItemStack(Items.MILK_BUCKET));					
					}
					if(pla.getItemInHand(han).getItem().equals(Items.SUGAR)) {
						lev.setBlockAndUpdate(pos, state.setValue(PROGRESS, 2));
						shr.ShrinkItem(pla, pla.getItemInHand(han));
						lev.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
					}
					break;
				case 3:
					lev.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
					Block.popResource(lev, pos, new ItemStack(ItemInit.CHEESE.get()));
				}
				return InteractionResult.CONSUME;
			} else {
				return InteractionResult.SUCCESS;
			}
	}
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		if(state.getValue(PROGRESS)==2) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public void randomTick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(PROGRESS) == 2) {
			lev.setBlockAndUpdate(pos, state.setValue(PROGRESS, 3));
		}
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(PROGRESS);
	}
	public MilkCauldron(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(PROGRESS, 1));
	}
	@Override
	public boolean hasAnalogOutputSignal(BlockState p_60457_) {
		return true;
	}
	@Override
	public int getAnalogOutputSignal(BlockState state, Level lev, BlockPos pos) {
		return 3;
	}
}
