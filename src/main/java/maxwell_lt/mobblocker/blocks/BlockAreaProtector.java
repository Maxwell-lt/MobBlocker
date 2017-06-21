package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.MobBlocker;
import maxwell_lt.mobblocker.integration.TOPInfoProvider;
import maxwell_lt.mobblocker.integration.WailaInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;

import java.util.List;

/**
 * Block class for the Area Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the area in which it is placed.
 * @see TileEntityAreaProtector
 */
public class BlockAreaProtector extends Block implements ITileEntityProvider, TOPInfoProvider, WailaInfoProvider {

    /**
     * Constructor for the BlockAreaProtector class
     *
     * This method handles initializing the block and adding it to the GameRegistry.
     * This contructor is only called once per game launch.
     */
    public BlockAreaProtector() {
        super(Material.ROCK);
        setUnlocalizedName(MobBlocker.MODID + ".areaprotector");
        setRegistryName("areaprotector");
        setHardness(1.5F);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileEntityAreaProtector.class, MobBlocker.MODID + "_areaprotector");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
                0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    /**
     * Overridden method to return the TileEntity for this block
     * @param worldIn World instance for current world
     * @param meta Metadata value
     * @return Returns a new instance of {@link TileEntityAreaProtector}
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAreaProtector();
    }

    /**
     * Called by TOP compatibility handler
     * @param mode
     * @param probeInfo
     * @param player
     * @param world
     * @param blockState
     * @param data
     */
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityAreaProtector) {
            int xrange = Config.areaProtectorX;
            int yrange = Config.areaProtectorY;
            int zrange = Config.areaProtectorZ;
            probeInfo.text(TextFormatting.BOLD + "Range:");
            probeInfo.text(TextFormatting.YELLOW + "    X" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(xrange));
            probeInfo.text(TextFormatting.YELLOW + "    Y" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(yrange));
            probeInfo.text(TextFormatting.YELLOW + "    Z" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(zrange));
        }
    }

    /**
     * Called by WAILA/HWYLA compatibility handler
     * @param itemStack
     * @param currenttip
     * @param accessor
     * @param config
     * @return
     */
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (te instanceof TileEntityAreaProtector) {
            int xrange = Config.areaProtectorX;
            int yrange = Config.areaProtectorY;
            int zrange = Config.areaProtectorZ;
            currenttip.add(TextFormatting.BOLD + "Range:");
            currenttip.add(TextFormatting.YELLOW + "    X" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(xrange));
            currenttip.add(TextFormatting.YELLOW + "    Y" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(yrange));
            currenttip.add(TextFormatting.YELLOW + "    Z" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(zrange));
        }
        return currenttip;
    }
}