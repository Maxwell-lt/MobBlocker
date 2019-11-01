package maxwell_lt.mobblocker.blocks;

import java.util.List;

import maxwell_lt.mobblocker.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Block class for the Chunk Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the chunk in which it is placed.
 * @see TileEntityChunkProtector
 */
public class BlockChunkProtector extends Block /*implements ITileEntityProvider, /*TOPInfoProvider, WailaInfoProvider*/ {

	public static IntegerProperty DECAYLEVEL = IntegerProperty.create("decay", 0, 2);

	/**
	 * Constructor for the BlockChunkProtector class
	 *
	 * This method handles initializing the block and adding it to the GameRegistry.
	 * This contructor is only called once per game launch.
	 */
	public BlockChunkProtector() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(Config.TICKS_TO_LIVE.get() != -1 ? -1.0F : 1.5F, 18000000));
		setRegistryName("chunkprotector");
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	/**
	 * Overridden method to return the TileEntity for this block
	 * @param world World instance for current world
	 * @param state Metadata value
	 * @return Returns a new instance of {@link TileEntityChunkProtector}
	 */
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityChunkProtector();
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
	/*public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
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
	}*/

	/**
	 * Called by WAILA/HWYLA compatibility handler
	 * @param itemStack
	 * @param currenttip
	 * @param accessor
	 * @param config
	 * @return
	 */
	/*@Override
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
	}*/


}