package com.verdantartifice.primalmagic.common.entities.misc;

import java.util.List;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.BlockUtil.FoundRectangle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Definition of a flying carpet entity, a vehicle that can be used by players to soar through the sky.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetEntity extends Entity {
    protected static final EntityDataAccessor<Integer> DYE_COLOR = SynchedEntityData.defineId(FlyingCarpetEntity.class, EntityDataSerializers.INT);

    private float momentum;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private boolean forwardInputDown;
    private boolean backInputDown;

    public FlyingCarpetEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.blocksBuilding = true;
        this.setNoGravity(true);
    }
    
    public FlyingCarpetEntity(Level worldIn, double x, double y, double z) {
        this(EntityTypesPM.FLYING_CARPET.get(), worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
    
    public FlyingCarpetEntity(Level worldIn, BlockPos pos) {
        this(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DYE_COLOR, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        // Nothing to do
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        // Nothing to do
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !this.isPassengerOfSameVehicle(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected Vec3 getRelativePortalPosition(Axis axis, FoundRectangle result) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.1D;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double)yaw;
        this.lerpPitch = (double)pitch;
        this.lerpSteps = 10;
    }

    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        super.tick();
        this.tickLerp();
        
        if (this.isVehicle() && this.isControlledByLocalInstance()) {
            this.updateMotion();
            if (this.level.isClientSide) {
                this.controlCarpet();
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
            if (this.level.isClientSide) {
                this.updateInputs(false, false);
            }
        }
        
        this.checkInsideBlocks();
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps > 0) {
            double newX = this.getX() + ((this.lerpX - this.getX()) / (double)this.lerpSteps);
            double newY = this.getY() + ((this.lerpY - this.getY()) / (double)this.lerpSteps);
            double newZ = this.getZ() + ((this.lerpZ - this.getZ()) / (double)this.lerpSteps);
            double deltaYaw = Mth.wrapDegrees(this.lerpYaw - (double)this.getYRot());
            float yRot = (float)((double)this.getYRot() + (deltaYaw / (double)this.lerpSteps));
            float xRot = (float)((double)this.getXRot() + ((this.lerpPitch - (double)this.getXRot()) / (double)this.lerpSteps));
            this.lerpSteps--;
            this.setPos(newX, newY, newZ);
            this.setRot(yRot, xRot);
        }
    }
    
    private void updateMotion() {
        this.momentum = 0.9F;
        this.setDeltaMovement(this.getDeltaMovement().scale(this.momentum).add(0.0D, (this.isNoGravity() ? 0.0D : -0.04D), 0.0D));
    }
    
    private void controlCarpet() {
        if (this.isVehicle()) {
            Entity pilot = this.getControllingPassenger();
            this.yRotO = this.getYRot();
            this.setYRot(pilot.getYRot());
            
            float f = 0.0F;
            if (this.forwardInputDown) {
                f += 0.03F;
            }
            if (this.backInputDown) {
                f -= 0.005F;
            }
            Vec3 newMotion = this.getDeltaMovement().add(
                    (double)(Mth.sin(-this.getYRot() * (float)(Math.PI / 180.0D)) * f), 
                    (double)(Mth.sin(-pilot.getXRot() * (float)(Math.PI / 180.0D)) * f), 
                    (double)(Mth.cos(this.getYRot() * (float)(Math.PI / 180.0D)) * f));
            this.setDeltaMovement(newMotion);
        }
    }
    
    public void updateInputs(boolean forwardDown, boolean backwardDown) {
        this.forwardInputDown = forwardDown;
        this.backInputDown = backwardDown;
    }

    /**
     * Applies this carpet's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onPassengerTurned(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level.isClientSide && this.isAlive()) {
            if (player.isSecondaryUseActive()) {
                ItemStack stack = new ItemStack(ItemsPM.FLYING_CARPET.get());
                DyeColor color = this.getDyeColor();
                if (color != null) {
                    ((FlyingCarpetItem)stack.getItem()).setDyeColor(stack, color);
                }
                this.spawnAtLocation(stack, 0.0F);
                this.discard();
                return InteractionResult.SUCCESS;
            } else {
                player.startRiding(this);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    public DyeColor getDyeColor() {
        int value = this.entityData.get(DYE_COLOR).intValue();
        if (value == -1) {
            return null;
        } else {
            return DyeColor.byId(value);
        }
    }
    
    public void setDyeColor(DyeColor color) {
        if (color == null) {
            this.entityData.set(DYE_COLOR, Integer.valueOf(-1));
        } else {
            this.entityData.set(DYE_COLOR, Integer.valueOf(color.getId()));
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        // Shield the carpet and passenger from falling damage
        this.fallDistance = 0;
        if (this.isVehicle()) {
            this.getControllingPassenger().fallDistance = 0;
        }
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYaw, (float)this.lerpPitch);
        }
    }
}
