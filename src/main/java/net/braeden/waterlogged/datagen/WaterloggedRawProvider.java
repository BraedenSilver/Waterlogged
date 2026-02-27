package net.braeden.waterlogged.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Raw JSON provider for files that have no standard datagen API:
 * sounds, atlases, particles, custom variant tags, complex block models,
 * some item models, minecraft namespace overrides, and worldgen features.
 */
public class WaterloggedRawProvider implements DataProvider {

    private final FabricDataOutput output;

    public WaterloggedRawProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        // ── Sounds ────────────────────────────────────────────────────────────
        futures.add(save(cache, asset("sounds.json"), sounds()));

        // ── Atlases ───────────────────────────────────────────────────────────
        futures.add(save(cache, asset("atlases/blocks.json"),          atlasBlocks()));
        futures.add(save(cache, asset("atlases/items.json"),           atlasItems()));
        futures.add(save(cache, asset("atlases/block_or_item.json"),   atlasBlockOrItem()));

        // ── Particles ─────────────────────────────────────────────────────────
        futures.add(save(cache, asset("particles/algae.json"),  particleAlgae()));
        futures.add(save(cache, asset("particles/worm.json"),   particleWorm()));

        // ── Complex block models ───────────────────────────────────────────────
        futures.add(save(cache, asset("models/block/algae.json"),         blockAlgae()));
        futures.add(save(cache, asset("models/block/clam.json"),          blockClam()));
        futures.add(save(cache, asset("models/block/duckweed.json"),      blockDuckweed()));
        futures.add(save(cache, asset("models/block/oysters.json"),       blockOysters()));
        futures.add(save(cache, asset("models/block/papyrus_age0.json"),  blockPapyrusAge0()));
        futures.add(save(cache, asset("models/block/papyrus_age1.json"),  blockPapyrusAge1()));
        futures.add(save(cache, asset("models/block/papyrus_age2.json"),  blockPapyrusAge2()));
        futures.add(save(cache, asset("models/block/roe.json"),           blockRoe()));
        futures.add(save(cache, asset("models/block/sargassum.json"),     blockSargassum()));
        futures.add(save(cache, asset("models/block/sea_slug_eggs.json"), blockSeaSlugEggs()));

        // ── Item models that can't be expressed via ModelTemplate ──────────────
        futures.add(save(cache, asset("models/item/oysters.json"),        itemOysters()));

        // ── Items/ definitions that require condition logic ────────────────────
        futures.add(save(cache, asset("items/dongfish_bucket.json"),      itemsDongfishBucket()));

        // ── Minecraft namespace overrides ─────────────────────────────────────
        futures.add(save(cache, asset("models/item/tropical_fish_bucket.json"),   mcTropicalFishBucketModel()));
        futures.add(save(cache, mcAsset("items/tropical_fish_bucket.json"),       mcTropicalFishBucketItem()));

        // ── Custom variant tags ────────────────────────────────────────────────
        futures.add(save(cache, data("tags/crab_variant/natural_variants.json"),      crabNaturalVariants()));
        futures.add(save(cache, data("tags/sea_slug_color/base_colors.json"),         seaSlugBaseColors()));
        futures.add(save(cache, data("tags/sea_slug_color/pattern_colors.json"),      seaSlugPatternColors()));
        futures.add(save(cache, data("tags/sea_slug_pattern/natural_patterns.json"),  seaSlugNaturalPatterns()));
        futures.add(save(cache, data("tags/sunfish_variant/natural_sunfish.json"),    sunfishNatural()));
        futures.add(save(cache, data("tags/sunfish_variant/pelican_beak_variants.json"), sunfishPelicanBeak()));

        // ── Worldgen: configured features ─────────────────────────────────────
        futures.add(save(cache, data("worldgen/configured_feature/algae.json"),           cfAlgae()));
        futures.add(save(cache, data("worldgen/configured_feature/anemone.json"),         cfAnemone()));
        futures.add(save(cache, data("worldgen/configured_feature/clam.json"),            cfClam()));
        futures.add(save(cache, data("worldgen/configured_feature/duckweed.json"),        cfDuckweed()));
        futures.add(save(cache, data("worldgen/configured_feature/hydrothermal_vent.json"), cfHydrothermalVent()));
        futures.add(save(cache, data("worldgen/configured_feature/oysters.json"),         cfOysters()));
        futures.add(save(cache, data("worldgen/configured_feature/papyrus.json"),         cfPapyrus()));
        futures.add(save(cache, data("worldgen/configured_feature/sargassum.json"),       cfSargassum()));
        futures.add(save(cache, data("worldgen/configured_feature/starfish.json"),        cfStarfish()));
        futures.add(save(cache, data("worldgen/configured_feature/urchin.json"),          cfUrchin()));
        futures.add(save(cache, data("worldgen/configured_feature/wormy_dirt.json"),      cfWormyDirt()));
        futures.add(save(cache, data("worldgen/configured_feature/wormy_mud.json"),       cfWormyMud()));

