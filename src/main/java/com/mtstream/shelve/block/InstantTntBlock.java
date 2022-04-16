package com.mtstream.shelve.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class InstantTntBlock extends TntBlock{

	public InstantTntBlock(Properties prop) {
		super(prop);
	}
	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
		explode(world, pos, igniter);
	}
	public static void explode(Level lev,BlockPos pos,@Nullable LivingEntity ign) {
		PrimedTnt tnt = new PrimedTnt(lev, pos.getX(), pos.getY(), pos.getZ(), ign);
		tnt.setFuse(0);
		lev.addFreshEntity(tnt);
		lev.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        lev.gameEvent(ign, GameEvent.PRIME_FUSE, pos);
	}
}
