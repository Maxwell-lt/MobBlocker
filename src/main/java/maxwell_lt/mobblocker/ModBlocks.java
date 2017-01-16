package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	
	public static BlockChunkProtector chunkProtector;
	public static BlockAreaProtector areaProtector;
	
	public static void init() {
		chunkProtector = new BlockChunkProtector();
		areaProtector = new BlockAreaProtector();
	}
	
	@SideOnly(Side.CLIENT)
    public static void initClient() {
		chunkProtector.initModel();
		areaProtector.initModel();
	}
}
