# Contributing to Waterlogged

This guide is for contributors who can read and modify Java/Gradle code, including contributors using coding agents (for example Claude Code, Copilot, etc.).

If you only want to report a gameplay issue, use the non-technical issue template instead of this workflow.

## Scope and Baseline

- Minecraft version is `1.21.11` (eleven).
- Java baseline is `21`.
- This is a multi-loader mod built from one source tree using Stonecutter.
- Fabric and NeoForge outputs must both keep working.

## Repository Layout (high-signal files)

- Main code: `src/main/java/net/braeden/waterlogged/`
- Generated resources: `src/main/generated/`
- Runtime resources/config: `src/main/resources/`
- Loader scripts: `fabric.gradle`, `neoforge.gradle`, `stonecutter.gradle`
- Core mod init: `src/main/java/net/braeden/waterlogged/WaterloggedMod.java`
- Full engineering notes: `CLAUDE.md`

## Fresh Clone Bootstrap (Do This First)

After cloning from GitHub, expected folders like `build/`, `run/`, and `src/main/generated/` are not present yet (they are gitignored on purpose).

Run these commands in order:

```bash
# macOS/Linux only (if Gradle wrapper is not executable)
chmod +x ./gradlew

# 1) Generate IDE sources
./gradlew genSources

# 2) Generate required resources that are not stored in git
./gradlew runDatagen

# 3) Compile and validate both loaders
./gradlew build

# 4) Launch one loader for testing
./gradlew :1.21.11-fabric:runClient
# or
./gradlew :1.21.11-neoforge:runClient
```

If those three steps work, your local setup is ready.

## Local Setup (Daily)

```bash
./gradlew build
./gradlew runDatagen
./gradlew runClient
./gradlew runServer
```

Notes:
- Root tasks cascade to both loader subprojects.
- `./gradlew build` builds both loaders.
- Run `./gradlew runDatagen` after any datagen provider change.
- NeoForge datagen is not required separately; Fabric datagen output is shared.

## Loader-Specific Commands

Use these when you only want one loader:

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

This is useful for faster iteration when a change is loader-specific.

## BlockBench Model Export (No Bash)

If you changed Java model geometry/animations and want fresh `.bbmodel` files:

```bash
python3 tools/java2bbmodel.py
```

This script is Waterlogged-specific and auto-runs the full model export:
- Input folder: `src/main/java/net/braeden/waterlogged/entity/client/blockbench/`
- Output folder: `tools/blockbench/`
- Animation pairing: automatic when matching `*Animations.java` files exist

No shell script is required.

## Stonecutter and Multi-Loader Rules

When editing Java code, keep preprocessor branches valid:

```java
//? if fabric {
// fabric-only code
//? } else {
/* neoforge-only code */
//? }
```

Required conventions:
- Loader-specific imports must be in Stonecutter branches.
- Shared vanilla/Mojang imports stay unbranched.
- In `else` blocks, NeoForge code must be wrapped in `/* ... */` so preprocessing works.

## Registry Safety Rules

- Fabric: registry objects are created/registered in static initialization patterns used by this project.
- NeoForge: do not construct `Block`, `Item`, `EntityType`, or `BlockEntityType` in static initializers.
- NeoForge registration must happen in `RegisterEvent` callbacks with assigned non-final fields.

If unsure, follow existing patterns in:
- `src/main/java/net/braeden/waterlogged/block/WaterloggedBlocks.java`
- `src/main/java/net/braeden/waterlogged/item/WaterloggedItems.java`
- `src/main/java/net/braeden/waterlogged/entity/WaterloggedEntities.java`

## Datagen Expectations

If you changed any datagen provider or generated asset definition:

1. Run `./gradlew runDatagen`
2. Verify output under `src/main/generated/`
3. Confirm the game still runs and assets resolve correctly

## Pull Request Expectations

Use `.github/PULL_REQUEST_TEMPLATE.md` and keep PRs focused.

Include:
- What changed and why
- Linked issue (`Closes #...`) when applicable
- Exact testing steps you ran

Before opening a PR:
- Run `./gradlew build`
- Run `./gradlew runDatagen` when required
- Verify no accidental unrelated file edits
- Update docs if behavior or contributor workflow changed

## Commit and History Note

When preparing a commit, add a short entry at the top of `docs/HISTORY.md` (most recent first) with:
- Commit hash
- Short title
- 1 to 2 sentence factual summary

## Issue Reporting Paths

- Code-level report: `.github/ISSUE_TEMPLATE/bug_report_technical.md`
- Player-friendly report: `.github/ISSUE_TEMPLATE/bug_report_non_technical.md`
