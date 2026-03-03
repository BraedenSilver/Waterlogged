package net.braeden.waterlogged.worldgen;

import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Bald / Pond Cypress (Taxodium distichum / ascendens).
 *
 * Silhouette: 3×3 base flaring for ~3 blocks then immediately 1×1 trunk,
 * inverted-cone crown (widest branches at the bottom, shortest at the top),
 * surface roots connected underground to cypress knees, and draping vines.
 *
 * All generation helpers are static so they can also be called by
 * OakSaplingCypressMixin (sapling-grown trees skip the root system).
 */
public class PondCypressFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockState LOG   = Blocks.OAK_LOG.defaultBlockState();
    private static final BlockState LOG_X = Blocks.OAK_LOG.defaultBlockState()
            .setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
    private static final BlockState LOG_Z = Blocks.OAK_LOG.defaultBlockState()
            .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
    private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState()
            .setValue(LeavesBlock.PERSISTENT, true);

    private static final Direction[] HORIZ = {
        Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };

    public PondCypressFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    // ── Feature entry point ───────────────────────────────────────────────────

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level  = ctx.level();
        BlockPos origin      = ctx.origin();
        RandomSource rand    = ctx.random();

        BlockPos ground = findGround(level, origin);
        if (ground == null) return false;

        // Reject cave floors and deep ponds — swamp water is only 1–2 blocks deep.
        if (ground.getY() < level.getSeaLevel() - 4) return false;
        int waterDepth = 0;
        for (int dy = 1; dy <= 8; dy++) {
            if (level.getFluidState(ground.above(dy)).isEmpty()) break;
            if (++waterDepth > 4) return false;
        }

        generateTree(level, ground.above(), 15 + rand.nextInt(6), rand, true);
        return true;
    }

    // ── Core generator (also called by OakSaplingCypressMixin) ───────────────

    /**
     * Generates the full cypress tree structure.
     *
     * @param base      y=0 of the trunk (one block above solid ground)
     * @param height    total trunk height in blocks (15–20)
     * @param withRoots whether to place buttress roots, underground connections,
     *                  and cypress knees (false for sapling-grown trees, for
     *                  consistency with vanilla tree behaviour)
     */
    public static void generateTree(WorldGenLevel level, BlockPos base, int height,
                                     RandomSource rand, boolean withRoots) {
        // ── Trunk ─────────────────────────────────────────────────────────────
        // t = min(1, y/5) with quadratic ease → full 3×3 for y=0–2, cross at y=3,
        // hard 1×1 from y=4 onward (the wine-glass flare of real Taxodium trunks).
        for (int y = 0; y < height; y++) {
            float t  = Math.min(1.0f, (float) y / 5.0f);
            float r2 = sq(lerp(1.6f, 0.45f, t * t));
            for (int dx = -2; dx <= 2; dx++)
                for (int dz = -2; dz <= 2; dz++)
                    if (dx * dx + dz * dz < r2)
                        level.setBlock(base.offset(dx, y, dz), LOG, 2);
        }

        // ── Root system ───────────────────────────────────────────────────────
        if (withRoots) placeRootSystem(level, base.below(), rand);

        // ── Crown: inverted cone ──────────────────────────────────────────────
        // p=0 at crown bottom, p=1 at crown top.
        // Branch arm length: lerp(4.5 → 1.0, p) — longest below, shortest above.
        // Leaf pad radius: 2 for lower crown half, 1 for upper crown half.
        int branchStart = (int)(height * 0.55);
        int branchTop   = height - 2;

        for (int y = branchStart; y <= branchTop; y++) {
            float p = (float)(y - branchStart) / Math.max(1, branchTop - branchStart);
            int maxRun    = Math.max(1, Math.round(lerp(4.5f, 1.0f, p)));
            int padRadius = (p < 0.5f) ? 2 : 1;

            for (Direction dir : HORIZ) {
                if (rand.nextFloat() > 0.60f) continue;
                int run  = Math.max(1, maxRun - rand.nextInt(2));
                int rise = (p > 0.65f) ? rand.nextInt(2) : 0;
                growBranch(level, base.offset(0, y, 0), dir, run, rise, padRadius, rand);
            }
        }

        // ── Crown tip ─────────────────────────────────────────────────────────
        placeCrownTip(level, base.offset(0, height - 1, 0));

        // ── Vines ─────────────────────────────────────────────────────────────
        placeVines(level, base, branchStart, rand);

        // ── Duckweed — same method as SwampLogFeature ─────────────────────────
        placeDuckweed(level, base, rand);

        // ── Swamp plants — moss carpet, firefly bushes, blue orchids ──────────
        placeSwampPlants(level, base, rand);

        // ── Leaf decay — BFS from logs to set correct distance on all leaves ──
        // scheduleTick() is a no-op in WorldGenLevel, so we compute distances
        // ourselves: every leaf gets PERSISTENT=false + the correct distance value.
        finalizeLeavesDecay(level, base, height);
    }

    // ── Branch helpers ────────────────────────────────────────────────────────

    private static void growBranch(WorldGenLevel level, BlockPos trunkCell, Direction dir,
                                    int run, int rise, int padRadius, RandomSource rand) {
        BlockState hLog = dir.getAxis() == Direction.Axis.X ? LOG_X : LOG_Z;

        for (int i = 1; i <= run; i++) {
            BlockPos p = trunkCell.offset(dir.getStepX() * i, 0, dir.getStepZ() * i);
            if (isReplaceable(level, p)) level.setBlock(p, hLog, 2);
        }

        BlockPos armEnd = trunkCell.offset(dir.getStepX() * run, 0, dir.getStepZ() * run);

        for (int i = 1; i <= rise; i++) {
            BlockPos up = armEnd.above(i);
            if (isReplaceable(level, up)) level.setBlock(up, LOG, 2);
        }

        placeLeafPad(level, armEnd.above(rise), padRadius, rand);

        // Vines on the perpendicular sides of the rise-log column only.
        // Each vine is placed directly adjacent to a log block so it uses a
        // horizontal face direction (no UP=true), which renders as a clean
        // vertical strip — not a ceiling panel.
        if (rise > 0) {
            for (int i = 1; i <= rise; i++) {
                BlockPos riseLog = armEnd.above(i);
                for (Direction sideDir : new Direction[]{ dir.getClockWise(), dir.getCounterClockWise() }) {
                    if (rand.nextFloat() > 0.5f) continue;
                    BlockPos vp = riseLog.relative(sideDir);
                    if (!level.getFluidState(vp).isEmpty()) continue;
                    if (!isReplaceable(level, vp)) continue;
                    level.setBlock(vp, vineFacingTrunk(sideDir), 2);
                }
            }
        }
    }

    private static void placeLeafPad(WorldGenLevel level, BlockPos center, int radius,
                                      RandomSource rand) {
        float r2 = sq(radius + 0.5f);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= r2) {
                    setLeaf(level, center.offset(dx, 0, dz));
                    if (rand.nextFloat() < 0.25f)
                        setLeaf(level, center.above().offset(dx, 0, dz));
                }
            }
        }
        setLeaf(level, center.above());

        // Hanging vines from the edge of the leaf pad — vanilla LeaveVineDecorator pattern:
        // for each edge leaf, place a vine on its outward-facing air side, then chain it
        // straight down using the SAME single face property (no UP=true).
        // VineBlock.canSupportAtFace allows a chain vine to be valid when the vine directly
        // above it shares the same face property — confirmed in decompiled VineBlock source.
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > r2) continue;
                for (Direction side : HORIZ) {
                    if (rand.nextFloat() > 0.25f) continue;
                    int nx = dx + side.getStepX();
                    int nz = dz + side.getStepZ();
                    if (nx * nx + nz * nz <= r2) continue; // only edge positions (outside circle)
                    BlockPos vp = center.offset(nx, 0, nz);
                    if (!isReplaceable(level, vp) || !level.getFluidState(vp).isEmpty()) continue;
                    BlockState vine = vineFacingTrunk(side); // face points back toward the leaf
                    level.setBlock(vp, vine, 2);
                    for (int j = 1; j <= 4; j++) {
                        BlockPos below = vp.below(j);
                        if (!isReplaceable(level, below) || !level.getFluidState(below).isEmpty()) break;
                        level.setBlock(below, vine, 2); // same face, no UP=true
                    }
                }
            }
        }
    }

    /** Conical crown tip: spire → cross → 3×3 → cross skirt. */
    private static void placeCrownTip(WorldGenLevel level, BlockPos top) {
        setLeaf(level, top.above());
        setLeaf(level, top);
        for (Direction d : HORIZ) setLeaf(level, top.relative(d));

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++)
                setLeaf(level, top.below().offset(dx, 0, dz));

        setLeaf(level, top.below(2));
        for (Direction d : HORIZ) setLeaf(level, top.below(2).relative(d));
    }

    // ── Vine helpers ──────────────────────────────────────────────────────────

    private static void placeVines(WorldGenLevel level, BlockPos base, int branchStart,
                                    RandomSource rand) {
        // Vanilla TrunkVineDecorator pattern: one vine per face per log block, no chaining, no UP=true.
        // Each vine is directly adjacent to a solid trunk log, so the horizontal face alone
        // is sufficient for stability (VineBlock.canSupportAtFace checks solid neighbour first).
        int strips = 4 + rand.nextInt(4);
        for (int s = 0; s < strips; s++) {
            int startY = 5 + rand.nextInt(Math.max(1, branchStart - 4));
            Direction side = HORIZ[rand.nextInt(4)];
            int len = 3 + rand.nextInt(4);
            BlockState vine = vineFacingTrunk(side);

            for (int i = 0; i < len; i++) {
                BlockPos vp = base.offset(side.getStepX(), startY - i, side.getStepZ());
                if (vp.getY() < base.getY())            break;
                if (!level.getFluidState(vp).isEmpty()) break;
                if (!isReplaceable(level, vp))          break;
                level.setBlock(vp, vine, 2);
            }
        }
    }

    /** Scatters duckweed on water surfaces within ~5 blocks of the trunk base. */
    private static void placeDuckweed(WorldGenLevel level, BlockPos base, RandomSource rand) {
        BlockState dw = WaterloggedBlocks.DUCKWEED.defaultBlockState();
        int radius = 5;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;
                if (rand.nextFloat() > 0.35f) continue;
                // Scan base Y ± 1 to handle varying water depths
                for (int dy = 1; dy >= -1; dy--) {
                    BlockPos water = base.offset(dx, dy, dz);
                    if (!level.getFluidState(water).isEmpty()) {
                        BlockPos surface = water.above();
                        if (level.getBlockState(surface).isAir() && dw.canSurvive(level, surface)) {
                            level.setBlock(surface, dw, 2);
                        }
                        break; // found water level, stop scanning down
                    }
                }
            }
        }
    }

    /** Scatters moss carpet, firefly bushes, and blue orchids on solid dry ground near the trunk. */
    private static void placeSwampPlants(WorldGenLevel level, BlockPos base, RandomSource rand) {
        int radius = 5;
        BlockState mossCarpet   = Blocks.MOSS_CARPET.defaultBlockState();
        BlockState fireflyBush  = Blocks.FIREFLY_BUSH.defaultBlockState();
        BlockState blueOrchid   = Blocks.BLUE_ORCHID.defaultBlockState();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;
                for (int dy = 1; dy >= -4; dy--) {
                    BlockPos pos = base.offset(dx, dy, dz);
                    if (!level.getFluidState(pos).isEmpty()) break; // water — skip
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) continue;
                    if (!state.isFaceSturdy(level, pos, Direction.UP)) break;
                    BlockPos above = pos.above();
                    if (!level.getBlockState(above).isAir()) break;

                    float roll = rand.nextFloat();
                    if (roll < 0.10f) {
                        if (fireflyBush.canSurvive(level, above))
                            level.setBlock(above, fireflyBush, 2);
                    } else if (roll < 0.45f) {
                        level.setBlock(above, mossCarpet, 2);
                    } else if (roll < 0.50f) {
                        if (blueOrchid.canSurvive(level, above))
                            level.setBlock(above, blueOrchid, 2);
                    } else if (roll < 0.55f) {
                        BlockState mushroom = rand.nextBoolean()
                                ? Blocks.BROWN_MUSHROOM.defaultBlockState()
                                : Blocks.RED_MUSHROOM.defaultBlockState();
                        if (mushroom.canSurvive(level, above))
                            level.setBlock(above, mushroom, 2);
                    }
                    // else ~45% — leave bare
                    break;
                }
            }
        }
    }

    private static BlockState vineFacingTrunk(Direction sideDir) {
        return switch (sideDir) {
            case EAST  -> Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST,  true);
            case WEST  -> Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST,  true);
            case SOUTH -> Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true);
            default    -> Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true);
        };
    }

    // ── Root system ───────────────────────────────────────────────────────────

    /**
     * Unified buttress roots + underground connections + cypress knees.
     *
     * Each cardinal arm: surface logs at ground level → underground log one
     * block below the arm tip → arm tip overwritten with vertical LOG → 1–2
     * knee logs rising above ground.  This creates a fully-connected column
     * of logs at every knee position: underground → surface → above ground.
     *
     * Extra scattered knees follow the same underground → surface → above
     * column pattern so they never appear as isolated floating stumps.
     *
     * @param ground  the solid surface block (NOT the block above it)
     */
    private static void placeRootSystem(WorldGenLevel level, BlockPos ground,
                                         RandomSource rand) {
        // Cardinal arms with knees at their tips
        for (Direction dir : HORIZ) {
            int armLen = 1 + rand.nextInt(3); // 1–3 blocks
            BlockState hLog = dir.getAxis() == Direction.Axis.X ? LOG_X : LOG_Z;

            BlockPos armTip = ground;
            for (int i = 1; i <= armLen; i++) {
                BlockPos p = ground.offset(dir.getStepX() * i, 0, dir.getStepZ() * i);
                if (!canPlaceRoot(level, p)) { armLen = i - 1; break; }
                level.setBlock(p, hLog, 2);
                armTip = p;
            }
            if (armLen == 0) continue;

            // Underground root below arm tip (root dips into the ground)
            BlockPos ug = armTip.below();
            if (canPlaceRoot(level, ug)) level.setBlock(ug, hLog, 2);

            // Knee: replace the horizontal arm tip with a vertical LOG,
            // then rise 1–2 blocks above.  This creates the connected column:
            //   ug (G-1) → armTip vertical (G) → knee (G+1, G+2)
            level.setBlock(armTip, LOG, 2);
            int kneeH = 1 + rand.nextInt(2);
            for (int y = 1; y <= kneeH; y++) {
                BlockPos kp = armTip.above(y);
                if (isReplaceable(level, kp)) level.setBlock(kp, LOG, 2);
                else break;
            }
        }

        // Diagonal root nodes (no knees, just surface + underground)
        int[][] diags = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        for (int[] d : diags) {
            if (rand.nextFloat() > 0.65f) continue;
            BlockPos p = ground.offset(d[0], 0, d[1]);
            if (!canPlaceRoot(level, p)) continue;
            level.setBlock(p, LOG, 2);
            BlockPos ug = p.below();
            if (canPlaceRoot(level, ug)) level.setBlock(ug, LOG, 2);
        }

        // Extra scattered knees further from the trunk (3–6 total)
        // Each one uses the same underground → surface → above-ground column.
        int extra = 3 + rand.nextInt(4);
        for (int k = 0; k < extra; k++) {
            int dx   = rand.nextInt(9) - 4;
            int dz   = rand.nextInt(9) - 4;
            int dist = Math.abs(dx) + Math.abs(dz);
            if (dist < 2 || dist > 6) continue;

            BlockPos kg = findKneeSurface(level, ground.offset(dx, 4, dz));
            if (kg == null) continue;

            // Underground root below the surface block
            if (canPlaceRoot(level, kg.below())) level.setBlock(kg.below(), LOG, 2);
            // Replace the surface block with a vertical knee-base log
            if (canPlaceRoot(level, kg)) level.setBlock(kg, LOG, 2);
            // Visible knee above ground
            int kh = 1 + rand.nextInt(2);
            for (int y = 1; y <= kh; y++) {
                BlockPos kp = kg.above(y);
                if (isReplaceable(level, kp)) level.setBlock(kp, LOG, 2);
                else break;
            }
        }
    }

    // ── Ground finding ────────────────────────────────────────────────────────

    /**
     * Scans downward through air, water, leaves, and logs to find solid ground.
     * Returns the solid ground block itself (NOT the block above it).
     * Public so OakSaplingCypressMixin can reuse it.
     */
    public static BlockPos findGround(WorldGenLevel level, BlockPos start) {
        for (int dy = 0; dy <= 24; dy++) {
            BlockPos pos     = start.below(dy);
            BlockState state = level.getBlockState(pos);
            FluidState fluid = level.getFluidState(pos);
            if (state.isAir()) continue;
            if (state.is(BlockTags.LEAVES)) continue;
            if (state.is(BlockTags.LOGS)) continue;
            if (!fluid.isEmpty()) continue;
            if (state.isFaceSturdy(level, pos, Direction.UP)) return pos;
        }
        return null;
    }

    private static BlockPos findKneeSurface(WorldGenLevel level, BlockPos start) {
        for (int dy = 0; dy <= 12; dy++) {
            BlockPos pos     = start.below(dy);
            BlockState state = level.getBlockState(pos);
            FluidState fluid = level.getFluidState(pos);
            if (state.isAir() || !fluid.isEmpty()) continue;
            if (state.isFaceSturdy(level, pos, Direction.UP)) return pos;
        }
        return null;
    }

    // ── Placement predicates ──────────────────────────────────────────────────

    private static boolean canPlaceRoot(WorldGenLevel level, BlockPos pos) {
        BlockState st = level.getBlockState(pos);
        return st.is(BlockTags.DIRT) || st.is(Blocks.CLAY) || st.is(Blocks.MUD)
                || st.is(Blocks.GRASS_BLOCK) || st.is(Blocks.SAND)
                || isReplaceable(level, pos);
    }

    private static boolean isReplaceable(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir()
                || state.is(BlockTags.LEAVES)
                || !level.getFluidState(pos).isEmpty()
                || state.canBeReplaced();
    }

    private static void setLeaf(WorldGenLevel level, BlockPos pos) {
        if (isReplaceable(level, pos)) level.setBlock(pos, LEAVES, 2);
    }

    /**
     * BFS from all log-adjacent positions to compute the true leaf distance for
     * every oak leaf in the tree's bounding box. scheduleTick() is a no-op in
     * WorldGenLevel, so we must set the distance value ourselves. Each leaf is
     * written as PERSISTENT=false with its computed distance so normal decay
     * cascades correctly when logs are removed later.
     */
    private static void finalizeLeavesDecay(WorldGenLevel level, BlockPos base, int height) {
        int r = 9;
        int yMin = -3;
        int yMax = height + 6;

        // BFS seeded from leaves directly adjacent to any log
        Deque<BlockPos> queue = new ArrayDeque<>();
        Map<Long, Integer> dist = new HashMap<>();

        for (int x = -r; x <= r; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos p = base.offset(x, y, z);
                    if (!level.getBlockState(p).is(BlockTags.LOGS)) continue;
                    for (Direction d : Direction.values()) {
                        BlockPos adj = p.relative(d);
                        long key = adj.asLong();
                        if (dist.containsKey(key)) continue;
                        if (!level.getBlockState(adj).is(BlockTags.LEAVES)) continue;
                        dist.put(key, 1);
                        queue.add(adj);
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos p = queue.poll();
            int d = dist.get(p.asLong());
            if (d >= 7) continue;
            for (Direction dir : Direction.values()) {
                BlockPos nb = p.relative(dir);
                long key = nb.asLong();
                if (dist.containsKey(key)) continue;
                if (!level.getBlockState(nb).is(BlockTags.LEAVES)) continue;
                dist.put(key, d + 1);
                queue.add(nb);
            }
        }

        // Write PERSISTENT=false + correct distance to every PERSISTENT oak leaf we placed
        for (int x = -r; x <= r; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos p = base.offset(x, y, z);
                    BlockState s = level.getBlockState(p);
                    if (!s.is(Blocks.OAK_LEAVES) || !s.getValue(LeavesBlock.PERSISTENT)) continue;
                    int leafDist = dist.getOrDefault(p.asLong(), 7);
                    level.setBlock(p, s.setValue(LeavesBlock.PERSISTENT, false)
                            .setValue(LeavesBlock.DISTANCE, leafDist), 2);
                }
            }
        }
    }

    // ── Math helpers ──────────────────────────────────────────────────────────

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float sq(float x)                      { return x * x; }
}
