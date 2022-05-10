package com.mtstream.shelve.event;

import java.util.Random;

import com.mtstream.shelve.Shelve;
import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
public class DeathEvent {
	@SubscribeEvent
	public static void deathEvent(LivingDeathEvent evt) {
		Level lev = evt.getEntity().getLevel();
		Entity ent = evt.getEntity();	
		BlockPos pos = ent.blockPosition();
		if(ent.getType().equals(EntityType.CREEPER)&&lev.getBlockState(pos.below()).is(Blocks.MYCELIUM)) {
			if(!lev.isClientSide) {
				lev.playSound(null, pos, SoundEvents.CREEPER_DEATH, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.setBlockAndUpdate(pos, BlockInit.CREEPSHROOM.get().defaultBlockState());
			}
		}
		if(ent.getType().equals(EntityType.BLAZE)&&lev.getBlockState(pos.below()).is(Blocks.OBSIDIAN)) {
			if(!lev.isClientSide) {
				lev.playSound(null, pos, SoundEvents.BLAZE_BURN, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.setBlockAndUpdate(pos.below(), BlockInit.FURIOUS_OBSIDIAN.get().defaultBlockState());
			}
			for(int i=0;i<10;i++) {
				lev.addParticle(ParticleTypes.SOUL, pos.getX()+new Random().nextDouble(), pos.getY()+0.1, pos.getZ()+new Random().nextDouble(), 0, 0.1, 0);
			}
		}
	}
}
