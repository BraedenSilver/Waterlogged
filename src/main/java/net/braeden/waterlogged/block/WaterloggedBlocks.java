package net.braeden.waterlogged.block;

import net.braeden.waterlogged.sound.WaterloggedSounds;
//?if fabric {
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//?}
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedBlocks {

    // Fields populated by Fabric static block or NeoForge RegisterEvent
    public static Block ROE;
    public static Block SEA_SLUG_EGGS;
    public static Block DUCKWEED;
    public static Block ALGAE;
    public static Block WORMY_DIRT;
    public static Block WORMY_MUD;
    public static Block OYSTERS;
    public static Block STARFISH;
    public static Block DEAD_STARFISH;
    public static Block CLAM;
    public static Block ANEMONE;
    public static Block URCHIN;
    public static Block SARGASSUM;
    public static Block SARGASSUM_BLOCK;
    public static Block PAPYRUS;
    public static Block AQUARIUM_GLASS;

//?if fabric {
    static {
        ROE = register("roe",
                props -> new RoeBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_ORANGE)
                        .noCollision()
                        .noOcclusion()
                        .destroyTime(0f)
                        .sound(SoundType.FROGSPAWN)
                        .lightLevel(state -> 2),
                false);

        SEA_SLUG_EGGS = register("sea_slug_eggs",
                props -> new SeaSlugEggsBlock(props),
                BlockBehaviour.Properties.ofFullCopy(ROE)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .dynamicShape()
                        .lightLevel(state -> state.getValue(SeaSlugEggsBlock.GLOWING) ? 3 : 0),
                false);

        DUCKWEED = register("duckweed",
                props -> new WaterFloatingPlant(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_GREEN)
                        .destroyTime(0f)
                        .noOcclusion()
                        .noCollision()
                        .sound(SoundType.WET_GRASS),
                false);

        ALGAE = register("algae",
                props -> new AlgaeBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_GREEN)
                        .sound(SoundType.FROGSPAWN)
                        .noCollision()
                        .noOcclusion()
                        .strength(0.1f)
                        .randomTicks(),
                true);

        WORMY_DIRT = register("wormy_dirt",
                props -> new WormyDirtBlock(props),
                BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT),
                true);

        WORMY_MUD = register("wormy_mud",
                props -> new WormyMudBlock(props),
                BlockBehaviour.Properties.ofFullCopy(Blocks.MUD),
                true);

        OYSTERS = register("oysters",
                props -> new OystersBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BROWN)
                        .strength(0.5f)
                        .noOcclusion()
                        .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                        .randomTicks(),
                true);

        STARFISH = register("starfish",
                props -> new StarfishBlock(props, false),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.SNOW)
                        .strength(0.1f)
                        .noOcclusion()
                        .noCollision()
                        .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                        .randomTicks(),
                false);

        DEAD_STARFISH = register("dead_starfish",
                props -> new StarfishBlock(props, true),
                BlockBehaviour.Properties.ofFullCopy(STARFISH),
                false);

        CLAM = register("clam",
                props -> new ClamBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.SNOW)
                        .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                        .strength(0.05f)
                        .noOcclusion()
                        .randomTicks(),
                true);

        ANEMONE = register("anemone",
                props -> new AnemoneBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.SNOW)
                        .strength(0.1f)
                        .noOcclusion()
                        .noCollision()
                        .sound(SoundType.SLIME_BLOCK)
                        .randomTicks(),
                true);

        URCHIN = register("urchin",
                props -> new UrchinBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_BLUE)
                        .strength(0.1f)
                        .noOcclusion()
                        .noCollision()
                        .sound(WaterloggedSounds.SHELL_SOUND_GROUP),
                false);

        SARGASSUM = register("sargassum",
                props -> new SargassumPlantBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_YELLOW)
                        .destroyTime(0f)
                        .noOcclusion()
                        .noCollision()
                        .sound(SoundType.WET_GRASS),
                false);

        SARGASSUM_BLOCK = register("sargassum_block",
                props -> new SargassumBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_YELLOW)
                        .strength(0.3f)
                        .noOcclusion()
                        .isSuffocating((state, world, pos) -> false)
                        .isViewBlocking((state, world, pos) -> false)
                        .sound(SoundType.WET_GRASS),
                true);

        PAPYRUS = register("papyrus",
                props -> new PapyrusBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_GREEN)
                        .strength(0)
                        .destroyTime(0f)
                        .noOcclusion()
                        .noCollision()
                        .sound(SoundType.AZALEA_LEAVES)
                        .randomTicks()
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .dynamicShape(),
                true);

        AQUARIUM_GLASS = register("aquarium_glass",
                props -> new AquariumGlassBlock(props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .strength(0.3F)
                        .sound(SoundType.GLASS)
                        .noOcclusion()
                        .isValidSpawn((state, world, pos, type) -> false)
                        .isRedstoneConductor((state, world, pos) -> false)
                        .isSuffocating((state, world, pos) -> false)
                        .isViewBlocking((state, world, pos) -> false),
                true);
    }

    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> factory,
                                                  BlockBehaviour.Properties baseProps, boolean hasBlockItem) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
        T block = factory.apply(baseProps.setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        if (hasBlockItem) {
            Identifier itemId = Identifier.fromNamespaceAndPath(MOD_ID, name);
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, itemId);
            Item.Properties itemProps = new Item.Properties().setId(itemKey);
            Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, itemProps));
        }
        return block;
    }
