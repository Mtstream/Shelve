package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallClockBlock extends NailHangingingBlock{
	
	public static final VoxelShape NAABB = Shapes.box(0.0625, 0.0625, 0, 0.9375, 0.9375, 0.125);
	public static final VoxelShape EAABB = Shapes.box(0.875, 0.0625, 0.0625, 1, 0.9375, 0.9375);
	public static final VoxelShape SAABB = Shapes.box(0.0625, 0.0625, 0.875, 0.9375, 0.9375, 1);
	public static final VoxelShape WAABB = Shapes.box(0, 0.0625, 0.0625, 0.125, 0.9375, 0.9375);

	public WallClockBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		switch(state.getValue(FACING)) {
		case EAST:
			return EAABB;
		case NORTH:
			return NAABB;
		case SOUTH:
			return SAABB;
		case WEST:
			return WAABB;
		default:
			return NAABB;
		
		}
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,WATERLOGGED);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		int h = (((int)lev.getDayTime() / 1000)+6)%24;
		int m = (int) ((int)lev.getDayTime() / 16.66d)%60;
		if(!lev.isClientSide) {
			pla.displayClientMessage(new TextComponent((h<10?"0":"")+h+(m<10?":0":":")+m), true);
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
}
