package maxwell_lt.mobblocker;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private static final String CATEGORY_GENERAL = "general";
	
	public static int ticksToLive = 72000;
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception exception) {
			MobBlocker.logger.log(Level.ERROR, "Problem loading config file!", exception);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        ticksToLive = cfg.getInt("ticksbeforedecay", CATEGORY_GENERAL, ticksToLive, -1, 4320000, "How long (in ticks) before the block decays. Set to -1 to disable decay.");
    }	
}
