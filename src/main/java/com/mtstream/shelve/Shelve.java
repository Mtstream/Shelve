package com.mtstream.shelve;

import com.mtstream.shelve.init.BlockEntityInit;
import com.mtstream.shelve.init.BlockInit;
import com.mtstream.shelve.init.ItemInit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("shelve")
public class Shelve {
	public static final String MOD_ID = "shelve";
	
	public Shelve() {
		IEventBus bus =FMLJavaModLoadingContext.get().getModEventBus();
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		BlockEntityInit.BLOCK_ENTITY.register(bus);
		MinecraftForge.EVENT_BUS.register(this);
	}
}
