package maxwell_lt.mobblocker.integration;

import maxwell_lt.mobblocker.MobBlocker;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public class TOPCompatibility {

    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "maxwell_lt.mobblocker.integration.TOPCompatibility$GetTheOneProbe");
    }


    public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            MobBlocker.logger.log(Level.INFO, "Enabled support for The One Probe");
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return "modtut:default";
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                    if (blockState.getBlock() instanceof TOPInfoProvider) {
                        TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                        provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                    }

                }
            });
            return null;
        }
    }
}