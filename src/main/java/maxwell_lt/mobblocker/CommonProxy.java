package maxwell_lt.mobblocker;

import java.io.File;

import maxwell_lt.mobblocker.integration.MainCompatHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "mobblocker.cfg"));
        Config.readConfig();
        
        ModBlocks.init();

        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
    	if (config.hasChanged()) {
    		config.save();
    	}
    }
}

