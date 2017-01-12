package maxwell_lt.mobblocker.blocks;

import java.util.List;

import akka.event.Logging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class TileEntityChunkProtector extends TileEntity {

	int tickCounter = 0;
	
	
	public void update() {
		tickCounter++;
		if (world.isRemote) {
			// Gets bounding box of chunk
			ChunkPos chunk = world.getChunkFromChunkCoords(getPos().getX(), getPos().getY()).getPos();
			
			List<EntityLivingBase> list =  world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(new BlockPos(chunk.getXStart(), 0, chunk.getZStart()), new BlockPos(chunk.getXEnd(), 256, chunk.getZEnd())));
			
			for (EntityLivingBase entity : list) {
				if (tickCounter % 100 == 0) {
					
				}
			}
		}
	}
}
