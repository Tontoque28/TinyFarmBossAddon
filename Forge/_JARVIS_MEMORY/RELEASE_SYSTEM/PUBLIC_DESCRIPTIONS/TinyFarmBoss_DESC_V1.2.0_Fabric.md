# Tiny Farm Boss Remastered (Fabric 1.20.1) - Versión 1.2.0

## Descripción General

Tiny Farm Boss Remastered es un addon esencial para Tiny Mob Farm, diseñado para expandir las capacidades de captura y farmeo a entidades que, de otro modo, serían imposibles de granjear en Minecraft 1.20.1. Esta versión ha sido completamente reescrita para Fabric y trae consigo importantes mejoras de estabilidad, compatibilidad y personalización. Captura bosses icónicos, entidades complejas y mods de terceros con total seguridad.

## Novedades en la Versión 1.2.0 (Fabric Port)

*   **Portabilidad Nativa a Fabric:** El mod ha sido reconstruido desde cero para aprovechar la API de Fabric, asegurando un rendimiento óptimo y compatibilidad con el ecosistema de Fabric 1.20.1.
*   **Inyección de Botín Global (GLM Equivalente):** Se ha implementado un sistema inteligente y no destructivo usando `LootTableEvents.MODIFY`. Esto garantiza que los bosses (Wither, Ender Dragon, Warden) dropeen sus ítems clave (Nether Star, Dragon Egg) incluso si otros mods alteran sus tablas de botín, sin borrar los drops de terceros.
*   **Soft Dependencies Extensas:** Soporte nativo e inyección segura para:
    *   **Ice and Fire:** Dragones de Fuego, Hielo y Rayo.
    *   **L_Ender's Cataclysm:** Ignis, Ender Golem y Netherite Monstrosity.
    *   **Terramity:** Todos los bosses del mod.
    *   **MineColonies:** Bárbaros y Piratas.
*   **Sistema de Configuración (Whitelist/Blacklist):** Nuevo archivo `tinyfarmbossaddon.json` en la carpeta `config`. 
    *   **`custom_farmable_mobs`:** Permite añadir cualquier entidad personalizada usando su formato `modid:entidad`.
    *   **`blacklisted_mobs`:** Protege tu servidor bloqueando namespaces completos (ej. `minecolonies:*`) para prevenir la captura de entidades que rompen la granja.
*   **Nuevos Comandos de Prueba:** Se ha añadido el comando `/tinyfarmbossaddon test` para obtener instantáneamente todos los lazos de prueba de los bosses y entidades soportadas (requiere permisos OP).
*   **Seguridad Anti-Crash Mejorada:** Las clases `EntityCrashHandler` y `SlimeCompatibilitySystem` han sido optimizadas para el entorno Fabric, garantizando que ninguna captura malformada corrompa el mundo.

## Características Base

*   **Captura de Bosses Vanilla:** Wither, Ender Dragon y Warden.
*   **Entity Crash Handler:** Sistema integral que envuelve el procesamiento de entidades, haciendo logs detallados y aplicando fallbacks dinámicos.
*   **Soporte Multipartes:** Permite capturar entidades complejas interactuando con sus hitboxes secundarias.

## Dependencias

*   Minecraft 1.20.1
*   Fabric Loader (>= 0.15.11)
*   Fabric API
*   Tiny Mob Farm (Requerido)