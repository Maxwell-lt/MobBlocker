package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Initializes blocks
 */
public final class ModBlocks {

	@ObjectHolder("mobblocker:chunkprotector")
	public static BlockChunkProtector CHUNKPROTECTOR;
	@ObjectHolder("mobblocker:chunkprotector")
	public static TileEntityType<TileEntityChunkProtector> CHUNKPROTECTOR_TILE;

	@ObjectHolder("mobblocker:areaprotector")
	public static BlockAreaProtector AREAPROTECTOR;
	@ObjectHolder("mobblocker:areaprotector")
	public static TileEntityType<TileEntityAreaProtector> AREAPROTECTOR_TILE;
	
	/*
	public static void init() {
		chunkProtector = new BlockChunkProtector();
		areaProtector = new BlockAreaProtector();
	}*/

    public static void initModels() {
		//CHUNKPROTECTOR.initModel();
		//AREAPROTECTOR.initModel();
	}
}
