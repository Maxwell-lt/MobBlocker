package maxwell_lt.mobblocker.blocks;

import java.util.List;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.MobBlocker;
import maxwell_lt.mobblocker.TOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
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

public class BlockChunkProtector extends Block implements ITileEntityProvider, TOPInfoProvider {
	
	public static PropertyInteger DECAYLEVEL = PropertyInteger.create("decay", 0, 2);
    
	public BlockChunkProtector() {
        super(Material.ROCK);
        setUnlocalizedName(MobBlocker.MODID + ".chunkprotector");
        setRegistryName("chunkprotector");
        if (Config.ticksToLive != -1) setBlockUnbreakable();
        else {
        	setHardness(1.5F);
        	setResistance(18000000);
        }
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this) {
			@Override
			public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
				if (Config.ticksToLive != -1) {
					BlockChunkProtector.addStringToTooltip("&5Good for: &4" + Config.ticksToLive + " &5ticks", list);
				}
				else {
					return;
				}
			}
		}.setRegistryName(this.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityChunkProtector.class, MobBlocker.MODID + "_chunkprotector");
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
    			0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityChunkProtector();
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DECAYLEVEL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DECAYLEVEL, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DECAYLEVEL);
		
	}
	
	public static void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}


	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if (te instanceof TileEntityChunkProtector) {
			TileEntityChunkProtector chunkprotector = (TileEntityChunkProtector) te;
			int secondsLeft = chunkprotector.getSecondsBeforeDestroyed();
			if (secondsLeft != -1) {
				probeInfo.text(TextFormatting.BLUE + Integer.toString(secondsLeft) + " seconds left in world");
			} else probeInfo.text(TextFormatting.GRAY + "Won't decay");
		}
	}
	
	
}
