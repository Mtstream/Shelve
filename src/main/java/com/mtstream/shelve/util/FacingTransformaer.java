package com.mtstream.shelve.util;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FacingTransformaer {
	public static int FacingToPosRelativeX(BlockState state) {
		int x;
		switch(state.getValue(HorizontalDirectionalBlock.FACING)) {
		case EAST:
			x=1;
			break;
		case NORTH:
			x=0;
			break;
		case SOUTH:
			x=0;
			break;
		case WEST:
			x=-1;
			break;
		default:
			x=0;
			break;
		}
		return x;
	}
	public static int FacingToPosRelativeZ(BlockState state) {
		int z;
		switch(state.getValue(HorizontalDirectionalBlock.FACING)) {
		case EAST:
			z=0;
			break;
		case NORTH:
			z=-1;
			break;
		case SOUTH:
			z=1;
			break;
		case WEST:
			z=0;
			break;
		default:
			z=-1;
			break;
		}
		return z;
	}
}
