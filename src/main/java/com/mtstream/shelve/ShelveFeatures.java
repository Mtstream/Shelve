package com.mtstream.shelve;

import com.mtstream.shelve.init.BlockInit;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class ShelveFeatures extends OreFeatures{
	public static final RuleTest END_ORE_REPLACEMENT = new BlockMatchTest(Blocks.END_STONE);
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_FRAMED_ENDSTONE = FeatureUtils.register("framed_end_stone", Feature.SCATTERED_ORE, new OreConfiguration(END_ORE_REPLACEMENT, BlockInit.FRAMED_END_STONE.get().defaultBlockState(), 2, 1.0F));
}
