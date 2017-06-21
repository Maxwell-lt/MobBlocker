package maxwell_lt.mobblocker;

import java.io.File;

import maxwell_lt.mobblocker.integration.MainCompatHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Handles initialization steps common to both clients and servers
 * Usually not called directly, instead is inherited by ClientProxy and ServerProxy and called as super()
 * Handles:
 * <ul>
 *     <li>Configuration reading and writing</li>
 *     <li>Block initialization calls</li>
 *     <li>TOP/Waila compatibility loading</li>
 * </ul>
 */
public class CommonProxy {
    public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
	    // Read configuration file
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "mobblocker.cfg"));
        Config.readConfig();

        // Initializes blocks
        ModBlocks.init();

        // Initialized TOP and Waila compatibility
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
	    // If creating initial config file or repairing existing, writes modified file to disk now
    	if (config.hasChanged()) {
    		config.save();
    	}
    }
}

