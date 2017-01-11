package maxwell_lt.mobblocker.blocks;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChunkProtector extends Block {
    public BlockChunkProtector() {
        super(Material.ROCK);
        setUnlocalizedName(MobBlocker.MODID + ".chunkprotector");
        setRegistryName("chunkprotector");
        setHardness(1.5F);
        setLightLevel(15 / 16F);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
    			0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
