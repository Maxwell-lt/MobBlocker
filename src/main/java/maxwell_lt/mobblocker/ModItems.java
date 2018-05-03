package maxwell_lt.mobblocker;

import maxwell_lt.mobblocker.blocks.BlockAreaProtector;
import maxwell_lt.mobblocker.blocks.BlockChunkProtector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by maxwell on 7/11/17.
 */
public class ModItems {

    @GameRegistry.ObjectHolder("mobblocker:chunkprotector")
    public static BlockChunkProtector blockChunkProtector;

    @GameRegistry.ObjectHolder("mobblocker:areaprotector")
    public static BlockAreaProtector blockAreaProtector;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockChunkProtector.initModel();
        blockAreaProtector.initModel();
    }
}
