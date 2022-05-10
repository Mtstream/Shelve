package com.mtstream.shelve;

import net.minecraft.world.damagesource.DamageSource;

public class ShelveDamageSource extends DamageSource{
	
	public static final DamageSource SHOCK = new DamageSource("elec_shock");
	
	public ShelveDamageSource(String msid) {
		super(msid);
	}
	
}