//?} else {
/*    public static void registerAll(net.neoforged.neoforge.registries.RegisterEvent event) {
        event.register(Registries.BLOCK, helper -> {
            ROE = registerNeo(helper, "roe",
                    props -> new RoeBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .noCollision()
                            .noOcclusion()
                            .destroyTime(0f)
                            .sound(SoundType.FROGSPAWN)
                            .lightLevel(state -> 2));

            SEA_SLUG_EGGS = registerNeo(helper, "sea_slug_eggs",
                    props -> new SeaSlugEggsBlock(props),
                    BlockBehaviour.Properties.ofFullCopy(ROE)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .dynamicShape()
                            .lightLevel(state -> state.getValue(SeaSlugEggsBlock.GLOWING) ? 3 : 0));

            DUCKWEED = registerNeo(helper, "duckweed",
                    props -> new WaterFloatingPlant(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .destroyTime(0f)
                            .noOcclusion()
                            .noCollision()
                            .sound(SoundType.WET_GRASS));

            ALGAE = registerNeo(helper, "algae",
                    props -> new AlgaeBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .sound(SoundType.FROGSPAWN)
                            .noCollision()
                            .noOcclusion()
                            .strength(0.1f)
                            .randomTicks());

            WORMY_DIRT = registerNeo(helper, "wormy_dirt",
                    props -> new WormyDirtBlock(props),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT));

            WORMY_MUD = registerNeo(helper, "wormy_mud",
                    props -> new WormyMudBlock(props),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.MUD));

            OYSTERS = registerNeo(helper, "oysters",
                    props -> new OystersBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .strength(0.5f)
                            .noOcclusion()
                            .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                            .randomTicks());

            STARFISH = registerNeo(helper, "starfish",
                    props -> new StarfishBlock(props, false),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SNOW)
                            .strength(0.1f)
                            .noOcclusion()
                            .noCollision()
                            .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                            .randomTicks());

            DEAD_STARFISH = registerNeo(helper, "dead_starfish",
                    props -> new StarfishBlock(props, true),
                    BlockBehaviour.Properties.ofFullCopy(STARFISH));

            CLAM = registerNeo(helper, "clam",
                    props -> new ClamBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SNOW)
                            .sound(WaterloggedSounds.SHELL_SOUND_GROUP)
                            .strength(0.05f)
                            .noOcclusion()
                            .randomTicks());

            ANEMONE = registerNeo(helper, "anemone",
                    props -> new AnemoneBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SNOW)
                            .strength(0.1f)
                            .noOcclusion()
                            .noCollision()
                            .sound(SoundType.SLIME_BLOCK)
                            .randomTicks());

            URCHIN = registerNeo(helper, "urchin",
                    props -> new UrchinBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_BLUE)
                            .strength(0.1f)
                            .noOcclusion()
                            .noCollision()
                            .sound(WaterloggedSounds.SHELL_SOUND_GROUP));

            SARGASSUM = registerNeo(helper, "sargassum",
                    props -> new SargassumPlantBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_YELLOW)
                            .destroyTime(0f)
                            .noOcclusion()
                            .noCollision()
                            .sound(SoundType.WET_GRASS));

            SARGASSUM_BLOCK = registerNeo(helper, "sargassum_block",
                    props -> new SargassumBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_YELLOW)
                            .strength(0.3f)
                            .noOcclusion()
                            .isSuffocating((state, world, pos) -> false)
                            .isViewBlocking((state, world, pos) -> false)
                            .sound(SoundType.WET_GRASS));

            PAPYRUS = registerNeo(helper, "papyrus",
                    props -> new PapyrusBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .strength(0)
                            .destroyTime(0f)
                            .noOcclusion()
                            .noCollision()
                            .sound(SoundType.AZALEA_LEAVES)
                            .randomTicks()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .dynamicShape());

            AQUARIUM_GLASS = registerNeo(helper, "aquarium_glass",
                    props -> new AquariumGlassBlock(props),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .strength(0.3F)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn((state, world, pos, type) -> false)
                            .isRedstoneConductor((state, world, pos) -> false)
                            .isSuffocating((state, world, pos) -> false)
                            .isViewBlocking((state, world, pos) -> false));
        });

        event.register(Registries.ITEM, helper -> {
            registerBlockItem(helper, "algae", ALGAE);
            registerBlockItem(helper, "wormy_dirt", WORMY_DIRT);
            registerBlockItem(helper, "wormy_mud", WORMY_MUD);
            registerBlockItem(helper, "oysters", OYSTERS);
            registerBlockItem(helper, "clam", CLAM);
            registerBlockItem(helper, "anemone", ANEMONE);
            registerBlockItem(helper, "sargassum_block", SARGASSUM_BLOCK);
            registerBlockItem(helper, "papyrus", PAPYRUS);
            registerBlockItem(helper, "aquarium_glass", AQUARIUM_GLASS);
        });
    }

    private static <T extends Block> T registerNeo(net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper<Block> helper,
                                                     String name, Function<BlockBehaviour.Properties, T> factory,
                                                     BlockBehaviour.Properties baseProps) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        T block = factory.apply(baseProps.setId(key));
        helper.register(id, block);
        return block;
    }

    private static void registerBlockItem(net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper<Item> helper,
                                           String name, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
        helper.register(id, new BlockItem(block, new Item.Properties().setId(itemKey)));
    }*/
//?}

    public static void init() {
        // Blocks are registered via static block (Fabric) or RegisterEvent (NeoForge)
    }
}
