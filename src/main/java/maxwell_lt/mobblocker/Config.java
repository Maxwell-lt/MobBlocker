package maxwell_lt.mobblocker;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

/**
 * Handles configuration file parsing
 * Configuration options are accessed directly from the static fields in this class when needed
 */
public class Config {

	// Configuration categories
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_PROTECTION = "protection";
	private static final String CATEGORY_AREAPROTECTOR = "areaprotector";

	// Declares and initializes with default values all config options
	public static int ticksToLive = 72000;
	
	public static boolean enableMobProtection = true;
	public static boolean enableArrowProtection = true;
	public static boolean enablePotionProtection = true;
	public static boolean enableWolfProtection = false;
	public static boolean enableSlimeProtection = true;
	
	public static boolean giveNewPlayersProtector = true;
	
	public static int areaProtectorX = 5;
	public static int areaProtectorY = 5;
	public static int areaProtectorZ = 5;
	
	public static boolean enableMobProtectionAreaProtector = true;
	public static boolean enableArrowProtectionAreaProtector = true;
	public static boolean enablePotionProtectionAreaProtector = true;
	public static boolean enableWolfProtectionAreaProtector = false;
	public static boolean enableSlimeProtectionAreaProtector = true;
	
	public static boolean enableAreaProtectorRecipe = true;


	/**
	 * Accesses config field from CommonProxy and loads data to the static fields in this class
	 */
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
			initProtectionConfig(cfg);
			initAreaProtConfig(cfg);
		} catch (Exception exception) {
			MobBlocker.logger.log(Level.ERROR, "Problem loading config file!", exception);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	/**
	 * Adds configuration entries to the General category of the config file
	 * @param cfg Configuration object
	 */
	private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        ticksToLive = cfg.getInt("ticksbeforedecay", CATEGORY_GENERAL, ticksToLive, -1, Integer.MAX_VALUE,
        		"How long (in ticks) before the Chunk Protector decays. Set to -1 to disable decay.");
        giveNewPlayersProtector = cfg.getBoolean("givenewplayerschunkprotector", CATEGORY_GENERAL, true,
        		"Set to true to give players a free Chunk Protector when first logging into a world.");
    }

	/**
	 * Adds configuration entries to the Protection category of the config file
	 * @param cfg Configuration object
	 */
	private static void initProtectionConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_PROTECTION, "Chunk Protector configuration");
		
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

	/**
	 * Adds configuration entries to the AreaProt category of the config file
	 * @param cfg Configuration object
	 */
	private static void initAreaProtConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_AREAPROTECTOR, "Area Protector configuration");
		
		areaProtectorX = cfg.getInt("areaprotectorx", CATEGORY_AREAPROTECTOR, 5, 1, 64,
				"Distance from the Area Protector to protect, along the x axis (total distance is 2*this+1)");
		areaProtectorY = cfg.getInt("areaprotectory", CATEGORY_AREAPROTECTOR, 5, 1, 64,
				"Distance from the Area Protector to protect, along the y axis (total distance is 2*this+1)");
		areaProtectorZ = cfg.getInt("areaprotectorz", CATEGORY_AREAPROTECTOR, 5, 1, 64,
				"Distance from the Area Protector to protect, along the z axis (total distance is 2*this+1)");
		
		enableMobProtectionAreaProtector = cfg.getBoolean("enablemobprotectionareaprotector", CATEGORY_AREAPROTECTOR, true,
        		"Set to false to disable teleporting hostile mobs out of the protected area.");
        enableArrowProtectionAreaProtector = cfg.getBoolean("enablearrowprotectionareaprotector", CATEGORY_AREAPROTECTOR, true,
        			"Set to false to disable killing Skeleton arrows shot into the protected area.");
        enablePotionProtectionAreaProtector = cfg.getBoolean("enablepotionprotectionareaprotector", CATEGORY_AREAPROTECTOR, true,
        			"Set to false to disable blocking Witch potions from entering the protected area.");
        enableWolfProtectionAreaProtector = cfg.getBoolean("enablewolfprotectionareaprotector", CATEGORY_AREAPROTECTOR, false,
        			"Set to false to disable calming hostile wolves in the protected area.");
        enableSlimeProtectionAreaProtector = cfg.getBoolean("enableslimeprotectionareaprotector", CATEGORY_AREAPROTECTOR, true,
        			"Set to false to disable teleporting slimes out of the protected area.");
        
        enableAreaProtectorRecipe = cfg.getBoolean("enableareaprotectorrecipe", CATEGORY_AREAPROTECTOR, true,
				"Enables the crafting recipe for the Area Protector block.");
	}
}
