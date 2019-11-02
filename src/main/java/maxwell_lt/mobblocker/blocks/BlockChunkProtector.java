package maxwell_lt.mobblocker.blocks;

import java.util.List;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.MobBlocker;
import maxwell_lt.mobblocker.integration.TOPInfoProvider;
import maxwell_lt.mobblocker.integration.WailaInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
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

/**
 * Block class for the Chunk Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the chunk in which it is placed.
 * @see TileEntityChunkProtector
 */
public class BlockChunkProtector extends Block implements ITileEntityProvider, /*TOPInfoProvider,*/ WailaInfoProvider {

	public static PropertyInteger DECAYLEVEL = PropertyInteger.create("decay", 0, 2);

	/**
	 * Constructor for the BlockChunkProtector class
	 *
	 * This method handles initializing the block and adding it to the GameRegistry.
	 * This contructor is only called once per game launch.
	 */
	public BlockChunkProtector() {
		super(Material.ROCK);
		setUnlocalizedName(MobBlocker.MODID + ".chunkprotector");
		setRegistryName("chunkprotector");

		// If the Chunk Protector is configured to never decay, block is breakable so that it won't cause clutter.
		// Otherwise, it is unbreakable, because it shouldn't last in the world for all that long, and I can't figure out how to get it to drop with metadata intact.
		if (Config.ticksToLive != -1) setBlockUnbreakable();
		else {
			setHardness(1.5F);
		}

		setResistance(18000000);

		// Register block to GameRegistry
		// GameRegistry.register(this);

		// Create anonymous class for the item form of this block
		// This allows the addition of custom tooltips that show how long the block will last in world once placed.
		/* GameRegistry.register(new ItemBlock(this) {
			@Override
			public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
				// Only add tooltips if decay is enabled
				if (Config.ticksToLive != -1) {
					BlockChunkProtector.addStringToTooltip("&5Good for: &4" + Config.ticksToLive + " &5ticks", list);
				}
				else {
					return;
				}
			}
		}.setRegistryName(this.getRegistryName())); */
		// Registers TileEntity associated with this block
		GameRegistry.registerTileEntity(TileEntityChunkProtector.class, MobBlocker.MODID + "_chunkprotector");
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
	 * @return Returns a new instance of {@link TileEntityChunkProtector}
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityChunkProtector();
	}

	/**
	 * Gets an instance of a BlockStateContainer with the specific properties for this block
	 * @return Returns a BlockStateContainer with the properties this block contains applied
	 */
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DECAYLEVEL);
	}

	/**
	 * Gets an instance of IBlockState corresponding to the passed metadata value
	 * Reverse of getMetaFromState
	 * @param meta int to be converted to a IBlockState value
	 * @return IBlockState corresponding to the passed metadata value
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DECAYLEVEL, meta);
	}

	/**
	 * Gets an integer value from 0-15 corresponding to the passed IBlockState value
	 * @param state IBlockState to be converted to a metadata value
	 * @return int corresponding to the passed IBlockState value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DECAYLEVEL);

	}

	/**
	 * Helper method for replacing ampersands with the correct formatting character and adding it to the tooltip list
	 * @param s String to be added
	 * @param tooltip Tooltip list that the formatted string should be added to
	 */
	public static void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
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
		if (te instanceof TileEntityChunkProtector) {
			TileEntityChunkProtector chunkprotector = (TileEntityChunkProtector) te;
			int secondsLeft = chunkprotector.getSecondsBeforeDestroyed();
			int ticksLeft = chunkprotector.getTicksBeforeDestroyed();
			if (ticksLeft != -1) {
				if (mode == ProbeMode.NORMAL) {
					probeInfo.text(TextFormatting.BLUE + Integer.toString(secondsLeft) + " seconds left in world");
				} else if (mode == ProbeMode.EXTENDED) {
					probeInfo.text(TextFormatting.BLUE + Integer.toString(ticksLeft) + " ticks left in world");
				} else if (mode == ProbeMode.DEBUG) {
					probeInfo.text(TextFormatting.BLUE + Integer.toString(secondsLeft) + " seconds left in world");
					probeInfo.text(TextFormatting.BLUE + Integer.toString(ticksLeft) + " ticks left in world");
				}
			} else probeInfo.text(TextFormatting.GRAY + "Won't decay");
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
		if (te instanceof TileEntityChunkProtector) {
			TileEntityChunkProtector chunkprotector = (TileEntityChunkProtector) te;
			int secondsLeft = chunkprotector.getSecondsBeforeDestroyed();
			int ticksLeft = chunkprotector.getTicksBeforeDestroyed();
			if (ticksLeft != -1) {
				if (accessor.getPlayer().isSneaking()) {
					currenttip.add(TextFormatting.BLUE + Integer.toString(ticksLeft) + " ticks left in world");
				} else currenttip.add(TextFormatting.BLUE + Integer.toString(secondsLeft) + " seconds left in world");
			} else currenttip.add(TextFormatting.GRAY + "Won't decay");
		}
		return currenttip;
	}


}