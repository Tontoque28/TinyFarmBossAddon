# JARVIS_SUMMARY.md
# Resumen de Proyecto y Changelogs: T28's Universal Veinminer

Este archivo contiene un registro profesional de todas las versiones publicadas de este mod, enfocado puramente en los changelogs históricos.

---

## [Version 1.0.2]
**Date:** 2026-04-11

**Changes:**
- Added automatic ore detection by name (any block ID containing `_ore` is now veinminable by default).
- Removed Veinminer functionality for logs/trees to focus exclusively on mining.

**Fixes:**
- N/A

**Notes:**
- This version significantly improves compatibility with other mods by automatically detecting their ores.

---

## [Version 1.0.1]
**Date:** 2026-04-11

**Changes:**
- Added special handling for Ancient Debris:
  - With Fortune, now drops Netherite Scraps directly with a chance for extra drops based on the enchantment level.
  - With Silk Touch, drops the Ancient Debris block as expected.
- Improved the block detection algorithm to search in a 3x3x3 area, including diagonals.
- Optimized the search logic for better performance and to ensure the configured block limit is reached more consistently.

**Fixes:**
- Fixed an issue where the veinminer would not mine all connected blocks in large or irregularly shaped veins.

**Notes:**
- This is a recommended update for all users.

---

## [Version 1.0.0]
**Date:** 2026-04-11

**Changes:**
- Initial release of "T28's Universal Veinminer" for Forge 1.20.1.
- Added universal veinmining mechanic, activated by sneaking.
- Implemented automatic dynamic detection for Ores and Logs using Forge Tags (`Tags.Blocks.ORES`, `BlockTags.LOGS`).
- Implemented a 3D BFS search algorithm for finding connected identical blocks.
- Added full vanilla integration: respects Fortune, Silk Touch, tool damage (Unbreaking), and player exhaustion.
- Added safety feature: veinmining stops automatically if the tool reaches 1 durability.
- Added an in-game toggle key (default 'V') with Action Bar notifications.
- Added an in-game command system (`/universalveinminer`) with auto-completion to whitelist custom blocks.
- Added configurable safety limit for maximum blocks mined per action (default: 64, max: 1024).
- Added English (`en_us`) and Spanish (`es_es`) localizations.

**Fixes:**
- Initial version; no fixes yet.

**Notes:**
- Mod requires Forge 1.20.1.
- Configuration is saved in `universalveinminer-common.toml`.
