package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.integration.TOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Block class for the Area Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the area in which it is placed.
 * @see TileEntityAreaProtector
 */
public class BlockAreaProtector extends Block implements TOPInfoProvider {

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

    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityAreaProtector) {
            int xrange = Config.AREA_PROTECTOR_X.get();
            int yrange = Config.AREA_PROTECTOR_Y.get();
            int zrange = Config.AREA_PROTECTOR_Z.get();
            probeInfo.text(new TranslationTextComponent("mobblocker.areaprotectorx", xrange).getFormattedText());
            probeInfo.text(new TranslationTextComponent("mobblocker.areaprotectory", yrange).getFormattedText());
            probeInfo.text(new TranslationTextComponent("mobblocker.areaprotectorz", zrange).getFormattedText());
        }
    }
}