package net.braeden.waterlogged.worldgen;

import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

/**
 * Places a horizontal line of oak logs in swamp biomes.
 * Scans down past tree canopy to find actual water or solid ground.
 * Floating logs get moss carpet on top and duckweed in adjacent water.
 * Fallen logs get moss carpet and the occasional mushroom.
 */
public class SwampLogFeature extends Feature<NoneFeatureConfiguration> {

    public SwampLogFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        var random = ctx.random();

        // Scan down past tree canopy (leaves/logs) to find real water or ground
        BlockPos surface = findSurface(level, origin);
        if (surface == null) return false;

        boolean inWater = !level.getFluidState(surface).isEmpty();
        // Floating: log at water level (displaces water). Fallen: one block above ground.
        BlockPos logBase = inWater ? surface : surface.above();

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        Direction dir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        // Perpendicular directions for duckweed spread beside floating logs
        Direction perp = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;

        int length = 5 + random.nextInt(3); // 5, 6, or 7 blocks
        int offset = -random.nextInt(length);

        BlockState logState = Blocks.OAK_LOG.defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, axis);

        // Pre-validate every position before placing anything
        for (int i = 0; i < length; i++) {
            if (!canPlace(level, logBase.relative(dir, offset + i))) return false;
        }

        for (int i = 0; i < length; i++) {
            level.setBlock(logBase.relative(dir, offset + i), logState, 2);
        }

        // Decoration pass — moss, mushrooms, and duckweed
        for (int i = 0; i < length; i++) {
            BlockPos logPos = logBase.relative(dir, offset + i);
            BlockPos above = logPos.above();

            if (level.getBlockState(above).isAir()) {
                if (inWater) {
                    // Moss carpet on top of floating logs (~45% per block)
                    if (random.nextFloat() < 0.45f) {
                        level.setBlock(above, Blocks.MOSS_CARPET.defaultBlockState(), 2);
                    }
                } else {
                    // Fallen logs: moss carpet (~35%) or a mushroom (~20%)
                    float roll = random.nextFloat();
                    if (roll < 0.20f) {
                        BlockState mushroom = (random.nextBoolean()
                                ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM).defaultBlockState();
                        level.setBlock(above, mushroom, 2);
                    } else if (roll < 0.55f) {
                        level.setBlock(above, Blocks.MOSS_CARPET.defaultBlockState(), 2);
                    }
                }
            }

            // Duckweed in adjacent water cells beside floating logs (~35% per side)
            if (inWater) {
                for (Direction side : new Direction[]{perp, perp.getOpposite()}) {
                    if (random.nextFloat() < 0.35f) {
                        BlockPos waterCell = logPos.relative(side);
                        BlockPos duckweedPos = waterCell.above();
                        if (!level.getFluidState(waterCell).isEmpty()
                                && level.getBlockState(duckweedPos).isAir()) {
                            BlockState dw = WaterloggedBlocks.DUCKWEED.defaultBlockState();
                            if (dw.canSurvive(level, duckweedPos)) {
                                level.setBlock(duckweedPos, dw, 2);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Scans downward from start, skipping air, leaves, and log trunks,
     * to find a water source block or solid non-tree ground.
     */
    private BlockPos findSurface(WorldGenLevel level, BlockPos start) {
        for (int dy = 0; dy <= 16; dy++) {
            BlockPos pos = start.below(dy);
            FluidState fluid = level.getFluidState(pos);
            BlockState state = level.getBlockState(pos);

            if (!fluid.isEmpty()) return pos; // water surface

            if (!state.isAir()
                    && !state.is(BlockTags.LEAVES)
                    && !state.is(BlockTags.LOGS)
                    && state.isFaceSturdy(level, pos, Direction.UP)) {
                return pos; // solid non-tree ground
            }
        }
        return null;
    }

    /**
     * A position can hold a log if it contains fluid, is air with sturdy support,
     * or is a replaceable plant (tall grass, ferns) with sturdy support.
     * Works for both water and land positions — logs can span the boundary.
     */
    private boolean canPlace(WorldGenLevel level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        BlockState state = level.getBlockState(pos);

        if (!fluid.isEmpty()) return true; // floating in water

        if (state.isAir() || state.canBeReplaced()) {
            return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
        }
        return false;
    }
}
