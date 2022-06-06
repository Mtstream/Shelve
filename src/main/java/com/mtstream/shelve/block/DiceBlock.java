package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DiceBlock extends Block{
	public int toInt(Direction dir) {
		switch(dir) {
		case DOWN:
			return 6;
		case EAST:
			return 5;
		case NORTH:
			return 4;
		case SOUTH:
			return 3;
		case UP:
			return 1;
		case WEST:
			return 2;
		default:
			return 1;
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		Random ran = new Random();
		Direction dir = Direction.getRandom(ran);
		if(!lev.isClientSide) {
			lev.setBlockAndUpdate(pos, state.setValue(FACING, dir));
			lev.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
			pla.displayClientMessage(new TextComponent(""+this.toInt(dir)).withStyle(this.toInt(dir)==1?ChatFormatting.RED:ChatFormatting.WHITE), true);
			return InteractionResult.CONSUME;
		}else {
			return InteractionResult.SUCCESS;
		}
	}
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public DiceBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter lev, BlockPos pos,
			CollisionContext con) {
		return Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
	}
}
