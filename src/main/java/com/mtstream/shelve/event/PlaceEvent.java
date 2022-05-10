package com.mtstream.shelve.event;

import com.mtstream.shelve.Shelve;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
public class PlaceEvent {
	@SubscribeEvent
	public static void placeEvent(EntityPlaceEvent evt) {
		for(Property<?> prop:evt.getState().getProperties()) {
			if(prop instanceof DirectionProperty&&evt.getEntity().isCrouching()) {
				if(!((Level)evt.getWorld()).isClientSide())((Level)evt.getWorld()).setBlockAndUpdate(evt.getPos(), evt.getState().setValue((DirectionProperty)prop, evt.getState().getValue((DirectionProperty)prop).getOpposite()));
				break;
			}
		}
	}
}
