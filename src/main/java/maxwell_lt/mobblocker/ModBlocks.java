package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Initializes blocks
 */
public final class ModBlocks {

	@GameRegistry.ObjectHolder("mobblocker:chunkprotector")
	public static BlockChunkProtector chunkProtector;
	@GameRegistry.ObjectHolder("mobblocker:areaprotector")
	public static BlockAreaProtector areaProtector;
	
	/*
	public static void init() {
		chunkProtector = new BlockChunkProtector();
		areaProtector = new BlockAreaProtector();
	}*/
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		chunkProtector.initModel();
		areaProtector.initModel();
	}
}
