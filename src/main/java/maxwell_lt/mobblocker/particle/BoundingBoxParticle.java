package maxwell_lt.mobblocker.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BoundingBoxParticle extends SpriteTexturedParticle {
    public BoundingBoxParticle(World world, double x, double y, double z, double xVel, double yVel, double zVel, float red, float green, float blue) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.motionX = xVel;
        this.motionY = yVel;
        this.motionZ = zVel;

        this.maxAge = 30;
        this.onUpdate();
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BoundingBoxParticleData> {

        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(BoundingBoxParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BoundingBoxParticle particle = new BoundingBoxParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.red, data.green, data.blue);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}
