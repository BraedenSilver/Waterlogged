package net.braeden.waterlogged.worldgen;

//?if fabric {
import net.minecraft.core.Registry;
//?}
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedFeatures {

    public static final Feature<NoneFeatureConfiguration> BEACH_SARGASSUM = register("beach_sargassum", new BeachSargassumFeature());
    public static final Feature<NoneFeatureConfiguration> DUCKWEED_SHORE = register("duckweed_shore", new DuckweedShoreFeature());
    public static final Feature<NoneFeatureConfiguration> SARGASSUM_PATCH = register("sargassum_patch", new SargassumPatchFeature());
    public static final Feature<NoneFeatureConfiguration> WATER_ADJACENT_PATCH = register("water_adjacent_patch", new WaterAdjacentPatchFeature());
    public static final Feature<NoneFeatureConfiguration> PAPYRUS = register("papyrus", new PapyrusFeature());
    public static final Feature<NoneFeatureConfiguration> STARFISH = register("starfish", new StarfishFeature());
    public static final Feature<NoneFeatureConfiguration> HYDROTHERMAL_VENT = register("hydrothermal_vent", new HydrothermalVentFeature());
    public static final Feature<NoneFeatureConfiguration> SWAMP_LOG = register("swamp_log", new SwampLogFeature());
    public static final Feature<NoneFeatureConfiguration> POND_CYPRESS = register("pond_cypress", new PondCypressFeature());

//?if fabric {
    private static <T extends Feature<?>> T register(String name, T feature) {
        //noinspection unchecked
        return (T) Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(MOD_ID, name), feature);
    }
//?} else {
/*    private static <T extends Feature<?>> T register(String name, T feature) {
        return feature;
    }

    public static void registerAll(net.neoforged.neoforge.registries.RegisterEvent event) {
        event.register(BuiltInRegistries.FEATURE.key(), helper -> {
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "beach_sargassum"), BEACH_SARGASSUM);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "duckweed_shore"), DUCKWEED_SHORE);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "sargassum_patch"), SARGASSUM_PATCH);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "water_adjacent_patch"), WATER_ADJACENT_PATCH);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "papyrus"), PAPYRUS);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "starfish"), STARFISH);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "hydrothermal_vent"), HYDROTHERMAL_VENT);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "swamp_log"), SWAMP_LOG);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "pond_cypress"), POND_CYPRESS);
        });
    }*/
//?}

    public static void init() {
        // Features registered via static field initializers above
    }
}
