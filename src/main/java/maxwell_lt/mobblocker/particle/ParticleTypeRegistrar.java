package maxwell_lt.mobblocker.particle;

import maxwell_lt.mobblocker.MobBlocker;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MobBlocker.MODID)
public class ParticleTypeRegistrar {
    public static ParticleType<BoundingBoxParticleData> BOUNDING_BOX = null;

    public static void registerParticleTypes() {
        BOUNDING_BOX = Registry.<ParticleType<BoundingBoxParticleData>>register(Registry.PARTICLE_TYPE,
                MobBlocker.MODID+":boundingbox",
                new ParticleType<>(false, BoundingBoxParticleData.DESERIALIZER));
    }
}
