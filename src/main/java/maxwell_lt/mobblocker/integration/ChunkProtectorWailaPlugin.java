package maxwell_lt.mobblocker.integration;

import maxwell_lt.mobblocker.blocks.BlockChunkProtector;
import maxwell_lt.mobblocker.blocks.TileEntityChunkProtector;
import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

@WailaPlugin
public class ChunkProtectorWailaPlugin implements IWailaPlugin, IComponentProvider, IServerDataProvider<TileEntity> {
    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.registerComponentProvider(this, TooltipPosition.BODY, BlockChunkProtector.class);
        iRegistrar.registerBlockDataProvider(this, BlockChunkProtector.class);
    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        int[] stats = accessor.getServerData().getIntArray("chunkprotector_stats");
        if (stats.length != 2) return;
        if (accessor.getPlayer().isSneaking()) {
            tooltip.add(new TranslationTextComponent("mobblocker.ticksremaining", stats[0]));
        } else {
            tooltip.add(new TranslationTextComponent("mobblocker.secondsremaining", stats[1]));
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        if (te instanceof TileEntityChunkProtector) {
            TileEntityChunkProtector chunkProtector = (TileEntityChunkProtector) te;
            tag.putIntArray("chunkprotector_stats", new int[] {chunkProtector.getTicksBeforeDestroyed(), chunkProtector.getSecondsBeforeDestroyed()});
        }
    }
}
