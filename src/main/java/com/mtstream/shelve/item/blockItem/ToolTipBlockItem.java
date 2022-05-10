package com.mtstream.shelve.item.blockItem;

import java.util.List;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ToolTipBlockItem extends BlockItem{
	public boolean shiftForMoreInf;
	
	public ToolTipBlockItem(Block block, Properties prop,boolean shift) {
		super(block, prop);
		shiftForMoreInf = shift;
	}
	@Override
	public void appendHoverText(ItemStack stack, Level lev, List<Component> cpn, TooltipFlag ttf) {
		if(shiftForMoreInf) {
			if(Screen.hasShiftDown()) {
				cpn.add(new TranslatableComponent("item.shelve."+this+".desc"));
				cpn.add(new TranslatableComponent("item.shelve."+this+".desc.shift"));
			}else {
				cpn.add(new TranslatableComponent("item.shelve."+this+".desc"));
				cpn.add(new TranslatableComponent("item.shelve.shift"));
			}
		}else {
			cpn.add(new TranslatableComponent("item.shelve."+this+".desc"));
		}
	}
}
