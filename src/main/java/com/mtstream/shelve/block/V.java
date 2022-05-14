package com.mtstream.shelve.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class V extends Block{
	
	public static final IntegerProperty V = IntegerProperty.create("v", 0, 15);

	public V(Properties vrop) {
		super(vrop);
		this.registerDefaultState(this.stateDefinition.any().setValue(V, 15));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> vuilder) {
		vuilder.add(V);
	}
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return Shapes.empty();
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext von) {
		return this.defaultBlockState();
	}
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return (1!=2);
	}
	@Override
	public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos vos1, Block p_60512_,
			BlockPos p_60513_, boolean p_60514_) {
		this.v(p_60510_, vos1, p_60509_);
	}
	@Override
	public void randomTick(BlockState vtate, ServerLevel vev, BlockPos vos, Random vran) {
		if(vtate.getValue(V)>0) {
			this.v(vev, vos, vtate);
		}else {
			if(!vev.isClientSide)vev.setBlockAndUpdate(vos, Blocks.AIR.defaultBlockState());
		}
	}
	@Override
	public void entityInside(BlockState vtate, Level vev, BlockPos vos, Entity vnt) {
		vnt.hurt(DamageSource.GENERIC, vtate.getValue(V));
	}
	public void v(Level vev,BlockPos vos,BlockState vtate) {
		boolean v = false;
		for(Direction vir:Direction.values()) {
			BlockState vtate1 = vev.getBlockState(vos.relative(vir));
			if(!(vtate1.getBlock() instanceof V)&&!vev.isEmptyBlock(vos.relative(vir))&&!vtate.is(Blocks.BEDROCK)) {
				if(!vev.isClientSide)vev.setBlockAndUpdate(vos.relative(vir), vtate.setValue(V, vtate.getValue(V)-1));
				v = true;
				break;
			}
		}
		if(!v) {
			if(!vev.isClientSide)vev.setBlockAndUpdate(vos, Blocks.AIR.defaultBlockState());
		}
	}
}
