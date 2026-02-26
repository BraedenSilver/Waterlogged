package net.braeden.waterlogged.entity.util;

import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

public enum StarfishColor implements StringRepresentable {
    RED(0,    "red",    0xFFE04040),
    BLUE(1,   "blue",   0xFF4070D0),
    ORANGE(2, "orange", 0xFFE88030),
    YELLOW(3, "yellow", 0xFFE8D840),
    PINK(4,   "pink",   0xFFE87098),
    RAINBOW(5, "rainbow", 0xFFFFFFFF); // Special cycling variant (like jeb_ sheep)

    private static final StarfishColor[] VALUES = values();
    private static final StarfishColor[] NON_RAINBOW = {RED, BLUE, ORANGE, YELLOW, PINK};
    /** Ticks for a full hue revolution (600 ticks = 30 seconds at 20 tps). */
    private static final float RAINBOW_CYCLE_TICKS = 600.0F;

    private final int id;
    private final String name;
    private final int argb;

    StarfishColor(int id, String name, int argb) {
        this.id = id;
        this.name = name;
        this.argb = argb;
    }

    public int getId() { return this.id; }

    /** Packed ARGB color used to tint the grayscale starfish texture. */
    public int getArgb() { return this.argb; }

    public static StarfishColor byId(int id) {
        if (id < 0 || id >= VALUES.length) return RED;
        return VALUES[id];
    }

    public static StarfishColor random(net.minecraft.util.RandomSource random) {
        // 1/100 chance for rainbow, otherwise normal colors
        if (random.nextInt(100) == 0) {
            return RAINBOW;
        }
        return NON_RAINBOW[random.nextInt(NON_RAINBOW.length)];
    }

    /**
     * For RAINBOW, smoothly cycles through the full hue wheel (like jeb_ sheep).
     * partialTick interpolates between game ticks for frame-smooth animation.
     * For other colors, returns the static ARGB value.
     */
    public int getArgbForTicks(long gameTicks, float partialTick) {
        if (this == RAINBOW) {
            float hue = ((gameTicks + partialTick) % RAINBOW_CYCLE_TICKS) / RAINBOW_CYCLE_TICKS;
            return 0xFF000000 | Mth.hsvToRgb(hue, 1.0F, 1.0F);
        }
        return this.argb;
    }

    @Override
    public String getSerializedName() { return this.name; }
}
