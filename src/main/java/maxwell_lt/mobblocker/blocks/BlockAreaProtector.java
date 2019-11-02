package maxwell_lt.mobblocker.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;

/**
 * Block class for the Area Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the area in which it is placed.
 * @see TileEntityAreaProtector
 */
public class BlockAreaProtector extends Block /*implements ITileEntityProvider, TOPInfoProvider, WailaInfoProvider*/ {

    /**
     * Constructor for the BlockAreaProtector class
     *
     * This method handles initializing the block and adding it to the GameRegistry.
     * This contructor is only called once per game launch.
     */
    public BlockAreaProtector() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6000));
        setRegistryName("areaprotector");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    /**
     * Overridden method to return the TileEntity for this block
     * @param world World instance for current world
     * @param state Metadata value
     * @return Returns a new instance of {@link TileEntityAreaProtector}
     */
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
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
    /*public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
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
    }*/
}