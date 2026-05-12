v1.2.0 (Forge):
- **Universal Loot Injector:** Added a new, non-destructive Global Loot Modifier that ensures bosses drop their key items (Nether Star, Dragon Egg, etc.) even if other mods overwrite their loot tables. It will only inject missing items without deleting anything.
- **Improved "Ice and Fire" Compatibility:** Switched to a safer compatibility layer (Soft Dependency) that prevents crashes when using custom forks like "Ice and Fire: Community Edition".
- **New Mod Support:** Added native compatibility to capture and farm bosses from **L_Ender's Cataclysm** and **Terramity**.
- **User Configuration File (.toml):** You can now customize the addon without needing an update!
  - `custom_farmable_mobs`: Add any mob from any mod to the capture whitelist.
  - `blacklisted_mobs`: Block problematic mods entirely using wildcards (e.g., `minecolonies:*`) to prevent the farm from crashing your server.
- Technical codebase refactoring and cleanup for upcoming NeoForge updates.