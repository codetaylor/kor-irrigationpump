package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 11/30/2016.
 */
public class FluidTankIrrigationPump extends
    FluidTank {

  public interface IOnFluidTankContentsChangedEventListener {
    void onFluidTankContentsChanged(FluidTank fluidTank);
  }

  private List<IOnFluidTankContentsChangedEventListener> contentsChangedEventListenerList;

  public FluidTankIrrigationPump(int capacity) {
    super(capacity);
    this.contentsChangedEventListenerList = new ArrayList<>();
  }

  @Override
  public boolean canDrain() {
    return false;
  }

  @Override
  public boolean canDrainFluidType(FluidStack fluid) {
    return false;
  }

  @Override
  public boolean canFill() {
    return true;
  }

  @Override
  public boolean canFillFluidType(FluidStack fluid) {
    return fluid.getFluid() == FluidRegistry.WATER;
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {
    return super.fill(resource, doFill);
  }

  public void addEventListener(IOnFluidTankContentsChangedEventListener eventListener) {
    this.contentsChangedEventListenerList.add(eventListener);
  }

  @Override
  protected void onContentsChanged() {

    for (IOnFluidTankContentsChangedEventListener listener : this.contentsChangedEventListenerList) {
      listener.onFluidTankContentsChanged(this);
    }
  }
}
