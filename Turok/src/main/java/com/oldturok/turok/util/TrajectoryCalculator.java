package com.oldturok.turok.util;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TrajectoryCalculator {
    public static ThrowingType getThrowType(EntityLivingBase entity) {
        if (entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return ThrowingType.NONE;
        }

        ItemStack itemStack = entity.getHeldItem(EnumHand.MAIN_HAND);
        Item item = itemStack.getItem();

        if (item instanceof ItemPotion) {

            if (itemStack.getItem() instanceof ItemSplashPotion){
                return ThrowingType.POTION;
            }
        } else if (item instanceof ItemBow && entity.isHandActive()) {
            return ThrowingType.BOW;
        } else if (item instanceof ItemExpBottle) {
            return ThrowingType.EXPERIENCE;
        } else if (item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl) {
            return ThrowingType.NORMAL;
        }

        return ThrowingType.NONE;
    }

    public enum ThrowingType {
        NONE, BOW, EXPERIENCE, POTION, NORMAL
    }

    public static final class FlightPath {
        private EntityLivingBase shooter;
        public Vec3d position;
        private Vec3d motion;
        private float yaw;
        private float pitch;
        private AxisAlignedBB boundingBox;
        private boolean collided;
        private RayTraceResult target;
        private ThrowingType throwingType;

        public FlightPath(EntityLivingBase entityLivingBase, ThrowingType throwingType) {
            this.shooter = entityLivingBase;
            this.throwingType = throwingType;

            double[] ipos = interpolate(shooter);

            this.setLocationAndAngles(ipos[0] + Wrapper.getMinecraft().getRenderManager().renderPosX, ipos[1] + this.shooter.getEyeHeight() + Wrapper.getMinecraft().getRenderManager().renderPosY, ipos[2] + Wrapper.getMinecraft().getRenderManager().renderPosZ,
                    this.shooter.rotationYaw, this.shooter.rotationPitch);
            Vec3d startingOffset = new Vec3d(MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * 0.16F, 0.1d,
                    MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * 0.16F);
            this.position = this.position.subtract(startingOffset);
            this.setPosition(this.position);

            this.motion = new Vec3d(-MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI),
                    -MathHelper.sin(this.pitch / 180.0F * (float) Math.PI),
                    MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI));
            this.setThrowableHeading(this.motion, this.getInitialVelocity());
        }

        public void onUpdate() {
            Vec3d prediction = this.position.add(this.motion);
            RayTraceResult blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction, false, true, false);

            if (blockCollision != null) {
                prediction = blockCollision.hitVec;
            }

            this.onCollideWithEntity(prediction, blockCollision);

            if (this.target != null) {
                this.collided = true;
                this.setPosition(this.target.hitVec);
                return;
            }

            if (this.position.y <= 0.0d) {
                this.collided = true;
                return;
            }

            this.position = this.position.add(this.motion);
            float motionModifier = 0.99F;

            if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.WATER)) {
                motionModifier = this.throwingType == ThrowingType.BOW ? 0.6F : 0.8F;
            }

            this.motion = mult(this.motion, motionModifier);
            this.motion = this.motion.subtract(0.0d, this.getGravityVelocity(), 0.0d);
            this.setPosition(this.position);
        }
        private void onCollideWithEntity(Vec3d prediction, RayTraceResult blockCollision) {
            Entity collidingEntity = null;
            double currentDistance = 0.0d;
            List<Entity> collisionEntities = this.shooter.world.getEntitiesWithinAABBExcludingEntity(this.shooter, this.boundingBox.expand(this.motion.x, this.motion.y, this.motion.z).expand(1.0D, 1.0D, 1.0D));;

            for (Entity entity : collisionEntities) {
                if (!entity.canBeCollidedWith() && entity != this.shooter) {
                    continue;
                }

                float collisionSize = entity.getCollisionBorderSize();
                AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                RayTraceResult objectPosition = expandedBox.calculateIntercept(this.position, prediction);

                if (objectPosition != null) {
                    double distanceTo = this.position.distanceTo(objectPosition.hitVec);

                    if (distanceTo < currentDistance || currentDistance == 0.0D) {
                        collidingEntity = entity;
                        currentDistance = distanceTo;
                    }
                }
            }

            if (collidingEntity != null) {
                this.target = new RayTraceResult(collidingEntity);
            } else {
                this.target = blockCollision;
            }
        }

        private float getInitialVelocity() {
            Item item = this.shooter.getHeldItem(EnumHand.MAIN_HAND).getItem();
            switch (this.throwingType) {
                case BOW:
                    ItemBow bow = (ItemBow) item;
                    int useDuration = bow.getMaxItemUseDuration(this.shooter.getHeldItem(EnumHand.MAIN_HAND)) - this.shooter.getItemInUseCount();
                    float velocity = (float) useDuration / 20.0F;
                    velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
                    if (velocity > 1.0F) {
                        velocity = 1.0F;
                    }

                    return (velocity * 2.0f) * 1.5f;
                case POTION:
                    return 0.5F;
                case EXPERIENCE:
                    return 0.7F;
                case NORMAL:
                    return 1.5f;
            }
            return 1.5f;
        }

        private float getGravityVelocity() {
            switch (this.throwingType) {
                case BOW:
                case POTION:
                    return 0.05f;
                case EXPERIENCE:
                    return 0.07f;
                case NORMAL:
                    return 0.03f;
            }

            return 0.03f;
        }

        private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
            this.position = new Vec3d(x, y, z);
            this.yaw = yaw;
            this.pitch = pitch;
        }

        private void setPosition(Vec3d position) {
            this.position = new Vec3d(position.x, position.y, position.z);
            // Usually this is this.width / 2.0f but throwables change
            double entitySize = (this.throwingType == ThrowingType.BOW ? 0.5d : 0.25d) / 2.0d;
            // Update the path's current bounding box
            this.boundingBox = new AxisAlignedBB(position.x - entitySize,
                    position.y - entitySize,
                    position.z - entitySize,
                    position.x + entitySize,
                    position.y + entitySize,
                    position.z + entitySize);
        }

        private void setThrowableHeading(Vec3d motion, float velocity) {
            // Divide the current motion by the length of the vector
            this.motion = div(motion, (float) motion.length());
            // Multiply by the velocity
            this.motion = mult(this.motion, velocity);
        }

        public boolean isCollided() {
            return collided;
        }

        public RayTraceResult getCollidingTarget() {
            return target;
        }
    }

    public static double[] interpolate(Entity entity) {
        double posX = interpolate(entity.posX, entity.lastTickPosX) - Wrapper.getMinecraft().renderManager.renderPosX;
        double posY = interpolate(entity.posY, entity.lastTickPosY) - Wrapper.getMinecraft().renderManager.renderPosY;
        double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - Wrapper.getMinecraft().renderManager.renderPosZ;
        return new double[] { posX, posY, posZ };
    }

    public static double interpolate(double now, double then) {
        return then + (now - then) * Wrapper.getMinecraft().getRenderPartialTicks();
    }

    public static Vec3d mult(Vec3d factor, float multiplier) {
        return new Vec3d(factor.x * multiplier, factor.y * multiplier, factor.z * multiplier);
    }

    public static Vec3d div(Vec3d factor, float divisor) {
        return new Vec3d(factor.x / divisor, factor.y / divisor, factor.z / divisor);
    }
}
