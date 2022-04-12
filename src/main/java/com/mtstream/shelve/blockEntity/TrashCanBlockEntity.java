package com.mtstream.shelve.blockEntity;

import com.mtstream.shelve.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TrashCanBlockEntity extends BlockEntity {
	
	protected final ItemStackHandler inv;
	protected LazyOptional<ItemStackHandler> handler;
	protected int timer;
	protected boolean upd;
	
	public TrashCanBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.TRASH_CAN.get(), pos, state);
		this.inv = createInventory();
		this.handler = LazyOptional.of(()->this.inv);
	}
	private ItemStackHandler createInventory() {
		return new ItemStackHandler(1) {
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				TrashCanBlockEntity.this.update();
				return super.extractItem(slot, amount, simulate);
			}
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				TrashCanBlockEntity.this.update();
				this.stacks.set(slot, ItemStack.EMPTY);
				return super.insertItem(slot, stack, simulate);
			}
		};
	}
	public void tick() {
		this.timer++;
		if(this.upd && this.level != null) {
			update();
			this.upd = false;
		}
	}
	public void update() {
		requestModelDataUpdate();
		setChanged();
		if(this.level != null) {
			this.level.setBlockAndUpdate(worldPosition, getBlockState());
		}
	}
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.handler.cast() : super.getCapability(cap, side);
	}
	public ItemStack extractItemStack() {
		this.upd = true;
		return this.handler.map(invt -> invt.extractItem(0, getFirstItem().getCount(), false)).orElse(ItemStack.EMPTY);
	}
	public ItemStack insertItem(ItemStack stack,int slot) {
		stack.shrink(stack.copy().getCount());
		this.upd = true;
		return this.handler.map(invt -> invt.insertItem(slot, stack.copy(), false)).orElse(ItemStack.EMPTY);
	}
	public ItemStack getFirstItem() {
		return this.handler.map(invt -> invt.getStackInSlot(0)).orElse(ItemStack.EMPTY);
	}
	public LazyOptional<ItemStackHandler> getHandler() {
		return handler;
	}
	public ItemStackHandler getInv() {
		return inv;
	}
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	@Override
	public CompoundTag getUpdateTag() {
		return serializeNBT();
	}
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		load(tag);
	}
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.handler.invalidate();
	}
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("trash", this.inv.serializeNBT());
	}
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.inv.deserializeNBT(tag.getCompound("trash"));
	}
	public void intract(BlockPos pos,Level lev,ItemStack stack,Player pla,InteractionHand han) {
		ItemStack stackO = getFirstItem().copy();
		ItemStack stackI = stack.copy();
		if(!stack.isEmpty()) {
			this.inv.setStackInSlot(0, stackI);
			if(!lev.isClientSide) {
				pla.setItemInHand(han, ItemStack.EMPTY);
				lev.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7f, 1.0f);
			}
		} else {	
			if(!lev.isClientSide) {
				pla.setItemInHand(han, stackO);
				lev.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7f, 1.0f);
			}
			this.inv.setStackInSlot(0, ItemStack.EMPTY);
		}
	}
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getTag());
	}
}
