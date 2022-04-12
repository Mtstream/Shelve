package com.mtstream.shelve.block;

import com.mtstream.shelve.blockEntity.TrashCanBlockEntity;
import com.mtstream.shelve.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrashCanBlock extends BaseEntityBlock{
	
	public static final VoxelShape CAN = Shapes.box(0.125, 0, 0.125, 0.875, 0.75, 0.875);
	public static final VoxelShape LID = Shapes.box(0.0625, 0.75, 0.0625, 0.9375, 0.9375, 0.9375);
	public static final VoxelShape HANDEL = Shapes.box(0.4375, 0.9375, 0.4375, 0.5625, 1, 0.5625);
	public static final VoxelShape AABB = Shapes.or(HANDEL, CAN, LID);
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		return AABB;
	}
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}
	
	public TrashCanBlock(Properties properties) {
		super(properties);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemStack stack = pla.getItemInHand(han);
		if(!lev.isClientSide) {
			if(lev.getBlockEntity(pos) instanceof TrashCanBlockEntity entity) {
				entity.intract(pos, lev, stack, pla, han);
			}
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.SUCCESS;
		}
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityInit.TRASH_CAN.get().create(pos, state);
	}

}
