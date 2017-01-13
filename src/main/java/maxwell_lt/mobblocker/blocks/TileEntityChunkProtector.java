package maxwell_lt.mobblocker.blocks;

import java.util.List;
import java.util.Random;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChunkProtector extends TileEntity implements ITickable {
	
	int tickCounter = 0;
	Random rand;
	
	public TileEntityChunkProtector() {
		super();
		this.rand = new Random();
	}
	
	@Override
	public void update() {
		if (world.isRemote) {
			// Gets bounding box of chunk
			
			
			
			AxisAlignedBB chunkBounds = getChunk(getPos());
		
			List<EntityMob> list =  world.getEntitiesWithinAABB(EntityMob.class, chunkBounds);
			
			for (EntityMob entity : list) {
				boolean moved = false;
				int counter = 0;
				while (!moved) {
					counter++;
					if (counter > 10) break;
					double newX = entity.posX + (this.rand.nextDouble() - 5.0D) * 64.0D;
					double newY = entity.posY + (double)(this.rand.nextInt(64) - 32);
					double newZ = entity.posZ + (this.rand.nextDouble() - 5.0D) * 64.0D;
					moved = entity.attemptTeleport(newX, newY, newZ);
					
					//Debug:
					MobBlocker.logger.info(world.getTopSolidOrLiquidBlock(new BlockPos(newX, newY, newZ)));
					MobBlocker.logger.info("Counter: " + counter);
					MobBlocker.logger.info("X: " + newX + " Y: " + newY + " Z: " + newZ);
				}
			}
		}
	}
	
	private AxisAlignedBB getChunk(BlockPos blockpos) {
		return new AxisAlignedBB((blockpos.getX() / 16) * 16, 0,
				(blockpos.getZ() / 16) * 16,
				((blockpos.getX() / 16) * 16) + 16, 256,
				((blockpos.getZ() / 16) * 16) + 16);
		
	}
}
