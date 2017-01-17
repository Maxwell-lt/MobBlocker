package maxwell_lt.mobblocker;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MobBlocker.MODID, name = MobBlocker.MODNAME, version = MobBlocker.MODVERSION, useMetadata = true, acceptedMinecraftVersions = "[1.10,1.11.2]")
public class MobBlocker {

    public static final String MODID = "mobblocker";
    public static final String MODNAME = "Mob Blocker";
    public static final String MODVERSION = "1.2.0";

    @SidedProxy(clientSide = "maxwell_lt.mobblocker.ClientProxy", serverSide = "maxwell_lt.mobblocker.ServerProxy")
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
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
