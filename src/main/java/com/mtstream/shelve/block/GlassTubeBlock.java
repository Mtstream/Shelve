package com.mtstream.shelve.block;

import java.util.Random;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlassTubeBlock extends Block{
	
	public static final IntegerProperty CONNECT = IntegerProperty.create("connect", 0, 3);
	public static final IntegerProperty CONTENT = IntegerProperty.create("content", 0, 5);
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);
	}
	
	public GlassTubeBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(CONNECT, 0).setValue(CONTENT, 0).setValue(POWER, 0).setValue(POWERED, false));
	}
	public static int getLightLevel(BlockState state) {
		switch(state.getValue(CONTENT)) {
		case 0:
			return 0;
		case 1:
			return 13;
		case 3:
			return state.getValue(POWERED)?1:0;
		case 4:
			return 14;
		case 5:
			return 5;
		default:
			return 0;
		}
	}
	public static boolean isEmissive(BlockState state) {
		return GlassTubeBlock.getLightLevel(state)>2;
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(CONNECT,CONTENT,POWER,POWERED);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return this.connect(con.getLevel(), this.defaultBlockState(), con.getClickedPos());
	}
	public final Object2IntMap<Item> items() {
		Object2IntMap<Item> items = new Object2IntOpenHashMap<>();
		items.put(Items.GLOW_INK_SAC, 1);
		items.put(Items.GLOWSTONE_DUST, 2);
		items.put(Items.REDSTONE, 3);
		items.put(Items.GLOWSTONE_DUST, 4);
		items.put(Items.EXPERIENCE_BOTTLE, 5);
		return items;
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemStack stack = pla.getItemInHand(han);
		if(this.items().containsKey(stack.getItem())) {
			int i = this.items().getInt(stack.getItem());
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(CONTENT, i));
				lev.updateNeighborsAt(pos, this);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else{
			return InteractionResult.FAIL;
		}
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		BlockState belowState = lev.getBlockState(pos.below());
		if(belowState.getBlock() instanceof GlassTubeBlock&&state.getValue(CONTENT) != 0&&belowState.getValue(CONTENT)==0) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(CONTENT, belowState.getValue(CONTENT)));
				lev.setBlockAndUpdate(pos.below(), belowState.setValue(CONTENT, state.getValue(CONTENT)));
				lev.updateNeighborsAt(pos, this);
				lev.updateNeighborsAt(pos.below(), this);
			}
			this.fall(state, lev, pos);
		}
	}
	public void fall(BlockState state, Level lev, BlockPos pos) {
		if(!lev.isClientSide) {
			lev.scheduleTick(pos, this, 10);
		}
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block block,
			BlockPos pos1, boolean bln) {
		BlockState connectState = this.connect(lev, state, pos);
		BlockState newState = connectState!=state?connectState:state;
		BlockState belowState = lev.getBlockState(pos.below());
		if(!lev.isClientSide)lev.setBlock(pos, newState, 2);
		if(belowState.getBlock() instanceof GlassTubeBlock&&state.getValue(CONTENT) != 0&&belowState.getValue(CONTENT)==0) {
			this.fall(newState, lev, pos);
		}
		this.updateSignal(lev, pos, newState);;
	}
	public void updateSignal(Level lev,BlockPos pos,BlockState state) {
		int pow = lev.getBestNeighborSignal(pos)-1<0?0:lev.getBestNeighborSignal(pos)-1;
		if(!lev.isClientSide) {
			if(state.getValue(CONTENT)==3) {
				lev.setBlockAndUpdate(pos, state.setValue(POWER, pow).setValue(POWERED, pow>0));
			}else {
				lev.setBlock(pos, state.setValue(POWERED, false).setValue(POWER, 0), 2);
			}
		}
	}
	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(CONTENT)==3;
	}
	@Override
	public int getSignal(BlockState state, BlockGetter lev, BlockPos pos, Direction dir) {
		return state.getValue(POWER);
	}
	public BlockState connect(Level lev,BlockState state,BlockPos pos) {
		boolean b1 = lev.getBlockState(pos.above()).getBlock() instanceof GlassTubeBlock;
		boolean b2 = lev.getBlockState(pos.below()).getBlock() instanceof GlassTubeBlock;
		return b1?b2?state.setValue(CONNECT, 3):state.setValue(CONNECT, 2):b2?state.setValue(CONNECT, 1):state.setValue(CONNECT, 0);
	}
	
}
