package net.braeden.waterlogged.entity;

import net.braeden.waterlogged.block.entity.*;
import net.braeden.waterlogged.block.WaterloggedBlocks;
//?if fabric {
import net.braeden.waterlogged.tags.WaterloggedBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
//?} else {
/*import java.util.Set;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;*/
//?}
//?if fabric {
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//?}
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
//?if fabric {
import net.minecraft.world.entity.SpawnPlacements;
//?}
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedEntities {

    // Block Entity Types — populated by Fabric static block or NeoForge RegisterEvent
    public static BlockEntityType<RoeBlockEntity> ROE;
    public static BlockEntityType<StarfishBlockEntity> STARFISH;
    public static BlockEntityType<SeaSlugEggsBlockEntity> SEA_SLUG_EGGS;
    public static BlockEntityType<AnemoneBlockEntity> ANEMONE;
    public static BlockEntityType<UrchinBlockEntity> URCHIN;

    // Entity Types
    public static EntityType<FryEntity> FRY;
    public static EntityType<SunfishEntity> SUNFISH;
    public static EntityType<PelicanEntity> PELICAN;
    public static EntityType<SeaSlugEntity> SEA_SLUG;
    public static EntityType<CrabEntity> CRAB;
    public static EntityType<DongfishEntity> DONGFISH;
    public static EntityType<CatfishEntity> CATFISH;
    public static EntityType<SeahorseEntity> SEAHORSE;
    public static EntityType<BubbleEyeEntity> BUBBLE_EYE;
    public static EntityType<AnomalocarisEntity> ANOMALOCARIS;
    public static EntityType<AnglerfishEntity> ANGLERFISH;
    public static EntityType<MahiMahiEntity> MAHI_MAHI;
    public static EntityType<OrcaEntity> ORCA;
    public static EntityType<RightWhaleEntity> RIGHT_WHALE;

//?if fabric {
    static {
        ROE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "roe"),
                FabricBlockEntityTypeBuilder.create(RoeBlockEntity::new, WaterloggedBlocks.ROE).build());
        STARFISH = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "starfish"),
                FabricBlockEntityTypeBuilder.create(StarfishBlockEntity::new, WaterloggedBlocks.STARFISH, WaterloggedBlocks.DEAD_STARFISH).build());
        SEA_SLUG_EGGS = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "sea_slug_eggs"),
                FabricBlockEntityTypeBuilder.create(SeaSlugEggsBlockEntity::new, WaterloggedBlocks.SEA_SLUG_EGGS).build());
        ANEMONE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "anemone"),
                FabricBlockEntityTypeBuilder.create(AnemoneBlockEntity::new, WaterloggedBlocks.ANEMONE).build());
        URCHIN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "urchin"),
                FabricBlockEntityTypeBuilder.create(UrchinBlockEntity::new, WaterloggedBlocks.URCHIN).build());

        FRY = registerEntity("fry", FryEntity::new, MobCategory.WATER_AMBIENT, 0.2f, 0.15f);
        SUNFISH = registerEntity("sunfish", SunfishEntity::new, MobCategory.WATER_AMBIENT, 0.5f, 0.3f);
        PELICAN = registerEntity("pelican", PelicanEntity::new, MobCategory.AMBIENT, 0.7f, 1.65f);
        SEA_SLUG = registerEntity("sea_slug", SeaSlugEntity::new, MobCategory.WATER_AMBIENT, 0.5f, 0.3f);
        CRAB = registerEntity("crab", CrabEntity::new, MobCategory.CREATURE, 0.7f, 0.4f);
        DONGFISH = registerEntity("dongfish", DongfishEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE, 0.4f, 0.3f);
        CATFISH = registerEntity("catfish", CatfishEntity::new, MobCategory.WATER_AMBIENT, 0.6f, 0.4f);
        SEAHORSE = registerEntity("seahorse", SeahorseEntity::new, MobCategory.WATER_AMBIENT, 0.35f, 0.6f);
        BUBBLE_EYE = registerEntity("bubble_eye", BubbleEyeEntity::new, MobCategory.WATER_AMBIENT, 0.4f, 0.3f);
        ANOMALOCARIS = registerEntity("anomalocaris", AnomalocarisEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE, 0.8f, 0.3f);
        ANGLERFISH = registerEntity("anglerfish", AnglerfishEntity::new, MobCategory.WATER_AMBIENT, 0.8f, 0.5f);
        MAHI_MAHI = registerEntity("mahi_mahi", MahiMahiEntity::new, MobCategory.WATER_CREATURE, 1f, 0.8f);
        ORCA = registerEntity("orca", OrcaEntity::new, MobCategory.WATER_CREATURE, 3.5f, 2.5f);
        RIGHT_WHALE = registerEntity("right_whale", RightWhaleEntity::new, MobCategory.WATER_CREATURE, 4.2f, 3.0f);
    }

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> registerEntity(
            String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, name));
        //noinspection unchecked
        return (EntityType<T>) Registry.register(BuiltInRegistries.ENTITY_TYPE, key,
                EntityType.Builder.of(factory, category).sized(width, height).build(key));
    }
