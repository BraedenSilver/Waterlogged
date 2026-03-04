package net.braeden.waterlogged.entity.client.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class AnemoneBlockEntityRenderState extends BlockEntityRenderState {
    public final AnimationState vibingAnimationState = new AnimationState();
    public float ageInTicks;
}
