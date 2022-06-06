package com.mtstream.shelve.item;

import java.util.Random;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class DiceItem extends BlockItem{

	public DiceItem(Block p_40565_, Properties p_40566_) {
		super(p_40565_, p_40566_);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level lev, Player pla, InteractionHand han) {
		ItemStack stack = pla.getItemInHand(han);
		Random ran = new Random();
		int i = ran.nextInt(6)+1;
		if(lev.isClientSide) {
			pla.displayClientMessage(new TextComponent(""+i).withStyle(i==1?ChatFormatting.RED:ChatFormatting.WHITE), true);
			pla.getCooldowns().addCooldown(this, 20);
		}
		return InteractionResultHolder.sidedSuccess(stack, lev.isClientSide);
	}
}
