package com.mtstream.shelve;

import com.mtstream.shelve.init.BlockEntityInit;
import com.mtstream.shelve.init.BlockInit;
import com.mtstream.shelve.init.ItemInit;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("shelve")
public class Shelve {
	public static final String MOD_ID = "shelve";
	
	public static final CreativeModeTab SHELVE_TOOL_TAB = new CreativeModeTab(MOD_ID) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.HANDHELD_DISPENSER.get());
		}
	};
	public Shelve() {
		IEventBus bus =FMLJavaModLoadingContext.get().getModEventBus();
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		BlockEntityInit.BLOCK_ENTITY.register(bus);
		MinecraftForge.EVENT_BUS.register(this);
	}
}
