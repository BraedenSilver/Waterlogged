package net.braeden.angling2.block.entity;

import net.braeden.angling2.entity.AnglingEntities;
import net.braeden.angling2.entity.FryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class RoeBlockEntity extends BlockEntity {

    private String parentTypeId = "";
    private int age = 0;
    // Packed variant ints for tropical fish parent colours; -1 = not a tropical fish
    private int parent1Variant = -1;
    private int parent2Variant = -1;
    /** Direct ARGB tint colour averaged from both parents. -1 = no tint. */
    private int tintArgb = -1;
    /** Generic parent variant ID for species like Sunfish. -1 = not set. */
    private int parentVariantId = -1;

    private static final int HATCH_TICKS = 6000;
    private static final int MIN_FRY = 2;
    private static final int MAX_FRY = 4;

    public RoeBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.ROE, pos, state);
    }

    public void setParentTypeId(String typeId) {
        this.parentTypeId = typeId;
        this.setChanged();
    }

    public String getParentTypeId() {
        return parentTypeId;
    }

    /** Store packed TropicalFish variants from both parents for colour mixing. */
    public void setParentVariants(int v1, int v2) {
        this.parent1Variant = v1;
        this.parent2Variant = v2;
        // Also derive tintArgb from the base colors of both parents
        int c1 = TropicalFish.getBaseColor(v1).getTextColor();
        int c2 = TropicalFish.getBaseColor(v2).getTextColor();
        int r = ((c1 >> 16 & 0xFF) + (c2 >> 16 & 0xFF)) / 2;
        int g = ((c1 >> 8 & 0xFF) + (c2 >> 8 & 0xFF)) / 2;
        int b = ((c1 & 0xFF) + (c2 & 0xFF)) / 2;
        this.tintArgb = 0xFF000000 | (r << 16) | (g << 8) | b;
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getParent1Variant() { return parent1Variant; }
    public int getParent2Variant() { return parent2Variant; }

    /** Set a direct ARGB tint (e.g. averaged from non-tropical-fish parents). */
    public void setTintArgb(int argb) {
        this.tintArgb = argb;
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getTintArgb() { return tintArgb; }

    public void setParentVariantId(int id) {
        this.parentVariantId = id;
        this.setChanged();
    }

    public int getParentVariantId() { return parentVariantId; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RoeBlockEntity be) {
        if (level.isClientSide()) return;
        be.age++;
        if (be.age % 100 == 0) be.setChanged();
        if (be.age >= HATCH_TICKS) {
            hatch((ServerLevel) level, pos, be);
        }
    }

    private static void hatch(ServerLevel level, BlockPos pos, RoeBlockEntity be) {
        level.removeBlock(pos, false);

        int count = MIN_FRY + level.getRandom().nextInt(MAX_FRY - MIN_FRY + 1);
        for (int i = 0; i < count; i++) {
            FryEntity fry = new FryEntity(AnglingEntities.FRY, level);
            if (!be.parentTypeId.isEmpty()) {
                fry.setParentTypeId(be.parentTypeId);
            }
            if (be.parent1Variant >= 0 && be.parent2Variant >= 0) {
                fry.setTropicalFishVariant(mixVariants(level.getRandom(), be.parent1Variant, be.parent2Variant));
            }
            if (be.tintArgb != -1) {
                fry.setTintArgb(be.tintArgb);
            }
            if (be.parentVariantId != -1) {
                fry.setParentVariantId(be.parentVariantId);
            }
            fry.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            level.addFreshEntity(fry);
        }
    }

    /** Randomly pick each colour/pattern component from one of the two parents. */
    private static int mixVariants(RandomSource rng, int v1, int v2) {
        TropicalFish.Pattern pattern = rng.nextBoolean() ? TropicalFish.getPattern(v1) : TropicalFish.getPattern(v2);
        net.minecraft.world.item.DyeColor base = rng.nextBoolean() ? TropicalFish.getBaseColor(v1) : TropicalFish.getBaseColor(v2);
        net.minecraft.world.item.DyeColor patternColor = rng.nextBoolean() ? TropicalFish.getPatternColor(v1) : TropicalFish.getPatternColor(v2);
        return new TropicalFish.Variant(pattern, base, patternColor).getPackedId();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("ParentType", parentTypeId);
        output.putInt("Age", age);
        output.putInt("Parent1Variant", parent1Variant);
        output.putInt("Parent2Variant", parent2Variant);
        output.putInt("TintArgb", tintArgb);
        output.putInt("ParentVariantId", parentVariantId);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        parentTypeId = input.getStringOr("ParentType", "");
        age = input.getIntOr("Age", 0);
        parent1Variant = input.getIntOr("Parent1Variant", -1);
        parent2Variant = input.getIntOr("Parent2Variant", -1);
        tintArgb = input.getIntOr("TintArgb", -1);
        parentVariantId = input.getIntOr("ParentVariantId", -1);
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
