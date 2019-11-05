package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.BlockAreaProtector;
import maxwell_lt.mobblocker.blocks.BlockChunkProtector;
import maxwell_lt.mobblocker.blocks.TileEntityAreaProtector;
import maxwell_lt.mobblocker.blocks.TileEntityChunkProtector;
import maxwell_lt.mobblocker.integration.TOPCompatHandler;
import maxwell_lt.mobblocker.setup.ClientProxy;
import maxwell_lt.mobblocker.setup.IProxy;
import maxwell_lt.mobblocker.setup.ModSetup;
import maxwell_lt.mobblocker.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static ModSetup setup = new ModSetup();

    public MobBlocker() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("mobblocker-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent e) {
        setup.init();
        proxy.init();
        TOPCompatHandler.registerTOP();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new BlockChunkProtector());
            event.getRegistry().register(new BlockAreaProtector());
        }
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties().group(setup.itemGroup);
            event.getRegistry().register(new BlockItem(ModBlocks.CHUNKPROTECTOR, properties).setRegistryName("chunkprotector"));
            event.getRegistry().register(new BlockItem(ModBlocks.AREAPROTECTOR, properties).setRegistryName("areaprotector"));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(TileEntityChunkProtector::new, ModBlocks.CHUNKPROTECTOR).build(null).setRegistryName("chunkprotector"));
            event.getRegistry().register(TileEntityType.Builder.create(TileEntityAreaProtector::new, ModBlocks.AREAPROTECTOR).build(null).setRegistryName("areaprotector"));
        }
    }
}
