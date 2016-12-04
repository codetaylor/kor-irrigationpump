package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;

/**
 * Created by codetaylor on 12/1/2016.
 */
public class ContainerIrrigationPump extends
    Container {

  private TileEntityIrrigationPump tileEntityIrrigationPump;

  public ContainerIrrigationPump(InventoryPlayer inventoryPlayer, TileEntityIrrigationPump tileEntityIrrigationPump) {
    this.tileEntityIrrigationPump = tileEntityIrrigationPump;

    int hotbarSlotCount = 9;
    int hotbarXPos = 8;
    int hotbarYPos = 142;
    int slotXSpacing = 18;
    int slotYSpacing = 18;
    int playerInventoryRowCount = 3;
    int playerInventoryColumnCount = 9;

    // player's hotbar
    for (int x = 0; x < hotbarSlotCount; x++) {
      this.addSlotToContainer(new Slot(inventoryPlayer, x, hotbarXPos + slotXSpacing * x, hotbarYPos));
    }

    // player's inventory
    for (int y = 0; y < playerInventoryRowCount; y++) {

      for (int x = 0; x < playerInventoryColumnCount; x++) {
        int slotNumber = hotbarSlotCount + y * playerInventoryColumnCount + x;
        int xPos = 8 + x * slotXSpacing;
        int yPos = 84 + y * slotYSpacing;
        this.addSlotToContainer(new Slot(inventoryPlayer, slotNumber, xPos, yPos));
      }
    }
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
    return this.tileEntityIrrigationPump.isUseableByPlayer(player);
  }
}
