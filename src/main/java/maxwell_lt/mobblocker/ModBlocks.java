package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.*;

public class ModBlocks {
	
	public static BlockChunkProtector chunkProtector;
	
	public static void init() {
		chunkProtector = new BlockChunkProtector();
	}
}
