package maxwell_lt.mobblocker.integration;

import maxwell_lt.mobblocker.MobBlocker;
import maxwell_lt.mobblocker.blocks.BlockAreaProtector;
import maxwell_lt.mobblocker.blocks.BlockChunkProtector;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.List;

public class WailaCompatibility implements IWailaDataProvider {

    public static final WailaCompatibility INSTANCE = new WailaCompatibility();

    private WailaCompatibility() {}

    private static boolean registered;
    private static boolean loaded;

    public static void load(IWailaRegistrar registrar) {
        System.out.println("WailaCompatibility.load");
        if (!registered){
            throw new RuntimeException("Please register this handler using the provided method.");
        }
        if (!loaded) {
            registrar.registerHeadProvider(INSTANCE, BlockChunkProtector.class);
            registrar.registerBodyProvider(INSTANCE, BlockChunkProtector.class);
            registrar.registerTailProvider(INSTANCE, BlockChunkProtector.class);

            registrar.registerHeadProvider(INSTANCE, BlockAreaProtector.class);
            registrar.registerBodyProvider(INSTANCE, BlockAreaProtector.class);
            registrar.registerTailProvider(INSTANCE, BlockAreaProtector.class);
            loaded = true;
        }
    }

    public static void register(){
        if (registered)
            return;
        registered = true;
        String mcVersion = Loader.instance().getMinecraftModContainer().getVersion();
        if (mcVersion.substring(3,4) == "0") {
            FMLInterModComms.sendMessage("Waila", "register", "maxwell_lt.mobblocker.integration.WailaCompatibility.load");
        } else FMLInterModComms.sendMessage("waila", "register", "maxwell_lt.mobblocker.integration.WailaCompatibility.load");
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof WailaInfoProvider) {
            return ((WailaInfoProvider) block).getWailaBody(itemStack, currenttip, accessor, config);
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

}
