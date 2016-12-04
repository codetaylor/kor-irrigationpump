package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import com.sudoplay.mc.kor.core.network.IPacketService;
import com.sudoplay.mc.kor.spi.Kor;
import com.sudoplay.mc.kor.spi.util.RNGUtils;
import com.sudoplay.mc.korirrigationpump.KorMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 11/29/2016.
 */
public class TileEntityIrrigationPump extends
    TileEntity implements
    IEnergyReceiver,
    ITickable {

  private static final int BLOCK_FARMLAND_MAX_MOISTURE = 7;

  private IParticleSpawner particleSpawner;
  private EnergyStorage energyStorage;
  private FluidTankIrrigationPump fluidTank;
  private boolean clientUpdateRequired;
  private int tickCount;
  private int powerCostPerTick = 100;
  private int fluidCostPerTick = 40;
  private int range = 4;
  private int delayModifier = 10;

  public TileEntityIrrigationPump() {
    this.energyStorage = new EnergyStorage(32000);
    this.energyStorage.setMaxReceive(this.powerCostPerTick * 8);
    this.fluidTank = new FluidTankIrrigationPump(4000);
    this.fluidTank.addEventListener(tank -> this.clientUpdateRequired = true);
    this.particleSpawner = ParticleSpawner.INSTANCE;
  }

  // -[ Misc ]---------------------------------------------

  public boolean isUseableByPlayer(EntityPlayer entityPlayer) {

    if (this.worldObj.getTileEntity(this.pos) != this) {
      return false;
    }

    double offset = 0.5;
    double maxDistanceSq = 8.0 * 8.0;

    return entityPlayer.getDistanceSq(
        this.pos.getX() + offset,
        this.pos.getY() + offset,
        this.pos.getZ() + offset
    ) < maxDistanceSq;
  }

  public FluidTank getFluidTank() {
    return this.fluidTank;
  }

  public int getFluidCostPerTick() {
    return this.fluidCostPerTick;
  }

  public int getPowerCostPerTick() {
    return this.powerCostPerTick;
  }

  @Nullable
  @Override
  public ITextComponent getDisplayName() {
    return new TextComponentTranslation("tile." + BlockIrrigationPump.NAME + ".name");
  }

  // -[ Capability ]---------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }

  @Nonnull
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.fluidTank;
    }

    return super.getCapability(capability, facing);
  }

  // -[ CoFH Energy ]--------------------------------------

  @Override
  public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
    int energy = this.energyStorage.receiveEnergy(maxReceive, simulate);

    if (!simulate && energy > 0) {
      this.clientUpdateRequired = true;
    }

    return energy;
  }

  @Override
  public int getEnergyStored(EnumFacing from) {
    return this.energyStorage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored(EnumFacing from) {
    return this.energyStorage.getMaxEnergyStored();
  }

  @Override
  public boolean canConnectEnergy(EnumFacing from) {
    return true;
  }

  // -[ Network ]------------------------------------------

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();
    this.writeToNBT(nbtTagCompound);
    int metadata = this.getBlockMetadata();
    return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();
    this.writeToNBT(nbtTagCompound);
    return nbtTagCompound;
  }

  @Override
  public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
    this.readFromNBT(tag);
  }

  // -[ NBT ]----------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    this.energyStorage.readFromNBT(compound.getCompoundTag("energyStorage"));
    this.fluidTank.readFromNBT(compound.getCompoundTag("fluidTank"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);

    compound.setTag("energyStorage", this.energyStorage.writeToNBT(new NBTTagCompound()));
    compound.setTag("fluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));

    return compound;
  }

  // -[ On Tick ]------------------------------------------

  @Override
  public void update() {

    if (this.worldObj.isRemote) {
      doClientUpdate();
      return;
    }

    boolean active = doOperationalCost(1);

    this.tickCount += 1;
    int tickWorkInterval = 20;

    if (this.tickCount >= tickWorkInterval) {
      this.tickCount = 0;

      if (active) {
        doWork(tickWorkInterval);
      }
    }

    if (this.clientUpdateRequired) {
      this.clientUpdateRequired = false;

      IBlockState blockState = this.worldObj.getBlockState(this.getPos());
      this.worldObj.notifyBlockUpdate(this.getPos(), blockState, blockState, 3);
      this.markDirty();
    }
  }

  private void doClientUpdate() {
    //

  }

  private void doWork(int tickCount) {
    int waterBlockCount = Math.max(1, tickCount >> 1);
    List<BlockPos> blockPosList = new ArrayList<>(waterBlockCount);

    for (int i = 0; i < waterBlockCount; i++) {
      BlockPos blockPos = this.waterBlockInRange(this.worldObj, this.pos);
      blockPosList.add(blockPos);
    }

    // network
    IPacketService packetService = Kor.getMod(KorMod.MOD_ID).getPacketService();
    packetService.sendToAllAround(new PacketIrrigationPumpAction(this, blockPosList), this);
  }

  private boolean doOperationalCost(int tickCount) {
    int powerCost = this.powerCostPerTick * tickCount;
    int fluidCost = this.fluidCostPerTick * tickCount;
    int energyStored = this.energyStorage.getEnergyStored();
    int fluidStored = this.fluidTank.getFluidAmount();

    boolean active = energyStored >= powerCost
        && fluidStored >= fluidCost;

    if (active) {
      this.energyStorage.setEnergyStored(energyStored - powerCost);
      this.fluidTank.drainInternal(fluidCost, true);
      this.clientUpdateRequired = true;
    }

    return active;
  }

  private BlockPos waterBlockInRange(
      World world,
      BlockPos pos
  ) {
    int x = pos.getX() + RNGUtils.RANDOM.nextInt(this.range * 2 + 1) - this.range;
    int z = pos.getZ() + RNGUtils.RANDOM.nextInt(this.range * 2 + 1) - this.range;

    pos = new BlockPos(x, pos.getY() + 1, z);

    _waterBlockInRange(world, pos);

    return pos;
  }

  private void _waterBlockInRange(
      World world,
      BlockPos pos
  ) {

    int limit = pos.getY() - 2;

    while (pos.getY() >= limit) {
      IBlockState blockState = world.getBlockState(pos);
      Block block = blockState.getBlock();

      // skip air blocks
      if (!world.isAirBlock(pos)) {
        waterBlock(world, pos, blockState, block);
      }
      pos = pos.down();
    }
  }

  private void waterBlock(
      World world,
      BlockPos pos,
      IBlockState blockState,
      Block block
  ) {
    int blockUpdateDelay;

    // put out fire
    if (block == Blocks.FIRE) {
      world.setBlockToAir(pos);
    }

    // moisturize farmland
    if (block == Blocks.FARMLAND
        && blockState.getValue(BlockFarmland.MOISTURE) < BLOCK_FARMLAND_MAX_MOISTURE) {
      world.setBlockState(pos, block.getDefaultState().withProperty(BlockFarmland.MOISTURE, BLOCK_FARMLAND_MAX_MOISTURE), 3);
    }

    blockUpdateDelay = getBlockUpdateDelay(blockState, block);

    if (blockUpdateDelay > 0 && block.getTickRandomly()) {
      world.scheduleBlockUpdate(pos, block, RNGUtils.RANDOM.nextInt(blockUpdateDelay), 0);
    }
  }

  private int getBlockUpdateDelay(IBlockState blockState, Block block) {
    int delay = -1;

    if (block == Blocks.GRASS) {
      delay = this.delayModifier;

    } else if (block == Blocks.MYCELIUM) {
      delay = this.delayModifier;

    } else if (block == Blocks.WHEAT) {
      delay = (int) (2.0f * this.delayModifier);

    } else if (block instanceof BlockSapling) {
      delay = (int) (2.5f * this.delayModifier);

    } else if (block instanceof IPlantable
        || block instanceof IGrowable) {
      delay = (int) (2.0f * this.delayModifier);

    } else if (block.getMaterial(blockState) == Material.GRASS) {
      delay = this.delayModifier;
    }
    return delay;
  }

  public void onMessage(IMessage message, MessageContext ctx) {

    if (message instanceof PacketIrrigationPumpAction) {

      for (BlockPos blockPos : ((PacketIrrigationPumpAction) message).getBlockPosList()) {
        this.particleSpawner.spawnParticles(this.worldObj, blockPos);
      }
    }
  }
}
