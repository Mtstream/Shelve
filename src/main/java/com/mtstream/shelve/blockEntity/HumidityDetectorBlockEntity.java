package com.mtstream.shelve.blockEntity;

import com.mtstream.shelve.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HumidityDetectorBlockEntity extends BlockEntity{

	public HumidityDetectorBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.HUMIDITY_DETECTOR.get(), pos, state);
	}

}
