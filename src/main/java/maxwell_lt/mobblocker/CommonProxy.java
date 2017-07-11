package maxwell_lt.mobblocker;

import java.io.File;
import java.util.List;

import maxwell_lt.mobblocker.blocks.BlockAreaProtector;
import maxwell_lt.mobblocker.blocks.BlockChunkProtector;
import maxwell_lt.mobblocker.integration.MainCompatHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
	    // Read configuration file
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "mobblocker.cfg"));
        Config.readConfig();

        // Initializes blocks
        // ModBlocks.init();

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

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockChunkProtector());
        event.getRegistry().register(new BlockAreaProtector());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.areaProtector).setRegistryName(ModBlocks.areaProtector.getRegistryName()));

        event.getRegistry().register(new ItemBlock(ModBlocks.chunkProtector) {
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
                // Only add tooltips if decay is enabled
                if (Config.ticksToLive != -1) {
                    BlockChunkProtector.addStringToTooltip("&5Good for: &4" + Config.ticksToLive + " &5ticks", list);
                }
                else {
                    return;
                }
            }
        }.setRegistryName(ModBlocks.chunkProtector.getRegistryName()));
    }
}

