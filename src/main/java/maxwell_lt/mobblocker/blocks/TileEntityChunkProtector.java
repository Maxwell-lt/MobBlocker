package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.handler.MobRemovalHandler;
import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.particle.ParticleBoxHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import static maxwell_lt.mobblocker.ModBlocks.CHUNKPROTECTOR_TILE;

/**
 * TileEntity for BlockChunkProtector
 * Handles world interactions
 * @see BlockChunkProtector
 */
public class TileEntityChunkProtector extends TileEntity implements ITickableTileEntity {
	// Ticks this block has existed in the world
	int ticksInWorld;
	// Ticks until the block is destroyed
	int ticksBeforeDestroyed;


	/**
	 * Calls super() and initiates fields
	 * Sets this.ticksInWorld to 0
	 * Sets this.ticksBeforeDestroyed as per the config file
	 */
	public TileEntityChunkProtector() {
		super(CHUNKPROTECTOR_TILE);
		this.ticksInWorld = 0;
		this.ticksBeforeDestroyed = Config.TICKS_TO_LIVE.get();
	}

	/**
	 * Handles logic each tick
	 * Calls methods to deal with potentially hostile entities based on config options
	 * Only runs on the server side
	 */
	@Override
	public void tick() {
		AxisAlignedBB chunkBounds = getChunk(getPos());
		if (!world.isRemote) {
			if (Config.ENABLE_MOB_PROTECTION.get()) MobRemovalHandler.teleportMobs(chunkBounds, world);
			if (Config.ENABLE_SLIME_PROTECTION.get()) MobRemovalHandler.teleportSlimes(chunkBounds, world);
			if (Config.ENABLE_ARROW_PROTECTION.get()) MobRemovalHandler.killArrows(chunkBounds, world);
			if (Config.ENABLE_POTION_PROTECTION.get()) MobRemovalHandler.killPotions(chunkBounds, world);
			if (Config.ENABLE_WOLF_PROTECTION.get()) MobRemovalHandler.calmAngryWolves(chunkBounds, world);

			// Handles decay mechanics of this block, if enabled in the config file
			// Sets the visual decay indicator based on which approximate third of the block's total lifetime the block has lived for
			this.ticksInWorld++;
			if (ticksBeforeDestroyed != -1) {
				if (ticksInWorld <= ticksBeforeDestroyed * 0.3F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockChunkProtector.DECAYLEVEL, 0));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.3F && ticksInWorld <= ticksBeforeDestroyed * 0.7F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockChunkProtector.DECAYLEVEL, 1));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.7F && ticksInWorld < ticksBeforeDestroyed) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockChunkProtector.DECAYLEVEL, 2));
				} else {
					// Destroys block when timer reaches 0
					world.setBlockState(getPos(), Blocks.AIR.getDefaultState(), 3);
				}
			} else world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockChunkProtector.DECAYLEVEL, 0));

			// Makes sure the block updates properly and has saved data if the chunk unloads
			markDirty();
			BlockState currState = world.getBlockState(getPos());
			world.notifyBlockUpdate(getPos(), currState, currState, 0);
		} else {
			if (world.getDayTime() % 20 == 0) {
				ParticleBoxHandler.drawBox(chunkBounds, world, 0, 0, 1);
			}
		}
	}

	/**
	 * Returns an AxisAlignedBB centered on the block's position
	 * @param blockpos Center of AxisAlignedBB
	 * @return AxisAlignedBB centered on the passed BlockPos coordinate, with a size set from the config file
	 */
	private AxisAlignedBB getChunk(BlockPos blockpos) {
		return new AxisAlignedBB(blockpos.getX() & ~0xF, 0,
				blockpos.getZ() & ~0xF,
				(blockpos.getX() & ~0xF) + 16, 256,
				(blockpos.getZ() & ~0xF) + 16);

	}

	/**
	 * Gets the time in seconds before the block is to be destroyed
	 * Only time the block spends in a loaded chunk counts towards this time
	 * @return Seconds before the block is scheduled to be destroyed
	 */
	public int getSecondsBeforeDestroyed() {
		if (Config.TICKS_TO_LIVE.get() != -1) {
			return (ticksBeforeDestroyed - ticksInWorld) / 20;
		} else return -1;
	}

	/**
	 * Gets time in ticks before the block is to be destroyed
	 * Only time the block spends in a loaded chunk counts towards this time
	 * @return Ticks before the block is scheduled to be destroyed
	 */
	public int getTicksBeforeDestroyed() {
		if (Config.TICKS_TO_LIVE.get() != -1) {
			return ticksBeforeDestroyed - ticksInWorld;
		} else return -1;
	}

	/**
	 * Returns CompoundNBT to be sent through networking
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	/**
	 * Creates data packets
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.write(nbtTag);
		return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
	}

	/**
	 * Handles data packets
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	/**
	 * Restores block on chunk reload
	 * Used for saving block data upon chunk unload
	 */
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ticksInWorld = compound.getInt("ticksInWorld");
	}

	/**
	 * Serializes data upon chunk unload
	 * @return CompoundNBT containing entire NBT structure of block
	 */
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("ticksInWorld", ticksInWorld);
		return compound;
	}
}