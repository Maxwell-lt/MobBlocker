package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point to the mod
 * Manages mod metadata
 * Sets up proxies
 * Initializes logger
 * Handles preinit, init, and postinit steps
 */
@Mod(MobBlocker.MODID)
public class MobBlocker {

    public static final String MODID = "mobblocker";
    public static final String MCVERSIONS = "[1.12, 1.12.2]";
    public static final String UPDATEJSON = "https://raw.githubusercontent.com/Maxwell-lt/MobBlocker/master/update.json";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static CommonProxy proxy;

    @Mod.Instance
    public static MobBlocker instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        MinecraftForge.EVENT_BUS.register(new LoginEventHandler());
        // ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
