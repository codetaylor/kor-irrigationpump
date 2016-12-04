package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

/* package */ class ParticleSpawner implements
    IParticleSpawner {

  public static final IParticleSpawner INSTANCE = new ParticleSpawner();

  @Override
  public void spawnParticles(
      World world,
      BlockPos blockPos
  ) {
    BlockPos pos;

    pos = this.getParticleSpawnBlockPos(world, blockPos);

    if (pos == null) {
      return;
    }

    for (int i = 0; i < 8; i++) {
      world.spawnParticle(EnumParticleTypes.WATER_SPLASH, blockPos.getX() + 0.5f, pos.getY(), blockPos.getZ() + 0.5f, 0.0, 0.0, 0.0);
    }
  }

  /**
   * Returns suitable spawn position for the water particles by checking blocks in a column.
   * Will return null if no suitable spawn position is found.
   *
   * @param world world
   * @param x     x
   * @param y     y
   * @param z     z
   * @return spawn position or null
   */
  @Nullable
  private BlockPos getParticleSpawnBlockPos(World world, BlockPos blockPos) {
    BlockPos pos = new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
    BlockPos nextPos;

    for (int y2 = pos.getY(); y2 >= blockPos.getY() - 1; y2--) {
      nextPos = pos.down();

      if (this.canSpawnParticlesAtBlockPos(world, pos, nextPos)) {
        return pos;
      }
      pos = nextPos;
    }

    return null;
  }

  /**
   * Uses a block and the block below it to evaluate if pos is a suitable particle spawn position.
   *
   * @param world   world
   * @param pos     candidate pos
   * @param nextPos pos below pos
   * @return true if suitable particle spawn location
   */
  private boolean canSpawnParticlesAtBlockPos(World world, BlockPos pos, BlockPos nextPos) {
    Block block = world.getBlockState(pos).getBlock();
    Block nextBlock = world.getBlockState(nextPos).getBlock();

    if (world.isAirBlock(pos)
        && !world.isAirBlock(nextPos)) {

      if (world.isBlockNormalCube(nextPos, true)
          || nextBlock == Blocks.FARMLAND) {
        return true;
      }
    }

    if (block instanceof IGrowable
        || block instanceof IPlantable) {

      if (world.isBlockNormalCube(nextPos, true)
          || nextBlock == Blocks.FARMLAND) {
        return true;
      }
    }

    return false;
  }

}