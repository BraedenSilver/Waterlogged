package net.braeden.waterlogged.entity.client.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class PelicanRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState swimmingAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState divingAnimationState = new AnimationState();
    public final AnimationState flappingAnimationState = new AnimationState();
    public final AnimationState beakOpenedAnimationState = new AnimationState();
    public boolean hasHeldEntity = false;
}
