package net.braeden.waterlogged.sound;

//?if fabric {
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.registries.BuiltInRegistries;*/
//?}
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
//?if neoforge {
/*import net.neoforged.neoforge.common.util.DeferredSoundType;*/
//?}

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedSounds {

    public static final SoundEvent ITEM_WORM_USE = create("item.worm.use");
    public static final SoundEvent BLOCK_ROE_HATCH = create("block.roe.hatch");
    public static final SoundEvent BLOCK_SEA_SLUG_EGGS_HATCH = create("block.sea_slug_eggs.hatch");
    public static final SoundEvent BLOCK_SHELL_BREAK = create("block.shell.break");
    public static final SoundEvent BLOCK_SHELL_HIT = create("block.shell.hit");
    public static final SoundEvent BLOCK_SHELL_STEP = create("block.shell.step");
    public static final SoundEvent BLOCK_SHELL_FALL = create("block.shell.fall");
    public static final SoundEvent BLOCK_SHELL_PLACE = create("block.shell.place");
    public static final SoundEvent ENTITY_FRY_HURT = create("entity.fry.hurt");
    public static final SoundEvent ENTITY_FRY_DEATH = create("entity.fry.death");
    public static final SoundEvent ENTITY_FRY_FLOP = create("entity.fry.flop");
    public static final SoundEvent ENTITY_SUNFISH_HURT = create("entity.sunfish.hurt");
    public static final SoundEvent ENTITY_SUNFISH_DEATH = create("entity.sunfish.death");
    public static final SoundEvent ENTITY_SUNFISH_FLOP = create("entity.sunfish.flop");
    public static final SoundEvent ENTITY_PELICAN_HURT = create("entity.pelican.hurt");
    public static final SoundEvent ENTITY_PELICAN_DEATH = create("entity.pelican.death");
    public static final SoundEvent ENTITY_PELICAN_AMBIENT = create("entity.pelican.ambient");
    public static final SoundEvent ENTITY_SEA_SLUG_HURT = create("entity.sea_slug.hurt");
    public static final SoundEvent ENTITY_SEA_SLUG_DEATH = create("entity.sea_slug.death");
    public static final SoundEvent ENTITY_CRAB_HURT = create("entity.crab.hurt");
    public static final SoundEvent ENTITY_CRAB_DEATH = create("entity.crab.death");
    public static final SoundEvent ENTITY_DONGFISH_HURT = create("entity.dongfish.hurt");
    public static final SoundEvent ENTITY_DONGFISH_DEATH = create("entity.dongfish.death");
    public static final SoundEvent ENTITY_DONGFISH_FLOP = create("entity.dongfish.flop");
    public static final SoundEvent ENTITY_DONGFISH_SHEAR = create("entity.dongfish.shear");
    public static final SoundEvent ENTITY_CATFISH_HURT = create("entity.catfish.hurt");
    public static final SoundEvent ENTITY_CATFISH_DEATH = create("entity.catfish.death");
    public static final SoundEvent ENTITY_CATFISH_FLOP = create("entity.catfish.flop");
    public static final SoundEvent ENTITY_SEAHORSE_HURT = create("entity.seahorse.hurt");
    public static final SoundEvent ENTITY_SEAHORSE_DEATH = create("entity.seahorse.death");
    public static final SoundEvent ENTITY_SEAHORSE_FLOP = create("entity.seahorse.flop");
    public static final SoundEvent ENTITY_BUBBLE_EYE_HURT = create("entity.bubble_eye.hurt");
    public static final SoundEvent ENTITY_BUBBLE_EYE_DEATH = create("entity.bubble_eye.death");
    public static final SoundEvent ENTITY_BUBBLE_EYE_FLOP = create("entity.bubble_eye.flop");
    public static final SoundEvent ENTITY_ANOMALOCARIS_HURT = create("entity.anomalocaris.hurt");
    public static final SoundEvent ENTITY_ANOMALOCARIS_DEATH = create("entity.anomalocaris.death");
    public static final SoundEvent ENTITY_ANOMALOCARIS_FLOP = create("entity.anomalocaris.flop");
    public static final SoundEvent ENTITY_ANGLERFISH_HURT = create("entity.anglerfish.hurt");
    public static final SoundEvent ENTITY_ANGLERFISH_DEATH = create("entity.anglerfish.death");
    public static final SoundEvent ENTITY_ANGLERFISH_FLOP = create("entity.anglerfish.flop");
    public static final SoundEvent ENTITY_MAHI_MAHI_HURT = create("entity.mahi_mahi.hurt");
    public static final SoundEvent ENTITY_MAHI_MAHI_DEATH = create("entity.mahi_mahi.death");
    public static final SoundEvent ENTITY_MAHI_MAHI_FLOP = create("entity.mahi_mahi.flop");
    public static final SoundEvent ENTITY_RIGHT_WHALE_AMBIENT = create("entity.right_whale.ambient");
    public static final SoundEvent ENTITY_RIGHT_WHALE_HURT = create("entity.right_whale.hurt");
    public static final SoundEvent ENTITY_RIGHT_WHALE_DEATH = create("entity.right_whale.death");

//?if fabric {
    public static final SoundType SHELL_SOUND_GROUP = new SoundType(1, 1.25f, BLOCK_SHELL_BREAK, BLOCK_SHELL_STEP, BLOCK_SHELL_PLACE, BLOCK_SHELL_HIT, BLOCK_SHELL_FALL);
//?} else {
/*    public static final SoundType SHELL_SOUND_GROUP = new DeferredSoundType(1, 1.25f, () -> BLOCK_SHELL_BREAK, () -> BLOCK_SHELL_STEP, () -> BLOCK_SHELL_PLACE, () -> BLOCK_SHELL_HIT, () -> BLOCK_SHELL_FALL);*/
//?}

    public static void init() {}

//?if fabric {
    private static SoundEvent create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
//?} else {
/*    private static SoundEvent create(String name) {
        return SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    public static void registerAll(net.neoforged.neoforge.registries.RegisterEvent event) {
        event.register(BuiltInRegistries.SOUND_EVENT.key(), helper -> {
            helper.register(ITEM_WORM_USE.location(), ITEM_WORM_USE);
            helper.register(BLOCK_ROE_HATCH.location(), BLOCK_ROE_HATCH);
            helper.register(BLOCK_SEA_SLUG_EGGS_HATCH.location(), BLOCK_SEA_SLUG_EGGS_HATCH);
            helper.register(BLOCK_SHELL_BREAK.location(), BLOCK_SHELL_BREAK);
            helper.register(BLOCK_SHELL_HIT.location(), BLOCK_SHELL_HIT);
            helper.register(BLOCK_SHELL_STEP.location(), BLOCK_SHELL_STEP);
            helper.register(BLOCK_SHELL_FALL.location(), BLOCK_SHELL_FALL);
            helper.register(BLOCK_SHELL_PLACE.location(), BLOCK_SHELL_PLACE);
            helper.register(ENTITY_FRY_HURT.location(), ENTITY_FRY_HURT);
            helper.register(ENTITY_FRY_DEATH.location(), ENTITY_FRY_DEATH);
            helper.register(ENTITY_FRY_FLOP.location(), ENTITY_FRY_FLOP);
            helper.register(ENTITY_SUNFISH_HURT.location(), ENTITY_SUNFISH_HURT);
            helper.register(ENTITY_SUNFISH_DEATH.location(), ENTITY_SUNFISH_DEATH);
            helper.register(ENTITY_SUNFISH_FLOP.location(), ENTITY_SUNFISH_FLOP);
            helper.register(ENTITY_PELICAN_HURT.location(), ENTITY_PELICAN_HURT);
            helper.register(ENTITY_PELICAN_DEATH.location(), ENTITY_PELICAN_DEATH);
            helper.register(ENTITY_PELICAN_AMBIENT.location(), ENTITY_PELICAN_AMBIENT);
            helper.register(ENTITY_SEA_SLUG_HURT.location(), ENTITY_SEA_SLUG_HURT);
            helper.register(ENTITY_SEA_SLUG_DEATH.location(), ENTITY_SEA_SLUG_DEATH);
            helper.register(ENTITY_CRAB_HURT.location(), ENTITY_CRAB_HURT);
            helper.register(ENTITY_CRAB_DEATH.location(), ENTITY_CRAB_DEATH);
            helper.register(ENTITY_DONGFISH_HURT.location(), ENTITY_DONGFISH_HURT);
            helper.register(ENTITY_DONGFISH_DEATH.location(), ENTITY_DONGFISH_DEATH);
            helper.register(ENTITY_DONGFISH_FLOP.location(), ENTITY_DONGFISH_FLOP);
            helper.register(ENTITY_DONGFISH_SHEAR.location(), ENTITY_DONGFISH_SHEAR);
            helper.register(ENTITY_CATFISH_HURT.location(), ENTITY_CATFISH_HURT);
            helper.register(ENTITY_CATFISH_DEATH.location(), ENTITY_CATFISH_DEATH);
            helper.register(ENTITY_CATFISH_FLOP.location(), ENTITY_CATFISH_FLOP);
            helper.register(ENTITY_SEAHORSE_HURT.location(), ENTITY_SEAHORSE_HURT);
            helper.register(ENTITY_SEAHORSE_DEATH.location(), ENTITY_SEAHORSE_DEATH);
            helper.register(ENTITY_SEAHORSE_FLOP.location(), ENTITY_SEAHORSE_FLOP);
            helper.register(ENTITY_BUBBLE_EYE_HURT.location(), ENTITY_BUBBLE_EYE_HURT);
            helper.register(ENTITY_BUBBLE_EYE_DEATH.location(), ENTITY_BUBBLE_EYE_DEATH);
            helper.register(ENTITY_BUBBLE_EYE_FLOP.location(), ENTITY_BUBBLE_EYE_FLOP);
            helper.register(ENTITY_ANOMALOCARIS_HURT.location(), ENTITY_ANOMALOCARIS_HURT);
            helper.register(ENTITY_ANOMALOCARIS_DEATH.location(), ENTITY_ANOMALOCARIS_DEATH);
            helper.register(ENTITY_ANOMALOCARIS_FLOP.location(), ENTITY_ANOMALOCARIS_FLOP);
            helper.register(ENTITY_ANGLERFISH_HURT.location(), ENTITY_ANGLERFISH_HURT);
            helper.register(ENTITY_ANGLERFISH_DEATH.location(), ENTITY_ANGLERFISH_DEATH);
            helper.register(ENTITY_ANGLERFISH_FLOP.location(), ENTITY_ANGLERFISH_FLOP);
            helper.register(ENTITY_MAHI_MAHI_HURT.location(), ENTITY_MAHI_MAHI_HURT);
            helper.register(ENTITY_MAHI_MAHI_DEATH.location(), ENTITY_MAHI_MAHI_DEATH);
            helper.register(ENTITY_MAHI_MAHI_FLOP.location(), ENTITY_MAHI_MAHI_FLOP);
            helper.register(ENTITY_RIGHT_WHALE_AMBIENT.location(), ENTITY_RIGHT_WHALE_AMBIENT);
            helper.register(ENTITY_RIGHT_WHALE_HURT.location(), ENTITY_RIGHT_WHALE_HURT);
            helper.register(ENTITY_RIGHT_WHALE_DEATH.location(), ENTITY_RIGHT_WHALE_DEATH);
        });
    }*/
//?}
}
