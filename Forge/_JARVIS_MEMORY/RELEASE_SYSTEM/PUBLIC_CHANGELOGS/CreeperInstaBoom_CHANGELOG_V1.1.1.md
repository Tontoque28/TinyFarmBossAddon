## Version 1.1.1
Date: 2024-05-22
Fixes:
- **Strict Spawning:** Reworked spawn logic to be strictly isolated per difficulty mode. This prevents Alpha Creepers from spawning in Easy mode.
- **No Fire Explosions:** Increased the priority of the explosion event interceptor to `HIGHEST`, ensuring all creeper explosions are handled by the mod and do not generate fire, regardless of other mods.