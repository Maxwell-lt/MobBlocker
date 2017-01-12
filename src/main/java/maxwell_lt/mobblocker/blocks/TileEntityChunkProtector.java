package maxwell_lt.mobblocker.blocks;

import java.util.List;

import akka.event.Logging;
import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class TileEntityChunkProtector extends TileEntity implements ITickable {
	
	public TileEntityChunkProtector() {
		super();
	}

	int tickCounter = 0;
	
	@Override
	public void update() {
		tickCounter++;
		if (world.isRemote) {
			// Gets bounding box of chunk
			
			
			
			AxisAlignedBB chunkBounds = getChunk(getPos());
			MobBlocker.logger.info(chunkBounds.toString());
		
			List<EntityLivingBase> list =  world.getEntitiesWithinAABB(EntityLivingBase.class, chunkBounds);
			
			for (EntityLivingBase entity : list) {
				MobBlocker.logger.error("Entity:");
				MobBlocker.logger.error(entity.getClass().toString());
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
