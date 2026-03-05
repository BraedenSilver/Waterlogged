![Banner](docs/banner.png)

# Waterlogged

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-62b47a?style=flat&logo=creeper&logoColor=white)
![Fabric](https://img.shields.io/badge/Fabric-0.18.4+-dbb68a?style=flat)
![NeoForge](https://img.shields.io/badge/NeoForge-21.11.x-f57542?style=flat)
![Version](https://img.shields.io/badge/Version-1.0.1-6fa8dc?style=flat)
![Java](https://img.shields.io/badge/Java-21+-f6a623?style=flat&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-CC0--1.0-lightgrey?style=flat)

[Roadmap →](docs/ROADMAP.md) • [Feature Wiki →](docs/features/README.md)

A **Fabric + NeoForge** mod for Minecraft 1.21.11 that expands the aquatic experience with new creatures, plants, and structures. Both loaders are built from a single source tree using [Stonecutter](https://stonecutter.kikugie.dev/).

---

## Features

Gameplay details now live in focused pages:

- [Mobs](docs/features/mobs.md)
- [Breeding](docs/features/breeding.md)
- [Blocks](docs/features/blocks.md)
- [Items](docs/features/items.md)
- [World Generation](docs/features/world-generation.md)
- [Advancements](docs/features/advancements.md)
- [All Feature Pages](docs/features/README.md)

---

## Building

Requires **Java 21**. Both loader jars are produced from a single source tree via [Stonecutter](https://stonecutter.kikugie.dev/).

### First-Time Setup (Fresh GitHub Clone)

If you just downloaded/cloned this repository, some folders are intentionally missing because they are gitignored (for example `build/`, `run/`, and `src/main/generated/`).

Do this in order:

1. Install Java 21.
2. Open a terminal in the project root.
3. If needed on macOS/Linux, make Gradle executable:

```bash
chmod +x ./gradlew
```

4. Generate IDE sources once:

```bash
./gradlew genSources
```

5. Run datagen once to create generated resources:

```bash
./gradlew runDatagen
```

6. Build everything:

```bash
./gradlew build
```

7. Start one dev client:

```bash
# Fabric
./gradlew :1.21.11-fabric:runClient

# or NeoForge
./gradlew :1.21.11-neoforge:runClient
```

That is the minimum setup needed after a fresh clone.

```bash
./gradlew build          # Build both Fabric + NeoForge → build/libs/
./gradlew runDatagen     # Regenerate data files → src/main/generated/
./gradlew runClient      # Launch dev client (active Stonecutter version)
./gradlew runServer      # Launch dev server
```

The active Stonecutter version is set in `stonecutter.gradle` (default: `1.21.11-fabric`). Loader-specific build scripts are `fabric.gradle` and `neoforge.gradle`.

### Loader-Specific Commands

Use these when you only want to run or build one loader:

```bash
# Fabric only
./gradlew :1.21.11-fabric:build
./gradlew :1.21.11-fabric:runClient
./gradlew :1.21.11-fabric:runServer

# NeoForge only
./gradlew :1.21.11-neoforge:build
./gradlew :1.21.11-neoforge:runClient
./gradlew :1.21.11-neoforge:runServer
```

### BlockBench Export (Python, No Bash)

If you want to edit entity models in BlockBench, run this from the repository root:

```bash
python3 tools/java2bbmodel.py
```

What it does automatically:
- Reads model Java files from `src/main/java/net/braeden/waterlogged/entity/client/blockbench/`
- Pairs animations when `*Animations.java` exists
- Writes `.bbmodel` files to `tools/blockbench/<Name>/<Name>.bbmodel`

---

## Reporting Issues

Please use the GitHub issue templates in `.github/ISSUE_TEMPLATE/`:

- **Technical Bug Report** (`bug_report_technical.md`) — for contributors comfortable with code/logs
- **Bug Report (Easy Form)** (`bug_report_non_technical.md`) — plain-language form for players and non-technical users

When possible, include your Waterlogged version, loader (Fabric/NeoForge), and Minecraft version.

---

## Contributing

Contributions are welcome.

If you are writing code (or using a coding agent), start with `.github/CONTRIBUTING.md` for Stonecutter/Fabric/NeoForge workflow requirements.

1. Fork the repository and create a feature branch.
2. Keep changes focused and clearly scoped.
3. Run relevant checks locally (for example `./gradlew build`).
4. If you changed datagen providers, run `./gradlew runDatagen`.
5. Open a pull request using `.github/PULL_REQUEST_TEMPLATE.md`.

For bug fixes, link the related issue using `Closes #` in your PR description.

---

## Credits

Made by **Braeden**, **EightSidedSquare**, **Diansu**, and **Sillvia**.

Based on [Angling](https://github.com/8s2/Angling) by EightSidedSquare.

Licensed under [CC0-1.0](https://creativecommons.org/publicdomain/zero/1.0/).