//?} else {
/*    public static void registerAll(RegisterEvent event) {
        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> {
            ROE = new BlockEntityType<>(RoeBlockEntity::new, Set.of(WaterloggedBlocks.ROE));
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "roe"), ROE);

            STARFISH = new BlockEntityType<>(StarfishBlockEntity::new, Set.of(WaterloggedBlocks.STARFISH, WaterloggedBlocks.DEAD_STARFISH));
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "starfish"), STARFISH);

            SEA_SLUG_EGGS = new BlockEntityType<>(SeaSlugEggsBlockEntity::new, Set.of(WaterloggedBlocks.SEA_SLUG_EGGS));
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "sea_slug_eggs"), SEA_SLUG_EGGS);

            ANEMONE = new BlockEntityType<>(AnemoneBlockEntity::new, Set.of(WaterloggedBlocks.ANEMONE));
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "anemone"), ANEMONE);

            URCHIN = new BlockEntityType<>(UrchinBlockEntity::new, Set.of(WaterloggedBlocks.URCHIN));
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "urchin"), URCHIN);
        });

        event.register(Registries.ENTITY_TYPE, helper -> {
            FRY = registerEntityNeo(helper, "fry", FryEntity::new, MobCategory.WATER_AMBIENT, 0.2f, 0.15f);
            SUNFISH = registerEntityNeo(helper, "sunfish", SunfishEntity::new, MobCategory.WATER_AMBIENT, 0.5f, 0.3f);
            PELICAN = registerEntityNeo(helper, "pelican", PelicanEntity::new, MobCategory.AMBIENT, 0.7f, 1.65f);
            SEA_SLUG = registerEntityNeo(helper, "sea_slug", SeaSlugEntity::new, MobCategory.WATER_AMBIENT, 0.5f, 0.3f);
            CRAB = registerEntityNeo(helper, "crab", CrabEntity::new, MobCategory.CREATURE, 0.7f, 0.4f);
            DONGFISH = registerEntityNeo(helper, "dongfish", DongfishEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE, 0.4f, 0.3f);
            CATFISH = registerEntityNeo(helper, "catfish", CatfishEntity::new, MobCategory.WATER_AMBIENT, 0.6f, 0.4f);
            SEAHORSE = registerEntityNeo(helper, "seahorse", SeahorseEntity::new, MobCategory.WATER_AMBIENT, 0.35f, 0.6f);
            BUBBLE_EYE = registerEntityNeo(helper, "bubble_eye", BubbleEyeEntity::new, MobCategory.WATER_AMBIENT, 0.4f, 0.3f);
            ANOMALOCARIS = registerEntityNeo(helper, "anomalocaris", AnomalocarisEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE, 0.8f, 0.3f);
            ANGLERFISH = registerEntityNeo(helper, "anglerfish", AnglerfishEntity::new, MobCategory.WATER_AMBIENT, 0.8f, 0.5f);
            MAHI_MAHI = registerEntityNeo(helper, "mahi_mahi", MahiMahiEntity::new, MobCategory.WATER_CREATURE, 1f, 0.8f);
            ORCA = registerEntityNeo(helper, "orca", OrcaEntity::new, MobCategory.WATER_CREATURE, 3.5f, 2.5f);
            RIGHT_WHALE = registerEntityNeo(helper, "right_whale", RightWhaleEntity::new, MobCategory.WATER_CREATURE, 4.2f, 3.0f);
        });
    }

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> registerEntityNeo(
            RegisterEvent.RegisterHelper<EntityType<?>> helper,
            String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        //noinspection unchecked
        EntityType<T> type = (EntityType<T>) EntityType.Builder.of(factory, category).sized(width, height).build(key);
        helper.register(id, type);
        return type;
    }*/
