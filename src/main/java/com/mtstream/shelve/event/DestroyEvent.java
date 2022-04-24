package com.mtstream.shelve.event;

import com.mtstream.shelve.Shelve;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
public class DestroyEvent {
	@SubscribeEvent
	public static void destroyEvent(LivingDestroyBlockEvent evt) {
		Level lev = evt.getEntity().getLevel();
		BlockPos pos = evt.getPos();
		BlockState state = lev.getBlockState(pos);
		if(state.is(Blocks.ICE)) {
			Block.popResource(lev, pos, new ItemStack(Items.ICE, 0));
		}
	}

}
