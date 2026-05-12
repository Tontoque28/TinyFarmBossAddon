# CHANGELOG_INTERNAL.md
# Historial de Cambios del Proyecto

## [1.1.1] - 2024-05-22
### Fixed
- **Strict Spawning:** Reworked spawn logic to be strictly isolated per difficulty mode. This prevents Alpha Creepers from spawning in Easy mode.
- **No Fire Explosions:** Increased the priority of the explosion event interceptor to `HIGHEST`, ensuring all creeper explosions are handled by the mod and do not generate fire, regardless of other mods.

## [1.1.0] - 2024-05-22
### Major Update
- **Rebranding:** Mod name reverted to **Creeper Insta Boom**.
- **Dynamic Difficulty Selection:** The mod is no longer locked to "Hard" mode.
- Added `/creeperdifficulty <easy|normal|hard>` command to change difficulty in real-time.
  - **Easy:** Fast Creepers (1.2x), No Charged, No Alpha.
  - **Normal:** Faster Creepers (1.4x), Moderate Charged, No Alpha.
  - **Hard:** Insane Speed (1.5x+), High Charged, Alphas Active. (Default)

### Added
- Intercepción de explosiones (`ExplosionEvent.Start`) para compatibilidad con otros mods (ej. Creeper Overhaul).
- Forzado de `swell = -1` para evitar animación de carga vanilla y asegurar "Insta Boom".
- Comando `/alphacreeper` para spawnear Alpha Creepers manualmente.

### Changed
- Aumentado radio de detección de "Insta Boom" a 4.0 bloques para prioridad sobre IAs nativas.
- Refactorización de código para cumplir estrictamente con estándares de estilo (sin comentarios).

### Fixed
- Creepers de otros mods generaban fuego o no explotaban instantáneamente.
- Creepers vanilla iniciaban animación de carga antes de explotar.

## [1.0.3-hard] - 2024-05-22
### Added
- Comando `/creeperdifficulty` inicial.
- Lógica de dificultad dinámica.

## [1.0.2] - 2024-05-21
### Added
- Comando `/alphacreeper`.
- Alpha Creepers.

## [1.0.1] - 2024-05-20
### Added
- Lógica básica de "Insta Boom".
- Eliminación de `SwellGoal`.

## [1.0.0] - 2024-05-19
### Added
- Inicialización del proyecto.
