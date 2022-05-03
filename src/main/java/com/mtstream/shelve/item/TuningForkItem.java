package com.mtstream.shelve.item;



import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TuningForkItem extends Item{
	
	public TuningForkItem(Properties prop) {
		super(prop);
	}
	@Override
	public void appendHoverText(ItemStack stack, Level lev, List<Component> cpn, TooltipFlag ttf) {
		cpn.add(new TranslatableComponent("item.shelve.tuning_fork.desc1").withStyle(ChatFormatting.BLUE));
		cpn.add(new TranslatableComponent("item.shelve.tuning_fork.desc2").withStyle(ChatFormatting.GRAY));
		cpn.add(new TranslatableComponent("item.shelve.tuning_fork.desc3").withStyle(ChatFormatting.BLUE));
		cpn.add(new TranslatableComponent("item.shelve.tuning_fork.desc4").withStyle(ChatFormatting.GRAY));
	}
	public String getNote(int note) {
		String[] notes = new String[] {"F#","G","G#","A","A#","B","C","C#","D","D#","E","F",
				"F#+","G+","G#+","A+","A#+","B+","C+","C#+","D+","D#+","E+","F+","F#++"};
		return notes[note];
	}
	@Override
	public InteractionResult useOn(UseOnContext con) {
		Level lev = con.getLevel();
		Player pla = con.getPlayer();
		BlockPos pos = con.getClickedPos();
		ItemStack stack = con.getItemInHand();
		BlockState state = lev.getBlockState(pos);
		boolean isNoteBlock = state.getBlock() instanceof NoteBlock;
		int currentNote = stack.getTagElement("NoteTag") != null?stack.getTagElement("NoteTag").getInt("Note"):-1;
		boolean currentMode = stack.getTagElement("ModeTag") != null?stack.getTagElement("ModeTag").getBoolean("Mode"):false;
		if(isNoteBlock) {
			if(pla.isCrouching()) {
				currentNote = currentMode?currentNote:state.getValue(NoteBlock.NOTE);
			}
		}else {
			currentMode = !currentMode;
		}
		if(!pla.isCrouching()&&isNoteBlock) {
			return InteractionResult.FAIL;
		}else {
			if(!lev.isClientSide) {
				CompoundTag nct = new CompoundTag();
				CompoundTag mct = new CompoundTag();
				mct.putBoolean("Mode", currentMode);
				if(stack.getTagElement("NoteTag") != null)nct.putInt("Note", currentNote);
				stack.addTagElement("NoteTag", nct);
				stack.addTagElement("ModeTag", mct);
				if(isNoteBlock) {
					if(currentMode) {
						if(currentNote != -1) {
							lev.setBlockAndUpdate(pos, state.setValue(NoteBlock.NOTE, currentNote));
							lev.blockEvent(pos, state.getBlock(), 0, 0);
							pla.displayClientMessage(new TextComponent("Pasted note: "+this.getNote(currentNote)), true);
						}else {
							pla.displayClientMessage(new TextComponent("Please copy a note from a note block first."), true);
						}
					}else {
						lev.blockEvent(pos, state.getBlock(), 0, 0);
						pla.displayClientMessage(new TextComponent("Copied note: "+this.getNote(currentNote)), true);
					}
				}else {
					pla.displayClientMessage(new TextComponent("Set Mode: "+(currentMode?"pasting":"coping")), true);
				}
				return InteractionResult.CONSUME;
			}else {
				return InteractionResult.SUCCESS;
			}
		}
	}
	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getTagElement("ModeTag") != null?stack.getTagElement("ModeTag").getBoolean("Mode"):false;
	}
}
