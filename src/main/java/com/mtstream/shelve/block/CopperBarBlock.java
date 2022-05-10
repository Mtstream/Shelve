package com.mtstream.shelve.block;


import java.util.Random;

import com.mtstream.shelve.ShelveDamageSource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;

public class CopperBarBlock extends IronBarsBlock{
	
	public static final IntegerProperty VOLTAGE = IntegerProperty.create("voltage", 0, 15);
	public static final BooleanProperty ISSRC = BooleanProperty.create("issrc");
	
	
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
		if(state.getValue(VOLTAGE)>0)this.shock(level, player, state.getValue(VOLTAGE));
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	@Override
	public void animateTick(BlockState state, Level lev, BlockPos pos, Random ran) {
		double i = ran.nextDouble();
		if(ran.nextInt(20-state.getValue(VOLTAGE))==0&&state.getValue(VOLTAGE)>0)
		lev.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.getX()+i, pos.getY()+i, pos.getZ()+i, 0, i, 0);
	}
	public void shock(Level lev,Entity ent,int dam) {
		if(!(ent instanceof ItemEntity)) {
			if(dam>0) {
				ent.hurt(ShelveDamageSource.SHOCK, dam/3);
			}
		}
		if(ent instanceof Creeper) {
			LightningBolt lig = EntityType.LIGHTNING_BOLT.create(lev);
			((Creeper) ent).thunderHit((ServerLevel)lev, lig);
		}
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
	public boolean checkIsSrc(Level lev,BlockPos pos) {
		boolean b = false;
		for(Direction dir:Direction.values()) {
			if(lev.getBlockState(pos.relative(dir)).getBlock() instanceof BarChargerBlock) {
				b=lev.getBlockState(pos.relative(dir)).getValue(BarChargerBlock.POWERED);
				break;
			}
		}
		return b;
	}
	public int checkPower(Level lev,BlockPos pos) {
		int neiPow;
		int curPow = 0;
		for(Direction dir:Direction.values()) {
			if(lev.getBlockState(pos.relative(dir)).getBlock() instanceof CopperBarBlock) {
				neiPow = lev.getBlockState(pos.relative(dir)).getValue(VOLTAGE);
				if(neiPow>curPow) {
					curPow = neiPow-1;
				}
			}
		}
		return curPow;
	}
	public void updateState(Level lev,BlockPos pos,BlockState state) {
		if(!lev.isClientSide) {
			if(this.checkIsSrc(lev, pos)) {
				lev.setBlockAndUpdate(pos, state.setValue(VOLTAGE, 15));
			}else {
				lev.setBlockAndUpdate(pos, state.setValue(VOLTAGE, this.checkPower(lev, pos)));
			}
		}
	}
}
