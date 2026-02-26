package net.braeden.waterlogged.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.braeden.waterlogged.block.AquariumGlassBlock;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Suppresses water face rendering on sides adjacent to AquariumGlassBlock so the
 * inside of an aquarium looks clear. Lava is intentionally unaffected so aquarium
 * glass cannot be exploited to see through lava for ancient debris hunting.
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    /**
     * Per-thread flag: true when the current tesselate() call is rendering water.
     * ThreadLocal is required because chunk compilation runs on multiple worker threads —
     * a plain static would cause races where a lava chunk reads a stale "water" flag
     * set by another thread, incorrectly hiding lava faces.
     */
    @Unique
    private static final ThreadLocal<Boolean> waterlogged_isRenderingWater =
            ThreadLocal.withInitial(() -> false);

    @Inject(method = "tesselate", at = @At("HEAD"))
    private void waterlogged_captureFluidType(
            BlockAndTintGetter level,
            BlockPos pos,
            VertexConsumer consumer,
            BlockState blockState,
            FluidState fluidState,
            CallbackInfo ci
    ) {
        waterlogged_isRenderingWater.set(fluidState.is(FluidTags.WATER));
    }

    @Inject(
            method = "isFaceOccludedByNeighbor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void waterlogged_occludeAquariumGlass(
            Direction direction,
            float f,
            BlockState blockState,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (waterlogged_isRenderingWater.get() && blockState.getBlock() instanceof AquariumGlassBlock) {
            cir.setReturnValue(true);
        }
    }
}
