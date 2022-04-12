package com.mtstream.shelve.event;

import com.mtstream.shelve.Shelve;
import com.mtstream.shelve.init.BlockInit;
import com.mtstream.shelve.init.ItemInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
public class RightClickEvent {
	@SubscribeEvent
	public static void rightclick(PlayerInteractEvent.RightClickBlock evt) {
		BlockPos pos = evt.getPos();
		Player pla = evt.getPlayer();
		InteractionHand han = evt.getHand();
		ItemStack stack = pla.getItemInHand(han);
		Level lev = evt.getWorld();
		BlockState state = lev.getBlockState(pos);
		if(stack.getItem().equals(Items.MILK_BUCKET) && state.getBlock().equals(Blocks.CAULDRON)) {
			if(!lev.isClientSide) {
				pla.setItemInHand(han, new ItemStack(Items.BUCKET));
				lev.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
				lev.setBlockAndUpdate(pos, BlockInit.MILK_CAULDRON.get().defaultBlockState());
			}
		}
		if(stack.getItem().equals(Items.SHEARS) && state.getBlock().equals(Blocks.GRASS_BLOCK)) {
			if(!lev.isClientSide) {
				Block.popResourceFromFace(lev, pos, Direction.UP, new ItemStack(ItemInit.TURF.get()));
				lev.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
				lev.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
				stack.hurtAndBreak(1, pla, (c) -> {c.broadcastBreakEvent(han);});
			}
		}
	}
}
