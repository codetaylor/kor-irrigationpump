package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import com.sudoplay.mc.kor.core.generation.annotation.*;
import com.sudoplay.mc.kor.spi.Kor;
import com.sudoplay.mc.kor.spi.block.IKorTileEntityProvider;
import com.sudoplay.mc.kor.spi.block.KorBlock;
import com.sudoplay.mc.kor.spi.registry.injection.KorInject;
import com.sudoplay.mc.korirrigationpump.KorMod;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by codetaylor on 11/29/2016.
 */

@KorGenerateBlockAssets(name = "irrigation_pump", modId = KorMod.MOD_ID)

@KorGenerateLangEntries(entries = {
    @KorLangEntry(key = "tile." + BlockIrrigationPump.NAME + ".name", value = "Irrigation Pump")
})

@KorGenerateImageSlices(slices = {
    @KorImageSliceEntry(col = 1, row = 1, target = "blocks/irrigation_pump", source = "KorIrrigationPump.png"),
})

@KorGenerateFileCopy(toCopy = {
    @KorFileCopyEntry(source = "KorIrrigationPumpGui.png", destination = "textures/gui/irrigation_pump.png")
})

public class BlockIrrigationPump extends
    KorBlock implements
    IKorTileEntityProvider {

  public static final String NAME = "irrigation_pump";
  private Kor kor;

  @KorInject
  public BlockIrrigationPump(
      Kor kor
  ) {
    super(
        KorMod.MOD_ID,
        NAME,
        Material.WATER
    );
    this.kor = kor;
    this.setHardness(2.0f);
    this.setSoundType(SoundType.METAL);
  }

  @Override
  public boolean isReplaceable(IBlockAccess worldIn, @Nonnull BlockPos pos) {
    return false;
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return true;
  }

  @Nonnull
  @Override
  public EnumPushReaction getMobilityFlag(IBlockState state) {
    return EnumPushReaction.NORMAL;
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileEntityIrrigationPump();
  }

  @Override
  public Class<? extends TileEntity> getTileEntityClass() {
    return TileEntityIrrigationPump.class;
  }

  @Override
  public String getTileEntityName() {
    return NAME;
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileEntityIrrigationPump) {

      if (worldIn.isRemote) {
        return true;
      }

      playerIn.openGui(KorMod.INSTANCE, GuiHandlerIrrigationPump.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }
}
