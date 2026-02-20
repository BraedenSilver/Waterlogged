package net.braeden.angling2.block.entity;

import net.braeden.angling2.entity.AnglingEntities;
import net.braeden.angling2.entity.SeaSlugEntity;
import net.braeden.angling2.entity.util.SeaSlugColor;
import net.braeden.angling2.entity.util.SeaSlugPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SeaSlugEggsBlockEntity extends BlockEntity {

    private int parent1Color = 0;
    private int parent1Pattern = 0;
    private int parent2Color = 0;
    private int parent2Pattern = 0;
    private int age = 0;

    private static final int HATCH_TICKS = 6000;
    private static final int MIN_BABIES = 1;
    private static final int MAX_BABIES = 3;
    /** Chance (0.0–1.0) that a trait mutates to a random value instead of copying from a parent. */
    private static final float MUTATION_CHANCE = 0.10f;

    public SeaSlugEggsBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.SEA_SLUG_EGGS, pos, state);
    }

    public void setParents(SeaSlugColor c1, SeaSlugPattern p1, SeaSlugColor c2, SeaSlugPattern p2) {
        this.parent1Color   = c1.getId();
        this.parent1Pattern = p1.getId();
        this.parent2Color   = c2.getId();
        this.parent2Pattern = p2.getId();
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getParent1Color() { return parent1Color; }
    public int getParent2Color() { return parent2Color; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SeaSlugEggsBlockEntity be) {
        if (level.isClientSide()) return;
        be.age++;
        if (be.age % 100 == 0) be.setChanged();
        if (be.age >= HATCH_TICKS) {
            hatch((ServerLevel) level, pos, be);
        }
    }

    private static void hatch(ServerLevel level, BlockPos pos, SeaSlugEggsBlockEntity be) {
        level.removeBlock(pos, false);
        RandomSource rng = level.getRandom();
        int count = MIN_BABIES + rng.nextInt(MAX_BABIES - MIN_BABIES + 1);
        for (int i = 0; i < count; i++) {
            SeaSlugEntity slug = new SeaSlugEntity(AnglingEntities.SEA_SLUG, level);
            slug.setColor(inheritTrait(rng, be.parent1Color, be.parent2Color, SeaSlugColor.values()));
            slug.setPattern(inheritTrait(rng, be.parent1Pattern, be.parent2Pattern, SeaSlugPattern.values()));
            slug.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            level.addFreshEntity(slug);
        }
    }

    /**
     * Randomly picks the trait from parent A or parent B.
     * With MUTATION_CHANCE probability the trait mutates to any value in the pool.
     */
    private static <T> T inheritTrait(RandomSource rng, int idA, int idB, T[] values) {
        if (rng.nextFloat() < MUTATION_CHANCE) {
            return values[rng.nextInt(values.length)];
        }
        return rng.nextBoolean() ? values[idA] : values[idB];
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Parent1Color",   parent1Color);
        output.putInt("Parent1Pattern", parent1Pattern);
        output.putInt("Parent2Color",   parent2Color);
        output.putInt("Parent2Pattern", parent2Pattern);
        output.putInt("Age", age);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        parent1Color   = input.getIntOr("Parent1Color",   0);
        parent1Pattern = input.getIntOr("Parent1Pattern", 0);
        parent2Color   = input.getIntOr("Parent2Color",   0);
        parent2Pattern = input.getIntOr("Parent2Pattern", 0);
        age = input.getIntOr("Age", 0);
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
