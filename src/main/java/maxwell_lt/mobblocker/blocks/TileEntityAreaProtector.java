package maxwell_lt.mobblocker.blocks;

import java.util.List;
import java.util.Random;

import maxwell_lt.mobblocker.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * TileEntity for BlockAreaProtector
 * Handles world interactions
 * @see BlockAreaProtector
 */
public class TileEntityAreaProtector extends TileEntity implements ITickable{
	// Used to get random teleportation coords:
	Random rand;

	/**
	 * Calls super() then initiates random generator
	 */
	public TileEntityAreaProtector() {
			super();
			this.rand = new Random();
		}

	/**
	 * Handles logic each tick
	 * Calls methods to deal with potentially hostile entities based on config options
	 * Only runs on the server side
	 */
	@Override
	public void update() {

		if (!world.isRemote) {
			AxisAlignedBB areaBounds = getArea(getPos());

			if (Config.enableMobProtectionAreaProtector) teleportMobs(areaBounds);
			if (Config.enableSlimeProtectionAreaProtector) teleportSlimes(areaBounds);
			if (Config.enableArrowProtectionAreaProtector) killArrows(areaBounds);
			if (Config.enablePotionProtectionAreaProtector) killPotions(areaBounds);
			if (Config.enableWolfProtectionAreaProtector) calmAngryWolves(areaBounds);
		}
	}


	/**
	 * Calls this.teleport on all mobs deriving from EntityMob
	 * @param areaBounds AxisAlignedBB in which block should act
	 */
	private void teleportMobs(AxisAlignedBB areaBounds) {

		// Gets a list of all the entities in the same chunk as this block
		List<EntityMob> entityMobList =  world.getEntitiesWithinAABB(EntityMob.class, areaBounds);

		for (EntityMob entity : entityMobList) {
			teleport(entity);
		}
	}

	/**
	 * Calls this.teleport on all mobs deriving from EntitySlime
	 * @param areaBounds AxisAlignedBB in which block should act
	 */
	private void teleportSlimes(AxisAlignedBB areaBounds) {
		List<EntitySlime> entitySlimeList = world.getEntitiesWithinAABB(EntitySlime.class, areaBounds);
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
	 * @param areaBounds AxisAlignedBB in which block should act
	 */
	private void killArrows(AxisAlignedBB areaBounds) {
		List<EntityArrow> list =  world.getEntitiesWithinAABB(EntityArrow.class, areaBounds);
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
	 * @param areaBounds AxisAlignedBB in which block should act
	 */
	private void killPotions(AxisAlignedBB areaBounds) {
		List<EntityPotion> list =  world.getEntitiesWithinAABB(EntityPotion.class, areaBounds);
		for (EntityPotion potion : list) {
			if (potion.getThrower() instanceof EntityWitch) {
				potion.setDead();
			}
		}
	}

	/**
	 * (BROKEN) Calms angry wolves in the protected area
	 * If anyone has any idea how to make this work, I'd love to hear it.
	 * @param areaBounds AxisAlignedBB in which block should act
	 */
	private void calmAngryWolves(AxisAlignedBB areaBounds) {
		// Currently broken, the wolf that was hit directly does not lose aggro.
		// The wolves aggroed indirectly are properly calmed
		List<EntityWolf> list =  world.getEntitiesWithinAABB(EntityWolf.class, areaBounds);
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
	private AxisAlignedBB getArea(BlockPos blockpos) {

		// Declare variables for each side of the bounding box
		int xmin;
		int ymin;
		int zmin;
		int xmax;
		int ymax;
		int zmax;

		xmax = blockpos.getX() + Config.areaProtectorX + 1;
		ymax = blockpos.getY() + Config.areaProtectorY;
		zmax = blockpos.getZ() + Config.areaProtectorZ + 1;

		xmin = blockpos.getX() - Config.areaProtectorX;
		ymin = blockpos.getY() - Config.areaProtectorY;
		zmin = blockpos.getZ() - Config.areaProtectorZ;

		// Handles potential case of Y coordinate going out of range
		if (ymin < 1) ymin = 1;
		if (ymax > 256) ymax = 256;

		return new AxisAlignedBB(xmax, ymax, zmax, xmin, ymin, zmin);
	}
}
