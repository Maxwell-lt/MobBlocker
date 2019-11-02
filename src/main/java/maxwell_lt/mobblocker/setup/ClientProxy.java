package maxwell_lt.mobblocker.setup;

import maxwell_lt.mobblocker.ModBlocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles initialization for clients
 * Calls initialization for {@link IProxy}
 * Additionally initializes block models and other client-side-only graphical features
 */
@Mod.EventBusSubscriber
public class ClientProxy implements IProxy {

    @Override
    public void init() {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
    }
}
