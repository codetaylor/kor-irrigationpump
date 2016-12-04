package com.sudoplay.mc.korirrigationpump.module.irrigationpump;

import com.google.common.eventbus.Subscribe;
import com.sudoplay.mc.kor.spi.IKorModule;
import com.sudoplay.mc.kor.spi.event.internal.OnRegisterBlocksEvent;
import com.sudoplay.mc.kor.spi.event.internal.OnRegisterGuiHandlersEvent;
import com.sudoplay.mc.kor.spi.event.internal.OnRegisterNetworkPacketsEvent;
import com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump.BlockIrrigationPump;
import com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump.GuiHandlerIrrigationPump;
import com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump.PacketIrrigationPumpAction;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by codetaylor on 11/29/2016.
 */
public class ModuleIrrigationPump implements
    IKorModule {

  public static final String MODULE_ID = "irrigation_pump";

  @Override
  public String getKorModuleId() {
    return MODULE_ID;
  }

  @Subscribe
  public void onRegisterBlocksEvent(OnRegisterBlocksEvent event) {
    event.getRegistryService().register(

        BlockIrrigationPump.class
    );
  }

  @Subscribe
  public void onRegisterGuiHandlersEvent(OnRegisterGuiHandlersEvent event) {
    event.getGuiHandlerRegistry().register(

        GuiHandlerIrrigationPump.class
    );
  }

  @Subscribe
  public void onRegisterNetworkPacketsEvent(OnRegisterNetworkPacketsEvent event) {
    event.getPacketHandlerRegistry()

        .register(PacketIrrigationPumpAction.class, PacketIrrigationPumpAction.class, Side.CLIENT);
  }
}
