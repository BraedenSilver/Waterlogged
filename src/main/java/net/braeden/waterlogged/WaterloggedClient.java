package net.braeden.waterlogged;

import net.braeden.waterlogged.block.entity.RoeBlockEntity;
import net.braeden.waterlogged.block.entity.SeaSlugEggsBlockEntity;
import net.braeden.waterlogged.entity.util.SeaSlugColor;
import net.braeden.waterlogged.particle.AlgaeParticle;
import net.braeden.waterlogged.particle.WormParticle;
import net.braeden.waterlogged.entity.client.*;
import net.braeden.waterlogged.entity.client.AnemoneBlockEntityRenderer;
import net.braeden.waterlogged.entity.client.StarfishBlockEntityRenderer;
import net.braeden.waterlogged.entity.client.UrchinBlockEntityRenderer;
import net.braeden.waterlogged.block.WaterloggedBlocks;
import net.braeden.waterlogged.entity.WaterloggedEntities;
import net.braeden.waterlogged.particle.WaterloggedParticles;
//?if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
//?} else {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;*/
//?}

//?if fabric {
public class WaterloggedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Block render layers
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.ROE, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.DUCKWEED, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.OYSTERS, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.CLAM, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.SEA_SLUG_EGGS, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.PAPYRUS, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.SARGASSUM, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.SARGASSUM_BLOCK, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.ALGAE, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(WaterloggedBlocks.AQUARIUM_GLASS, ChunkSectionLayer.CUTOUT);

        // Entity renderers
        EntityRenderers.register(WaterloggedEntities.FRY,          FryRenderer::new);
        EntityRenderers.register(WaterloggedEntities.SUNFISH,       SunfishRenderer::new);
        EntityRenderers.register(WaterloggedEntities.PELICAN,       PelicanRenderer::new);
        EntityRenderers.register(WaterloggedEntities.SEA_SLUG,      SeaSlugRenderer::new);
        EntityRenderers.register(WaterloggedEntities.CRAB,          CrabRenderer::new);
        EntityRenderers.register(WaterloggedEntities.DONGFISH,      DongfishRenderer::new);
        EntityRenderers.register(WaterloggedEntities.CATFISH,       CatfishRenderer::new);
        EntityRenderers.register(WaterloggedEntities.SEAHORSE,      SeahorseRenderer::new);
        EntityRenderers.register(WaterloggedEntities.BUBBLE_EYE,    BubbleEyeRenderer::new);
        EntityRenderers.register(WaterloggedEntities.ANOMALOCARIS,  AnomalocarisRenderer::new);
        EntityRenderers.register(WaterloggedEntities.ANGLERFISH,    AnglerfishRenderer::new);
        EntityRenderers.register(WaterloggedEntities.MAHI_MAHI,     MahiMahiRenderer::new);
        EntityRenderers.register(WaterloggedEntities.ORCA,           OrcaRenderer::new);
        EntityRenderers.register(WaterloggedEntities.RIGHT_WHALE,    RightWhaleRenderer::new);

        // Model layer definitions
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.STARFISH,       StarfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.ANEMONE,        AnemoneModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.URCHIN,         UrchinModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.FRY,            FryModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.SUNFISH,        SunfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.PELICAN,        PelicanModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.SEA_SLUG,       SeaSlugModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.CRAB,           CrabModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.DONGFISH,       DongfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.CATFISH,        CatfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.SEAHORSE,       SeahorseModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.BUBBLE_EYE,     BubbleEyeModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.ANOMALOCARIS,   AnomalocarisModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.ANGLERFISH,     AnglerfishModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.MAHI_MAHI,      MahiMahiModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.ORCA,            OrcaModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterloggedEntityModelLayers.RIGHT_WHALE,     RightWhaleModel::getTexturedModelData);

        // Block entity renderers
        BlockEntityRenderers.register(WaterloggedEntities.STARFISH, StarfishBlockEntityRenderer::new);
        BlockEntityRenderers.register(WaterloggedEntities.ANEMONE, AnemoneBlockEntityRenderer::new);
        BlockEntityRenderers.register(WaterloggedEntities.URCHIN, UrchinBlockEntityRenderer::new);

        // Particles
        ParticleFactoryRegistry.getInstance().register(WaterloggedParticles.ALGAE, AlgaeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(WaterloggedParticles.WORM, WormParticle.Factory::new);

        // Block color providers — tint roe and sea-slug-egg block models with parent colours
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return -1;
            if (world.getBlockEntity(pos) instanceof RoeBlockEntity roe) {
                int argb = roe.getTintArgb();
                if (argb != -1) return argb;
            }
            return -1;
        }, WaterloggedBlocks.ROE);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return -1;
            if (world.getBlockEntity(pos) instanceof SeaSlugEggsBlockEntity eggs) {
                return SeaSlugColor.byId(eggs.getLayingParentColorId()).getArgb();
            }
            return -1;
        }, WaterloggedBlocks.SEA_SLUG_EGGS);
    }
