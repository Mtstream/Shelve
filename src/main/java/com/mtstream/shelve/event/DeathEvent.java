package com.mtstream.shelve.event;

import com.mtstream.shelve.Shelve;
import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.BlockPos;
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
	}
}
