package maxwell_lt.mobblocker.particle;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MobBlocker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistrationEventHandler {

    @SubscribeEvent
    public static void onParticleTypeRegistryEvent(RegistryEvent.Register<ParticleType<?>> event) {
        ParticleTypeRegistrar.registerParticleTypes();
    }

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ParticleRegistrar.registerParticles();
    }
}
