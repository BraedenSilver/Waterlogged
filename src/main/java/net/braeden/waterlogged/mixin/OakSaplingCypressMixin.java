package net.braeden.waterlogged.mixin;

import net.braeden.waterlogged.worldgen.PondCypressFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts oak sapling growth: if a 3×3 grid of oak saplings is present,
 * cancel vanilla growth and generate a Pond Cypress tree instead.
 * No roots are placed (consistent with vanilla tree behaviour; farmable).
 */
@Mixin(SaplingBlock.class)
public class OakSaplingCypressMixin {

    @Inject(method = "advanceTree", at = @At("HEAD"), cancellable = true)
    private void waterlogged_advanceTree(ServerLevel level, BlockPos pos, BlockState state,
                                          RandomSource rand, CallbackInfo ci) {
        if (!state.is(Blocks.OAK_SAPLING)) return;

        // pos can be any of the 9 cells — try every possible SW corner offset
        for (int cornerDx = 0; cornerDx >= -2; cornerDx--) {
            for (int cornerDz = 0; cornerDz >= -2; cornerDz--) {
                BlockPos corner = pos.offset(cornerDx, 0, cornerDz);
                if (!isOakSaplingGrid(level, corner)) continue;

                // Find ground below the grid centre before committing
                BlockPos center = corner.offset(1, 0, 1);
                BlockPos ground = PondCypressFeature.findGround((WorldGenLevel) level, center);
                if (ground == null) continue;

                // Commit: remove all 9 saplings
                for (int dx = 0; dx < 3; dx++)
                    for (int dz = 0; dz < 3; dz++)
                        level.removeBlock(corner.offset(dx, 0, dz), false);

                int height = 15 + rand.nextInt(6);
                PondCypressFeature.generateTree((WorldGenLevel) level, ground.above(), height, rand, false);
                ci.cancel();
                return;
            }
        }
    }

    private static boolean isOakSaplingGrid(ServerLevel level, BlockPos corner) {
        for (int dx = 0; dx < 3; dx++)
            for (int dz = 0; dz < 3; dz++)
                if (!level.getBlockState(corner.offset(dx, 0, dz)).is(Blocks.OAK_SAPLING))
                    return false;
        return true;
    }
}
