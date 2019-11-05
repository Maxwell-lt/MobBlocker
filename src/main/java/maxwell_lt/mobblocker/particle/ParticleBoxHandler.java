package maxwell_lt.mobblocker.particle;

import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ParticleBoxHandler {
    public static void drawBox(AxisAlignedBB area, World world, float red, float green, float blue) {
        IParticleData particleData = new BoundingBoxParticleData(ParticleTypeRegistrar.BOUNDING_BOX, red, green, blue);
        for (double i = area.minX; i < area.maxX; i += 0.5) {
            world.addParticle(particleData, i, area.maxY, area.maxZ, 0, 0, 0);
            world.addParticle(particleData, i, area.minY, area.maxZ, 0, 0, 0);
            world.addParticle(particleData, i, area.maxY, area.minZ, 0, 0, 0);
            world.addParticle(particleData, i, area.minY, area.minZ, 0, 0, 0);
        }
        for (double i = area.minY; i < area.maxY; i += 0.5) {
            world.addParticle(particleData, area.maxX, i, area.maxZ, 0, 0, 0);
            world.addParticle(particleData, area.minX, i, area.maxZ, 0, 0, 0);
            world.addParticle(particleData, area.maxX, i, area.minZ, 0, 0, 0);
            world.addParticle(particleData, area.minX, i, area.minZ, 0, 0, 0);
        }
        for (double i = area.minZ; i < area.maxZ; i += 0.5) {
            world.addParticle(particleData, area.maxX, area.maxY, i, 0, 0, 0);
            world.addParticle(particleData, area.minX, area.maxY, i, 0, 0, 0);
            world.addParticle(particleData, area.maxX, area.minY, i, 0, 0, 0);
            world.addParticle(particleData, area.minX, area.minY, i, 0, 0, 0);
        }
        world.addParticle(particleData, area.maxX, area.maxY, area.maxZ, 0, 0, 0);
    }
}
