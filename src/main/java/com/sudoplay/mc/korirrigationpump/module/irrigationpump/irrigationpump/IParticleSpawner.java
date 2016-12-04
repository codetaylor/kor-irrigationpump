package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by codetaylor on 11/27/2016.
 */
/* package */ interface IParticleSpawner {

  IParticleSpawner NO_OP = (world, blockPos) -> {
    //
  };

  void spawnParticles(
      World world,
      BlockPos blockPos);
}
