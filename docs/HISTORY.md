# Waterlogged Changelog

## [HASH] — Documentation cleanup and project organization
Created docs/HISTORY.md changelog documenting all past commits, added commit checklist to CLAUDE.md to ensure history stays up-to-date, reorganized root-level documentation, and updated .gitignore.

## 7d1648c — README and GitHub configuration
Updated documentation and GitHub-specific files with build and contribution instructions.

## 588e5c6 — Stonecutter multi-loader support
Added NeoForge 21.11.x-beta support alongside Fabric using Stonecutter 0.8.3 preprocessor. Refactored all 9 registry classes to support dual-loader architecture via RegisterEvent pattern; added 17 biome modifier JSONs, entity spawn modifiers, and NeoForge-specific access transformers. Dual jar output configured.

## 1254a75 — README update
Minor documentation fix.

## 1e7043e — .gitignore update
Updated gitignore rules.

## 1cdfd53 — Cypress fixes and advancement updates
Fixed Pond Cypress tree generation and advancement triggers. Added CaughtFishWithNetCriterion for fishing net advancement, expanded PondCypressFeature with correct sapling/bone meal behavior (instant tick vs. 1-in-3 bone meal chance). Improved Pelican interaction mechanics and DongfishEntity sound synthesis.

## 2e3e1a2 — Pond Cypress trees in swamps
Added procedurally generated Pond Cypress (Taxodium distichum) trees replacing vanilla swamp oaks, with hanging cypress knees, edge leaf vines, dynamic ground decoration, and natural leaf decay mechanics.

## 6a12f64 — Additional generated features
Expanded worldgen with BeachSargassumFeature (ocean/beach sargassum wash-up), DuckweedShoreFeature (swamp/mangrove duckweed), and SwampLogFeature (fallen/floating oak logs with moss, mushrooms, and duckweed). Added LilyPadBonemealMixin for lily pad spreading.

## 6841a3a — Hydrothermal vents with ore generation
Enhanced HydrothermalVentFeature to generate raw ores (copper, iron, gold, lapis) within vent ore veins. Fixed Anglerfish jaw animation.

## ca535d0 — Datagen refactor and tropical fish updates
Consolidated all custom JSON output into WaterloggedRawProvider (1225 lines) including sounds, block/item models, atlases, particles, and worldgen features. Reorganized StarfishBlock rendering with color variants and proper block entity renderer integration.

## 4cf798f — README update
Documentation changes.

## 264f06f — Crab Rave and credits
Added crab variant artwork and contributor credits to README.

## 42afed5 — Orca: apex aquatic predator
Introduced Orca entity with detailed BlockBench model (512x512 texture), articulated jaw/dorsal fin/tail, three core animations (swim/attack/breach), jumping behavior, and predator interaction with fish schools. Orca is the mod's premier aquatic predator.

## 177f9a3 — Hydrothermal vents, whale updates, urchin changes
Added HydrothermalVentFeature generating underwater ore veins in hot spring biomes. Enhanced Right Whale animations and reworked Sea Urchin block entity rendering.

## 906794d — Rebrand: Angling 2 → Waterlogged
Renamed mod from Angling to Waterlogged: updated package (net.braeden.angling2 → net.braeden.waterlogged), mod ID (angling → waterlogged), all class names, resource namespaces, config files, and asset directories.

## 5356f46 — Major refactor: package migration
Migrated codebase from com.eightsidedsquare.angling to net.braeden.angling2, reorganizing package structure: block/entity separation, entity/client client-only organization, entity/util for variants/colors, entity/ai for goals. Cleaned up 318 files and updated build configuration.

## 8144cb0 — Pelican trading system and entity expansion
Introduced Pelican entity with flying AI, held entity system, and fish-for-item trading mechanic. Expanded food items (raw/cooked dongfish, seahorse, bubble eye, anomalocaris, anglerfish). Reworked Crab breeding to produce live offspring with variant inheritance. Enhanced entity rendering with baby scaling and color tinting. Added StarfishFeature with directional placement and color randomization.

## 044cc15 — Initial commit
Project foundation: Angling 2 mod with initial entity framework, mixins, and mod structure. Authors: EightSidedSquare, with later contributions from Braeden Silver, Diansu, and Sillvia.
