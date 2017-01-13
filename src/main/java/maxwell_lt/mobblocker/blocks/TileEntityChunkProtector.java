package maxwell_lt.mobblocker.blocks;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChunkProtector extends TileEntity implements ITickable {
	
	// Used to get random teleportation coords:
	Random rand;
	
	public TileEntityChunkProtector() {
		super();
		this.rand = new Random();
	}
	
	@Override
	public void update() {
		
		if (!world.isRemote) {
			AxisAlignedBB chunkBounds = getChunk(getPos());
			
			teleportMobs(chunkBounds);
			killArrows(chunkBounds);
			killPotions(chunkBounds);
			calmAngryWolves(chunkBounds);
		}
	}
	
	// Teleports every hostile mob in the chunk like endermen.
	private void teleportMobs(AxisAlignedBB chunkBounds) {

		
		// Gets a list of all the entities in the same chunk as this block
		List<EntityMob> list =  world.getEntitiesWithinAABB(EntityMob.class, chunkBounds);
		
		for (EntityMob entity : list) {
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
	}
	
	private void killArrows(AxisAlignedBB chunkBounds) {
		List<EntityArrow> list =  world.getEntitiesWithinAABB(EntityArrow.class, chunkBounds);
		for (EntityArrow arrow : list) {
			if (arrow.shootingEntity instanceof AbstractSkeleton) {
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
	
	private void calmAngryWolves(AxisAlignedBB chunkBounds) {
		/*
		List<EntityWolf> list =  world.getEntitiesWithinAABB(EntityWolf.class, chunkBounds);
		for (EntityWolf wolf : list) {
			if (wolf.isAngry()) {
				wolf.setAttackTarget(null);
				wolf.setRevengeTarget(null);
				wolf.setAngry(false);
			}
		}
		*/
	}
	
	// Returns an AxisAlignedBB that surrounds the entire chunk a given BlockPos is in.
	private AxisAlignedBB getChunk(BlockPos blockpos) {
		return new AxisAlignedBB(blockpos.getX() & ~0xF, 0,
				blockpos.getZ() & ~0xF,
				(blockpos.getX() & ~0xF) + 16, 256,
				(blockpos.getZ() & ~0xF) + 16);
		
	}
}
