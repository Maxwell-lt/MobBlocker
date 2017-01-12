package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChunkProtector extends Block implements ITileEntityProvider {
    
	public BlockChunkProtector() {
        super(Material.ROCK);
        setUnlocalizedName(MobBlocker.MODID + ".chunkprotector");
        setRegistryName("chunkprotector");
        setHardness(1.5F);
        setLightLevel(15 / 16F);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileEntityChunkProtector.class, MobBlocker.MODID + "_chunkprotector");
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
    			0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityChunkProtector();
	}
	
	private TileEntityChunkProtector getTE(IBlockAccess world, BlockPos pos) {
        return (TileEntityChunkProtector) world.getTileEntity(pos);
	}
}
