package com.mtstream.shelve.block;


import java.util.Random;

import com.mtstream.shelve.block.blockinterface.ConductorBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.FluidState;

public class CopperBarBlock extends IronBarsBlock implements ConductorBlock{
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(VOLTAGE,ISSRC);
		super.createBlockStateDefinition(builder);
	}
	public CopperBarBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(super.defaultBlockState().setValue(VOLTAGE, 0).setValue(ISSRC, false));
	}
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {
		Item item = player.getItemInHand(player.getUsedItemHand()).getItem();
		if(!(item instanceof TieredItem)||((TieredItem) item).getTier() != Tiers.WOOD) {
			if(state.getValue(VOLTAGE)>0&&!level.isClientSide)this.shock(level, player, state.getValue(VOLTAGE));
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		double i = ran.nextDouble();
		if(ran.nextInt(20-state.getValue(VOLTAGE))==0&&state.getValue(VOLTAGE)>0)
		lev.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.getX()+i, pos.getY()+i, pos.getZ()+i, 0, i, 0);
	}
	
	@Override
	public void entityInside(BlockState state, Level lev, BlockPos pos, Entity ent) {
		if(state.getValue(VOLTAGE)>0)this.shock(lev, ent, state.getValue(VOLTAGE));
	}
	@Override
	public void stepOn(Level lev, BlockPos pos, BlockState state, Entity ent) {
		if(state.getValue(VOLTAGE)>0)this.shock(lev, ent, state.getValue(VOLTAGE));
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext con) {
		return super.getStateForPlacement(con).setValue(VOLTAGE, this.checkPower(con.getLevel(), con.getClickedPos())).setValue(ISSRC, this.checkIsSrc(con.getLevel(), con.getClickedPos()));
	}
	@Override
	public void neighborChanged(BlockState state, Level lev, BlockPos pos, Block blo,
			BlockPos pos1, boolean bln) {
		this.updateState(lev, pos, state);
	}
	
}
