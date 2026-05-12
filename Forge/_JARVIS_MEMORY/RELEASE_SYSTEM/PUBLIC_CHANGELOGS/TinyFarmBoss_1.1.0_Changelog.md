v1.1.0:
- Added Anti-Crash System: safely capture and farm complex mobs from other mods (like MineColonies and Ice and Fire) and Vanilla Slimes without game/server crashes.
- The mod now uses custom simulated drops for these complex entities to ensure stability while maintaining the farming experience.
- Implemented robust error handling (try-catch) when serializing captured mob NBT data to prevent server crashes with corrupted data or incompatible mods.
- Re-added multi-part entity support to ensure boss parts (like Ender Dragon wings) can be properly lassoed.
- General performance and stability improvements for the Lasso capture logic.