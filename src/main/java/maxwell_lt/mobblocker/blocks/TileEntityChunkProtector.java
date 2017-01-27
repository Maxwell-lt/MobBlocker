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

public class TileEntityChunkProtector extends TileEntity implements ITickable {

	// Used to get random teleportation coords:
	Random rand;

	int ticksInWorld;
	int ticksBeforeDestroyed;


	public TileEntityChunkProtector() {
		super();
		this.rand = new Random();

		this.ticksInWorld = 0;
		this.ticksBeforeDestroyed = Config.ticksToLive;
	}

	@Override
	public void update() {

		if (!world.isRemote) {
			AxisAlignedBB chunkBounds = getChunk(getPos());

			if (Config.enableMobProtection) teleportMobs(chunkBounds);
			if (Config.enableSlimeProtection) teleportSlimes(chunkBounds);
			if (Config.enableArrowProtection) killArrows(chunkBounds);
			if (Config.enablePotionProtection) killPotions(chunkBounds);
			if (Config.enableWolfProtection) calmAngryWolves(chunkBounds);

			// Set metadata
			this.ticksInWorld++;
			if (ticksBeforeDestroyed != -1) {
				if (ticksInWorld <= ticksBeforeDestroyed * 0.3F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 0));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.3F && ticksInWorld <= ticksBeforeDestroyed * 0.7F) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 1));
				} else if (ticksInWorld > ticksBeforeDestroyed * 0.7F && ticksInWorld < ticksBeforeDestroyed) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 2));
				} else {
					world.setBlockToAir(getPos());
				}
			} else world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockChunkProtector.DECAYLEVEL, 0));

			markDirty();
		}
	}


	// Teleports every hostile mob in the chunk like endermen.
	private void teleportMobs(AxisAlignedBB chunkBounds) {

		// Gets a list of all the entities in the same chunk as this block
		List<EntityMob> entityMobList =  world.getEntitiesWithinAABB(EntityMob.class, chunkBounds);

		for (EntityMob entity : entityMobList) {
			teleport(entity);
		}
	}

	private void teleportSlimes(AxisAlignedBB chunkBounds) {
		List<EntitySlime> entitySlimeList = world.getEntitiesWithinAABB(EntitySlime.class, chunkBounds);
		for (EntitySlime entity : entitySlimeList) {
			teleport(entity);
		}
	}

	private void killArrows(AxisAlignedBB chunkBounds) {
		List<EntityArrow> list =  world.getEntitiesWithinAABB(EntityArrow.class, chunkBounds);
		for (EntityArrow arrow : list) {
			if (arrow.shootingEntity instanceof IRangedAttackMob) {
				if (arrow.isBurning()) {
					arrow.setDead();
				} else {
					arrow.setFire(1);
					arrow.setVelocity(0, 0, 0);
				}
			}
		}
	}

	private void killPotions(AxisAlignedBB chunkBounds) {
		List<EntityPotion> list =  world.getEntitiesWithinAABB(EntityPotion.class, chunkBounds);
		for (EntityPotion potion : list) {
			if (potion.getThrower() instanceof EntityWitch) {
				potion.setDead();
			}
		}
	}

	// BROKEN
	private void calmAngryWolves(AxisAlignedBB chunkBounds) {
		// Currently broken, only the helper wolves are calmed.
		List<EntityWolf> list =  world.getEntitiesWithinAABB(EntityWolf.class, chunkBounds);
		for (EntityWolf wolf : list) {
			if (wolf.isAngry()) {
				wolf.setAttackTarget(null);
				wolf.setRevengeTarget(null);
				wolf.setAngry(false);
			}
		}
	}

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

	// Returns an AxisAlignedBB that surrounds the entire chunk a given BlockPos is in.
	private AxisAlignedBB getChunk(BlockPos blockpos) {
		return new AxisAlignedBB(blockpos.getX() & ~0xF, 0,
				blockpos.getZ() & ~0xF,
				(blockpos.getX() & ~0xF) + 16, 256,
				(blockpos.getZ() & ~0xF) + 16);

	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	public int getSecondsBeforeDestroyed() {
		if (Config.ticksToLive != -1) {
			return (ticksBeforeDestroyed - ticksInWorld) / 20;
		} else return -1;
	}

	public int getTicksBeforeDestroyed() {
		if (Config.ticksToLive != -1) {
			return ticksBeforeDestroyed - ticksInWorld;
		} else return -1;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}


	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ticksInWorld = compound.getInteger("ticksInWorld");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("ticksInWorld", ticksInWorld);
		return compound;
	}
}