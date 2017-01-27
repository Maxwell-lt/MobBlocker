package maxwell_lt.mobblocker.integration;

import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {
    public static void registerTOP() {
        if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
    }

    public static void registerWaila() {
        if (Loader.isModLoaded("Waila") || Loader.isModLoaded("waila")) {
            WailaCompatibility.register();
        }
    }

}
