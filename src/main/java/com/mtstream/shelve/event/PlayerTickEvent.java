package com.mtstream.shelve.event;

import com.mtstream.shelve.Shelve;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
public class PlayerTickEvent {
	@SubscribeEvent
	public static void playerTickEvent(net.minecraftforge.event.TickEvent.PlayerTickEvent evt) {
		Player pla = evt.player;
		Level lev = pla.getLevel();
		for(InteractionHand han:InteractionHand.values()) {
			if(pla.getItemInHand(han).is(Items.LIGHTNING_ROD)&&lev.isThundering()) {
				LightningBolt lig = EntityType.LIGHTNING_BOLT.create(lev);
				lig.setPos(pla.getX(), pla.getY(), pla.getZ());
				if(!lev.isClientSide) {
					lev.addFreshEntity(lig);
					lev.playSound(null, pla.getOnPos(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 1.0f, 1.0f);
				}
			}
		}
	}
}
