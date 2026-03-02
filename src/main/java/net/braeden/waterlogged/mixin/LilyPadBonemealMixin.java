package net.braeden.waterlogged.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Makes vanilla lily pads bonemealable, spreading one lily pad to an adjacent
 * open water surface — mirroring the existing duckweed spread behaviour.
 */
@Mixin(WaterlilyBlock.class)
public abstract class LilyPadBonemealMixin implements BonemealableBlock {

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
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
            if (level.getBlockState(targetPos).isAir() && state.canSurvive(level, targetPos)) {
                level.setBlock(targetPos, state, 3);
                return;
            }
        }
    }
}
