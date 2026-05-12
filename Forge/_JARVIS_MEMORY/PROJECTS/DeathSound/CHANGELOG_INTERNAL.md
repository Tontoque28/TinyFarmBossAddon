# CHANGELOG_INTERNAL.md
# Historial de Cambios Interno: DeathSound

## [1.0.1] - Versión Anterior
- Estado inicial registrado en memoria.
- Funcionalidad básica: Reproduce sonido de muerte de la serie Hardcore.
- Configuración básica de Forge 1.20.1.
- Archivos de idioma base.

## [1.0.2] - Versión Actual (FINALIZADA)
- Actualización de versión en gradle.properties y mods.toml.
- [FIX] Sonido de muerte ahora es GLOBAL (se escucha en todas las dimensiones y por todos los jugadores).
- [FEAT] Mensajes de muerte en chat ahora se fuerzan a ROJO y NEGRITA con calaveras (☠).
- [FIX] Mejorada la detección de mensajes de muerte para incluir casos sin clave de traducción (fallback de texto).
- [FIX] Corrección de falsos positivos en chat (ignora mensajes con ":").
- [FIX] Solución de crash por concurrencia y reflexión segura.
- [DOC] Generado PUBLISH_INFO.md para publicación.

## Changelog Público (v1.0.2)
# Death Sound Mod - v1.0.2

## New Features
- **Global Death Sound:** Now, when a player dies, the hardcore death sound is played for **everyone on the server**, regardless of dimension (Overworld, Nether, End).
- **Hardcore Death Messages:** Death messages in chat are now forced to appear in **BOLD RED** with skull icons (☠), mimicking the Hardcore mode style.
- **Enhanced Death Screen:** The "You Died!" screen now also displays the death cause in the hardcore red style.

## Fixes & Improvements
- **Crash Fix:** Resolved a potential crash when accessing player lists during death events.
- **Better Detection:** Improved death message detection to work even without specific translation keys (fallback for modded deaths).
- **Safe Reflection:** Implemented robust reflection to ensure compatibility across different environments without crashing.

---
*Compatible with Minecraft 1.20.1 (Forge)*