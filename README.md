![Banner](banner.png)

# Waterlogged

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-62b47a?style=flat&logo=creeper&logoColor=white)
![Loader](https://img.shields.io/badge/Loader-Fabric-dbb68a?style=flat)
![Version](https://img.shields.io/badge/Version-1.0.1-6fa8dc?style=flat)
![Java](https://img.shields.io/badge/Java-21+-f6a623?style=flat&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-CC0--1.0-lightgrey?style=flat)

[Roadmap →](ROADMAP.md)

A Fabric mod for Minecraft 1.21.11 that expands the aquatic experience with new creatures, plants, and structures.

---

## Mobs

| Mob | Spawns In | Notes | Drops | Breedable | Bucketable |
|-----|-----------|-------|-------|:---------:|:---------:|
| Fry | From Roe | Inherits parent appearance; grows into adult in ~5 min | — | — | ✓ |
| Sunfish | Swamp | 9 variants; 5% mutation on breed; name tag "Diansu" unlocks Diansus Diansur | Raw Sunfish | ✓ | ✓ |
| Catfish | Swamp | Eats nearby Algae | Raw Catfish | ✓ | ✓ |
| Dongfish | Dark or nighttime water | Shears trim its "horngus"; sheared state saved in bucket | — | ✓ | ✓ |
| Seahorse | Lukewarm/Warm Ocean | Spawns in small groups | — | ✓ | ✓ |
| Bubble Eye | Rivers, Forest biomes² | Spawns in small groups | Raw Bubble Eye | ✓ | ✓ |
| Mahi Mahi | Warm/Lukewarm Oceans | — | Raw Mahi Mahi | ✓ | ✓ |
| Anglerfish | Deep Ocean variants | Glowing lure | Raw Anglerfish | ✓ | ✓ |
| Anomalocaris | Mushroom Fields (underground) | — | — | ✓ | ✓ |
| Sea Slug | Warm Ocean | 20 colors × 5 patterns; 30% bioluminescent; hatched slugs never despawn | — | ✓ | ✓ |
| Crab | Coastal biomes³ | Walks on land; 4 biome variants | Raw Crab Legs | ✓ | ✓ |
| Orca | Cold/Frozen Oceans | Forms pods; adults defend calves; 25% spawn as calves; hostile if threatened | — | — | — |
| Right Whale | Deep Cold/Frozen Ocean | 30% spawn as calves; Shears barnacles → 1–3 Nautilus Shells per level | — | — | — |
| Pelican | Coastal⁴ | Catches and carries fish; trade a raw fish **or mob bucket** with it → *Paying the Bill* (bucket returned as empty Water Bucket) | — | — | — |

² River, Frozen River, Forest, Birch Forest, Old Growth Birch Forest, Dark Forest
³ Dungeness: Beach | Ghost: Swamp/Mangrove Swamp | Blue Claw: Stony Shore | Mojang: Swamp/Mangrove Swamp (5%)
⁴ Spawns like a Wandering Trader; despawns naturally

---

## Breeding

Feed two fish of the same species a **Worm** each to put them in breeding mode. They'll lay **Roe** — a glowing block of eggs that hatches into **2–4 Fry** after ~5 minutes. Fry inherit parent appearance and grow into adults after another ~5 minutes.

Worms come from mining **Wormy Dirt** or **Wormy Mud**.

---

## Blocks

| Block | Found In | Notes |
|-------|----------|-------|
| Algae | Ocean floors | Spreads via random ticks; Bonemeal accelerates; feeds Clam/Oyster (which feed Starfish) |
| Clam | Sandy ocean floors | Eats adjacent Algae to spread; break for Pearl (10–70% with Fortune); Silk Touch collects block |
| Oysters | Cold/temperate ocean floors | Same as Clam |
| Starfish | Seafloor | Eats Clam/Oyster to spread; color assigned on placement and carried when spreading |
| Dead Starfish | Seafloor | Inert; decorative |
| Roe | Breeding output | Hatches into 2–4 Fry in ~5 min; Silk Touch to collect; unlocks *Uncured Caviar* |
| Sea Slug Eggs | Breeding output | Silk Touch to collect; unlocks *Forbidden Cinnamon Roll* |
| Anemone | Ocean floor | Decorative |
| Urchin | Ocean floor | Deals 1 damage on contact (walking); right-click with empty Bucket to pick up |
| Duckweed | Freshwater surface | Breaks instantly; water-only placement |
| Sargassum | Ocean surface | Breaks instantly; water-only placement |
| Papyrus | Swamp, riverbanks | Tall water-adjacent plant |
| Aquarium Glass | Crafted | Mobs can't spawn inside enclosed structures; no redstone conduction |
| Wormy Dirt | Underground (most overworld biomes) | Mining drops Worm |
| Wormy Mud | Underground (Mangrove Swamp) | Mining drops Worm |

---

## Items

### Food

| Item | Notes |
|------|-------|
| Raw Sunfish | Cookable |
| Raw Catfish | Cookable |
| Raw Bubble Eye | Cookable |
| Raw Mahi Mahi | Cookable |
| Raw Anglerfish | Cookable |
| Raw Crab Legs | Cookable |
| Worm | Causes nausea if eaten |

All raw modded fish (except crab legs) have a **25% chance** to appear when fishing.

### Tools

**Fishing Net** — Left-click a fish to capture it and receive its raw drop. Durability: 131. Enchantable. Unlocks *Catch a Fish... Without a Rod, Again!*

### Materials

**Pearl** — Crafting ingredient.

### Buckets

Right-click most fish with a **Water Bucket** to capture them. Buckets preserve color, variant, and state (a sheared Dongfish stays sheared; Fry keep their tint). Mobs released from a bucket never despawn. Urchin uses an empty Bucket instead.

Bucketable: Fry, Sunfish, Catfish, Dongfish, Seahorse, Bubble Eye, Anglerfish, Mahi Mahi, Anomalocaris, Sea Slug, Crab, Urchin.

---

## World Generation

### Ocean

| Feature | Biomes |
|---------|--------|
| Algae | Ocean, Deep Ocean, Cold Ocean, Lukewarm Ocean, Warm Ocean, and swamp variants |
| Sargassum | Lukewarm Ocean, Deep Lukewarm Ocean |
| Sargassum (beach wash-up) | Beach |
| Clams | Warm Ocean, Lukewarm Ocean, Deep Lukewarm Ocean |
| Oysters | Ocean, Deep Ocean, Cold Ocean, Deep Cold Ocean, Frozen Ocean, Deep Frozen Ocean |
| Starfish | Warm Ocean, Lukewarm Ocean |
| Anemone | Warm Ocean, Lukewarm Ocean |
| Urchin | Most ocean biomes |
| Hydrothermal Vents | Deep ocean (excl. Deep Lukewarm, rare) |

### Swamp & Wetland

| Feature | Biomes |
|---------|--------|
| Duckweed | Swamp, Mangrove Swamp |
| Papyrus | Swamp, Mangrove Swamp, Savanna, Desert |
| Pond Cypress | Swamp — replaces vanilla swamp oak |
| Swamp Logs | Swamp — fallen/floating oak logs with moss, mushrooms, and duckweed |

### Underground

| Feature | Biomes |
|---------|--------|
| Wormy Dirt | Common overworld biomes |
| Wormy Mud | Mangrove Swamp |

**Pond Cypress** are large swamp trees (15–20 blocks) with buttress roots, cypress knees, inverted-cone crowns, and draping vines. Place 9 oak saplings in a 3×3 grid to grow one manually — bone meal requires a random number of applications (average ~3) before the tree sprouts.

**Vanilla change:** Bonemeal on a lily pad now spreads it to an adjacent water block.

---

## Advancements

| Advancement | How to Get |
|-------------|------------|
| **Dirt Noodle** | Collect a Worm |
| **Uncured Caviar** | Collect Roe with Silk Touch |
| **Forbidden Cinnamon Roll** | Collect Sea Slug Eggs with Silk Touch |
| **Paying the Bill** | Trade a raw fish or mob bucket with a Pelican while it's holding one |
| **Catch a Fish... Without a Rod, Again!** | Catch a fish using a Fishing Net |

---

## Credits

Made by **Braeden Silver**, **EightSidedSquare**, **Diansu**, and **Sillvia**.

Licensed under [CC0-1.0](https://creativecommons.org/publicdomain/zero/1.0/).