        // ── Worldgen: placed features ──────────────────────────────────────────
        futures.add(save(cache, data("worldgen/placed_feature/algae.json"),           pfAlgae()));
        futures.add(save(cache, data("worldgen/placed_feature/algae_swamp.json"),     pfAlgaeSwamp()));
        futures.add(save(cache, data("worldgen/placed_feature/anemone.json"),         pfAnemone()));
        futures.add(save(cache, data("worldgen/placed_feature/clam.json"),            pfClam()));
        futures.add(save(cache, data("worldgen/placed_feature/duckweed.json"),        pfDuckweed()));
        futures.add(save(cache, data("worldgen/placed_feature/hydrothermal_vent.json"), pfHydrothermalVent()));
        futures.add(save(cache, data("worldgen/placed_feature/oysters.json"),         pfOysters()));
        futures.add(save(cache, data("worldgen/placed_feature/papyrus.json"),         pfPapyrus()));
        futures.add(save(cache, data("worldgen/placed_feature/sargassum.json"),       pfSargassum()));
        futures.add(save(cache, data("worldgen/placed_feature/starfish.json"),        pfStarfish()));
        futures.add(save(cache, data("worldgen/placed_feature/urchin.json"),          pfUrchin()));
        futures.add(save(cache, data("worldgen/placed_feature/wormy_dirt.json"),      pfWormyDirt()));
        futures.add(save(cache, data("worldgen/placed_feature/wormy_mud.json"),       pfWormyMud()));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Waterlogged Raw JSON";
    }

    // ── Path helpers ──────────────────────────────────────────────────────────

    private Path asset(String rel) {
        return output.getOutputFolder().resolve("assets/waterlogged/" + rel);
    }

    private Path mcAsset(String rel) {
        return output.getOutputFolder().resolve("assets/minecraft/" + rel);
    }

    private Path data(String rel) {
        return output.getOutputFolder().resolve("data/waterlogged/" + rel);
    }

    private CompletableFuture<?> save(CachedOutput cache, Path path, JsonObject json) {
        return DataProvider.saveStable(cache, json, path);
    }

    // ── Sounds ────────────────────────────────────────────────────────────────

    private JsonObject sounds() {
        JsonObject root = new JsonObject();

        root.add("item.worm.use",      soundEntry(true, "subtitles.item.worm.use",
                soundObj("waterlogged:item/worm/use1"), soundObj("waterlogged:item/worm/use2")));

        root.add("block.shell.break",  soundEntry(false, "subtitles.block.generic.break",
                soundVol("mob/turtle/egg/jump_egg1", 0.85), soundVol("mob/turtle/egg/jump_egg2", 0.85),
                soundVol("mob/turtle/egg/jump_egg3", 0.85), soundVol("mob/turtle/egg/jump_egg4", 0.85)));
        root.add("block.shell.fall",   soundEntrySimple(null,
                "waterlogged:block/shell/step1","waterlogged:block/shell/step2",
                "waterlogged:block/shell/step3","waterlogged:block/shell/step4"));
        root.add("block.shell.hit",    soundEntrySimple("subtitles.block.generic.hit",
                "waterlogged:block/shell/step1","waterlogged:block/shell/step2",
                "waterlogged:block/shell/step3","waterlogged:block/shell/step4"));
        root.add("block.shell.place",  soundEntry(false, "subtitles.block.generic.place",
                soundVol("mob/turtle/egg/jump_egg1", 0.85), soundVol("mob/turtle/egg/jump_egg2", 0.85),
                soundVol("mob/turtle/egg/jump_egg3", 0.85), soundVol("mob/turtle/egg/jump_egg4", 0.85)));
        root.add("block.shell.step",   soundEntrySimple("subtitles.block.generic.footsteps",
                "waterlogged:block/shell/step1","waterlogged:block/shell/step2",
                "waterlogged:block/shell/step3","waterlogged:block/shell/step4"));

        root.add("block.roe.hatch",      soundEntry(false, "subtitles.block.roe.hatch",
                soundPitchVol("block/frogspawn/hatch1",1.2,0.6), soundPitchVol("block/frogspawn/hatch2",1.2,0.6),
                soundPitchVol("block/frogspawn/hatch3",1.2,0.6), soundPitchVol("block/frogspawn/hatch4",1.2,0.6)));
        root.add("block.sea_slug_eggs.hatch", soundEntry(false, "subtitles.block.sea_slug_eggs.hatch",
                soundPitchVol("block/frogspawn/hatch1",1.2,0.6), soundPitchVol("block/frogspawn/hatch2",1.2,0.6),
                soundPitchVol("block/frogspawn/hatch3",1.2,0.6), soundPitchVol("block/frogspawn/hatch4",1.2,0.6)));

        root.add("entity.fry.death",   soundEntrySimple("subtitles.entity.fry.death",
                "mob/tadpole/death1","mob/tadpole/death2"));
        root.add("entity.fry.flop",    soundEntry(false,"subtitles.entity.fry.flop",
                soundEvent("entity.tropical_fish.flop")));
        root.add("entity.fry.hurt",    soundEntrySimple("subtitles.entity.fry.hurt",
                "mob/tadpole/hurt1","mob/tadpole/hurt2","mob/tadpole/hurt3","mob/tadpole/hurt4"));

        addFishSounds(root, "sunfish");
        addFishSounds(root, "catfish");
        addFishSounds(root, "seahorse");
        addFishSounds(root, "anomalocaris");
        addFishSounds(root, "anglerfish");
        addFishSounds(root, "mahi_mahi");

        root.add("entity.pelican.hurt",   soundEntrySimple("subtitles.entity.pelican.hurt",
                "waterlogged:entity/pelican/hurt1","waterlogged:entity/pelican/hurt2",
                "waterlogged:entity/pelican/hurt3","waterlogged:entity/pelican/hurt4"));
        root.add("entity.pelican.death",  soundEntrySimple("subtitles.entity.pelican.death",
                "waterlogged:entity/pelican/hurt1","waterlogged:entity/pelican/hurt2",
                "waterlogged:entity/pelican/hurt3","waterlogged:entity/pelican/hurt4"));
        root.add("entity.pelican.ambient",soundEntrySimple("subtitles.entity.pelican.ambient",
                "waterlogged:entity/pelican/ambient1","waterlogged:entity/pelican/ambient2",
                "waterlogged:entity/pelican/ambient3","waterlogged:entity/pelican/ambient4"));

        root.add("entity.sea_slug.death", soundEntrySimple("subtitles.entity.sea_slug.death",
                "mob/tadpole/death1","mob/tadpole/death2"));
        root.add("entity.sea_slug.hurt",  soundEntrySimple("subtitles.entity.sea_slug.hurt",
                "mob/tadpole/hurt1","mob/tadpole/hurt2","mob/tadpole/hurt3","mob/tadpole/hurt4"));

        root.add("entity.crab.hurt",  soundEntrySimple("subtitles.entity.crab.hurt",
                "waterlogged:block/shell/step1","waterlogged:block/shell/step2",
                "waterlogged:block/shell/step3","waterlogged:block/shell/step4"));
        root.add("entity.crab.death", soundEntrySimple("subtitles.entity.crab.death",
                "waterlogged:block/shell/step1","waterlogged:block/shell/step2",
                "waterlogged:block/shell/step3","waterlogged:block/shell/step4"));

        root.add("entity.dongfish.death", soundEntrySimple("subtitles.entity.dongfish.death",
                "mob/tadpole/death1","mob/tadpole/death2"));
        root.add("entity.dongfish.flop",  soundEntry(false,"subtitles.entity.dongfish.flop",
                soundEvent("entity.tropical_fish.flop")));
        root.add("entity.dongfish.hurt",  soundEntrySimple("subtitles.entity.dongfish.hurt",
                "mob/tadpole/hurt1","mob/tadpole/hurt2","mob/tadpole/hurt3","mob/tadpole/hurt4"));
        root.add("entity.dongfish.shear", soundEntry(false,"subtitles.entity.dongfish.shear",
                soundPitch("mob/sheep/shear", 2)));

        root.add("entity.bubble_eye.death", soundEntrySimple("subtitles.entity.bubble_eye.death",
                "mob/tadpole/death1","mob/tadpole/death2"));
        root.add("entity.bubble_eye.flop",  soundEntry(false,"subtitles.entity.bubble_eye.flop",
                soundEvent("entity.tropical_fish.flop")));
        root.add("entity.bubble_eye.hurt",  soundEntrySimple("subtitles.entity.bubble_eye.hurt",
                "mob/tadpole/hurt1","mob/tadpole/hurt2","mob/tadpole/hurt3","mob/tadpole/hurt4"));

        root.add("entity.right_whale.ambient", soundEntry(false, "subtitles.entity.right_whale.ambient",
                soundPitchVol("waterlogged:entity/right_whale/whale_call",  0.7, 1.2),
                soundPitchVol("waterlogged:entity/right_whale/whale_groan", 0.7, 1.0)));
        root.add("entity.right_whale.hurt",  soundEntry(false, "subtitles.entity.right_whale.hurt",
                soundPitchVol("waterlogged:entity/right_whale/whale_hurt",  0.7, 4.0)));
        root.add("entity.right_whale.death", soundEntry(false, "subtitles.entity.right_whale.death",
                soundPitchVol("waterlogged:entity/right_whale/whale_groan", 0.6, 4.0)));

        return root;
    }

    /** Add death/flop/hurt for standard fish (pitch 0.8, flop volume 0.3). */
    private void addFishSounds(JsonObject root, String entity) {
        root.add("entity." + entity + ".death", soundEntry(false, "subtitles.entity." + entity + ".death",
                soundPitch("entity/fish/hurt1",0.8), soundPitch("entity/fish/hurt2",0.8),
                soundPitch("entity/fish/hurt3",0.8), soundPitch("entity/fish/hurt4",0.8)));
        root.add("entity." + entity + ".flop", soundEntry(false, "subtitles.entity." + entity + ".flop",
                soundPitchVol("entity/fish/flop1",0.8,0.3), soundPitchVol("entity/fish/flop2",0.8,0.3),
                soundPitchVol("entity/fish/flop3",0.8,0.3), soundPitchVol("entity/fish/flop4",0.8,0.3)));
        root.add("entity." + entity + ".hurt", soundEntry(false, "subtitles.entity." + entity + ".hurt",
                soundPitch("entity/fish/hurt1",0.8), soundPitch("entity/fish/hurt2",0.8),
                soundPitch("entity/fish/hurt3",0.8), soundPitch("entity/fish/hurt4",0.8)));
    }

    // Sound entry helpers
    private JsonObject soundEntry(boolean replace, String subtitle, JsonObject... sounds) {
        JsonObject obj = new JsonObject();
        if (replace) obj.addProperty("replace", true);
        JsonArray arr = new JsonArray();
        for (JsonObject s : sounds) arr.add(s);
        obj.add("sounds", arr);
        if (subtitle != null) obj.addProperty("subtitle", subtitle);
        return obj;
    }
    private JsonObject soundEntrySimple(String subtitle, String... names) {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        for (String name : names) arr.add(name);
        obj.add("sounds", arr);
        if (subtitle != null) obj.addProperty("subtitle", subtitle);
        return obj;
    }
    private JsonObject soundObj(String name) {
        JsonObject o = new JsonObject(); o.addProperty("name", name); return o;
    }
    private JsonObject soundVol(String name, double volume) {
        JsonObject o = new JsonObject(); o.addProperty("name", name); o.addProperty("volume", volume); return o;
    }
    private JsonObject soundPitch(String name, double pitch) {
        JsonObject o = new JsonObject(); o.addProperty("name", name); o.addProperty("pitch", pitch); return o;
    }
    private JsonObject soundPitchVol(String name, double pitch, double volume) {
        JsonObject o = new JsonObject(); o.addProperty("name", name); o.addProperty("pitch", pitch); o.addProperty("volume", volume); return o;
    }
    private JsonObject soundEvent(String event) {
        JsonObject o = new JsonObject(); o.addProperty("name", event); o.addProperty("type", "event"); return o;
    }

    // ── Atlases ───────────────────────────────────────────────────────────────

    private JsonObject atlasBlocks() {
        JsonObject root = new JsonObject();
        JsonArray sources = new JsonArray();
        sources.add(atlasSource("waterlogged:entity/starfish/starfish"));
        sources.add(atlasSource("waterlogged:entity/starfish/dead_starfish"));
        sources.add(atlasSource("waterlogged:entity/urchin/urchin"));
        sources.add(atlasSource("waterlogged:item/starfish"));
        sources.add(atlasSource("waterlogged:item/dead_starfish"));
        root.add("sources", sources);
        return root;
    }

    private JsonObject atlasItems() {
        JsonObject root = new JsonObject();
        JsonArray sources = new JsonArray();
        sources.add(atlasSource("waterlogged:entity/anemone/anemone"));
        root.add("sources", sources);
        return root;
    }

    private JsonObject atlasBlockOrItem() {
        JsonObject root = new JsonObject();
        JsonArray sources = new JsonArray();
        sources.add(atlasSource("waterlogged:entity/anemone/anemone"));
        sources.add(atlasSource("waterlogged:entity/starfish/starfish"));
        sources.add(atlasSource("waterlogged:entity/starfish/dead_starfish"));
        sources.add(atlasSource("waterlogged:entity/urchin/urchin"));
        root.add("sources", sources);
        return root;
    }

    private JsonObject atlasSource(String resource) {
        JsonObject o = new JsonObject();
        o.addProperty("type", "minecraft:single");
        o.addProperty("resource", resource);
        return o;
    }

    // ── Particles ─────────────────────────────────────────────────────────────

    private JsonObject particleAlgae() {
        JsonObject root = new JsonObject();
        JsonArray tex = new JsonArray();
        tex.add("minecraft:generic_0");
        root.add("textures", tex);
        return root;
    }

    private JsonObject particleWorm() {
        JsonObject root = new JsonObject();
        JsonArray tex = new JsonArray();
        // worm_0..9 repeated 10 times = 100 entries (matches original)
        for (int repeat = 0; repeat < 10; repeat++) {
            for (int i = 0; i <= 9; i++) {
                tex.add("waterlogged:worm_" + i);
            }
        }
        root.add("textures", tex);
        return root;
    }

    // ── Block models ──────────────────────────────────────────────────────────

    private JsonObject blockAlgae() {
        JsonObject root = new JsonObject();
        root.addProperty("ambientocclusion", false);
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", "waterlogged:block/algae");
        textures.addProperty("0", "waterlogged:block/algae");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();
        JsonObject el = new JsonObject();
        el.add("from", arr(0, 0, 0.1));
        el.add("to",   arr(16, 16, 0.1));
        JsonObject faces = new JsonObject();
        faces.add("north", face(new int[]{16,0,0,16}, "#0"));
        faces.add("south", face(new int[]{0,0,16,16}, "#0"));
        el.add("faces", faces);
        elements.add(el);
        root.add("elements", elements);
        return root;
    }

    private JsonObject blockClam() {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        textures.addProperty("0", "waterlogged:block/clam");
        textures.addProperty("particle", "waterlogged:block/clam");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();

        // Upper shell: rotated 22.5° on X axis
        JsonObject el1 = new JsonObject();
        el1.add("from", arr(4, 0.1, 5.5));
        el1.add("to",   arr(12, 0.1, 10.5));
        el1.addProperty("shade", false);
        el1.add("rotation", rotation(22.5, "x", new double[]{8, 0.1, 10.5}));
        JsonObject f1 = new JsonObject();
        f1.add("up",   face(new int[]{0,0,8,5}, "#0"));
        f1.add("down", face(new int[]{8,5,16,0}, "#0"));
        el1.add("faces", f1);
        elements.add(el1);

        // Lower shell: no rotation
        JsonObject el2 = new JsonObject();
        el2.add("from", arr(4, 0.1, 5.5));
        el2.add("to",   arr(12, 0.1, 10.5));
        el2.addProperty("shade", false);
        el2.add("rotation", rotation(0, "y", new double[]{8, 0.1, 10.5}));
        JsonObject f2 = new JsonObject();
        f2.add("up",   face(new int[]{8,0,16,5}, "#0"));
        f2.add("down", face(new int[]{0,5,8,0}, "#0"));
        el2.add("faces", f2);
        elements.add(el2);

        root.add("elements", elements);
        return root;
    }

    private JsonObject blockDuckweed() {
        JsonObject root = new JsonObject();
        root.addProperty("ambientocclusion", false);
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", "waterlogged:block/duckweed");
        textures.addProperty("texture", "waterlogged:block/duckweed");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();
        JsonObject el = new JsonObject();
        el.add("from", arr(0, 0.25, 0));
        el.add("to",   arr(16, 0.25, 16));
        JsonObject faces = new JsonObject();
        faces.add("down", face(new int[]{0,16,16,0}, "#texture"));
        faces.add("up",   face(new int[]{0,0,16,16}, "#texture"));
        el.add("faces", faces);
        elements.add(el);
        root.add("elements", elements);
        return root;
    }

    private JsonObject blockOysters() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        JsonObject textures = new JsonObject();
        textures.addProperty("0", "waterlogged:block/oysters");
        textures.addProperty("particle", "waterlogged:block/oysters");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();

        // 4 vertical X-planes
        addOystersXPlane(elements, 4,  true,  22.5);
        addOystersXPlane(elements, 4,  false, -22.5);
        addOystersXPlane(elements, 12, true,  22.5);
        addOystersXPlane(elements, 12, false, -22.5);
        // 4 vertical Z-planes
        addOystersZPlane(elements, 12, false, -22.5);
        addOystersZPlane(elements, 12, true,   22.5);
        addOystersZPlane(elements, 4,  false, -22.5);
        addOystersZPlane(elements, 4,  true,   22.5);

        root.add("elements", elements);
        return root;
    }

    private void addOystersXPlane(JsonArray elements, int x, boolean upperUV, double angle) {
        JsonObject el = new JsonObject();
        el.add("from", arr(x, 0, 0));
        el.add("to",   arr(x, 8, 16));
        el.addProperty("shade", false);
        el.add("rotation", rotation(angle, "z", new double[]{x, 0, 8}));
        JsonObject faces = new JsonObject();
        faces.add("east", face(upperUV ? new int[]{16,0,0,8} : new int[]{16,8,0,16}, "#0"));
        faces.add("west", face(upperUV ? new int[]{0,0,16,8} : new int[]{0,8,16,16}, "#0"));
        el.add("faces", faces);
        elements.add(el);
    }

    private void addOystersZPlane(JsonArray elements, int z, boolean upperUV, double angle) {
        JsonObject el = new JsonObject();
        el.add("from", arr(0, 0, z));
        el.add("to",   arr(16, 8, z));
        el.addProperty("shade", false);
        el.add("rotation", rotation(angle, "x", new double[]{8, 0, z}));
        JsonObject faces = new JsonObject();
        faces.add("north", face(upperUV ? new int[]{0,0,16,8} : new int[]{0,8,16,16}, "#0"));
        faces.add("south", face(upperUV ? new int[]{16,0,0,8} : new int[]{16,8,0,16}, "#0"));
        el.add("faces", faces);
        elements.add(el);
    }

    private JsonObject blockPapyrusAge0() {
        // Sprout: 4 thin stems + small bud elements at y~8
        JsonObject root = blockBase(false);
        JsonObject tex = new JsonObject();
        tex.addProperty("0", "waterlogged:block/papyrus");
        tex.addProperty("particle", "waterlogged:block/papyrus");
        root.add("textures", tex);

        JsonArray el = new JsonArray();
        // 4 thin stems
        addStem(el, new double[]{7,0,7}, new double[]{7,16,8}, 22.5, "z", new double[]{7,0,7.5}, "east", "west", new int[]{0,0,1,16});
        addStem(el, new double[]{9,0,8}, new double[]{9,16,9},-22.5,"z", new double[]{9,0,8.5}, "east","west",  new int[]{0,0,1,16});
        addStem(el, new double[]{8,0,7}, new double[]{9,16,7},-22.5,"x", new double[]{8.5,0,7}, "north","south", new int[]{1,0,2,16});
        addStem(el, new double[]{7,0,9}, new double[]{8,16,9}, 22.5, "x", new double[]{7.5,0,9}, "north","south", new int[]{2,0,3,16});
        // bud elements
        addBudPlane(el, new double[]{5.5,8,7}, new double[]{9.5,8,11}, 22.5,"x",new double[]{7.5,0,9}, new int[]{9,11,13,15},180);
        addBudPlane(el, new double[]{6.5,7,5}, new double[]{10.5,7,9},-22.5,"x",new double[]{8.5,0,7}, new int[]{9,11,13,15},180);
        addBudPlane_z(el, new double[]{5.5,6,6},new double[]{8.5,6,9},  22.5,"z",new double[]{7,0,7.5}, new int[]{13,11,16,14},0);
        addBudPlane_z(el, new double[]{7.5,6,7},new double[]{10.5,6,10},-22.5,"z",new double[]{9,0,8.5}, new int[]{13,11,16,14},0);
        // seed
        addSeed(el);
        root.add("elements", el);
        return root;
    }

    private JsonObject blockPapyrusAge1() {
        // Mature stem: slightly different UV offsets for stem + upper bud cluster
        JsonObject root = blockBase(false);
        JsonObject tex = new JsonObject();
        tex.addProperty("0", "waterlogged:block/papyrus");
        tex.addProperty("particle", "waterlogged:block/papyrus");
        root.add("textures", tex);

        JsonArray el = new JsonArray();
        // 4 thin stems (UV 3-4, 4-5, 5-6)
        addStem(el, new double[]{7,0,7}, new double[]{7,16,8}, 22.5,"z", new double[]{7,0,7.5}, "east","west", new int[]{3,0,4,16});
        addStem(el, new double[]{9,0,8}, new double[]{9,16,9},-22.5,"z", new double[]{9,0,8.5}, "east","west", new int[]{3,0,4,16});
        addStem(el, new double[]{8,0,7}, new double[]{9,16,7},-22.5,"x", new double[]{8.5,0,7},"north","south",new int[]{4,0,5,16});
        addStem(el, new double[]{7,0,9}, new double[]{8,16,9}, 22.5,"x", new double[]{7.5,0,9},"north","south",new int[]{5,0,6,16});
        // leaf planes at top
        addBudTopPlane(el, new double[]{5.5,16,7}, new double[]{9.5,16,11}, 22.5,"x",new double[]{7.5,0,9},  new int[]{9,11,13,15},180);
        addBudTopPlane90(el,new double[]{7,13,6.5},new double[]{11,13,10.5},-22.5,"z",new double[]{9,0,8.5}, new int[]{9,11,13,15},90);
        addBudTopPlane90(el,new double[]{5,13,5.5},new double[]{9,13,9.5},  22.5,"z",new double[]{7,0,7.5},  new int[]{9,11,13,15},90);
        addBudTopPlane(el, new double[]{6.5,15,5}, new double[]{10.5,15,9},-22.5,"x",new double[]{8.5,0,7},  new int[]{9,11,13,15},180);
        // seed
        addSeed(el);
        root.add("elements", el);
        return root;
    }

    private JsonObject blockPapyrusAge2() {
        // Full papyrus with flower head - complex with stem extensions above y=16
        JsonObject root = blockBase(false);
        JsonObject tex = new JsonObject();
        tex.addProperty("0", "waterlogged:block/papyrus");
        tex.addProperty("particle", "waterlogged:block/papyrus");
        root.add("textures", tex);

        // Build directly from the JSON definition
        JsonArray el = new JsonArray();
        // Stem 1 (z-axis 22.5)
        addStemExt(el, new double[]{7,0,9},new double[]{8,16,9}, 22.5,"x",new double[]{7.5,0,9},"north","south",new int[]{6,0,7,16});
        addStemExt(el, new double[]{7.5,25,5.5},new double[]{7.5,29,12.5},22.5,"x",new double[]{7.5,0,9},"east","west",new int[]{9,0,16,4});
        addStemExt(el, new double[]{4,25,9},new double[]{11,29,9},         22.5,"x",new double[]{7.5,0,9},"north","south",new int[]{9,0,16,4});
        addBudExtPlane(el, new double[]{4,27,5.5},new double[]{11,27,12.5},22.5,"x",new double[]{7.5,0,9}, new int[]{9,4,16,11},0);
        addStemExt(el, new double[]{7,16,9},new double[]{8,25,9},          22.5,"x",new double[]{7.5,0,9},"north","south",new int[]{7,7,8,16});
        // Stem 2 (x-axis -22.5)
        addStemExt(el, new double[]{8,0,7},new double[]{9,16,7},-22.5,"x",new double[]{8.5,0,7},"north","south",new int[]{6,0,7,16});
        addStemExt(el, new double[]{8.5,25,3.5},new double[]{8.5,29,10.5},-22.5,"x",new double[]{8.5,0,7},"east","west",new int[]{9,0,16,4});
        addStemExt(el, new double[]{5,25,7},new double[]{12,29,7},         -22.5,"x",new double[]{8.5,0,7},"north","south",new int[]{9,0,16,4});
        addBudExtPlane(el, new double[]{5,27,3.5},new double[]{12,27,10.5},-22.5,"x",new double[]{8.5,0,7}, new int[]{9,4,16,11},180);
        addStemExt(el, new double[]{8,16,7},new double[]{9,25,7},          -22.5,"x",new double[]{8.5,0,7},"north","south",new int[]{7,7,8,16});
        // Stem 3 (z-axis 22.5 x-component)
        addStemExt(el, new double[]{7,0,7},new double[]{7,16,8},  22.5,"z",new double[]{7,0,7.5},"east","west",new int[]{6,0,7,16});
        addStemExt(el, new double[]{3.5,23,7.5},new double[]{10.5,27,7.5},22.5,"z",new double[]{7,0,7.5},"north","south",new int[]{9,0,16,4});
        addStemExt(el, new double[]{7,23,4},new double[]{7,27,11},         22.5,"z",new double[]{7,0,7.5},"east","west",new int[]{9,0,16,4});
        addBudExtPlaneZ(el, new double[]{3.5,25,4},new double[]{10.5,25,11},22.5,"z",new double[]{7,0,7.5}, new int[]{9,4,16,11},90);
        addStemExt(el, new double[]{7,16,7},new double[]{7,25,8},          22.5,"z",new double[]{7,0,7.5},"east","west",new int[]{8,7,9,16});
        // Stem 4 (z-axis -22.5)
        addStemExt(el, new double[]{9,0,8},new double[]{9,16,9},-22.5,"z",new double[]{9,0,8.5},"east","west",new int[]{6,0,7,16});
        addStemExt(el, new double[]{5.5,23,8.5},new double[]{12.5,27,8.5},-22.5,"z",new double[]{9,0,8.5},"north","south",new int[]{9,0,16,4});
        addStemExt(el, new double[]{9,23,5},new double[]{9,27,12},         -22.5,"z",new double[]{9,0,8.5},"east","west",new int[]{9,0,16,4});
        addBudExtPlaneZ(el, new double[]{5.5,25,5},new double[]{12.5,25,12},-22.5,"z",new double[]{9,0,8.5}, new int[]{9,4,16,11},270);
        addStemExt(el, new double[]{9,16,8},new double[]{9,25,9},          -22.5,"z",new double[]{9,0,8.5},"east","west",new int[]{8,7,9,16});
        // seed
        addSeed(el);
        root.add("elements", el);
        return root;
    }

    private JsonObject blockRoe() {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        textures.addProperty("0", "waterlogged:block/roe_bottom");
        textures.addProperty("1", "waterlogged:block/roe_top");
        textures.addProperty("particle", "waterlogged:block/roe_bottom");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();

        // Bottom layer with tint 0
        JsonObject el1 = new JsonObject();
        el1.add("from", arr(0, 0.25, 0));
        el1.add("to",   arr(16, 0.25, 16));
        JsonObject f1 = new JsonObject();
        f1.add("up",   faceTinted(new int[]{0,0,16,16}, "#0", 0));
        f1.add("down", faceTinted(new int[]{0,0,16,16}, "#0", 0));
        el1.add("faces", f1);
        elements.add(el1);

        // Top layer with tint 1
        JsonObject el2 = new JsonObject();
        el2.add("from", arr(0, 0.75, 0));
        el2.add("to",   arr(16, 0.75, 16));
        JsonObject f2 = new JsonObject();
        f2.add("up",   faceTinted(new int[]{0,0,16,16}, "#1", 1));
        f2.add("down", faceTinted(new int[]{0,0,16,16}, "#1", 1));
        el2.add("faces", f2);
        elements.add(el2);

        root.add("elements", elements);
        return root;
    }

    private JsonObject blockSargassum() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        root.addProperty("ambientocclusion", false);
        JsonObject textures = new JsonObject();
        textures.addProperty("0", "waterlogged:block/sargassum");
        textures.addProperty("1", "waterlogged:block/sargassum_side");
        textures.addProperty("particle", "waterlogged:block/sargassum");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();

        // Flat top
        JsonObject top = new JsonObject();
        top.add("from", arr(0, 0.5, 0));
        top.add("to",   arr(16, 0.5, 16));
        top.addProperty("shade", false);
        JsonObject tf = new JsonObject();
        tf.add("up",   face(new int[]{0,0,16,16}, "#0"));
        tf.add("down", face(new int[]{0,16,16,0}, "#0"));
        top.add("faces", tf);
        elements.add(top);

        // +45° diagonal fin
        addSargassumFin(elements, 45);
        // -45° diagonal fin
        addSargassumFin(elements, -45);

        root.add("elements", elements);
        return root;
    }

    private void addSargassumFin(JsonArray elements, double angle) {
        JsonObject el = new JsonObject();
        el.add("from", arr(-3.31371, -3.5, 8));
        el.add("to",   arr(19.31371, 4.5, 8));
        el.addProperty("shade", false);
        el.add("rotation", rotation(angle, "y", new double[]{8,0,8}));
        JsonObject f = new JsonObject();
        f.add("north", face(new int[]{0,4,16,12}, "#1"));
        f.add("south", face(new int[]{0,4,16,12}, "#1"));
        el.add("faces", f);
        elements.add(el);
    }

    private JsonObject blockSeaSlugEggs() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        JsonObject textures = new JsonObject();
        textures.addProperty("0", "waterlogged:block/sea_slug_eggs");
        textures.addProperty("particle", "waterlogged:block/sea_slug_eggs");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();

        // Top disk
        JsonObject disk1 = new JsonObject();
        disk1.add("from", arr(4, 4.5, 4));
        disk1.add("to",   arr(12, 4.5, 12));
        disk1.add("rotation", rotation(0, "y", new double[]{0,0.5,0}));
        JsonObject df1 = new JsonObject();
        df1.add("up",   faceTinted(new int[]{0,0,8,8}, "#0", 0));
        df1.add("down", faceTinted(new int[]{0,8,8,0}, "#0", 0));
        disk1.add("faces", df1);
        elements.add(disk1);

        // Bottom disk
        JsonObject disk2 = new JsonObject();
        disk2.add("from", arr(5, 2.25, 5));
        disk2.add("to",   arr(11, 2.25, 11));
        disk2.add("rotation", rotation(0, "y", new double[]{0,0.5,0}));
        JsonObject df2 = new JsonObject();
        df2.add("up",   faceTinted(new int[]{8,0,14,6}, "#0", 0));
        df2.add("down", faceTinted(new int[]{8,6,14,0}, "#0", 0));
        disk2.add("faces", df2);
        elements.add(disk2);

        // 4 side fins
        addEggFin(elements, new double[]{4,-0.5,4}, new double[]{12,4.5,4}, -22.5,"x",new double[]{8,4.5,4}, "north","south");
        addEggFin(elements, new double[]{4,-0.5,12},new double[]{12,4.5,12},  22.5,"x",new double[]{8,4.5,12},"north","south");
        addEggFin(elements, new double[]{4,-0.5,4}, new double[]{4,4.5,12},   22.5,"z",new double[]{4,4.5,8},  "east","west");
        addEggFin(elements, new double[]{12,-0.5,4},new double[]{12,4.5,12}, -22.5,"z",new double[]{12,4.5,8},"east","west");

        root.add("elements", elements);
        // display transforms
        root.add("display", eggDisplay());
        return root;
    }

    private void addEggFin(JsonArray elements, double[] from, double[] to,
                           double angle, String axis, double[] origin, String face1, String face2) {
        JsonObject el = new JsonObject();
        el.add("from", arr(from));
        el.add("to",   arr(to));
        el.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add(face1, faceTinted(new int[]{0,8,8,13}, "#0", 0));
        faces.add(face2, faceTinted(new int[]{8,8,0,13}, "#0", 0));
        el.add("faces", faces);
        elements.add(el);
    }

    private JsonObject eggDisplay() {
        JsonObject display = new JsonObject();
        JsonObject tpr = new JsonObject(); tpr.add("rotation", arr(75,45,0)); tpr.add("translation", arr(0,2,2)); tpr.add("scale", arr(0.375,0.375,0.375)); display.add("thirdperson_righthand", tpr);
        JsonObject fpr = new JsonObject(); fpr.add("rotation", arr(0,45,0)); fpr.add("translation", arr(0,4.5,0)); fpr.add("scale", arr(0.6,0.6,0.6)); display.add("firstperson_righthand", fpr);
        JsonObject gnd = new JsonObject(); gnd.add("translation", arr(0,3,0)); gnd.add("scale", arr(0.4,0.4,0.4)); display.add("ground", gnd);
        JsonObject gui = new JsonObject(); gui.add("rotation", arr(30,225,0)); gui.add("translation", arr(0,4,0)); display.add("gui", gui);
        JsonObject head = new JsonObject(); head.add("translation", arr(0,15.25,0)); display.add("head", head);
        JsonObject fixed = new JsonObject(); fixed.add("rotation", arr(-90,0,0)); fixed.add("translation", arr(0,0,-5.5)); fixed.add("scale", arr(0.75,0.75,0.75)); display.add("fixed", fixed);
        return display;
    }

    // ── Item models ───────────────────────────────────────────────────────────

    private JsonObject itemsDongfishBucket() {
        JsonObject onTrue = new JsonObject();
        onTrue.addProperty("type", "minecraft:model");
        onTrue.addProperty("model", "waterlogged:item/dongfish_bucket_horngus");
        JsonObject onFalse = new JsonObject();
        onFalse.addProperty("type", "minecraft:model");
        onFalse.addProperty("model", "waterlogged:item/dongfish_bucket_no_horngus");
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "minecraft:condition");
        condition.addProperty("property", "minecraft:custom_model_data");
        condition.addProperty("index", 0);
        condition.add("on_true", onTrue);
        condition.add("on_false", onFalse);
        JsonObject root = new JsonObject();
        root.add("model", condition);
        return root;
    }

    private JsonObject itemOysters() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "waterlogged:item/oysters");
        root.add("textures", textures);
        JsonObject display = new JsonObject();
        display.add("fixed",               displayTransform(null, new double[]{0,6,0}, null));
        display.add("firstperson_righthand", displayTransform(new double[]{0,-90,25}, new double[]{0,5,0}, new double[]{0.68,0.68,0.68}));
        display.add("thirdperson_righthand", displayTransform(null, new double[]{0,4,1}, new double[]{0.55,0.55,0.55}));
        display.add("head",                displayTransform(null, new double[]{0,14,-5}, null));
        display.add("gui",                 displayTransform(null, new double[]{0,2,0}, null));
        root.add("display", display);
        return root;
    }

    private JsonObject displayTransform(double[] rotation, double[] translation, double[] scale) {
        JsonObject o = new JsonObject();
        if (rotation    != null) o.add("rotation",    arr(rotation));
        if (translation != null) o.add("translation", arr(translation));
        if (scale       != null) o.add("scale",       arr(scale));
        return o;
    }

    // ── Minecraft namespace overrides ─────────────────────────────────────────

    private JsonObject mcTropicalFishBucketModel() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "minecraft:item/tropical_fish_bucket");
        textures.addProperty("layer1", "minecraft:item/tropical_fish_bucket_overlay_0");
        textures.addProperty("layer2", "minecraft:item/tropical_fish_bucket_overlay_1");
        root.add("textures", textures);
        return root;
    }

    private JsonObject mcTropicalFishBucketItem() {
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", "waterlogged:item/tropical_fish_bucket");
        JsonArray tints = new JsonArray();
        JsonObject t0 = new JsonObject(); t0.addProperty("type", "minecraft:constant"); t0.addProperty("value", -1); tints.add(t0);
        JsonObject t1 = new JsonObject(); t1.addProperty("type", "minecraft:custom_model_data"); t1.addProperty("default", 16777215); t1.addProperty("index", 1); tints.add(t1);
        JsonObject t2 = new JsonObject(); t2.addProperty("type", "minecraft:custom_model_data"); t2.addProperty("default", 16777215); t2.addProperty("index", 0); tints.add(t2);
        model.add("tints", tints);
        root.add("model", model);
        return root;
    }

    // ── Custom variant tags ────────────────────────────────────────────────────

    private JsonObject crabNaturalVariants() {
        return tagValues("waterlogged:dungeness", "waterlogged:ghost", "waterlogged:blue_claw");
    }

    private JsonObject seaSlugBaseColors() {
        return tagValues("waterlogged:ivory","waterlogged:onyx","waterlogged:periwinkle",
                "waterlogged:jade","waterlogged:midnight","waterlogged:ultramarine",
                "waterlogged:coffee","waterlogged:eggplant","waterlogged:cyclamen",
                "waterlogged:amber","waterlogged:iris");
    }

    private JsonObject seaSlugPatternColors() {
        return tagValues("waterlogged:ivory","waterlogged:burgundy","waterlogged:coquelicot",
                "waterlogged:gamboge","waterlogged:celeste","waterlogged:olivine",
                "waterlogged:pear","waterlogged:amber","waterlogged:orchid","waterlogged:folly");
    }

    private JsonObject seaSlugNaturalPatterns() {
        return tagValues("waterlogged:none","waterlogged:stripes","waterlogged:squiggles",
                "waterlogged:spots","waterlogged:rings");
    }

    private JsonObject sunfishNatural() {
        return tagValues("waterlogged:pumpkinseed","waterlogged:longear","waterlogged:bluegill",
                "waterlogged:redbreast","waterlogged:green","waterlogged:warmouth");
    }

    private JsonObject sunfishPelicanBeak() {
        return tagValues("#waterlogged:natural_sunfish",
                "waterlogged:bluegill_and_redbreast_hybrid",
                "waterlogged:bluegill_and_pumpkinseed_hybrid");
    }

    private JsonObject tagValues(String... values) {
        JsonObject root = new JsonObject();
        JsonArray arr = new JsonArray();
        for (String v : values) arr.add(v);
        root.add("values", arr);
        return root;
    }

    // ── Worldgen: configured features ─────────────────────────────────────────

    private JsonObject cfAlgae() {
        return randomPatch(15, 3, 0,
                simpleBlock(wlState("waterlogged:algae",
                        prop("down","true"), prop("east","false"), prop("north","false"),
                        prop("south","false"), prop("up","false"), prop("waterlogged","true"), prop("west","false"))),
                waterPredicate());
    }

    private JsonObject cfAnemone() {
        return simpleBlock(wlState("waterlogged:anemone", prop("waterlogged","true")));
    }

    private JsonObject cfClam() {
        return randomPatch(8, 2, 0,
                weightedSimpleBlock(
                        wlState("waterlogged:clam", prop("facing","north"), prop("waterlogged","true")), 1,
                        wlState("waterlogged:clam", prop("facing","south"), prop("waterlogged","true")), 1,
                        wlState("waterlogged:clam", prop("facing","east"),  prop("waterlogged","true")), 1,
                        wlState("waterlogged:clam", prop("facing","west"),  prop("waterlogged","true")), 1),
                waterPredicate());
    }

    private JsonObject cfDuckweed() {
        return simpleBlock(wlState("waterlogged:duckweed"));
    }

    private JsonObject cfHydrothermalVent() {
        JsonObject root = new JsonObject();
        root.addProperty("type", "waterlogged:hydrothermal_vent");
        root.add("config", new JsonObject());
        return root;
    }

    private JsonObject cfOysters() {
        return randomPatch(8, 2, 0,
                simpleBlock(wlState("waterlogged:oysters", prop("waterlogged","true"))),
                waterPredicate());
    }

    private JsonObject cfPapyrus() {
        JsonObject root = new JsonObject();
        root.addProperty("type", "waterlogged:papyrus");
        root.add("config", new JsonObject());
        return root;
    }

    private JsonObject cfSargassum() {
        return randomPatch(80, 10, 0,
                simpleBlock(wlState("waterlogged:sargassum")),
                null);
    }

    private JsonObject cfStarfish() {
        JsonObject root = new JsonObject();
        root.addProperty("type", "waterlogged:starfish");
        root.add("config", new JsonObject());
        return root;
    }

    private JsonObject cfUrchin() {
        return randomPatch(12, 4, 0,
                simpleBlock(wlState("waterlogged:urchin", prop("waterlogged","true"))),
                waterPredicate());
    }

    private JsonObject cfWormyDirt() {
        return oreFeature(
                target("minecraft:block_match","minecraft:grass_block","waterlogged:wormy_dirt"),
                target("minecraft:block_match","minecraft:dirt",       "waterlogged:wormy_dirt"));
    }

    private JsonObject cfWormyMud() {
        return oreFeature(
                target("minecraft:block_match","minecraft:mud",                "waterlogged:wormy_mud"),
                target("minecraft:block_match","minecraft:muddy_mangrove_roots","waterlogged:wormy_mud"));
    }

    // ── Worldgen: placed features ──────────────────────────────────────────────

    private JsonObject pfAlgae() {
        return placedFeature("waterlogged:algae",
                rarityFilter(3), count(1), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfAlgaeSwamp() {
        return placedFeature("waterlogged:algae",
                count(6), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfAnemone() {
        return placedFeature("waterlogged:anemone",
                count(2), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfClam() {
        return placedFeature("waterlogged:clam",
                rarityFilter(2), count(1), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfDuckweed() {
        return placedFeature("waterlogged:duckweed",
                count(4), inSquare(), heightmap("MOTION_BLOCKING"), biome());
    }
    private JsonObject pfHydrothermalVent() {
        return placedFeature("waterlogged:hydrothermal_vent",
                rarityFilter(64), inSquare(), heightmap("OCEAN_FLOOR_WG"), biome());
    }
    private JsonObject pfOysters() {
        return placedFeature("waterlogged:oysters",
                rarityFilter(2), count(1), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfPapyrus() {
        return placedFeature("waterlogged:papyrus",
                count(3), inSquare(), heightmap("MOTION_BLOCKING"), biome());
    }
    private JsonObject pfSargassum() {
        return placedFeature("waterlogged:sargassum",
                rarityFilter(20), inSquare(), heightmap("MOTION_BLOCKING"), biome());
    }
    private JsonObject pfStarfish() {
        return placedFeature("waterlogged:starfish",
                rarityFilter(2), count(1), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfUrchin() {
        return placedFeature("waterlogged:urchin",
                rarityFilter(8), count(1), inSquare(), heightmap("OCEAN_FLOOR"), waterFilter(), biome());
    }
    private JsonObject pfWormyDirt() {
        return placedFeature("waterlogged:wormy_dirt",
                rarityFilter(6), inSquare(), heightmap("MOTION_BLOCKING_NO_LEAVES"), biome());
    }
    private JsonObject pfWormyMud() {
        return placedFeature("waterlogged:wormy_mud",
                rarityFilter(8), inSquare(), heightmap("MOTION_BLOCKING_NO_LEAVES"), biome());
    }

    // ── Worldgen builder helpers ───────────────────────────────────────────────

    private JsonObject randomPatch(int tries, int xzSpread, int ySpread,
                                   JsonObject inlineFeature, JsonObject placementFilter) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:random_patch");
        JsonObject config = new JsonObject();
        config.addProperty("tries", tries);
        config.addProperty("xz_spread", xzSpread);
        config.addProperty("y_spread", ySpread);
        JsonObject featureHolder = new JsonObject();
        featureHolder.add("feature", inlineFeature);
        JsonArray placement = new JsonArray();
        if (placementFilter != null) placement.add(placementFilter);
        featureHolder.add("placement", placement);
        config.add("feature", featureHolder);
        root.add("config", config);
        return root;
    }

    private JsonObject simpleBlock(JsonObject blockState) {
        JsonObject provider = new JsonObject();
        provider.addProperty("type", "minecraft:simple_state_provider");
        provider.add("state", blockState);
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:simple_block");
        JsonObject config = new JsonObject();
        config.add("to_place", provider);
        root.add("config", config);
        return root;
    }

    private JsonObject weightedSimpleBlock(Object... statesAndWeights) {
        JsonObject provider = new JsonObject();
        provider.addProperty("type", "minecraft:weighted_state_provider");
        JsonArray entries = new JsonArray();
        for (int i = 0; i < statesAndWeights.length; i += 2) {
            JsonObject entry = new JsonObject();
            entry.addProperty("weight", (int) statesAndWeights[i + 1]);
            entry.add("data", (JsonObject) statesAndWeights[i]);
            entries.add(entry);
        }
        provider.add("entries", entries);

        JsonObject feature = new JsonObject();
        feature.addProperty("type", "minecraft:simple_block");
        JsonObject config = new JsonObject();
        config.add("to_place", provider);
        feature.add("config", config);
        return feature;
    }

    private JsonObject wlState(String name, String[]... props) {
        JsonObject state = new JsonObject();
        state.addProperty("Name", name);
        if (props.length > 0) {
            JsonObject properties = new JsonObject();
            for (String[] p : props) properties.addProperty(p[0], p[1]);
            state.add("Properties", properties);
        }
        return state;
    }

    private String[] prop(String key, String value) {
        return new String[]{key, value};
    }

    private JsonObject waterPredicate() {
        JsonObject pred = new JsonObject();
        pred.addProperty("type", "minecraft:block_predicate_filter");
        JsonObject predicate = new JsonObject();
        predicate.addProperty("type", "minecraft:matching_fluids");
        JsonArray fluids = new JsonArray();
        fluids.add("minecraft:water");
        predicate.add("fluids", fluids);
        pred.add("predicate", predicate);
        return pred;
    }

    private JsonObject oreFeature(JsonObject... targets) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:ore");
        JsonObject config = new JsonObject();
        config.addProperty("size", 4);
        config.addProperty("discard_chance_on_air_exposure", 0.0);
        JsonArray targetsArr = new JsonArray();
        for (JsonObject t : targets) targetsArr.add(t);
        config.add("targets", targetsArr);
        root.add("config", config);
        return root;
    }

    private JsonObject target(String predicateType, String block, String stateName) {
        JsonObject t = new JsonObject();
        JsonObject targetPred = new JsonObject();
        targetPred.addProperty("predicate_type", predicateType);
        targetPred.addProperty("block", block);
        t.add("target", targetPred);
        JsonObject state = new JsonObject();
        state.addProperty("Name", stateName);
        t.add("state", state);
        return t;
    }

    private JsonObject placedFeature(String feature, JsonObject... modifiers) {
        JsonObject root = new JsonObject();
        root.addProperty("feature", feature);
        JsonArray placement = new JsonArray();
        for (JsonObject m : modifiers) placement.add(m);
        root.add("placement", placement);
        return root;
    }

    private JsonObject rarityFilter(int chance) {
        JsonObject o = new JsonObject(); o.addProperty("type","minecraft:rarity_filter"); o.addProperty("chance",chance); return o;
    }
    private JsonObject count(int count) {
        JsonObject o = new JsonObject(); o.addProperty("type","minecraft:count"); o.addProperty("count",count); return o;
    }
    private JsonObject inSquare() {
        JsonObject o = new JsonObject(); o.addProperty("type","minecraft:in_square"); return o;
    }
    private JsonObject heightmap(String type) {
        JsonObject o = new JsonObject(); o.addProperty("type","minecraft:heightmap"); o.addProperty("heightmap",type); return o;
    }
    private JsonObject waterFilter() {
        JsonObject o = new JsonObject();
        o.addProperty("type","minecraft:block_predicate_filter");
        JsonObject p = new JsonObject(); p.addProperty("type","minecraft:matching_fluids");
        JsonArray f = new JsonArray(); f.add("minecraft:water");
        p.add("fluids",f); o.add("predicate",p);
        return o;
    }
    private JsonObject biome() {
        JsonObject o = new JsonObject(); o.addProperty("type","minecraft:biome"); return o;
    }

    // ── Papyrus element helpers ────────────────────────────────────────────────

    private void addStem(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                         String face1, String face2, int[] uv) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add(face1, face(uv, "#0"));
        faces.add(face2, face(uv, "#0"));
        e.add("faces", faces);
        el.add(e);
    }

    private void addStemExt(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                            String face1, String face2, int[] uv) {
        addStem(el, from, to, angle, axis, origin, face1, face2, uv);
    }

    private void addBudPlane(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                             int[] uv, int rotation) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add("up",   faceRot(uv, "#0", rotation));
        faces.add("down", faceRot(new int[]{uv[0],uv[3],uv[2],uv[1]}, "#0", rotation));
        e.add("faces", faces);
        el.add(e);
    }

    private void addBudTopPlane(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                                int[] uv, int rotation) {
        addBudPlane(el, from, to, angle, axis, origin, uv, rotation);
    }

    private void addBudTopPlane90(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                                  int[] uv, int rotation) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add("up",   faceRot(uv, "#0", rotation));
        faces.add("down", faceRot(new int[]{uv[0],uv[3],uv[2],uv[1]}, "#0", rotation));
        e.add("faces", faces);
        el.add(e);
    }

    private void addBudPlane_z(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                               int[] uv, int rotation) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add("up",   face(uv, "#0"));
        faces.add("down", face(uv, "#0"));
        e.add("faces", faces);
        el.add(e);
    }

    private void addBudExtPlane(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                                int[] uv, int rotation) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add("up",   faceRot(uv, "#0", rotation));
        faces.add("down", faceRot(new int[]{uv[0],uv[3],uv[2],uv[1]}, "#0", rotation));
        e.add("faces", faces);
        el.add(e);
    }

    private void addBudExtPlaneZ(JsonArray el, double[] from, double[] to, double angle, String axis, double[] origin,
                                 int[] uv, int rotation) {
        JsonObject e = new JsonObject();
        e.add("from", arr(from)); e.add("to", arr(to));
        e.addProperty("shade", false);
        e.add("rotation", rotation(angle, axis, origin));
        JsonObject faces = new JsonObject();
        faces.add("up",   faceRot(uv, "#0", rotation));
        faces.add("down", faceRot(new int[]{uv[0],uv[3],uv[2],uv[1]}, "#0", rotation));
        e.add("faces", faces);
        el.add(e);
    }

    private void addSeed(JsonArray el) {
        JsonObject e = new JsonObject();
        e.add("from", arr(7, 0.1, 7));
        e.add("to",   arr(9, 0.1, 9));
        e.addProperty("shade", false);
        JsonObject faces = new JsonObject();
        faces.add("up",   face(new int[]{7,0,9,2}, "#0"));
        faces.add("down", face(new int[]{7,0,9,2}, "#0"));
        e.add("faces", faces);
        el.add(e);
    }

    private JsonObject blockBase(boolean hasParent) {
        JsonObject root = new JsonObject();
        if (hasParent) root.addProperty("parent", "minecraft:block/block");
        return root;
    }

    // ── JSON primitive helpers ─────────────────────────────────────────────────

    private JsonArray arr(double... values) {
        JsonArray a = new JsonArray();
        for (double v : values) a.add(v);
        return a;
    }

    private JsonObject face(int[] uv, String texture) {
        JsonObject f = new JsonObject();
        JsonArray uvArr = new JsonArray();
        for (int v : uv) uvArr.add(v);
        f.add("uv", uvArr);
        f.addProperty("texture", texture);
        return f;
    }

    private JsonObject faceTinted(int[] uv, String texture, int tintindex) {
        JsonObject f = face(uv, texture);
        f.addProperty("tintindex", tintindex);
        return f;
    }

    private JsonObject faceRot(int[] uv, String texture, int rotation) {
        JsonObject f = face(uv, texture);
        if (rotation != 0) f.addProperty("rotation", rotation);
        return f;
    }

    private JsonObject rotation(double angle, String axis, double[] origin) {
        JsonObject r = new JsonObject();
        r.addProperty("angle", angle);
        r.addProperty("axis", axis);
        JsonArray o = new JsonArray();
        for (double v : origin) o.add(v);
        r.add("origin", o);
        return r;
    }
}
