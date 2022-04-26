package com.mtstream.shelve.init;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.mtstream.shelve.Shelve;
import com.mtstream.shelve.block.BearTrapBlock;
import com.mtstream.shelve.block.ChannelerBlock;
import com.mtstream.shelve.block.CheeseCakeBlock;
import com.mtstream.shelve.block.ChimneyBlock;
import com.mtstream.shelve.block.CreepshroomBlock;
import com.mtstream.shelve.block.CrystalBallBlock;
import com.mtstream.shelve.block.EggCartonBlock;
import com.mtstream.shelve.block.FireCrackerBlock;
import com.mtstream.shelve.block.HarvesterBlock;
import com.mtstream.shelve.block.HumidityDetectorBlock;
import com.mtstream.shelve.block.IgniterBlock;
import com.mtstream.shelve.block.InstantTntBlock;
import com.mtstream.shelve.block.MegaGlowBerryBlock;
import com.mtstream.shelve.block.MilkCauldron;
import com.mtstream.shelve.block.StaticDetectorBlock;
import com.mtstream.shelve.block.TrashCanBlock;
import com.mtstream.shelve.block.WaterCageBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Shelve.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
	
	
	public static final RegistryObject<Block> WATER_CAGE = register("water_cage",
			() -> new WaterCageBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).dynamicShape().sound(SoundType.LANTERN)
					.strength(2.8f)),
			object -> () -> new BlockItem(object.get(),new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> CHANNELER = register("channeler",
			() -> new ChannelerBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).dynamicShape().sound(SoundType.COPPER)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> IGNITER = register("igniter",
			() -> new IgniterBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).sound(SoundType.STONE)
					.requiresCorrectToolForDrops().strength(1.5F, 5.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> HARVESTER = register("harvester",
			() -> new HarvesterBlock(BlockBehaviour.Properties.copy(Blocks.PISTON).dynamicShape().sound(SoundType.STONE)
					.requiresCorrectToolForDrops().strength(1.5F, 5.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> HUMIDITY_DETECTOR = register("humidity_detector",
			() -> new HumidityDetectorBlock(BlockBehaviour.Properties.copy(Blocks.DAYLIGHT_DETECTOR).dynamicShape().sound(SoundType.STONE)
					.strength(1.5f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> STATIC_DETECTOR = register("static_detector",
			() -> new StaticDetectorBlock(BlockBehaviour.Properties.copy(Blocks.DAYLIGHT_DETECTOR).dynamicShape().sound(SoundType.COPPER)
					.strength(1.5f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
			
	public static final RegistryObject<Block> TRASH_CAN = register("trash_can",
			() -> new TrashCanBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).dynamicShape().sound(SoundType.LANTERN)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
			
	public static final RegistryObject<Block> MILK_CAULDRON = register("milk_cauldron",
			() -> new MilkCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON).dynamicShape().sound(SoundType.METAL)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties()));
	public static final RegistryObject<Block> CRYSTAL_BALL = register("crystal_ball",
			() -> new CrystalBallBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).dynamicShape().sound(SoundType.AMETHYST).lightLevel($->12)
					.strength(1.5f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Block> INSTANT_TNT = register("instant_tnt",
			() -> new InstantTntBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).instabreak().sound(SoundType.GRASS)), 
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	public static final RegistryObject<Block> FIRECRACKER = register("firecracker",
			() -> new FireCrackerBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).instabreak().dynamicShape().sound(SoundType.GRASS)), 
			object -> () -> new BlockItem(object.get(), new Item.Properties()));
	public static final RegistryObject<Block> CHEESE_CAKE = register("cheese_cake",
			() -> new CheeseCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).dynamicShape().sound(SoundType.WOOL)
					.strength(0.5f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
	public static final RegistryObject<Block> MEGA_GLOW_BERRY = register("mega_glow_berry",
			() -> new MegaGlowBerryBlock(BlockBehaviour.Properties.copy(Blocks.MELON).dynamicShape().sound(SoundType.SWEET_BERRY_BUSH).lightLevel($ -> 14)
					.strength(0.7f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
	public static final RegistryObject<Block> CREEPSHROOM = register("creepshroom",
			() -> new CreepshroomBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).dynamicShape().sound(SoundType.SWEET_BERRY_BUSH)
					.instabreak()),
			object -> () -> new BlockItem(object.get(), new Item.Properties()));
	public static final RegistryObject<Block> EGG_CARTON = register("egg_carton",
			() -> new EggCartonBlock(BlockBehaviour.Properties.of(Material.WOOL).dynamicShape().sound(SoundType.SWEET_BERRY_BUSH)
					.strength(0.5f)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Block> BRICKS_CHIMNEY = register("bricks_chimney",
			() -> new ChimneyBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS).dynamicShape().sound(SoundType.STONE)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> STONE_BRICKS_CHIMNEY = register("stone_bricks_chimney",
			() -> new ChimneyBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).dynamicShape().sound(SoundType.STONE)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> BEAR_TRAP = register("bear_trap",
			() -> new BearTrapBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).dynamicShape().sound(SoundType.LANTERN)
					.requiresCorrectToolForDrops().strength(2.0F, 6.0F)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	

	private static <T extends Block> RegistryObject<T> registerBlock(final String name,final Supplier<? extends T> block){
		return BLOCKS.register(name, block);
	}		
	
	private static <T extends Block> RegistryObject<T> register(final String name,final Supplier<? extends T> block,
			Function<RegistryObject<T>, Supplier<? extends Item>> item){
		RegistryObject<T> obj = registerBlock(name,block);
		ITEMS.register(name,item.apply(obj));
		return obj;
	}
}
