package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;

public class BlockAreaProtector extends Block implements ITileEntityProvider {
	
	public BlockAreaProtector() {
        super(Material.ROCK);
        setUnlocalizedName(MobBlocker.MODID + ".areaprotector");
        setRegistryName("areaprotector");
        setHardness(1.5F);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileEntityAreaProtector.class, MobBlocker.MODID + "_areaprotector");
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
    			0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAreaProtector();
	}
}
