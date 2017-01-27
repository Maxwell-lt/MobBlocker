package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.MobBlocker;
import maxwell_lt.mobblocker.TOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;

public class BlockAreaProtector extends Block implements ITileEntityProvider, TOPInfoProvider {
	
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAreaProtector();
	}

    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityAreaProtector) {
            TileEntityAreaProtector areaprotector = (TileEntityAreaProtector) te;
            int xrange = Config.areaProtectorX;
            int yrange = Config.areaProtectorY;
            int zrange = Config.areaProtectorZ;
            probeInfo.text(TextFormatting.BOLD + "Range:");
            probeInfo.text(TextFormatting.YELLOW + "    X" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(xrange));
            probeInfo.text(TextFormatting.YELLOW + "    Y" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(yrange));
            probeInfo.text(TextFormatting.YELLOW + "    Z" + TextFormatting.RESET + " = " + TextFormatting.GREEN + "±" + Integer.toString(zrange));
        }
    }
}
