package net.braeden.angling2.entity;

import net.braeden.angling2.criteria.AnglingCriteria;
import net.braeden.angling2.sound.AnglingSounds;
import net.braeden.angling2.tags.AnglingEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class PelicanEntity extends Animal implements FlyingAnimal {

    private static final EntityDataAccessor<String> HELD_ENTITY_ID =
            SynchedEntityData.defineId(PelicanEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> TRADED =
            SynchedEntityData.defineId(PelicanEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState swimmingAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState divingAnimationState = new AnimationState();
    public final AnimationState flappingAnimationState = new AnimationState();
    public final AnimationState beakOpenedAnimationState = new AnimationState();

    private int despawnDelay = 0;
    private int flyAwayTimer = -1;

    public PelicanEntity(EntityType<? extends PelicanEntity> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HELD_ENTITY_ID, "");
        builder.define(TRADED, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround() && !this.isInWater();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState blockState, BlockPos pos) {
        // No fall damage for pelicans
    }

    // --- Held entity methods ---

    public String getHeldEntityId() {
        return this.getEntityData().get(HELD_ENTITY_ID);
    }

    public void setHeldEntityId(String id) {
        this.getEntityData().set(HELD_ENTITY_ID, id);
    }

    public boolean hasHeldEntity() {
        // Check for actual passenger first, fall back to string ID
        return !getPassengers().isEmpty() || !getHeldEntityId().isEmpty();
    }

    /** Get the actual mob riding in the beak. */
    @Nullable
    public Entity getHeldPassenger() {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    public boolean hasTraded() {
        return this.getEntityData().get(TRADED);
    }

    public void setTraded(boolean traded) {
        this.getEntityData().set(TRADED, traded);
    }

    public void setDespawnDelay(int delay) {
        this.despawnDelay = delay;
    }

    // --- Choose random held entity from tags ---

    public void chooseRandomHeldEntity(ServerLevel level) {
        boolean useCommon = level.getRandom().nextFloat() < 0.8f;
        var tag = useCommon ? AnglingEntityTypeTags.COMMON_ENTITIES_IN_PELICAN_BEAK
                : AnglingEntityTypeTags.UNCOMMON_ENTITIES_IN_PELICAN_BEAK;

        List<EntityType<?>> candidates = BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(entityType -> entityType.builtInRegistryHolder().is(tag))
                .collect(Collectors.toList());

        if (!candidates.isEmpty()) {
            EntityType<?> chosen = candidates.get(level.getRandom().nextInt(candidates.size()));
            Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(chosen);
            setHeldEntityId(id.toString());
            spawnAndMountHeldEntity(level, chosen);
        }
    }

    /** Actually spawn the held mob and mount it as a passenger in the beak. */
    private void spawnAndMountHeldEntity(ServerLevel level, EntityType<?> type) {
        Entity entity = type.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (entity != null) {
            entity.snapTo(this.getX(), this.getY(), this.getZ());
            // Make the held entity passive: no AI, invulnerable, silent, scaled down
            if (entity instanceof Mob mob) {
                mob.setNoAi(true);
            }
            if (entity instanceof LivingEntity living) {
                var scaleAttr = living.getAttribute(Attributes.SCALE);
                if (scaleAttr != null) {
                    scaleAttr.setBaseValue(0.5);
                }
            }
            entity.setInvulnerable(true);
            entity.setSilent(true);
            level.addFreshEntity(entity);
            entity.startRiding(this, true, true);
        }
    }

    // --- Release the held entity ---

    private void releaseHeldEntity(ServerLevel level) {
        Entity passenger = getHeldPassenger();
        if (passenger != null) {
            // Restore normal behavior before dismounting
            if (passenger instanceof Mob mob) {
                mob.setNoAi(false);
            }
            if (passenger instanceof LivingEntity living) {
                var scaleAttr = living.getAttribute(Attributes.SCALE);
                if (scaleAttr != null) {
                    scaleAttr.setBaseValue(1.0);
                }
            }
            passenger.setInvulnerable(false);
            passenger.setSilent(false);
            passenger.stopRiding();
            // Position it at the pelican's feet
            passenger.snapTo(this.getX(), this.getY(), this.getZ());
        }
        setHeldEntityId("");
    }

    // --- Passenger positioning: place rider in the beak ---

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions entityDimensions, float f) {
        // +Z is forward (the direction the entity faces) in entity attachment space
        float yRotRad = -this.getYRot() * Mth.DEG_TO_RAD;
        // When flying, the head dips down and stretches forward in the animation
        double yOffset = isFlying() ? 0.6 : 1.1;
        double zOffset = isFlying() ? 1.1 : 0.6;
        return new Vec3(0.0, yOffset * f, zOffset * f).yRot(yRotRad);
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().isEmpty();
    }

    // --- Trading interaction ---

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.level().isClientSide() && hasHeldEntity() && !hasTraded() && isFishItem(stack)) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            // Release the held entity
            releaseHeldEntity(serverLevel);

            // Consume the fish from the player
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Mark as traded
            setTraded(true);

            // Play sound
            this.playSound(SoundEvents.ITEM_PICKUP, 1.0f, 1.0f);

            // Trigger advancement
            if (player instanceof ServerPlayer sp) {
                AnglingCriteria.TRADED_WITH_PELICAN.trigger(sp);
            }

            // Start fly-away timer
            flyAwayTimer = 60; // 3 seconds before flying off

            // Heart particles
            this.level().broadcastEntityEvent(this, (byte) 7);

            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    private boolean isFishItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH)
                || stack.is(Items.PUFFERFISH) || stack.is(Items.COOKED_COD) || stack.is(Items.COOKED_SALMON)
                || isModdedFishItem(stack);
    }

    private boolean isModdedFishItem(ItemStack stack) {
        var item = stack.getItem();
        return item == net.braeden.angling2.item.AnglingItems.SUNFISH
                || item == net.braeden.angling2.item.AnglingItems.RAW_MAHI_MAHI
                || item == net.braeden.angling2.item.AnglingItems.RAW_CATFISH
                || item == net.braeden.angling2.item.AnglingItems.RAW_DONGFISH
                || item == net.braeden.angling2.item.AnglingItems.RAW_SEAHORSE
                || item == net.braeden.angling2.item.AnglingItems.RAW_BUBBLE_EYE
                || item == net.braeden.angling2.item.AnglingItems.RAW_ANOMALOCARIS
                || item == net.braeden.angling2.item.AnglingItems.RAW_ANGLERFISH;
    }

    // --- Sounds ---

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return AnglingSounds.ENTITY_PELICAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_PELICAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_PELICAN_DEATH;
    }

    // --- Finalize spawn: choose held entity ---

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                                   EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        if (world instanceof ServerLevel serverLevel) {
            chooseRandomHeldEntity(serverLevel);
        }
        return data;
    }

    // --- Save/Load ---

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("HeldEntityId", getHeldEntityId());
        output.putBoolean("Traded", hasTraded());
        output.putInt("DespawnDelay", despawnDelay);
        output.putInt("FlyAwayTimer", flyAwayTimer);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setHeldEntityId(input.getStringOr("HeldEntityId", ""));
        setTraded(input.getBooleanOr("Traded", false));
        despawnDelay = input.getIntOr("DespawnDelay", 0);
        flyAwayTimer = input.getIntOr("FlyAwayTimer", -1);
    }

    // --- Breeding disabled ---

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    // --- Tick behavior ---

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            // Migration: if we have a saved entity ID but no passenger, spawn it
            String heldId = getHeldEntityId();
            if (!heldId.isEmpty() && getPassengers().isEmpty()) {
                Identifier entityId = Identifier.tryParse(heldId);
                if (entityId != null) {
                    ServerLevel serverLevel = (ServerLevel) this.level();
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(entityId);
                    spawnAndMountHeldEntity(serverLevel, type);
                }
            }

            // Despawn timer (like wandering trader)
            if (despawnDelay > 0) {
                despawnDelay--;
                if (despawnDelay <= 0 && !this.isRemoved()) {
                    // Discard any held passenger too
                    Entity passenger = getHeldPassenger();
                    if (passenger != null) {
                        passenger.stopRiding();
                        passenger.discard();
                    }
                    this.discard();
                    return;
                }
            }

            // Fly away after trading
            if (flyAwayTimer > 0) {
                flyAwayTimer--;
                if (flyAwayTimer <= 0) {
                    // Start flying up and away
                    this.setNoGravity(true);
                    this.despawnDelay = 200; // Despawn after 10 seconds of flying
                }
            }

            // If flying away (no gravity set after trade), move upward
            if (this.isNoGravity() && hasTraded()) {
                Vec3 motion = this.getDeltaMovement();
                this.setDeltaMovement(motion.x * 0.9, 0.15, motion.z * 0.9);
            }

            // Keep pelican afloat when in water (buoyancy)
            if (this.isInWater()) {
                Vec3 motion = this.getDeltaMovement();
                if (motion.y < 0) {
                    this.setDeltaMovement(motion.x, Math.max(motion.y, -0.02), motion.z);
                }
                if (this.isUnderWater()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, 0.08, 0));
                }
            }
        }

        // Client-side animations
        if (this.level().isClientSide()) {
            boolean inWater = this.isInWater();
            boolean onGround = this.onGround();
            boolean moving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4D;
            boolean flying = !onGround && !inWater;

            if (inWater) {
                this.swimmingAnimationState.startIfStopped(this.tickCount);
                this.idleAnimationState.stop();
                this.walkingAnimationState.stop();
                this.flyingAnimationState.stop();
            } else if (flying) {
                this.flyingAnimationState.startIfStopped(this.tickCount);
                this.idleAnimationState.stop();
                this.walkingAnimationState.stop();
                this.swimmingAnimationState.stop();
            } else if (moving) {
                this.walkingAnimationState.startIfStopped(this.tickCount);
                this.idleAnimationState.stop();
                this.flyingAnimationState.stop();
                this.swimmingAnimationState.stop();
            } else {
                this.idleAnimationState.startIfStopped(this.tickCount);
                this.walkingAnimationState.stop();
                this.flyingAnimationState.stop();
                this.swimmingAnimationState.stop();
            }

            // Beak open when holding a passenger and not traded yet
            if (!getPassengers().isEmpty() && !hasTraded()) {
                this.beakOpenedAnimationState.startIfStopped(this.tickCount);
            } else {
                this.beakOpenedAnimationState.stop();
            }

            this.divingAnimationState.stop();
            this.flappingAnimationState.stop();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                double ox = this.random.nextGaussian() * 0.02;
                double oy = this.random.nextGaussian() * 0.02;
                double oz = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART,
                        this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0 - this.getBbWidth(),
                        this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(),
                        this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0 - this.getBbWidth(),
                        ox, oy, oz);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    // Don't despawn naturally — controlled by our despawnDelay system
    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return despawnDelay > 0 || super.requiresCustomPersistence();
    }
}
