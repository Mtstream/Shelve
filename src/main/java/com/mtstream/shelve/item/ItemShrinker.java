package com.mtstream.shelve.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemShrinker {
	public void ShrinkItem(Player pla,ItemStack stack) {
		if (!pla.getAbilities().instabuild) {
	         stack.shrink(1);
	      }
	}
	public void ShrinkItem(Player pla,ItemStack stack,int items) {
		if (!pla.getAbilities().instabuild) {
	         stack.shrink(items);
		}
	}
}
