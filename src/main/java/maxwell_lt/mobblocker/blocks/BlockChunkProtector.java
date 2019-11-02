package maxwell_lt.mobblocker.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Block class for the Chunk Protector block
 *
 * The purpose of this block is to teleport hostile mobs away from the chunk in which it is placed.
 * @see TileEntityChunkProtector
 */
public class BlockChunkProtector extends Block {

	public static IntegerProperty DECAYLEVEL = IntegerProperty.create("decay", 0, 2);

	/**
	 * Constructor for the BlockChunkProtector class
	 *
	 * This method handles initializing the block and adding it to the GameRegistry.
	 * This contructor is only called once per game launch.
	 */
	public BlockChunkProtector() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 18000000));
		this.setDefaultState(this.getStateContainer().getBaseState().with(DECAYLEVEL, 0));
		setRegistryName("chunkprotector");
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(DECAYLEVEL);
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
	/*public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, IBlockState blockState, IProbeHitData data) {
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
}