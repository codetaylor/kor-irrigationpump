package com.sudoplay.mc.korirrigationpump.util;

import com.sudoplay.mc.kor.core.generation.AssetGenerator;
import com.sudoplay.mc.kor.spi.util.FileUtils;
import com.sudoplay.mc.korirrigationpump.module.irrigationpump.ModuleIrrigationPump;

import java.io.File;
import java.io.IOException;

/**
 * Created by codetaylor on 11/24/2016.
 */
public class KorIrrigationPumpAssetGenerator {

  public static void main(String... args) {

    File assetRoot = new File("subprojects/kor-sprinkler/src/main/resources/assets/ctkorirrigationpump");

    try {
      FileUtils.delete(assetRoot, System.out::println);

    } catch (IOException e) {
      e.printStackTrace();
    }

    AssetGenerator generator = new AssetGenerator(
        "subprojects/kor-sprinkler/assets",
        "subprojects/kor-sprinkler/src/main/resources/assets/ctkorirrigationpump"
    );

    generator.generate(
        new ModuleIrrigationPump()
    );
  }
}
