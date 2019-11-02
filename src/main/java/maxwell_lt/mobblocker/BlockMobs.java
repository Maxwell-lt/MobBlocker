package maxwell_lt.mobblocker;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.List;
import java.util.Random;

public class BlockMobs {
    private static Random rand = new Random();
    /**
     * Calls this.teleport on all mobs deriving from EntityMob
     * @param areaBounds AxisAlignedBB in which block should act
     */
    public static void teleportMobs(AxisAlignedBB areaBounds, World world) {

        // Gets a list of all the entities in the same chunk as this block
        List<MobEntity> entityMobList =  world.getEntitiesWithinAABB(MobEntity.class, areaBounds);

        for (MobEntity entity : entityMobList) {
            teleport(entity, world);
        }
    }

    /**
     * Calls this.teleport on all mobs deriving from EntitySlime
     * @param areaBounds AxisAlignedBB in which block should act
     */
    public static void teleportSlimes(AxisAlignedBB areaBounds, World world) {
        List<SlimeEntity> entitySlimeList = world.getEntitiesWithinAABB(SlimeEntity.class, areaBounds);
        for (SlimeEntity entity : entitySlimeList) {
            teleport(entity, world);
        }
    }

    /**
     * Destroys skeleton arrows in the protected area
     * Checks whether the arrow was shot by a class deriving from IRangedAttackMob
     * Sets the arrow on fire for visual effect by default
     * An arrow already on fire is killed
     * The effect of this is that the arrow, for one tick, is engulfed in flame, as it looks strange to have the arrow disappear for no reason
     * @param areaBounds AxisAlignedBB in which block should act
     */
    public static void killArrows(AxisAlignedBB areaBounds, World world) {
        List<ArrowEntity> list =  world.getEntitiesWithinAABB(ArrowEntity.class, areaBounds);
        for (ArrowEntity arrow : list) {
            if (true /*arrow.shootingEntity instanceof IRangedAttackMob*/) {
                if (arrow.isBurning()) {
                    arrow.remove();
                } else {
                    arrow.setFire(1);
                    arrow.setVelocity(0, 0, 0);
                }
            }
        }
    }

    /**
     * Destroys Witch Potions that are currently in the protected area
     * Only destroys the EntityPotion if it was thrown by an EntityWitch or derivatives
     * @param areaBounds AxisAlignedBB in which block should act
     */
    public static void killPotions(AxisAlignedBB areaBounds, World world) {
        List<PotionEntity> list =  world.getEntitiesWithinAABB(PotionEntity.class, areaBounds);
        for (PotionEntity potion : list) {
            if (potion.getThrower() instanceof WitchEntity) {
                potion.remove();
            }
        }
    }

    /**
     * (BROKEN) Calms angry wolves in the protected area
     * If anyone has any idea how to make this work, I'd love to hear it.
     * @param areaBounds AxisAlignedBB in which block should act
     */
    public static void calmAngryWolves(AxisAlignedBB areaBounds, World world) {
        // Currently broken, the wolf that was hit directly does not lose aggro.
        // The wolves aggroed indirectly are properly calmed
        List<WolfEntity> list =  world.getEntitiesWithinAABB(WolfEntity.class, areaBounds);
        for (WolfEntity wolf : list) {
            if (wolf.isAngry()) {
                wolf.setAttackTarget(null);
                wolf.setRevengeTarget(null);
                wolf.setAngry(false);
            }
        }
    }

    /**
     * Teleports entities randomly up to 16 blocks away
     * Copy of code from Enderman class
     * Tries 10 times to teleport the entity before giving for this tick cycle
     * @param entity Entity to be teleported
     */
    private static void teleport(LivingEntity entity, World world) {
        boolean moved = false; 	// Stores the status of teleportation attempts.
        int counter = 0;		// Used to prevent infinite loops.
        while (!moved) {
            counter++;
            if (counter > 10) break; // Breaks out of a possible infinite loop.

            // Implementation of Enderman random teleport code:
            double newX = entity.posX + (rand.nextDouble() - 0.5D) * 64.0D;
            double newY = entity.posY + (double)(rand.nextInt(64) - 32);
            double newZ = entity.posZ + (rand.nextDouble() - 0.5D) * 64.0D;
            moved = entity.attemptTeleport(newX, world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(newX, newY, newZ)).getY(), newZ, true);
        }
    }
}
