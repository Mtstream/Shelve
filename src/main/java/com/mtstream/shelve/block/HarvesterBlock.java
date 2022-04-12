package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HarvesterBlock extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	
	public static final VoxelShape NORTH = Shapes.box(0, 0, 0.125, 1, 1, 1);
	public static final VoxelShape EAST = Shapes.box(0, 0, 0, 0.875, 1, 1);
	public static final VoxelShape SOUTH = Shapes.box(0, 0, 0, 1, 1, 0.875);
	public static final VoxelShape WEST = Shapes.box(0.125, 0, 0, 1, 1, 1);
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
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
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.defaultBlockState().setValue(POWERED, Boolean.valueOf(con.getLevel().hasNeighborSignal(con.getClickedPos())))
				.setValue(FACING, con.getHorizontalDirection().getOpposite());
	}
	public HarvesterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING,POWERED);
	}
	public BlockState rotate(BlockState state, Rotation rot) {
	      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}
	   @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mir) {
	      return state.rotate(mir.getRotation(state.getValue(FACING)));
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos blopos, boolean bln) {
		if (state.getValue(POWERED) && !lev.hasNeighborSignal(pos)) {
	         lev.setBlock(pos, state.setValue(POWERED,false), 2);
			
	      }
		if (!state.getValue(POWERED) && lev.hasNeighborSignal(pos)) {
			lev.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 0.3f, 1.0f);
	         lev.setBlock(pos, state.setValue(POWERED,true), 2);
	      }
		BlockPos frontpos = pos.relative(state.getValue(FACING));
		BlockState frontstate = lev.getBlockState(frontpos);
		if(lev.hasNeighborSignal(pos)) {
			harvest(lev,frontstate,frontpos);
		}
	}
	public void harvest(Level lev,BlockState state,BlockPos pos) {
		if(state.getBlock() instanceof CropBlock) {
			if(state.getValue(CropBlock.AGE) == 7) {
				lev.destroyBlock(pos, true);
				lev.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.8f, 1.0f);
				lev.setBlockAndUpdate(pos, state.setValue(CropBlock.AGE, 1));
			}
		}else
		if(state.getBlock() instanceof NetherWartBlock) {
			if(state.getValue(NetherWartBlock.AGE) == 3) {
				lev.destroyBlock(pos, true);
				lev.playSound(null, pos, SoundEvents.NETHER_WART_BREAK, SoundSource.BLOCKS, 0.8f, 1.0f);
				lev.setBlockAndUpdate(pos, state.setValue(NetherWartBlock.AGE, 0));
			}
		}else
		if(state.getBlock() instanceof SweetBerryBushBlock) {
			if(state.getValue(SweetBerryBushBlock.AGE) == 3) {
				int j = 1 + lev.random.nextInt(2);
		         popResource(lev, pos, new ItemStack(Items.SWEET_BERRIES, j + 1));
		         lev.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + lev.random.nextFloat() * 0.4F);
		         lev.setBlock(pos, state.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
			}
		}else {}
	}

}
