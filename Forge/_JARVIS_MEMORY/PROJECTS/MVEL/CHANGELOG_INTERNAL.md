# CHANGELOG_INTERNAL.md
# Historial de Cambios: MVEL

## [v1.0.2 - Critical Hotfix]
### Removed
- **Unusing Feature:** Eliminada la funcionalidad de "Unusing" debido a conflictos graves con otros mods (duplicación de ítems, crasheos). Se ha revertido el mod a una versión estable sin esta característica.

## [v1.0.1 - PTD Balance & Fixes]
### Changed
- **Rebalanceo Masivo:** Se han ajustado los niveles máximos de encantamientos para escalar con la dificultad de *Progressive Time Difficulty (PTD)*.
    - **Protección (Todas):** Aumentado a **10** (antes 7). Necesario para sobrevivir al daño x2.5 en Hardcore.
    - **Daño (Sharpness, Smite, etc.):** Aumentado a **10** (antes 8). Para combatir mobs con HP escalado.
    - **Eficiencia:** Aumentado a **10** (antes 8).
    - **Looting / Fortune:** Aumentado a **7** (antes 5). Recompensa por riesgo alto.
    - **Unbreaking:** Aumentado a **12** (antes 10). Durabilidad mejorada.
    - **Depth Strider:** Aumentado a **8** (antes 6). Mayor agilidad acuática.
    - **Feather Falling:** Mantenido en **7**.
    - **Thorns:** Mantenido en **6**.
    - **Swift Sneak / Soul Speed:** Mantenido en **6**.
    - **Respiration:** Mantenido en **6**.

### Added
- Soporte explícito para `Mending` (Nivel 1) y `Infinity` (Nivel 1) en la lógica de override.

### Fixed
- Corregido un crasheo fatal causado por una inyección de Mixin incorrecta en `Enchantment.getMaxLevel`.
- Unificadas las inyecciones de Mixin para `getMaxLevel` en un solo método robusto que funciona tanto en entornos de desarrollo como de producción.
- Corregidas las advertencias de `ClassNotFoundException` al apuntar a los nombres de clase de encantamiento de pesca correctos (`LuckEnchantment`, `LureEnchantment`).
- Añadido `VanillaEnchantmentSubclassesMixin` para manejar encantamientos con clases específicas.

### Technical
- Actualizada la documentación de arquitectura para reflejar la estrategia de doble Mixin.
- Archivado el código corregido de los Mixins en `FULL_CODE_REFERENCES.md`.

## [v1.0.0 - Unreleased]
### Added
- Estructura inicial del proyecto.
- Configuración de Mixins.
- `EnchantmentMixin` básico.

### Technical
- Inicialización de memoria JARVIS.
