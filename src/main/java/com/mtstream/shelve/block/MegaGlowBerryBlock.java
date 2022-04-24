package com.mtstream.shelve.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MegaGlowBerryBlock extends Block{
	
	public static final VoxelShape FULL = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.625, 0.8125);
	public static final VoxelShape BITE1 = Shapes.box(0.1875, 0, 0.1875, 0.625, 0.625, 0.8125);
	public static final VoxelShape BITE2 = Shapes.box(0.375, 0, 0.1875, 0.625, 0.625, 0.8125);
	
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 2);
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos,
			CollisionContext con) {
		switch(state.getValue(BITES)) {
		case 0:
			return FULL;
		case 1:
			return BITE1;
		case 2:
			return BITE2;
		default:
			return FULL;
		}
	}
	public MegaGlowBerryBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
	}
	@Override
	public boolean canSurvive(BlockState state, LevelReader lev, BlockPos pos) {
		return lev.getBlockState(pos.below()).getMaterial().isSolid() || lev.getBlockState(pos.above()).getMaterial().isSolid();
	}
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor lev, BlockPos pos, BlockPos pos1) {
	    return !state.canSurvive(lev, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, state1, lev, pos, pos1);
	}
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		if(!pla.canEat(false)) {
			return InteractionResult.PASS;
		}else {
			if(!lev.isClientSide) {
				int remain = state.getValue(BITES) + 1;
				pla.getFoodData().eat(2, 0.4f);
				pla.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300));
				lev.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);	
				if(!(remain > 2)) {
					lev.setBlockAndUpdate(pos, state.setValue(BITES, remain));
				}else {
					lev.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				}
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}
	}
}
