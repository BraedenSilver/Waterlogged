package net.braeden.waterlogged.worldgen;

import net.braeden.waterlogged.block.SargassumBlock;
import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Places a cohesive sargassum iceberg patch. Each patch is a randomly rotated
 * ellipse: the centre goes 3–4 blocks deep, depth tapers toward the edges, and
 * the outermost fringe is floating sargassum only with no block column below.
 *
 * Origin is the topmost water block (MOTION_BLOCKING heightmap).
 */
public class SargassumPatchFeature extends Feature<NoneFeatureConfiguration> {

    private static final float     POWER         = 1.4f;
    private static final Direction[] SIDE_AND_DOWN = {
        Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN
    };

    public SargassumPatchFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level  = ctx.level();
        BlockPos      origin = ctx.origin();
        var           random = ctx.random();

        // Per-patch shape parameters
        int   maxDepth = 3 + random.nextInt(2);          // 3 or 4 blocks deep
        int   radius   = 5 + random.nextInt(4);          // 5–8 block bounding radius
        float angle    = random.nextFloat() * (float) Math.PI;
        float cos      = (float) Math.cos(angle);
        float sin      = (float) Math.sin(angle);
        // Stretch one axis more than the other for fluid, non-circular blobs
        float scaleX   = 0.55f + random.nextFloat() * 0.65f; // 0.55–1.2
        float scaleZ   = 0.55f + random.nextFloat() * 0.65f; // 0.55–1.2

        boolean placed = false;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {

                // Rotate and scale into ellipse space
                float rx = ( dx * cos + dz * sin) / scaleX;
                float rz = (-dx * sin + dz * cos) / scaleZ;
                float dist = (float) Math.sqrt(rx * rx + rz * rz) / radius;
                if (dist > 1.0f) continue;

                // Per-cell jitter for a ragged, organic outline
                float jitter = (random.nextFloat() - 0.5f) * 0.45f;
                float d = Math.min(1.0f, Math.max(0.0f, dist + jitter));

                // Column depth: maxDepth at centre, 0 at rim
                int depth = Math.round(maxDepth * (float) Math.pow(1.0f - d, POWER));

                // Thin fringe: occasionally skip a cell entirely for a ragged edge
                if (depth == 0 && random.nextFloat() < 0.4f) continue;

                BlockPos surface = findWaterSurface(level, origin.offset(dx, 0, dz));
                if (surface == null) continue;

                // ── Floating sargassum (one block above the water surface) ─────
                BlockPos plantPos = surface.above();
                BlockState plantSlot = level.getBlockState(plantPos);
                if (plantSlot.canBeReplaced() && level.getFluidState(plantPos).isEmpty()) {
                    BlockState plant = WaterloggedBlocks.SARGASSUM.defaultBlockState();
                    if (plant.canSurvive(level, plantPos)) {
                        level.setBlock(plantPos, plant, 2);
                        placed = true;
                    }
                }

                // ── Sargassum block column + side/bottom fronds ───────────────
                for (int y = 0; y < depth; y++) {
                    BlockPos blockPos = surface.below(y);
                    if (!level.getFluidState(blockPos).is(FluidTags.WATER)) break;
                    if (!level.getBlockState(blockPos).canBeReplaced()) break;
                    level.setBlock(blockPos,
                            WaterloggedBlocks.SARGASSUM_BLOCK.defaultBlockState()
                                    .setValue(SargassumBlock.WATERLOGGED, true),
                            2);
                    // Attach sargassum fronds to the sides and underside of each block
                    for (Direction dir : SIDE_AND_DOWN) {
                        if (random.nextFloat() > 0.25f) continue;
                        BlockPos frondPos = blockPos.relative(dir);
                        if (!level.getFluidState(frondPos).is(FluidTags.WATER)) continue;
                        if (!level.getBlockState(frondPos).canBeReplaced()) continue;
                        BlockState frond = WaterloggedBlocks.SARGASSUM.defaultBlockState()
                                .setValue(BlockStateProperties.FACING, dir)
                                .setValue(BlockStateProperties.WATERLOGGED, true);
                        if (frond.canSurvive(level, frondPos)) {
                            level.setBlock(frondPos, frond, 2);
                        }
                    }
                }
            }
        }

        return placed;
    }

    /**
     * Scans ±5 blocks vertically from {@code pos} to find the topmost water
     * block that has a non-fluid block (air or solid) directly above it.
     */
    private BlockPos findWaterSurface(WorldGenLevel level, BlockPos pos) {
        for (int dy = 5; dy >= -5; dy--) {
            BlockPos candidate = pos.offset(0, dy, 0);
            if (level.getFluidState(candidate).is(FluidTags.WATER)
                    && level.getFluidState(candidate.above()).isEmpty()) {
                return candidate;
            }
        }
        return null;
    }
}
