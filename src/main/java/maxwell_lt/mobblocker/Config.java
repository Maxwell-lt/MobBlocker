package maxwell_lt.mobblocker;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

/**
 * Handles configuration file parsing
 * Configuration options are accessed directly from the static fields in this class when needed
 */
public class Config {

	// Configuration categories
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_PROTECTION = "protection";
	private static final String CATEGORY_AREAPROTECTOR = "areaprotector";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.IntValue TICKS_TO_LIVE;
	
	public static ForgeConfigSpec.BooleanValue ENABLE_MOB_PROTECTION;
	public static ForgeConfigSpec.BooleanValue ENABLE_ARROW_PROTECTION;
	public static ForgeConfigSpec.BooleanValue ENABLE_POTION_PROTECTION;
	public static ForgeConfigSpec.BooleanValue ENABLE_WOLF_PROTECTION;
	public static ForgeConfigSpec.BooleanValue ENABLE_SLIME_PROTECTION;
	
	public static ForgeConfigSpec.BooleanValue GIVE_NEW_PLAYERS_PROTECTOR;
	
	public static ForgeConfigSpec.IntValue AREA_PROTECTOR_X;
	public static ForgeConfigSpec.IntValue AREA_PROTECTOR_Y;
	public static ForgeConfigSpec.IntValue AREA_PROTECTOR_Z;
	
	public static ForgeConfigSpec.BooleanValue ENABLE_MOB_PROTECTION_AREA_PROTECTOR;
	public static ForgeConfigSpec.BooleanValue ENABLE_ARROW_PROTECTION_AREA_PROTECTOR;
	public static ForgeConfigSpec.BooleanValue ENABLE_POTION_PROTECTION_AREA_PROTECTOR;
	public static ForgeConfigSpec.BooleanValue ENABLE_WOLF_PROTECTION_AREA_PROTECTOR;
	public static ForgeConfigSpec.BooleanValue ENABLE_SLIME_PROTECTION_AREA_PROTECTOR;
	
	public static ForgeConfigSpec.BooleanValue ENABLE_AREA_PROTECTOR_RECIPE;

	static {
		COMMON_BUILDER.comment("General Configuration").push(CATEGORY_GENERAL);
		initGeneralConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Chunk Protector Configuration").push(CATEGORY_PROTECTION);
		initProtectionConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Area Protector Configuration").push(CATEGORY_AREAPROTECTOR);
		initAreaProtConfig();
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	/**
	 * Adds configuration entries to the General category of the config file
	 */
	private static void initGeneralConfig() {
		TICKS_TO_LIVE = COMMON_BUILDER
				.comment("How long (in ticks) before the Chunk Protector decays. Set to -1 to disable decay.")
				.defineInRange("ticksbeforedecay", 72000, -1, Integer.MAX_VALUE);
		GIVE_NEW_PLAYERS_PROTECTOR = COMMON_BUILDER
				.comment("Set to true to give players a free Chunk Protector when first logging into a world.")
				.define("givenewplayerschunkprotector", true);
    }

	/**
	 * Adds configuration entries to the Protection category of the config file
	 */
	private static void initProtectionConfig() {
		ENABLE_MOB_PROTECTION = COMMON_BUILDER
				.comment("Set to false to disable teleporting hostile mobs out of the protected area.")
				.define("enablemobprotection", true);
		ENABLE_ARROW_PROTECTION = COMMON_BUILDER
				.comment("Set to false to disable killing Skeleton arrows shot into the protected area.")
				.define("enablearrowprotection", true);
		ENABLE_POTION_PROTECTION = COMMON_BUILDER
				.comment("Set to false to disable blocking Witch potions from entering the protected area.")
				.define("enablepotionprotection", true);
		ENABLE_WOLF_PROTECTION = COMMON_BUILDER
				.comment("Set to false to disable calming hostile wolves in the protected area.")
				.define("enablewolfprotection", false);
		ENABLE_SLIME_PROTECTION = COMMON_BUILDER
				.comment("Set to false to disable teleporting slimes out of the protected area.")
				.define("enableslimeprotection", true);
	}

	/**
	 * Adds configuration entries to the AreaProt category of the config file
	 */
	private static void initAreaProtConfig() {
		AREA_PROTECTOR_X = COMMON_BUILDER
				.comment("Distance from the Area Protector to protect, along the x axis (total distance is 2*this+1)")
				.defineInRange("areaprotectorx", 5, 1, 64);
		AREA_PROTECTOR_Y = COMMON_BUILDER
				.comment("Distance from the Area Protector to protect, along the y axis (total distance is 2*this+1)")
				.defineInRange("areaprotectory", 5, 1, 64);
		AREA_PROTECTOR_Z = COMMON_BUILDER
				.comment("Distance from the Area Protector to protect, along the z axis (total distance is 2*this+1)")
				.defineInRange("areaprotectorz", 5, 1, 64);
		ENABLE_MOB_PROTECTION_AREA_PROTECTOR = COMMON_BUILDER
				.comment("Set to false to disable teleporting hostile mobs out of the protected area.")
				.define("enablemobprotectionareaprotector", true);
		ENABLE_ARROW_PROTECTION_AREA_PROTECTOR = COMMON_BUILDER
				.comment("Set to false to disable killing Skeleton arrows shot into the protected area.")
				.define("enablearrowprotectionareaprotector", true);
		ENABLE_POTION_PROTECTION_AREA_PROTECTOR = COMMON_BUILDER
				.comment("Set to false to disable blocking Witch potions from entering the protected area.")
				.define("enablepotionprotectionareaprotector", true);
		ENABLE_WOLF_PROTECTION_AREA_PROTECTOR = COMMON_BUILDER
				.comment("Set to false to disable calming hostile wolves in the protected area.")
				.define("enablewolfprotectionareaprotector", false);
		ENABLE_SLIME_PROTECTION_AREA_PROTECTOR = COMMON_BUILDER
				.comment("Set to false to disable teleporting slimes out of the protected area.")
				.define("enableslimeprotectionareaprotector", true);
		ENABLE_AREA_PROTECTOR_RECIPE = COMMON_BUILDER
				.comment("Enables the crafting recipe for the Area Protector block.")
				.define("enableareaprotectorrecipe", true);
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {

		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		configData.load();
		spec.setConfig(configData);
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {

	}

	@SubscribeEvent
	public static void onReload(final ModConfig.ConfigReloading configEvent) {

	}

}