//?} else {
/*@EventBusSubscriber(modid = "waterlogged", value = Dist.CLIENT)
public class WaterloggedClient {

    // Block render layers on NeoForge 1.21+ are set via "render_type" in model JSON.
    // Ensure datagen models include "render_type": "minecraft:cutout" / "minecraft:translucent".

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Entity renderers
        event.registerEntityRenderer(WaterloggedEntities.FRY,          FryRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.SUNFISH,       SunfishRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.PELICAN,       PelicanRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.SEA_SLUG,      SeaSlugRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.CRAB,          CrabRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.DONGFISH,      DongfishRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.CATFISH,       CatfishRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.SEAHORSE,      SeahorseRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.BUBBLE_EYE,    BubbleEyeRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.ANOMALOCARIS,  AnomalocarisRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.ANGLERFISH,    AnglerfishRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.MAHI_MAHI,     MahiMahiRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.ORCA,           OrcaRenderer::new);
        event.registerEntityRenderer(WaterloggedEntities.RIGHT_WHALE,    RightWhaleRenderer::new);

        // Block entity renderers
        event.registerBlockEntityRenderer(WaterloggedEntities.STARFISH, StarfishBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(WaterloggedEntities.ANEMONE, AnemoneBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(WaterloggedEntities.URCHIN, UrchinBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WaterloggedEntityModelLayers.STARFISH,       StarfishModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.ANEMONE,        AnemoneModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.URCHIN,         UrchinModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.FRY,            FryModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.SUNFISH,        SunfishModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.PELICAN,        PelicanModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.SEA_SLUG,       SeaSlugModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.CRAB,           CrabModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.DONGFISH,       DongfishModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.CATFISH,        CatfishModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.SEAHORSE,       SeahorseModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.BUBBLE_EYE,     BubbleEyeModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.ANOMALOCARIS,   AnomalocarisModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.ANGLERFISH,     AnglerfishModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.MAHI_MAHI,      MahiMahiModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.ORCA,            OrcaModel::getTexturedModelData);
        event.registerLayerDefinition(WaterloggedEntityModelLayers.RIGHT_WHALE,     RightWhaleModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WaterloggedParticles.ALGAE, AlgaeParticle.Factory::new);
        event.registerSpriteSet(WaterloggedParticles.WORM, WormParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return -1;
            if (world.getBlockEntity(pos) instanceof RoeBlockEntity roe) {
                int argb = roe.getTintArgb();
                if (argb != -1) return argb;
            }
            return -1;
        }, WaterloggedBlocks.ROE);

        event.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return -1;
            if (world.getBlockEntity(pos) instanceof SeaSlugEggsBlockEntity eggs) {
                return SeaSlugColor.byId(eggs.getLayingParentColorId()).getArgb();
            }
            return -1;
        }, WaterloggedBlocks.SEA_SLUG_EGGS);
    }*/
//?}
}
