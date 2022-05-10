package com.mtstream.shelve.util;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ProjectileShooter {
	public SmallFireball ShootfireBall(Level lev,Vec3 from,Vec3 to,@Nullable Player pla) {
		ClipContext clc = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, pla);
		BlockHitResult res = lev.clip(clc);
		double dt = Math.sqrt(Math.sqrt(pla.distanceToSqr(to))) * 0.5D;
		double xv = res.getBlockPos().getX() - from.x;
		double yv = res.getBlockPos().getY() - from.y;
		double zv = res.getBlockPos().getZ() - from.z;
		return new SmallFireball(lev, pla, xv*dt, yv, zv*dt);
	}
}
