package com.mtstream.shelve.init;

import com.mtstream.shelve.Shelve;
import com.mtstream.shelve.blockEntity.HumidityDetectorBlockEntity;
import com.mtstream.shelve.blockEntity.StaticDetectorBlockEntity;
import com.mtstream.shelve.blockEntity.TrashCanBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Shelve.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<HumidityDetectorBlockEntity>> HUMIDITY_DETECTOR = BLOCK_ENTITY
			.register("humidity_detector", ()->BlockEntityType.Builder.of(HumidityDetectorBlockEntity::new, BlockInit.HUMIDITY_DETECTOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<StaticDetectorBlockEntity>> STATIC_DETECTOR = BLOCK_ENTITY
			.register("static_detector", ()->BlockEntityType.Builder.of(StaticDetectorBlockEntity::new, BlockInit.STATIC_DETECTOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TrashCanBlockEntity>> TRASH_CAN = BLOCK_ENTITY
			.register("trash_can", ()->BlockEntityType.Builder.of(TrashCanBlockEntity::new, BlockInit.TRASH_CAN.get()).build(null));
	private BlockEntityInit() {
		
	}
}
