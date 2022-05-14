package com.mtstream.shelve.block;



import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IForgeShearable;

public class ShearerBlock extends Block{
	
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final VoxelShape NORTH = Shapes.box(0, 0, 0, 1, 1, 0.875);
	public static final VoxelShape EAST = Shapes.box(0.125, 0, 0, 1, 1, 1);
	public static final VoxelShape SOUTH = Shapes.box(0, 0, 0.125, 1, 1, 1);
	public static final VoxelShape WEST = Shapes.box(0, 0, 0, 0.875, 1, 1);
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter lev, BlockPos pos,
			CollisionContext con) {
		switch(state.getValue(FACING)) {
		case EAST:
			return EAST;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		default:
			return NORTH;
		}
	}

	public ShearerBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(FACING, Direction.NORTH));
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(FACING, con.getHorizontalDirection());
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block block,
			BlockPos pos1, boolean bln) {
		if(state.getValue(POWERED)!=lev.hasNeighborSignal(pos)) {
			if(!lev.isClientSide)lev.setBlockAndUpdate(pos, state.setValue(POWERED, lev.hasNeighborSignal(pos)));
		}
	}
	@Override
	public void entityInside(BlockState state, Level lev, BlockPos pos, Entity ent) {
		if(!lev.isClientSide&&state.getValue(POWERED))this.shear(lev, pos, ent, state);
	}
	public void shear(Level lev,BlockPos pos,Entity ent,BlockState state) {
		if(ent instanceof IForgeShearable) {
			if(!lev.isClientSide&&((IForgeShearable)ent).isShearable(new ItemStack(Items.SHEARS), lev, pos)) {
				lev.playSound(null, ent.getOnPos().above(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0f, 1.0f);
				List<ItemStack> drops = ((IForgeShearable)ent).onSheared(null, new ItemStack(Items.SHEARS), lev, pos, 1);
				for(ItemStack drop:drops) {
					Block.popResourceFromFace(lev, pos, state.getValue(FACING).getOpposite(), drop);
				}
			}
		}
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED,FACING);
	}

}
