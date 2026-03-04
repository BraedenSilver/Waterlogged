package net.braeden.waterlogged;

import net.braeden.waterlogged.entity.util.*;
import net.braeden.waterlogged.entity.PelicanSpawner;
import net.braeden.waterlogged.item.FishingNetItem;
import net.braeden.waterlogged.worldgen.WaterloggedFeatures;
import net.braeden.waterlogged.worldgen.WaterloggedPlacedFeatures;
import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.braeden.waterlogged.item.WaterloggedItemGroup;
import net.braeden.waterlogged.item.WaterloggedItems;
import net.braeden.waterlogged.sound.WaterloggedSounds;
import net.braeden.waterlogged.entity.WaterloggedEntities;
import net.braeden.waterlogged.criteria.WaterloggedCriteria;
import net.braeden.waterlogged.particle.WaterloggedParticles;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
//?if fabric {
import net.braeden.waterlogged.tags.WaterloggedBiomeTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.levelgen.GenerationStep;
//?} else {
/*import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;*/
//?}

//?if fabric {
public class WaterloggedMod implements ModInitializer {
//?} else {
/*@Mod("waterlogged")
public class WaterloggedMod {*/
//?}
	public static final String MOD_ID = "waterlogged";

//?if fabric {
	@Override
	public void onInitialize() {
//?} else {
/*	public WaterloggedMod(IEventBus modEventBus) {*/
//?}
		//?if neoforge {
		/*// Register deferred registration listeners on the mod event bus
		modEventBus.addListener(WaterloggedBlocks::registerAll);
		modEventBus.addListener(WaterloggedItems::registerAll);
		modEventBus.addListener(WaterloggedEntities::registerAll);
		modEventBus.addListener(WaterloggedSounds::registerAll);
		modEventBus.addListener(WaterloggedCriteria::registerAll);
		modEventBus.addListener(WaterloggedFeatures::registerAll);
		modEventBus.addListener(WaterloggedParticles::registerAll);
		modEventBus.addListener(WaterloggedItemGroup::registerAll);
		modEventBus.addListener(WaterloggedEntities::registerAttributes);
		modEventBus.addListener(WaterloggedEntities::registerSpawnPlacements);*/
		//?}
		WaterloggedBlocks.init();
		WaterloggedItems.init();
		WaterloggedItemGroup.init();
		WaterloggedSounds.init();
		WaterloggedEntities.init();
		WaterloggedCriteria.init();
		WaterloggedFeatures.init();
		WaterloggedPlacedFeatures.init();
		WaterloggedParticles.init();
		FishVariantInheritance.init();

		// Add world gen features to biomes
//?if fabric {
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.DUCKWEED_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.DUCKWEED);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SARGASSUM_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.SARGASSUM);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SARGASSUM_BEACH_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.SARGASSUM_BEACH);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.PAPYRUS_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.PAPYRUS);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.CLAMS_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.CLAM);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.OYSTER_REEF_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.OYSTERS);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.STARFISH_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.STARFISH);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ANEMONE_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.ANEMONE);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ALGAE_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.ALGAE);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.ALGAE_SWAMP_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.ALGAE_SWAMP);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.WORMY_DIRT_BIOMES),
				GenerationStep.Decoration.UNDERGROUND_ORES, WaterloggedPlacedFeatures.WORMY_DIRT);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.WORMY_MUD_BIOMES),
				GenerationStep.Decoration.UNDERGROUND_ORES, WaterloggedPlacedFeatures.WORMY_MUD);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.URCHIN_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.URCHIN);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(BiomeTags.IS_DEEP_OCEAN)
						&& !ctx.getBiomeKey().equals(ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace("deep_lukewarm_ocean"))),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.HYDROTHERMAL_VENT);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.SWAMP_LOG_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.SWAMP_LOG);
		BiomeModifications.addFeature(
				ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.POND_CYPRESS_BIOMES),
				GenerationStep.Decoration.VEGETAL_DECORATION, WaterloggedPlacedFeatures.POND_CYPRESS);
		// Remove vanilla swamp oak trees from cypress biomes so cypress fills that role
		BiomeModifications.create(Identifier.fromNamespaceAndPath(MOD_ID, "swamp_tree_adjustment"))
				.add(ModificationPhase.REMOVALS,
						ctx -> ctx.getBiomeRegistryEntry().is(WaterloggedBiomeTags.POND_CYPRESS_BIOMES),
						ctx -> ctx.getGenerationSettings().removeFeature(
								GenerationStep.Decoration.VEGETAL_DECORATION,
								ResourceKey.create(Registries.PLACED_FEATURE,
										Identifier.withDefaultNamespace("trees_swamp"))));
