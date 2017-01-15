package maxwell_lt.mobblocker;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_PROTECTION = "protection";
	
	public static int ticksToLive = 72000;
	
	public static boolean enableMobProtection = true;
	public static boolean enableArrowProtection = true;
	public static boolean enablePotionProtection = true;
	public static boolean enableWolfProtection = false;
	public static boolean enableSlimeProtection = true;
	
	public static boolean giveNewPlayersProtector = true;
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
			initProtectionConfig(cfg);
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
        ticksToLive = cfg.getInt("ticksbeforedecay", CATEGORY_GENERAL, ticksToLive, -1, 4320000,
        		"How long (in ticks) before the block decays. Set to -1 to disable decay.");
        giveNewPlayersProtector = cfg.getBoolean("givenewplayerschunkprotector", CATEGORY_GENERAL, true,
        		"Set to true to give players a free Chunk Protector when first logging into a world.");
    }
	private static void initProtectionConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_PROTECTION, "Protection configuration");
		
		enableMobProtection = cfg.getBoolean("enablemobprotection", CATEGORY_PROTECTION, true,
        		"Set to false to disable teleporting hostile mobs out of the protected area.");
        enableArrowProtection = cfg.getBoolean("enablearrowprotection", CATEGORY_PROTECTION, true,
        			"Set to false to disable killing Skeleton arrows shot into the protected area.");
        enablePotionProtection = cfg.getBoolean("enablepotionprotection", CATEGORY_PROTECTION, true,
        			"Set to false to disable blocking Witch potions from entering the protected area.");
        enableWolfProtection = cfg.getBoolean("enablewolfprotection", CATEGORY_PROTECTION, false,
        			"Set to false to disable calming hostile wolves in the protected area.");
        enableSlimeProtection = cfg.getBoolean("enableslimeprotection", CATEGORY_PROTECTION, true,
        			"Set to false to disable teleporting slimes out of the protected area.");
	}
}
