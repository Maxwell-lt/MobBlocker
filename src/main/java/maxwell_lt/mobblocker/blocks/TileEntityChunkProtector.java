package maxwell_lt.mobblocker.blocks;

import java.util.List;
import java.util.Random;

import maxwell_lt.mobblocker.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * TileEntity for BlockChunkProtector
 * Handles world interactions
 * @see BlockChunkProtector
 */
public class TileEntityChunkProtector extends TileEntity implements ITickable {

	// Used to get random teleportation coords:
	Random rand;

	// Ticks this block has existed in the world
	int ticksInWorld;
	// Ticks until the block is destroyed
	int ticksBeforeDestroyed;


	/**
	 * Calls super() and initiates fields
	 * Initializes RNG
	 * Sets this.ticksInWorld to 0
	 * Sets this.ticksBeforeDestroyed as per the config file
	 */
	public TileEntityChunkProtector() {
		super();
		this.rand = new Random();

		this.ticksInWorld = 0;
		this.ticksBeforeDestroyed = Config.ticksToLive;
	}

	/**
	 * Handles logic each tick
	 * Calls methods to deal with potentially hostile entities based on config options
	 * Only runs on the server side
	 */
	@Override
	public void update() {

		if (!world.isRemote) {
			AxisAlignedBB chunkBounds = getChunk(getPos());

			if (Config.enableMobProtection) teleportMobs(chunkBounds);
			if (Config.enableSlimeProtection) teleportSlimes(chunkBounds);
			if (Config.enableArrowProtection) killArrows(chunkBounds);
			if (Config.enablePotionProtection) killPotions(chunkBounds);
			if (Config.enableWolfProtection) calmAngryWolves(chunkBounds);

			// Handles decay mechanics of this block, if enabled in the config file
			// Sets the visual decay indicator based on which approximate third of the block's total lifetime the block has lived for
			this.ticksInWorld++;
			if (ticksBeforeDestroyed != -1) {
				if (ticksInWorld <= ticksBeforeDestroyed * 0.3F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 0));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.3F && ticksInWorld <= ticksBeforeDestroyed * 0.7F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 1));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.7F && ticksInWorld < ticksBeforeDestroyed) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 2));
				} else {
					// Destroys block when timer reaches 0
					world.setBlockToAir(getPos());
				}
			} else world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 0));

			// Makes sure the block updates properly and has saved data if the chunk unloads
			markDirty();
			IBlockState currState = world.getBlockState(getPos());
			world.notifyBlockUpdate(getPos(), currState, currState, 0);
		}
	}


	/**
	 * Calls this.teleport on all mobs deriving from EntityMob
	 * @param chunkBounds AxisAlignedBB in which block should act
	 */
	private void teleportMobs(AxisAlignedBB chunkBounds) {

		// Gets a list of all the entities in the same chunk as this block
		List<EntityMob> entityMobList =  world.getEntitiesWithinAABB(EntityMob.class, chunkBounds);

		for (EntityMob entity : entityMobList) {
			teleport(entity);
		}
	}

	/**
	 * Calls this.teleport on all mobs deriving from EntitySlime
	 * @param chunkBounds AxisAlignedBB in which block should act
	 */
	private void teleportSlimes(AxisAlignedBB chunkBounds) {
		List<EntitySlime> entitySlimeList = world.getEntitiesWithinAABB(EntitySlime.class, chunkBounds);
		for (EntitySlime entity : entitySlimeList) {
			teleport(entity);
		}
	}

	/**
	 * Destroys skeleton arrows in the protected area
	 * Checks whether the arrow was shot by a class deriving from IRangedAttackMob
	 * Sets the arrow on fire for visual effect by default
	 * An arrow already on fire is killed
	 * The effect of this is that the arrow, for one tick, is engulfed in flame, as it looks strange to have the arrow disappear for no reason
	 * @param chunkBounds AxisAlignedBB in which block should act
	 */
	private void killArrows(AxisAlignedBB chunkBounds) {
		List<EntityArrow> list =  world.getEntitiesWithinAABB(EntityArrow.class, chunkBounds);
		for (EntityArrow arrow : list) {
			if (arrow.shootingEntity instanceof IRangedAttackMob) {
				if (arrow.isBurning()) {
					arrow.setDead();
				} else {
					arrow.setFire(1);
					arrow.addVelocity(-arrow.motionX, 0, -arrow.motionZ);
				}
			}
		}
	}

	/**
	 * Destroys Witch Potions that are currently in the protected area
	 * Only destroys the EntityPotion if it was thrown by an EntityWitch or derivatives
	 * @param chunkBounds AxisAlignedBB in which block should act
	 */
	private void killPotions(AxisAlignedBB chunkBounds) {
		List<EntityPotion> list =  world.getEntitiesWithinAABB(EntityPotion.class, chunkBounds);
		for (EntityPotion potion : list) {
			if (potion.getThrower() instanceof EntityWitch) {
				potion.setDead();
			}
		}
	}

	/**
	 * (BROKEN) Calms angry wolves in the protected area
	 * If anyone has any idea how to make this work, I'd love to hear it.
	 * @param chunkBounds AxisAlignedBB in which block should act
	 */
	private void calmAngryWolves(AxisAlignedBB chunkBounds) {
		// Currently broken, the wolf that was hit directly does not lose aggro.
		// The wolves aggroed indirectly are properly calmed
		List<EntityWolf> list =  world.getEntitiesWithinAABB(EntityWolf.class, chunkBounds);
		for (EntityWolf wolf : list) {
			if (wolf.isAngry()) {
				wolf.setAttackTarget(null);
				wolf.setRevengeTarget(null);
				wolf.setAngry(false);
			}
		}
	}

	/**
	 * Teleports entities randomly up to 16 blocks away
	 * Copy of code from Enderman class
	 * Tries 10 times to teleport the entity before giving for this tick cycle
	 * @param entity Entity to be teleported
	 */
	private void teleport(EntityLivingBase entity) {
		boolean moved = false; 	// Stores the status of teleportation attempts.
		int counter = 0;		// Used to prevent infinite loops.
		while (!moved) {
			counter++;
			if (counter > 10) break; // Breaks out of a possible infinite loop.

			// Implementation of Enderman random teleport code:
			double newX = entity.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
			double newY = entity.posY + (double)(this.rand.nextInt(64) - 32);
			double newZ = entity.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
			moved = entity.attemptTeleport(newX, world.getTopSolidOrLiquidBlock(new BlockPos(newX, newY, newZ)).getY(), newZ);
		}

		// Reset loop controllers:
		counter = 0;
		moved = false;
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
	 * Manages whether the block should be refreshed
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	/**
	 * Gets the time in seconds before the block is to be destroyed
	 * Only time the block spends in a loaded chunk counts towards this time
	 * @return Seconds before the block is scheduled to be destroyed
	 */
	public int getSecondsBeforeDestroyed() {
		if (Config.ticksToLive != -1) {
			return (ticksBeforeDestroyed - ticksInWorld) / 20;
		} else return -1;
	}

	/**
	 * Gets time in ticks before the block is to be destroyed
	 * Only time the block spends in a loaded chunk counts towards this time
	 * @return Ticks before the block is scheduled to be destroyed
	 */
	public int getTicksBeforeDestroyed() {
		if (Config.ticksToLive != -1) {
			return ticksBeforeDestroyed - ticksInWorld;
		} else return -1;
	}

	/**
	 * Returns NBTTagCompound to be sent through networking
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	/**
	 * Creates data packets
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	/**
	 * Handles data packets
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	/**
	 * Restores block on chunk reload
	 * Used for saving block data upon chunk unload
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ticksInWorld = compound.getInteger("ticksInWorld");
	}

	/**
	 * Serializes data upon chunk unload
	 * @return NBTTagCompound containing entire NBT structure of block
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("ticksInWorld", ticksInWorld);
		return compound;
	}
}