package com.mtstream.shelve.block.blockinterface;

import com.mtstream.shelve.ShelveDamageSource;
import com.mtstream.shelve.block.BarChargerBlock;
import com.mtstream.shelve.block.CopperBarBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface ConductorBlock {
	
	public static final IntegerProperty VOLTAGE = IntegerProperty.create("voltage", 0, 15);
	public static final BooleanProperty ISSRC = BooleanProperty.create("issrc");
	
	public default void shock(Level lev,Entity ent,int dam) {
		if(!(ent instanceof ItemEntity)) {
			if(dam>0) {
				ent.hurt(ShelveDamageSource.SHOCK, dam/3);
			}
		}
		if(ent instanceof Creeper) {
			ent.hurt(ShelveDamageSource.SHOCK, dam/3);
			CompoundTag cpt = new CompoundTag();
			((Creeper)ent).addAdditionalSaveData(cpt);
			cpt.putBoolean("powered", true);
			((Creeper)ent).readAdditionalSaveData(cpt);
		}
	}
	public default boolean checkIsSrc(Level lev,BlockPos pos) {
		boolean b = false;
		for(Direction dir:Direction.values()) {
			if(lev.getBlockState(pos.relative(dir)).getBlock() instanceof BarChargerBlock) {
				b=lev.getBlockState(pos.relative(dir)).getValue(BarChargerBlock.POWERED);
				break;
			}
		}
		return b;
	}
	public default int checkPower(Level lev,BlockPos pos) {
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
	public default void updateState(Level lev,BlockPos pos,BlockState state) {
		if(!lev.isClientSide) {
			if(this.checkIsSrc(lev, pos)) {
				lev.setBlockAndUpdate(pos, state.setValue(VOLTAGE, 15));
			}else {
				lev.setBlockAndUpdate(pos, state.setValue(VOLTAGE, this.checkPower(lev, pos)));
			}
		}
	}
}
