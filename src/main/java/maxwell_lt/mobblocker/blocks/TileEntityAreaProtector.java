package maxwell_lt.mobblocker.blocks;

import java.util.Random;
import maxwell_lt.mobblocker.BlockMobs;
import maxwell_lt.mobblocker.Config;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import static maxwell_lt.mobblocker.ModBlocks.AREAPROTECTOR_TILE;

/**
 * TileEntity for BlockAreaProtector
 * Handles world interactions
 * @see BlockAreaProtector
 */
public class TileEntityAreaProtector extends TileEntity implements ITickableTileEntity {

	public TileEntityAreaProtector() {
			super(AREAPROTECTOR_TILE);
		}

	/**
	 * Handles logic each tick
	 * Calls methods to deal with potentially hostile entities based on config options
	 * Only runs on the server side
	 */
	@Override
	public void tick() {

		if (!world.isRemote) {
			AxisAlignedBB areaBounds = getArea(getPos());

			if (Config.ENABLE_MOB_PROTECTION_AREA_PROTECTOR.get()) BlockMobs.teleportMobs(areaBounds, world);
			if (Config.ENABLE_SLIME_PROTECTION_AREA_PROTECTOR.get()) BlockMobs.teleportSlimes(areaBounds, world);
			if (Config.ENABLE_ARROW_PROTECTION_AREA_PROTECTOR.get()) BlockMobs.killArrows(areaBounds, world);
			if (Config.ENABLE_POTION_PROTECTION_AREA_PROTECTOR.get()) BlockMobs.killPotions(areaBounds, world);
			if (Config.ENABLE_WOLF_PROTECTION_AREA_PROTECTOR.get()) BlockMobs.calmAngryWolves(areaBounds, world);
		}
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

		xmax = blockpos.getX() + Config.AREA_PROTECTOR_X.get() + 1;
		ymax = blockpos.getY() + Config.AREA_PROTECTOR_Y.get();
		zmax = blockpos.getZ() + Config.AREA_PROTECTOR_Z.get() + 1;

		xmin = blockpos.getX() - Config.AREA_PROTECTOR_X.get();
		ymin = blockpos.getY() - Config.AREA_PROTECTOR_Y.get();
		zmin = blockpos.getZ() - Config.AREA_PROTECTOR_Z.get();

		// Handles potential case of Y coordinate going out of range
		if (ymin < 1) ymin = 1;
		if (ymax > 256) ymax = 256;

		return new AxisAlignedBB(xmax, ymax, zmax, xmin, ymin, zmin);
	}
}
