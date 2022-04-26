package com.mtstream.shelve.block;

import java.util.List;
import java.util.Random;

import com.mtstream.shelve.item.ItemShrinker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BearTrapBlock extends Block{
	
	public static final VoxelShape AABB = Shapes.box(0, 0.01, 0, 1, 0.1875, 1);
	public static final net.minecraft.world.phys.AABB TOUCH_AABB = new net.minecraft.world.phys.AABB(0, 0.01, 0, 1, 0.5, 1);
	public static final BooleanProperty ACTIVED = BooleanProperty.create("actived");
	public static final BooleanProperty SPIKE = BooleanProperty.create("spike");
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return AABB;
	}

	public BearTrapBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVED, false).setValue(SPIKE, false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ACTIVED,SPIKE);
	}
	@Override
	public void tick(BlockState state, ServerLevel lev, BlockPos pos, Random ran) {
		if(state.getValue(ACTIVED)) {
			this.updateState(lev, state, pos);
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemShrinker shr = new ItemShrinker();
		ItemStack stack = pla.getItemInHand(han);
		if(stack.is(Items.IRON_SWORD)) {
			if(!lev.isClientSide) {
				lev.setBlockAndUpdate(pos, state.setValue(SPIKE, true));
				shr.ShrinkItem(pla, stack);
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}else {
			return InteractionResult.FAIL;
		}
	}
	public void updateState(Level lev,BlockState state,BlockPos pos) {
		Boolean active = false;
		List<? extends Entity> entityList = lev.getEntitiesOfClass(LivingEntity.class, TOUCH_AABB.move(pos));
		if(!entityList.isEmpty()) {
			for(Entity entity:entityList) {
				if(!entity.isIgnoringBlockTriggers()) {
					active = true;
					break;
				}
			}
		}
		lev.setBlockAndUpdate(pos, state.setValue(ACTIVED, active));
		if(active) {
			lev.scheduleTick(new BlockPos(pos), this, 10);
		}
	}
	@Override
	public boolean isSignalSource(BlockState p_60571_) {
		return true;
	}
	@Override
	public int getSignal(BlockState state, BlockGetter get, BlockPos pos, Direction dir) {
		return state.getValue(ACTIVED)?15:0;
	}
	@Override
	public void entityInside(BlockState state, Level lev, BlockPos pos, Entity ent) {
		ent.makeStuckInBlock(state, new Vec3(0.01, 0.5, 0.01));
		if(!lev.isClientSide) {
			this.updateState(lev, state, pos);
		}
		if(state.getValue(SPIKE)) {
			ent.hurt(DamageSource.GENERIC, 3.0f);
		}
	}
	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_,
			CollisionContext p_60575_) {
		return Shapes.empty();
	}
}
