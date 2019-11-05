package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.handler.MobRemovalHandler;
import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.particle.ParticleBoxHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
			if (Config.ENABLE_MOB_PROTECTION_AREA_PROTECTOR.get()) MobRemovalHandler.teleportMobs(areaBounds, world);
			if (Config.ENABLE_SLIME_PROTECTION_AREA_PROTECTOR.get()) MobRemovalHandler.teleportSlimes(areaBounds, world);
			if (Config.ENABLE_ARROW_PROTECTION_AREA_PROTECTOR.get()) MobRemovalHandler.killArrows(areaBounds, world);
			if (Config.ENABLE_POTION_PROTECTION_AREA_PROTECTOR.get()) MobRemovalHandler.killPotions(areaBounds, world);
			if (Config.ENABLE_WOLF_PROTECTION_AREA_PROTECTOR.get()) MobRemovalHandler.calmAngryWolves(areaBounds, world);
		} else {
			if (Config.ENABLE_PARTICLE_BOX.get() && world.getDayTime() % 20 == 0) {
				AxisAlignedBB areaBounds = getArea(getPos());
				ParticleBoxHandler.drawBox(areaBounds, world, 0.624F, 0.192F, 0.192F);
			}
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

	/**
	 * Returns CompoundNBT to be sent through networking
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	/**
	 * Creates data packets
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.write(nbtTag);
		return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
	}

	/**
	 * Handles data packets
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	/**
	 * Restores block on chunk reload
	 * Used for saving block data upon chunk unload
	 */
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
	}

	/**
	 * Serializes data upon chunk unload
	 * @return CompoundNBT containing entire NBT structure of block
	 */
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		return compound;
	}
}
