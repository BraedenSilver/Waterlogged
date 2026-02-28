package net.braeden.waterlogged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The sargassum plant. Can float on the water surface (FACING=UP) or attach
 * to any solid block face, including sides and undersides (like vines).
 * Waterlogged when submerged.
 */
public class SargassumPlantBlock extends Block implements SimpleWaterloggedBlock, BonemealableBlock {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty   WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_UP    = Block.box(0,  0, 0, 16,  4, 16);
    private static final VoxelShape SHAPE_DOWN  = Block.box(0, 12, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_NORTH = Block.box(0,  0, 12, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0,  0,  0, 16, 16,  4);
    private static final VoxelShape SHAPE_EAST  = Block.box(0,  0,  0,  4, 16, 16); // west side (attachment point for east-facing plant)
    private static final VoxelShape SHAPE_WEST  = Block.box(12, 0,  0, 16, 16, 16); // east side (attachment point for west-facing plant)

    public SargassumPlantBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        boolean waterlogged = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState().setValue(FACING, face).setValue(WATERLOGGED, waterlogged);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            BlockPos below = pos.below();
            // Floating on the water surface: air here, water directly below
            if (!level.getFluidState(pos).is(FluidTags.WATER)
                    && level.getFluidState(below).is(FluidTags.WATER)) {
                return true;
            }
            // Supported by sargassum_block or any block with a sturdy top face
            BlockState support = level.getBlockState(below);
            return support.is(WaterloggedBlocks.SARGASSUM_BLOCK)
                    || support.isFaceSturdy(level, below, Direction.UP);
        }
        // All other facings: need a sturdy face on the support side
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case DOWN  -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST  -> SHAPE_EAST;
            case WEST  -> SHAPE_WEST;
            default    -> SHAPE_UP;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state.canSurvive(level, pos)
                ? super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random)
                : Blocks.AIR.defaultBlockState();
    }

    // ── Bonemeal (floating variant only) ──────────────────────────────────────

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(FACING) == Direction.UP;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        for (int i = 0; i < 9; i++) {
            int x = random.nextInt(3) - 1;
            int z = random.nextInt(3) - 1;
            if (x == 0 && z == 0) continue;
            BlockPos targetPos = pos.offset(x, 0, z);
            BlockState spread = defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false);
            if (level.getBlockState(targetPos).isAir() && spread.canSurvive(level, targetPos)) {
                level.setBlock(targetPos, spread, 3);
                return;
            }
        }
    }
}
