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

public class TileEntityAreaProtector extends TileEntity implements ITickable{
	// Used to get random teleportation coords:
		Random rand;
		
		int ticksInWorld;
		int ticksBeforeDestroyed;
		
		
		public TileEntityAreaProtector() {
			super();
			this.rand = new Random();
		}
		
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
		
		
		// Teleports every hostile mob in the chunk like endermen.
		private void teleportMobs(AxisAlignedBB areaBounds) {

			// Gets a list of all the entities in the same chunk as this block
			List<EntityMob> entityMobList =  world.getEntitiesWithinAABB(EntityMob.class, areaBounds);
			
			for (EntityMob entity : entityMobList) {
				teleport(entity);
			}
		}
		
		private void teleportSlimes(AxisAlignedBB areaBounds) {
			List<EntitySlime> entitySlimeList = world.getEntitiesWithinAABB(EntitySlime.class, areaBounds);
			for (EntitySlime entity : entitySlimeList) {
				teleport(entity);
			}
		}
		
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
		
		private void killPotions(AxisAlignedBB areaBounds) {
			List<EntityPotion> list =  world.getEntitiesWithinAABB(EntityPotion.class, areaBounds);
			for (EntityPotion potion : list) {
				if (potion.getThrower() instanceof EntityWitch) {
					potion.setDead();
				}
			}
		}
		
		// BROKEN
		private void calmAngryWolves(AxisAlignedBB areaBounds) {
			// Currently broken, only the helper wolves are calmed.
			List<EntityWolf> list =  world.getEntitiesWithinAABB(EntityWolf.class, areaBounds);
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
		
		// Returns an AxisAlignedBB based on config options.
		private AxisAlignedBB getArea(BlockPos blockpos) {
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
			
			if (ymin < 1) ymin = 1;
			if (ymax > 256) ymax = 256;
			
			return new AxisAlignedBB(xmax, ymax, zmax, xmin, ymin, zmin);
		}
}
