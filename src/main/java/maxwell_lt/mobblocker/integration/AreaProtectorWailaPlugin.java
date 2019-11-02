package maxwell_lt.mobblocker.integration;

import maxwell_lt.mobblocker.Config;
import maxwell_lt.mobblocker.blocks.BlockAreaProtector;
import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

@WailaPlugin
public class AreaProtectorWailaPlugin implements IWailaPlugin, IComponentProvider, IServerDataProvider<TileEntity> {

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        tag.putIntArray("areaprotector_size", new int[] {Config.AREA_PROTECTOR_X.get(), Config.AREA_PROTECTOR_Y.get(), Config.AREA_PROTECTOR_Z.get()});
    }

    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.registerComponentProvider(this, TooltipPosition.BODY, BlockAreaProtector.class);
        iRegistrar.registerBlockDataProvider(this, BlockAreaProtector.class);
    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        int[] stats = accessor.getServerData().getIntArray("areaprotector_size");
        if (stats.length != 3) return;
        tooltip.add(new TranslationTextComponent("mobblocker.areaprotectorx", stats[0]));
        tooltip.add(new TranslationTextComponent("mobblocker.areaprotectory", stats[1]));
        tooltip.add(new TranslationTextComponent("mobblocker.areaprotectorz", stats[2]));
    }
}