//?} else {
/*		// NeoForge: biome features handled via data/waterlogged/neoforge/biome_modifier/ JSONs*/
//?}

//?if fabric {
		PelicanSpawner spawner = new PelicanSpawner();
		ServerTickEvents.END_WORLD_TICK.register(level -> spawner.spawn(level, true, true));
//?} else {
/*		PelicanSpawner spawner = new PelicanSpawner();
		NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post event) -> {
			if (event.getLevel() instanceof ServerLevel serverLevel) {
				spawner.spawn(serverLevel, true, true);
			}
		});*/
//?}

		// Fishing net: left-click catchable fish to collect raw item
//?if fabric {
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!(player.getItemInHand(hand).getItem() instanceof FishingNetItem)) return InteractionResult.PASS;
			if (!(entity instanceof LivingEntity living) || FishingNetItem.getRawFish(living) == null) return InteractionResult.PASS;
			if (!world.isClientSide()) {
				FishingNetItem.doCapture(player, hand, living);
			}
			return InteractionResult.SUCCESS;
		});
//?} else {
/*		NeoForge.EVENT_BUS.addListener((AttackEntityEvent event) -> {
			var player = event.getEntity();
			var target = event.getTarget();
			var hand = InteractionHand.MAIN_HAND;
			if (!(player.getItemInHand(hand).getItem() instanceof FishingNetItem)) return;
			if (!(target instanceof LivingEntity living) || FishingNetItem.getRawFish(living) == null) return;
			if (!player.level().isClientSide()) {
				FishingNetItem.doCapture(player, hand, living);
			}
			event.setCanceled(true);
		});*/
//?}

		// Boost vanilla nautilus shell drop rate as compensation for removing our nautilus mob
		ResourceKey<LootTable> nautilusLoot = ResourceKey.create(Registries.LOOT_TABLE,
				Identifier.withDefaultNamespace("entities/nautilus"));
		// Modded fish catchable by fishing rod
		ResourceKey<LootTable> fishingFishLoot = ResourceKey.create(Registries.LOOT_TABLE,
				Identifier.withDefaultNamespace("gameplay/fishing/fish"));
//?if fabric {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (key.equals(nautilusLoot)) {
				tableBuilder.withPool(
						LootPool.lootPool()
								.add(LootItem.lootTableItem(Items.NAUTILUS_SHELL))
								.when(LootItemRandomChanceCondition.randomChance(0.5f))
				);
			}
			if (key.equals(fishingFishLoot)) {
				tableBuilder.withPool(
						LootPool.lootPool()
								.setRolls(net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1))
								.when(LootItemRandomChanceCondition.randomChance(0.25f))
								.add(LootItem.lootTableItem(WaterloggedItems.SUNFISH))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_MAHI_MAHI))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_CATFISH))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_BUBBLE_EYE))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_ANGLERFISH))
				);
			}
		});
//?} else {
/*		NeoForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
			if (event.getKey().equals(nautilusLoot)) {
			event.getTable().addPool(
						LootPool.lootPool()
								.add(LootItem.lootTableItem(Items.NAUTILUS_SHELL))
								.when(LootItemRandomChanceCondition.randomChance(0.5f))
								.build()
				);
			}
			if (event.getKey().equals(fishingFishLoot)) {
			event.getTable().addPool(
						LootPool.lootPool()
								.setRolls(net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1))
								.when(LootItemRandomChanceCondition.randomChance(0.25f))
								.add(LootItem.lootTableItem(WaterloggedItems.SUNFISH))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_MAHI_MAHI))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_CATFISH))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_BUBBLE_EYE))
								.add(LootItem.lootTableItem(WaterloggedItems.RAW_ANGLERFISH))
								.build()
				);
			}
		});*/
//?}
	}
}
