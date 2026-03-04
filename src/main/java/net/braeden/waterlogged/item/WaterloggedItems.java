package net.braeden.waterlogged.item;

import net.braeden.waterlogged.item.RoeBlockItem;
import net.braeden.waterlogged.item.SeaSlugBucketItem;
import net.braeden.waterlogged.item.SunfishBucketItem;
import net.braeden.waterlogged.item.UrchinBucketItem;
import net.braeden.waterlogged.item.WormItem;
import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.braeden.waterlogged.entity.WaterloggedEntities;
//?if fabric {
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
//?}
//?if fabric {
import net.minecraft.core.Registry;
//?}
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

@SuppressWarnings("unused")
public class WaterloggedItems {

    // Fields populated by Fabric static block or NeoForge RegisterEvent
    public static Item FRY_SPAWN_EGG;
    public static Item SUNFISH_SPAWN_EGG;
    public static Item PELICAN_SPAWN_EGG;
    public static Item SEA_SLUG_SPAWN_EGG;
    public static Item CRAB_SPAWN_EGG;
    public static Item DONGFISH_SPAWN_EGG;
    public static Item CATFISH_SPAWN_EGG;
    public static Item SEAHORSE_SPAWN_EGG;
    public static Item BUBBLE_EYE_SPAWN_EGG;
    public static Item ANOMALOCARIS_SPAWN_EGG;
    public static Item ANGLERFISH_SPAWN_EGG;
    public static Item MAHI_MAHI_SPAWN_EGG;
    public static Item ORCA_SPAWN_EGG;
    public static Item RIGHT_WHALE_SPAWN_EGG;

