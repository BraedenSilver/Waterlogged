package net.braeden.waterlogged.worldgen;

import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Scatters a small washed-up sargassum cluster on dry beach sand.
 * Origin is the MOTION_BLOCKING surface (the sand block itself).
 */
public class BeachSargassumFeature extends Feature<NoneFeatureConfiguration> {

    public BeachSargassumFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        var random = ctx.random();

        int placed = 0;

        for (int attempt = 0; attempt < 20; attempt++) {
            BlockPos base = origin.offset(
                    random.nextInt(9) - 4,
                    0,
                    random.nextInt(9) - 4
            );

            // Find the dry solid surface at this XZ (scan ±4 vertically)
            BlockPos surface = findDrySurface(level, base);
            if (surface == null) continue;

            BlockPos plantPos = surface.above();
            if (!level.getBlockState(plantPos).isAir()) continue;
            if (!level.getFluidState(plantPos).isEmpty()) continue;

            // Default state is FACING=UP, WATERLOGGED=false — survives on any sturdy surface
            BlockState sargassum = WaterloggedBlocks.SARGASSUM.defaultBlockState();
            if (!sargassum.canSurvive(level, plantPos)) continue;

            level.setBlock(plantPos, sargassum, 2);
            placed++;
        }

        return placed > 0;
    }

    /**
     * Scans ±4 blocks vertically to find a solid, non-waterlogged block with
     * dry air above it (no water at either the block or above it).
     */
    private BlockPos findDrySurface(WorldGenLevel level, BlockPos pos) {
        for (int dy = 4; dy >= -4; dy--) {
            BlockPos candidate = pos.offset(0, dy, 0);
            if (!level.getFluidState(candidate).isEmpty()) continue;
            if (!level.getBlockState(candidate).isFaceSturdy(level, candidate, net.minecraft.core.Direction.UP)) continue;
            BlockPos above = candidate.above();
            if (!level.getBlockState(above).isAir()) continue;
            if (!level.getFluidState(above).isEmpty()) continue;
            return candidate;
        }
        return null;
    }
}
