package com.sudoplay.mc.korirrigationpump;

import com.sudoplay.mc.kor.spi.Kor;
import com.sudoplay.mc.korirrigationpump.module.irrigationpump.ModuleIrrigationPump;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by codetaylor on 11/29/2016.
 */
@Mod(
    modid = KorMod.MOD_ID,
    version = KorMod.VERSION,
    name = KorMod.NAME
)
public class KorMod extends
    Kor {
  public static final String MOD_ID = "ctkorirrigationpump";
  public static final String VERSION = "snapshot";
  public static final String NAME = "CTKor Irrigation Pump";
  public static final double JSON_CONFIGS_VERSION = 1.0;

  @SuppressWarnings("unused")
  @Mod.Instance
  public static KorMod INSTANCE;

  @Override
  public double getJsonConfigsVersion() {
    return KorMod.JSON_CONFIGS_VERSION;
  }

  @Override
  public String getModId() {
    return KorMod.MOD_ID;
  }

  @Override
  public String getModVersion() {
    return KorMod.VERSION;
  }

  @Override
  public String getModName() {
    return KorMod.NAME;
  }

  @Mod.EventHandler
  protected void onPreInitialization(FMLPreInitializationEvent event) {

    this.registerModules(
        // Register modules here
        new ModuleIrrigationPump()
    );

    super.onPreInitialization(event);
  }

  @Mod.EventHandler
  protected void onInitialization(FMLInitializationEvent event) {
    super.onInitialization(event);
  }

  @Mod.EventHandler
  protected void onPostInitialization(FMLPostInitializationEvent event) {
    super.onPostInitialization(event);
  }
}
