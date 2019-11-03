package maxwell_lt.mobblocker.integration;

import net.minecraftforge.fml.ModList;

public class TOPCompatHandler {
    public static void registerTOP() {
        if (ModList.get().isLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
    }

}