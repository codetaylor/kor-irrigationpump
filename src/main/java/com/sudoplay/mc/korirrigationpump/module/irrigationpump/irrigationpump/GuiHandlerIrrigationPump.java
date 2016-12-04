package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import com.sudoplay.mc.kor.spi.gui.KorGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by codetaylor on 12/1/2016.
 */
public class GuiHandlerIrrigationPump extends
    KorGuiHandler {

  public static final int ID = 0;

  @Override
  public int getId() {
    return GuiHandlerIrrigationPump.ID;
  }

  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

    if (id != GuiHandlerIrrigationPump.ID) {
      // TODO
    }

    BlockPos blockPos = new BlockPos(x, y, z);
    TileEntity tileEntity = world.getTileEntity(blockPos);

    if (tileEntity instanceof TileEntityIrrigationPump) {
      return new ContainerIrrigationPump(player.inventory, (TileEntityIrrigationPump) tileEntity);
    }

    return null;
  }

  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

    if (id != GuiHandlerIrrigationPump.ID) {
      // TODO
    }

    BlockPos blockPos = new BlockPos(x, y, z);
    TileEntity tileEntity = world.getTileEntity(blockPos);

    if (tileEntity instanceof TileEntityIrrigationPump) {
      return new GuiIrrigationPump(player.inventory, (TileEntityIrrigationPump) tileEntity);
    }

    return null;
  }
}
