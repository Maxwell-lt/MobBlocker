package maxwell_lt.mobblocker.integration;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TOPCompatibility {

    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
    }


    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;
        private static Logger logger = LogManager.getLogger();

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            logger.info("Enabled support for The One Probe");
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return "modtut:default";
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
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
