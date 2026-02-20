package net.braeden.angling2.block.entity;

import net.braeden.angling2.entity.AnglingEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UrchinBlockEntity extends BlockEntity {
    public int animTickCount = 0;

    public UrchinBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.URCHIN, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, UrchinBlockEntity entity) {
        entity.animTickCount++;
    }
}