    public static Item ROE;
    public static Item SEA_SLUG_EGGS;
    public static Item STARFISH;
    public static Item DEAD_STARFISH;
    public static Item DUCKWEED;
    public static Item SARGASSUM;
    public static Item SUNFISH_BUCKET;
    public static Item FRY_BUCKET;
    public static Item SEA_SLUG_BUCKET;
    public static Item CRAB_BUCKET;
    public static Item DONGFISH_BUCKET;
    public static Item CATFISH_BUCKET;
    public static Item SEAHORSE_BUCKET;
    public static Item BUBBLE_EYE_BUCKET;
    public static Item ANOMALOCARIS_BUCKET;
    public static Item ANGLERFISH_BUCKET;
    public static Item MAHI_MAHI_BUCKET;
    public static Item URCHIN_BUCKET;
    public static Item PEARL;
    public static Item WORM;
    public static Item RAW_MAHI_MAHI;
    public static Item COOKED_MAHI_MAHI;
    public static Item RAW_CRAB_LEGS;
    public static Item COOKED_CRAB_LEGS;
    public static Item RAW_CATFISH;
    public static Item COOKED_CATFISH;
    public static Item RAW_BUBBLE_EYE;
    public static Item COOKED_BUBBLE_EYE;
    public static Item RAW_ANGLERFISH;
    public static Item COOKED_ANGLERFISH;
    public static Item SUNFISH;
    public static Item FRIED_SUNFISH;
    public static Item FISHING_NET;

//?if fabric {
    static {
        FRY_SPAWN_EGG = registerSpawnEgg("fry", WaterloggedEntities.FRY);
        SUNFISH_SPAWN_EGG = registerSpawnEgg("sunfish", WaterloggedEntities.SUNFISH);
        PELICAN_SPAWN_EGG = registerSpawnEgg("pelican", WaterloggedEntities.PELICAN);
        SEA_SLUG_SPAWN_EGG = registerSpawnEgg("sea_slug", WaterloggedEntities.SEA_SLUG);
        CRAB_SPAWN_EGG = registerSpawnEgg("crab", WaterloggedEntities.CRAB);
        DONGFISH_SPAWN_EGG = registerSpawnEgg("dongfish", WaterloggedEntities.DONGFISH);
        CATFISH_SPAWN_EGG = registerSpawnEgg("catfish", WaterloggedEntities.CATFISH);
        SEAHORSE_SPAWN_EGG = registerSpawnEgg("seahorse", WaterloggedEntities.SEAHORSE);
        BUBBLE_EYE_SPAWN_EGG = registerSpawnEgg("bubble_eye", WaterloggedEntities.BUBBLE_EYE);
        ANOMALOCARIS_SPAWN_EGG = registerSpawnEgg("anomalocaris", WaterloggedEntities.ANOMALOCARIS);
        ANGLERFISH_SPAWN_EGG = registerSpawnEgg("anglerfish", WaterloggedEntities.ANGLERFISH);
        MAHI_MAHI_SPAWN_EGG = registerSpawnEgg("mahi_mahi", WaterloggedEntities.MAHI_MAHI);
        ORCA_SPAWN_EGG = registerSpawnEgg("orca", WaterloggedEntities.ORCA);
        RIGHT_WHALE_SPAWN_EGG = registerSpawnEgg("right_whale", WaterloggedEntities.RIGHT_WHALE);

        ROE = register("roe", id -> new RoeBlockItem(WaterloggedBlocks.ROE,
                new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
        SEA_SLUG_EGGS = register("sea_slug_eggs", id -> new SeaSlugEggsBlockItem(WaterloggedBlocks.SEA_SLUG_EGGS,
                new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
        STARFISH = register("starfish", id -> new StarfishBlockItem(WaterloggedBlocks.STARFISH,
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
        DEAD_STARFISH = register("dead_starfish", id -> new StarfishBlockItem(WaterloggedBlocks.DEAD_STARFISH,
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
        DUCKWEED = register("duckweed", id -> new PlaceOnWaterBlockItem(WaterloggedBlocks.DUCKWEED,
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
        SARGASSUM = register("sargassum", id -> new PlaceOnWaterBlockItem(WaterloggedBlocks.SARGASSUM,
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

        SUNFISH_BUCKET = register("sunfish_bucket", id -> new SunfishBucketItem(WaterloggedEntities.SUNFISH,
                new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
        FRY_BUCKET = registerBucket("fry", WaterloggedEntities.FRY);
        SEA_SLUG_BUCKET = register("sea_slug_bucket", id -> new SeaSlugBucketItem(WaterloggedEntities.SEA_SLUG,
                new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
        CRAB_BUCKET = registerBucket("crab", WaterloggedEntities.CRAB);
        DONGFISH_BUCKET = registerBucket("dongfish", WaterloggedEntities.DONGFISH);
        CATFISH_BUCKET = registerBucket("catfish", WaterloggedEntities.CATFISH);
        SEAHORSE_BUCKET = registerBucket("seahorse", WaterloggedEntities.SEAHORSE);
        BUBBLE_EYE_BUCKET = registerBucket("bubble_eye", WaterloggedEntities.BUBBLE_EYE);
        ANOMALOCARIS_BUCKET = registerBucket("anomalocaris", WaterloggedEntities.ANOMALOCARIS);
        ANGLERFISH_BUCKET = registerBucket("anglerfish", WaterloggedEntities.ANGLERFISH);
        MAHI_MAHI_BUCKET = registerBucket("mahi_mahi", WaterloggedEntities.MAHI_MAHI);
        URCHIN_BUCKET = register("urchin_bucket", id -> new UrchinBucketItem(
                new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));

        PEARL = register("pearl", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
        WORM = register("worm", id -> new WormItem(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).build(),
                        Consumables.defaultFood()
                                .onConsume(new ApplyStatusEffectsConsumeEffect(
                                        new MobEffectInstance(MobEffects.NAUSEA, 200, 0), 1.0f))
                                .build())
                .setId(ResourceKey.create(Registries.ITEM, id))));

        RAW_MAHI_MAHI = register("raw_mahi_mahi", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        COOKED_MAHI_MAHI = register("cooked_mahi_mahi", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        RAW_CRAB_LEGS = register("raw_crab_legs", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        COOKED_CRAB_LEGS = register("cooked_crab_legs", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        RAW_CATFISH = register("raw_catfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        COOKED_CATFISH = register("cooked_catfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        RAW_BUBBLE_EYE = register("raw_bubble_eye", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        COOKED_BUBBLE_EYE = register("cooked_bubble_eye", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        RAW_ANGLERFISH = register("raw_anglerfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        COOKED_ANGLERFISH = register("cooked_anglerfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        SUNFISH = register("sunfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        FRIED_SUNFISH = register("fried_sunfish", id -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                .setId(ResourceKey.create(Registries.ITEM, id))));
        FISHING_NET = register("fishing_net", id -> new FishingNetItem(new Item.Properties()
                .durability(131).enchantable(10).setId(ResourceKey.create(Registries.ITEM, id))));
    }

    private static <T extends Item> T register(String name, java.util.function.Function<Identifier, T> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        T item = factory.apply(id);
        Registry.register(BuiltInRegistries.ITEM, id, item);
        return item;
    }

    private static Item registerSpawnEgg(String name, EntityType<?> type) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name + "_spawn_egg");
        Item.Properties props = new Item.Properties().stacksTo(64).spawnEgg(type)
                .setId(ResourceKey.create(Registries.ITEM, id));
        return Registry.register(BuiltInRegistries.ITEM, id, new SpawnEggItem(props));
    }

    private static Item registerBucket(String name, EntityType<? extends Mob> type) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name + "_bucket");
        Item.Properties props = new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id));
        return Registry.register(BuiltInRegistries.ITEM, id,
                new MobBucketItem(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props));
    }
//?} else {
/*    public static void registerAll(net.neoforged.neoforge.registries.RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
            FRY_SPAWN_EGG = registerSpawnEggNeo(helper, "fry", WaterloggedEntities.FRY);
            SUNFISH_SPAWN_EGG = registerSpawnEggNeo(helper, "sunfish", WaterloggedEntities.SUNFISH);
            PELICAN_SPAWN_EGG = registerSpawnEggNeo(helper, "pelican", WaterloggedEntities.PELICAN);
            SEA_SLUG_SPAWN_EGG = registerSpawnEggNeo(helper, "sea_slug", WaterloggedEntities.SEA_SLUG);
            CRAB_SPAWN_EGG = registerSpawnEggNeo(helper, "crab", WaterloggedEntities.CRAB);
            DONGFISH_SPAWN_EGG = registerSpawnEggNeo(helper, "dongfish", WaterloggedEntities.DONGFISH);
            CATFISH_SPAWN_EGG = registerSpawnEggNeo(helper, "catfish", WaterloggedEntities.CATFISH);
            SEAHORSE_SPAWN_EGG = registerSpawnEggNeo(helper, "seahorse", WaterloggedEntities.SEAHORSE);
            BUBBLE_EYE_SPAWN_EGG = registerSpawnEggNeo(helper, "bubble_eye", WaterloggedEntities.BUBBLE_EYE);
            ANOMALOCARIS_SPAWN_EGG = registerSpawnEggNeo(helper, "anomalocaris", WaterloggedEntities.ANOMALOCARIS);
            ANGLERFISH_SPAWN_EGG = registerSpawnEggNeo(helper, "anglerfish", WaterloggedEntities.ANGLERFISH);
            MAHI_MAHI_SPAWN_EGG = registerSpawnEggNeo(helper, "mahi_mahi", WaterloggedEntities.MAHI_MAHI);
            ORCA_SPAWN_EGG = registerSpawnEggNeo(helper, "orca", WaterloggedEntities.ORCA);
            RIGHT_WHALE_SPAWN_EGG = registerSpawnEggNeo(helper, "right_whale", WaterloggedEntities.RIGHT_WHALE);

            ROE = registerNeo(helper, "roe", id -> new RoeBlockItem(WaterloggedBlocks.ROE,
                    new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
            SEA_SLUG_EGGS = registerNeo(helper, "sea_slug_eggs", id -> new SeaSlugEggsBlockItem(WaterloggedBlocks.SEA_SLUG_EGGS,
                    new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
            STARFISH = registerNeo(helper, "starfish", id -> new StarfishBlockItem(WaterloggedBlocks.STARFISH,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
            DEAD_STARFISH = registerNeo(helper, "dead_starfish", id -> new StarfishBlockItem(WaterloggedBlocks.DEAD_STARFISH,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
            DUCKWEED = registerNeo(helper, "duckweed", id -> new PlaceOnWaterBlockItem(WaterloggedBlocks.DUCKWEED,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
            SARGASSUM = registerNeo(helper, "sargassum", id -> new PlaceOnWaterBlockItem(WaterloggedBlocks.SARGASSUM,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

            SUNFISH_BUCKET = registerNeo(helper, "sunfish_bucket", id -> new SunfishBucketItem(WaterloggedEntities.SUNFISH,
                    new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
            FRY_BUCKET = registerBucketNeo(helper, "fry", WaterloggedEntities.FRY);
            SEA_SLUG_BUCKET = registerNeo(helper, "sea_slug_bucket", id -> new SeaSlugBucketItem(WaterloggedEntities.SEA_SLUG,
                    new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
            CRAB_BUCKET = registerBucketNeo(helper, "crab", WaterloggedEntities.CRAB);
            DONGFISH_BUCKET = registerBucketNeo(helper, "dongfish", WaterloggedEntities.DONGFISH);
            CATFISH_BUCKET = registerBucketNeo(helper, "catfish", WaterloggedEntities.CATFISH);
            SEAHORSE_BUCKET = registerBucketNeo(helper, "seahorse", WaterloggedEntities.SEAHORSE);
            BUBBLE_EYE_BUCKET = registerBucketNeo(helper, "bubble_eye", WaterloggedEntities.BUBBLE_EYE);
            ANOMALOCARIS_BUCKET = registerBucketNeo(helper, "anomalocaris", WaterloggedEntities.ANOMALOCARIS);
            ANGLERFISH_BUCKET = registerBucketNeo(helper, "anglerfish", WaterloggedEntities.ANGLERFISH);
            MAHI_MAHI_BUCKET = registerBucketNeo(helper, "mahi_mahi", WaterloggedEntities.MAHI_MAHI);
            URCHIN_BUCKET = registerNeo(helper, "urchin_bucket", id -> new UrchinBucketItem(
                    new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));

            PEARL = registerNeo(helper, "pearl", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
            WORM = registerNeo(helper, "worm", id -> new WormItem(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).build(),
                            Consumables.defaultFood()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                                            new MobEffectInstance(MobEffects.NAUSEA, 200, 0), 1.0f))
                                    .build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));

            RAW_MAHI_MAHI = registerNeo(helper, "raw_mahi_mahi", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            COOKED_MAHI_MAHI = registerNeo(helper, "cooked_mahi_mahi", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            RAW_CRAB_LEGS = registerNeo(helper, "raw_crab_legs", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            COOKED_CRAB_LEGS = registerNeo(helper, "cooked_crab_legs", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            RAW_CATFISH = registerNeo(helper, "raw_catfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            COOKED_CATFISH = registerNeo(helper, "cooked_catfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            RAW_BUBBLE_EYE = registerNeo(helper, "raw_bubble_eye", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            COOKED_BUBBLE_EYE = registerNeo(helper, "cooked_bubble_eye", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            RAW_ANGLERFISH = registerNeo(helper, "raw_anglerfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            COOKED_ANGLERFISH = registerNeo(helper, "cooked_anglerfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            SUNFISH = registerNeo(helper, "sunfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            FRIED_SUNFISH = registerNeo(helper, "fried_sunfish", id -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.9f).build())
                    .setId(ResourceKey.create(Registries.ITEM, id))));
            FISHING_NET = registerNeo(helper, "fishing_net", id -> new FishingNetItem(new Item.Properties()
                    .durability(131).enchantable(10).setId(ResourceKey.create(Registries.ITEM, id))));
        });
    }

    private static <T extends Item> T registerNeo(net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper<Item> helper,
                                                    String name, java.util.function.Function<Identifier, T> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        T item = factory.apply(id);
        helper.register(id, item);
        return item;
    }

    private static Item registerSpawnEggNeo(net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper<Item> helper,
                                             String name, EntityType<?> type) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name + "_spawn_egg");
        Item.Properties props = new Item.Properties().stacksTo(64).spawnEgg(type)
                .setId(ResourceKey.create(Registries.ITEM, id));
        SpawnEggItem item = new SpawnEggItem(props);
        helper.register(id, item);
        return item;
    }

    private static Item registerBucketNeo(net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper<Item> helper,
                                           String name, EntityType<? extends Mob> type) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name + "_bucket");
        Item.Properties props = new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id));
        MobBucketItem item = new MobBucketItem(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props);
        helper.register(id, item);
        return item;
    }*/
//?}

    public static void init() {
        // Items are registered via static block (Fabric) or RegisterEvent (NeoForge)
        //?if fabric {
        CompostingChanceRegistry.INSTANCE.add(DUCKWEED, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(SARGASSUM, 0.3f);
        // CompostingChanceRegistry.INSTANCE.add(WORM, 1f); // Removing this so worms are not compostable
        CompostingChanceRegistry.INSTANCE.add(WaterloggedBlocks.ALGAE.asItem(), 0.2f);
        CompostingChanceRegistry.INSTANCE.add(WaterloggedBlocks.PAPYRUS.asItem(), 0.2f);
        //?} else {
        /*// NeoForge: compostables registered via data/waterlogged/neoforge/data_maps/item/compostables.json*/
        //?}
    }
}
