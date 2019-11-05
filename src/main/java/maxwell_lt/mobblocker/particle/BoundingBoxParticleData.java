package maxwell_lt.mobblocker.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class BoundingBoxParticleData implements IParticleData {

    public static final IParticleData.IDeserializer<BoundingBoxParticleData> DESERIALIZER = new IDeserializer<BoundingBoxParticleData>() {
        @Override
        public BoundingBoxParticleData deserialize(ParticleType<BoundingBoxParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new BoundingBoxParticleData(particleTypeIn, reader.readFloat(), reader.readFloat(), reader.readFloat());
        }

        @Override
        public BoundingBoxParticleData read(ParticleType<BoundingBoxParticleData> particleTypeIn, PacketBuffer buffer) {
            return new BoundingBoxParticleData(particleTypeIn, buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

    private final ParticleType<BoundingBoxParticleData> particleType;
    public float red;
    public float green;
    public float blue;

    public BoundingBoxParticleData(ParticleType<BoundingBoxParticleData> particleType, float red, float green, float blue) {
        this.particleType = particleType;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<?> getType() {
        return this.particleType;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(red);
        buffer.writeFloat(green);
        buffer.writeFloat(blue);
    }

    @Override
    public String getParameters() {
        return String.format("%f %f %f", this.red, this.green, this.blue);
    }
}
