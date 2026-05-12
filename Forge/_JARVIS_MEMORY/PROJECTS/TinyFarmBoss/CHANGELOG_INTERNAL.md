# CHANGELOG_INTERNAL.md

Historial técnico interno del proyecto.

Formato:

## Versión X.X.X
Fecha:
Cambios técnicos:
Notas internas:

Regla:
No modificar versiones anteriores.
Solo añadir nuevas versiones al final.

## Versión 1.1.0
Fecha: 2026-03-07
Cambios técnicos:
- Creado sistema `SafeMobSimulationHandler` para manejar mobs que crashean al intentar ser guardados/generados en Tiny Mob Farm.
- Implementadas funciones seguras anti-crash para capturar y generar loot de mobs de otros mods problemáticos: MineColonies (Barbarians y Pirates), Ice and Fire (Dragones) y Vanilla Slimes.
- Modificado `LassoHandler` con manejo `try-catch` robusto a la hora de serializar la información NBT del mob capturado, evitando caída del servidor si hay datos corruptos o de mods complejos.
- Añadidas `LootTables` personalizadas (`safe_minecolonies_barbarian`, `safe_iceandfire_fire_dragon`, etc.) para simular el comportamiento de drop de dichos mobs sin invocar su lógica interna.
- Actualizada y limpiada la clase de comandos.
Notas internas:
El comando temporal `givetestmobs` se utilizó en la fase `-Test` para probar compatibilidad con mobs complejos y fue removido exitosamente tras comprobar estabilidad.

## Versión 1.1.1
Fecha: 2026-03-07
Cambios técnicos:
- Implementado `SlimeCompatibilitySystem` para detectar, aislar y procesar Slimes (o entidades similares a slimes) que suelen romper lógicas de granjas debido a la mutabilidad de su NBT/Size.
- Implementado `EntityCrashHandler`, una capa de seguridad (wrapper `Runnable`) que envuelve la ejecución crítica con un `try-catch`. Previene la caída total del tick rate / servidor si un mob del Lasso lanza un throw o error por inconsistencia.
- Creado sistema de reportería de logs: En lugar de saturar la consola o crashear, los errores se derivan a un archivo de texto independiente dentro de `/logs/tmf_entity_crashes/`.
- Añadido chequeo dinámico de modpacks (Safe Mode Environment Detector) vía `ModList.get().isLoaded` buscando incompatibilidades visuales agresivas (como OptiFine, EMF, Radium, Entity Culling) y forzando sanitización extrema (Size = 1, sin fuego, invisibilidad o pociones) al NBT del mob cuando se guardan en el Lasso.
- Añadido Hash map con control de cooldown a 10s para evitar SPAM excesivo de logs con la misma entidad corrompida.
Notas internas:
Build testeada. Se modificó levemente el alcance original para hacerlo agnóstico de la entidad específica (el wrapper protege generalizadamente cualquier Boss o Entity). La versión de "Test" pasó a Release limpio.

## Versión 1.1.2
Fecha: 2026-03-07
Cambios técnicos:
- Sincronizadas las tablas de botín (`loot_tables`) de los jefes (Wither, Warden, Ender Dragon) entre las versiones de Forge y Fabric para unificar la experiencia de juego.
- Cambiado el `mod_id` a `tinyfarmbossaddon` para estandarizar la identificación del mod en ambas plataformas.
- Ajustado el `build.gradle` para generar el JAR con un nombre de archivo que incluye el modloader (`tinyfarmbossaddon-1.1.2 Forge.jar`).
- Creados changelogs públicos separados para Forge y Fabric.
- Actualizada la descripción pública para reflejar la disponibilidad en ambos modloaders.
Notas internas:
Esta versión marca la primera release unificada para Forge y Fabric, cerrando la brecha de características entre ambas.

## Versión 1.2.0
Fecha: 2026-05-02
Cambios técnicos:
- Reestructuración de `LootTables` a formato `pools` independientes.
- Añadido `BossLootModifier` (Global Loot Modifier) no destructivo que inyecta ítems vitales usando filtrado `noneMatch()`.
- Refactorización de *Hard Dependencies* a *Soft Dependencies* usando `ModList.get().isLoaded(...)` para solucionar ClassNotFoundException con "Ice and Fire: Community Edition".
- Añadido soporte nativo vía *Soft Dependency* para "L_Ender's Cataclysm" y "Terramity".
- Creación de configuración nativa Forge TOML (`TinyFarmBossConfig`) con soporte para Whitelist (`custom_farmable_mobs`) y Blacklist (`blacklisted_mobs` con comodines).
- Corrección de advertencias de *deprecation* en invocaciones a `ResourceLocation` y `ModLoadingContext`.
Notas internas:
La versión escala considerablemente en compatibilidad de modpacks. La lógica universal en el GLM asegura coexistencia pasiva con otros mods de jefes. Preparado para release.
