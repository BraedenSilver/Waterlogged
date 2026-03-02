package net.braeden.waterlogged.worldgen;

import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Places duckweed only on water surfaces that are within 1–3 blocks of a solid
 * shore, creating a natural border between open water and land in swamp biomes.
 */
public class DuckweedShoreFeature extends Feature<NoneFeatureConfiguration> {

    private static final Direction[] HORIZONTALS = {
        Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
    };

    public DuckweedShoreFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        var random = ctx.random();

        int placed = 0;

        for (int attempt = 0; attempt < 64; attempt++) {
            BlockPos pos = origin.offset(
                    random.nextInt(16) - 8,
                    0,
                    random.nextInt(16) - 8
            );

            // Find the water surface at this XZ position
            BlockPos waterSurface = findWaterSurface(level, pos);
            if (waterSurface == null) continue;

            // Duckweed sits one block above the water surface (like lily pads)
            BlockPos plantPos = waterSurface.above();
            if (!level.getBlockState(plantPos).isAir()) continue;

            // Only place within 1–3 blocks of a solid shore.
            // Check at waterSurface Y (the water block level), not plantPos Y — at
            // plantPos Y, lily pads, logs and other duckweed are all solid non-fluid
            // and would create false positives far from any real shoreline.
            if (!isWithinShoreDistance(level, waterSurface)) continue;

            BlockState duckweed = WaterloggedBlocks.DUCKWEED.defaultBlockState();
            if (!duckweed.canSurvive(level, plantPos)) continue;

            level.setBlock(plantPos, duckweed, 2);
            placed++;
        }

        return placed > 0;
    }

    /** Scans ±4 blocks vertically to find a water block with empty fluid above. */
    private BlockPos findWaterSurface(WorldGenLevel level, BlockPos pos) {
        for (int dy = 4; dy >= -4; dy--) {
            BlockPos candidate = pos.offset(0, dy, 0);
            if (level.getFluidState(candidate).is(FluidTags.WATER)
                    && level.getFluidState(candidate.above()).isEmpty()) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Returns true if any solid (non-fluid) block exists within 1–3 horizontal
     * blocks at the same Y as the water surface. At that level, only actual terrain
     * (dirt/mud banks) can be solid — lily pads and surface clutter are one above.
     */
    private boolean isWithinShoreDistance(WorldGenLevel level, BlockPos waterSurface) {
        for (int dist = 1; dist <= 3; dist++) {
            for (Direction dir : HORIZONTALS) {
                BlockPos check = waterSurface.relative(dir, dist);
                if (level.getFluidState(check).isEmpty()
                        && !level.getBlockState(check).isAir()) {
                    return true;
                }
            }
        }
        return false;
    }
}
