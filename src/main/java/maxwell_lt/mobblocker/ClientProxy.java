package maxwell_lt.mobblocker;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Handles initialization for clients
 * Calls initialization for {@link CommonProxy}
 * Additionally initializes block models and other client-side-only graphical features
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        // Calls client-only initialization steps
        ModBlocks.initClient();
    }
}
