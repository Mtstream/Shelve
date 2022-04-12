package com.mtstream.shelve.loot;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mtstream.shelve.Shelve;

import net.minecraft.resources.ResourceLocation;

public class ShelveLootTable {
	private static final Set<ResourceLocation> LOC = Sets.newHashSet();
	public static final ResourceLocation MYSTERIOUS_TRASH = register("box/mysterious_trash");
	public static final ResourceLocation MYSTERIOUS_TRESURE = register("box/mysterious_tresure");
	public static final ResourceLocation INFERNAL_TRASH = register("box/infernal_trash");
	public static final ResourceLocation INFERNAL_TRESURE = register("box/infernal_tresure");
	public static final ResourceLocation MARINE_TRASH = register("box/marine_trash");
	public static final ResourceLocation MARINE_TRESURE = register("box/marine_tresure");
	private static final Set<ResourceLocation> LOC_ = Collections.unmodifiableSet(LOC);
	
	public static ResourceLocation register(String string) {
		return register(new ResourceLocation(Shelve.MOD_ID,string));
	}
	public static ResourceLocation register(ResourceLocation location) {
		return location;
	}
	public static Set<ResourceLocation> all(){
		return LOC_;
	}
}
