package net.braeden.waterlogged.entity.client.state;

import net.braeden.waterlogged.entity.util.SunfishVariant;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class SunfishRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState floppingAnimationState = new AnimationState();
    public final AnimationState flopAnimationState = new AnimationState();
    public SunfishVariant variant = SunfishVariant.BLUEGILL;
}
