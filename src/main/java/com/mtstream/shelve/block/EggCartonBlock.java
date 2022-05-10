package com.mtstream.shelve.block;

import java.util.Random;

import com.mtstream.shelve.init.BlockInit;
import com.mtstream.shelve.util.ItemShrinker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EggCartonBlock extends HorizontalDirectionalBlock{
	
	public static final VoxelShape XAABB = Shapes.box(0, 0, 0.1875, 1, 0.4375, 0.8125);
	public static final VoxelShape ZAABB = Shapes.box(0.1875, 0, 0, 0.8125, 0.4375, 1);
	public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 0, 6);
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		Direction direction = state.getValue(FACING);
	      return direction.getAxis() == Direction.Axis.Z ? XAABB : ZAABB;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
	public EggCartonBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(EGGS, 0).setValue(FACING, Direction.NORTH));
	}
	@Override
	public void onRemove(BlockState state, Level lev, BlockPos pos, BlockState state1,
			boolean bln) {
		if(!(state1.is(BlockInit.EGG_CARTON.get())))Block.popResource(lev, pos, new ItemStack(Items.EGG, state.getValue(EGGS)));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,EGGS);
	}
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return (state.getValue(EGGS) > 0);
	}
	@Override
	public void randomTick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(lev.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK)) {
			int resl = state.getValue(EGGS) - 1;
			if(resl>=0) {
				if(!lev.isClientSide) {
					lev.setBlockAndUpdate(pos, state.setValue(EGGS, resl));
					Chicken ckn = EntityType.CHICKEN.create(lev);
					ckn.setBaby(true);
					ckn.setPos(pos.getX()+0.5f, pos.getY()+1.0f, pos.getZ()+0.5f);
					lev.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
					lev.addFreshEntity(ckn);
				}
			}
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemShrinker shr = new ItemShrinker();
		ItemStack stack = pla.getItemInHand(han);
		if(stack.is(Items.EGG)) {
			int resl = state.getValue(EGGS) + 1;
			if(resl <= 6) {
				if(!lev.isClientSide) {
					lev.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
					lev.setBlockAndUpdate(pos, state.setValue(EGGS, resl));
					shr.ShrinkItem(pla, stack);
					return InteractionResult.CONSUME;
				}else {
					return InteractionResult.SUCCESS;
				}
			}else{
				return InteractionResult.FAIL;
			}
		}else {
			int resl = state.getValue(EGGS) - 1;
			if(resl >= 0) {
				if(!lev.isClientSide) {
					lev.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
					lev.setBlockAndUpdate(pos, state.setValue(EGGS, resl));
					pla.addItem(new ItemStack(Items.EGG, 1));
					return InteractionResult.CONSUME;
				}else {
					return InteractionResult.SUCCESS;
				}
			}else {
				return InteractionResult.FAIL;
			}
		}
	}
}
