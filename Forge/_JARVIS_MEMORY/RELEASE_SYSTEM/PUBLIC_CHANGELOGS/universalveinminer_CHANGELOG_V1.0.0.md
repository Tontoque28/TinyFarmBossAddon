## Version 1.0.0
Date: 2026-04-11

Changes:
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

Fixes:
- Initial version; no fixes yet.

Notes:
- Mod requires Forge 1.20.1.
- Configuration is saved in `universalveinminer-common.toml`.