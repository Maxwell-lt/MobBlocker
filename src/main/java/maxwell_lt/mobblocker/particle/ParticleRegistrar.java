package maxwell_lt.mobblocker.particle;

import net.minecraft.client.Minecraft;

public class ParticleRegistrar {
    public static void registerParticles() {
        Minecraft.getInstance().particles.registerFactory(ParticleTypeRegistrar.BOUNDING_BOX, BoundingBoxParticle.Factory::new);
    }
}
