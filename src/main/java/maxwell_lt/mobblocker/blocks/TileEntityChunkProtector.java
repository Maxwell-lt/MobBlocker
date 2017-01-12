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
		
		// Gets bounding box of chunk
		ChunkPos chunk = world.getChunkFromChunkCoords(getPos().getX(), getPos().getY()).getPos();
			
		AxisAlignedBB chunkBounds = new AxisAlignedBB(new BlockPos(chunk.getXStart(), 0, chunk.getZStart()), new BlockPos(chunk.getXEnd(), 256, chunk.getZEnd()));
		
		List<EntityLivingBase> list =  world.getEntitiesWithinAABB(EntityLivingBase.class, chunkBounds);
		MobBlocker.logger.debug("update() called");	
		MobBlocker.logger.debug(chunkBounds.toString());
			
		for (EntityLivingBase entity : list) {
			MobBlocker.logger.debug(entity.getClass().toString());
		}
	}
}
