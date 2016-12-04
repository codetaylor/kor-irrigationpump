package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import com.sudoplay.mc.kor.spi.util.GuiUtils;
import com.sudoplay.mc.korirrigationpump.KorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 12/1/2016.
 */
public class GuiIrrigationPump extends
    GuiContainer {

  private static final ResourceLocation TEXTURE = new ResourceLocation(KorMod.MOD_ID, "textures/gui/irrigation_pump.png");

  private InventoryPlayer inventoryPlayer;
  private TileEntityIrrigationPump tileEntityIrrigationPump;
  private List<String> tempList;

  public static final int GUI_LEFT_OFFSET = 8;
  public static final int GUI_TOP_OFFSET = 16;
  public static final int STORAGE_BAR_HEIGHT = 53;
  public static final int STORAGE_BAR_SPACING = 18;
  public static final int STORAGE_BAR_WIDTH = 16;

  public GuiIrrigationPump(InventoryPlayer inventoryPlayer, TileEntityIrrigationPump tileEntityIrrigationPump) {
    super(new ContainerIrrigationPump(inventoryPlayer, tileEntityIrrigationPump));
    this.inventoryPlayer = inventoryPlayer;
    this.tileEntityIrrigationPump = tileEntityIrrigationPump;
    this.tempList = new ArrayList<>();

    this.xSize = 176;
    this.ySize = 166;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    int maxEnergyStored = this.tileEntityIrrigationPump.getMaxEnergyStored(null);
    int energyStored = this.tileEntityIrrigationPump.getEnergyStored(null);

    if (energyStored > 0) {
      // draw the energy
      float fill = (float) energyStored * STORAGE_BAR_HEIGHT / (float) maxEnergyStored;
      this.drawTexturedModalRect(this.guiLeft + GUI_LEFT_OFFSET, this.guiTop + GUI_TOP_OFFSET + STORAGE_BAR_HEIGHT - Math.round(fill), this.xSize, 0, STORAGE_BAR_WIDTH, Math.round(fill));
    }

    FluidTank fluidTank = this.tileEntityIrrigationPump.getFluidTank();
    FluidStack fluid = fluidTank.getFluid();

    if (fluid != null && fluid.amount > 0) {
      // draw the fluid
      ResourceLocation location = fluid.getFluid().getStill();
      TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(location.toString());
      Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

      int xCoord = this.guiLeft + GUI_LEFT_OFFSET + STORAGE_BAR_SPACING;
      int fillHeight = Math.round((float) fluid.amount * STORAGE_BAR_HEIGHT / (float) fluidTank.getCapacity());
      int fullSprites = fillHeight / 16;

      for (int i = 0; i < fullSprites; i++) {
        int yCoord = this.guiTop + GUI_TOP_OFFSET + STORAGE_BAR_HEIGHT - (16 * (i + 1));
        this.drawTexturedModalRect(xCoord, yCoord, sprite, STORAGE_BAR_WIDTH, 16);
      }

      int partialHeight = fillHeight % 16;
      int yCoord = this.guiTop + GUI_TOP_OFFSET + STORAGE_BAR_HEIGHT - (16 * fullSprites + partialHeight);
      GuiUtils.drawPartialTexturedModalRect(xCoord, yCoord, this.zLevel, sprite, STORAGE_BAR_WIDTH, partialHeight);

      // draw the fluid lines
      Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
      yCoord = this.guiTop + GUI_TOP_OFFSET;
      int textureX = this.xSize + STORAGE_BAR_WIDTH;
      this.drawTexturedModalRect(xCoord, yCoord, textureX, 0, STORAGE_BAR_WIDTH, STORAGE_BAR_HEIGHT);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    int xPos = 8;
    int yPos = 5;
    ITextComponent displayName = this.tileEntityIrrigationPump.getDisplayName();

    if (displayName != null) {
      this.fontRendererObj
          .drawString(
              displayName.getUnformattedText(),
              xPos,
              yPos,
              Color.darkGray.getRGB()
          );
    }

    displayName = this.inventoryPlayer.getDisplayName();
    yPos = 73;

    this.fontRendererObj
        .drawString(
            displayName.getUnformattedText(),
            xPos,
            yPos,
            Color.darkGray.getRGB()
        );

    renderPowerTooltip(mouseX, mouseY);
    renderWaterTooltip(mouseX, mouseY);
  }

  private void renderPowerTooltip(int mouseX, int mouseY) {

    if (mouseX >= this.guiLeft + GUI_LEFT_OFFSET
        && mouseX <= this.guiLeft + GUI_LEFT_OFFSET + STORAGE_BAR_WIDTH
        && mouseY >= this.guiTop + GUI_TOP_OFFSET
        && mouseY <= this.guiTop + GUI_TOP_OFFSET + STORAGE_BAR_HEIGHT) {

      this.tempList.clear();
      this.tempList.add("Base Use: " + this.tileEntityIrrigationPump.getPowerCostPerTick() + " RF/t");
      this.tempList.add(this.tileEntityIrrigationPump.getEnergyStored(null) + "/" + this.tileEntityIrrigationPump.getMaxEnergyStored(null) + " RF");
      this.drawHoveringText(this.tempList, mouseX - this.guiLeft, mouseY - this.guiTop);
    }
  }

  private void renderWaterTooltip(int mouseX, int mouseY) {

    if (mouseX >= this.guiLeft + GUI_LEFT_OFFSET + STORAGE_BAR_WIDTH
        && mouseX <= this.guiLeft + GUI_LEFT_OFFSET + STORAGE_BAR_WIDTH * 2
        && mouseY >= this.guiTop + GUI_TOP_OFFSET
        && mouseY <= this.guiTop + GUI_TOP_OFFSET + STORAGE_BAR_HEIGHT) {

      FluidTank fluidTank = this.tileEntityIrrigationPump.getFluidTank();

      this.tempList.clear();
      this.tempList.add("Base Use: " + this.tileEntityIrrigationPump.getFluidCostPerTick() + " mB/t");
      this.tempList.add(fluidTank.getFluidAmount() + "/" + fluidTank.getCapacity() + " mB");
      this.drawHoveringText(this.tempList, mouseX - this.guiLeft, mouseY - this.guiTop);
    }
  }

}
