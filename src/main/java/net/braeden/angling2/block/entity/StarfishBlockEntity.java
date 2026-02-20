package net.braeden.angling2.block.entity;

import net.braeden.angling2.entity.AnglingEntities;
import net.braeden.angling2.entity.util.StarfishColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class StarfishBlockEntity extends BlockEntity {

    private StarfishColor color = StarfishColor.RED;

    public StarfishBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.STARFISH, pos, state);
    }

    public StarfishColor getColor() {
        return color;
    }

    public void setColor(StarfishColor color) {
        this.color = color;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Color", color.getId());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        color = StarfishColor.byId(input.getIntOr("Color", 0));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }
}
