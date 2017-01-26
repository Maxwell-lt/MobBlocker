package maxwell_lt.mobblocker;

import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {
    public static void registerTOP() {
        if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
    }

}