//?}

    public static void init() {
        // Register attributes
//?if fabric {
        FabricDefaultAttributeRegistry.register(FRY, FryEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SUNFISH, SunfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(PELICAN, PelicanEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEA_SLUG, SeaSlugEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DONGFISH, DongfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CATFISH, CatfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEAHORSE, SeahorseEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BUBBLE_EYE, BubbleEyeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ANOMALOCARIS, AnomalocarisEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ANGLERFISH, AnglerfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MAHI_MAHI, MahiMahiEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ORCA, OrcaEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RIGHT_WHALE, RightWhaleEntity.createAttributes());
//?} else {
/*        // Attributes registered via WaterloggedEntities.registerAttributes() listener*/
//?}

        // Register spawn placements
//?if fabric {
        SpawnPlacements.register(FRY, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(SUNFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(SEA_SLUG, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaSlugEntity::canSpawn);
        SpawnPlacements.register(CRAB, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CrabEntity::canSpawn);
        SpawnPlacements.register(DONGFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DongfishEntity::canSpawn);
        SpawnPlacements.register(CATFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(SEAHORSE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(BUBBLE_EYE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(ANOMALOCARIS, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnomalocarisEntity::canSpawn);
        SpawnPlacements.register(ANGLERFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(MAHI_MAHI, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(ORCA, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(RIGHT_WHALE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
//?} else {
/*        // Spawn placements registered via WaterloggedEntities.registerSpawnPlacements() listener*/
//?}

        // Register biome spawns
//?if fabric {
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SUNFISH_SPAWN_IN),
                MobCategory.WATER_AMBIENT, SUNFISH, 5, 2, 5
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SEA_SLUG_SPAWN_IN),
                MobCategory.WATER_AMBIENT, SEA_SLUG, 15, 2, 6
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.CATFISH_SPAWN_IN),
                MobCategory.WATER_AMBIENT, CATFISH, 3, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.CRAB_SPAWN_IN),
                MobCategory.CREATURE, CRAB, 8, 3, 5
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SEAHORSE_SPAWN_IN),
                MobCategory.WATER_AMBIENT, SEAHORSE, 4, 3, 8
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.BUBBLE_EYE_SPAWN_IN),
                MobCategory.WATER_AMBIENT, BUBBLE_EYE, 2, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ANOMALOCARIS_SPAWN_IN),
                MobCategory.UNDERGROUND_WATER_CREATURE, ANOMALOCARIS, 20, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.DONGFISH_SPAWN_IN),
                MobCategory.UNDERGROUND_WATER_CREATURE, DONGFISH, 4, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ANGLERFISH_SPAWN_IN),
                MobCategory.WATER_AMBIENT, ANGLERFISH, 6, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.MAHI_MAHI_SPAWN_IN),
                MobCategory.WATER_CREATURE, MAHI_MAHI, 4, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ORCA_SPAWN_IN),
                MobCategory.WATER_CREATURE, ORCA, 1, 1, 1
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().is(WaterloggedBiomeTags.RIGHT_WHALE_SPAWN_IN),
                MobCategory.WATER_CREATURE, RIGHT_WHALE, 2, 1, 3
        );
//?} else {
/*        // NeoForge: biome spawns handled via data/waterlogged/neoforge/biome_modifier/spawn_*.json*/
//?}
    }

//?if fabric {
//?} else {
/*    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(FRY, FryEntity.createAttributes().build());
        event.put(SUNFISH, SunfishEntity.createAttributes().build());
        event.put(PELICAN, PelicanEntity.createAttributes().build());
        event.put(SEA_SLUG, SeaSlugEntity.createAttributes().build());
        event.put(CRAB, CrabEntity.createAttributes().build());
        event.put(DONGFISH, DongfishEntity.createAttributes().build());
        event.put(CATFISH, CatfishEntity.createAttributes().build());
        event.put(SEAHORSE, SeahorseEntity.createAttributes().build());
        event.put(BUBBLE_EYE, BubbleEyeEntity.createAttributes().build());
        event.put(ANOMALOCARIS, AnomalocarisEntity.createAttributes().build());
        event.put(ANGLERFISH, AnglerfishEntity.createAttributes().build());
        event.put(MAHI_MAHI, MahiMahiEntity.createAttributes().build());
        event.put(ORCA, OrcaEntity.createAttributes().build());
        event.put(RIGHT_WHALE, RightWhaleEntity.createAttributes().build());
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(FRY, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(SUNFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(SEA_SLUG, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaSlugEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CRAB, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CrabEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(DONGFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DongfishEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CATFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(SEAHORSE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(BUBBLE_EYE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(ANOMALOCARIS, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnomalocarisEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(ANGLERFISH, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(MAHI_MAHI, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(ORCA, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(RIGHT_WHALE, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }*/
//?}
}
