package com.mtstream.shelve.block;

import com.mtstream.shelve.Shelve;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class RespawnPendulumBlock extends Block{
	
	public static final IntegerProperty CHARGING = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;

	public RespawnPendulumBlock(Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(CHARGING, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(CHARGING);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level lev, BlockPos pos, Player pla,
			InteractionHand han, BlockHitResult res) {
		ItemStack stack = pla.getItemInHand(han);
		if(stack.is(Items.OBSIDIAN)) {
			if(state.getValue(CHARGING)<4) {
				if(!lev.isClientSide) {
					ServerPlayer serverPla = (ServerPlayer)pla;
					lev.setBlockAndUpdate(pos, state.setValue(CHARGING, state.getValue(CHARGING)+1));
					if(this.canSetSpawn(lev)&&serverPla.getRespawnDimension()!=lev.dimension()||serverPla.getRespawnPosition()!=pos)serverPla.setRespawnPosition(Level.END, pos, 0.0f, false, true);
					lev.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
					return InteractionResult.CONSUME;
				}else {
					return InteractionResult.SUCCESS;
				}
			}else {
				if(this.canSetSpawn(lev)) {
					return InteractionResult.PASS;
				}else {
					if(!lev.isClientSide) {
						return InteractionResult.CONSUME;
					}else {
						return InteractionResult.SUCCESS;
					}
				}
			}
		}else {
			return InteractionResult.PASS;
		}
	}
	
	public boolean canSetSpawn(Level lev) {
		return !lev.dimensionType().bedWorks()&&!lev.dimensionType().respawnAnchorWorks();
	}
//	@Mod.EventBusSubscriber(modid = Shelve.MOD_ID)
//	static class RespawnEvent {
//		@SubscribeEvent
//		public static void respawnEvent(PlayerEvent.PlayerRespawnEvent evt) {
//			Level lev = evt.getPlayer().getLevel();
//			if(!lev.isClientSide) {
//				BlockPos respawnPos = ((ServerPlayer)evt.getPlayer()).getRespawnPosition();
//				Player pla = evt.getPlayer();
//				BlockState state = lev.getBlockState(respawnPos);
//				if(state.getBlock() instanceof RespawnPendulumBlock) {
//					if(state.getValue(CHARGING)>0) {
//						if(state.getValue(CHARGING)==1) {
//							((ServerPlayer)pla).setRespawnPosition(Level.END, null, 0.0f, false, false);
//						}
//					lev.setBlockAndUpdate(respawnPos, state.setValue(CHARGING, state.getValue(CHARGING)-1));
//					}
//				}
//			}
//		}
//	}
}