## Version 1.2.0
Date: 2026-05-02
Changes:
- **Portabilidad Completa a Fabric 1.20.1:** El mod ha sido reescrito desde cero para funcionar de forma nativa en el ecosistema de Fabric.
- **Inyección de Botín Global:** Implementado un sistema no destructivo (equivalente a GLM) usando `LootTableEvents.MODIFY` para asegurar los drops de los bosses sin interferir con otros mods.
- **Soporte Extendido (Soft Dependencies):** Añadida compatibilidad dinámica para jefes de Ice and Fire, L_Ender's Cataclysm y Terramity.
- **Sistema de Configuración (Whitelist/Blacklist):** Creado un archivo de configuración JSON (`tinyfarmbossaddon.json`) para permitir a los usuarios añadir o bloquear entidades personalizadas.
- **Nuevos Comandos:** Renombrado el comando base a `/tinyfarmbossaddon` y añadido el subcomando `/test` para obtener todos los lazos de prueba.
Fixes:
- Resueltos todos los problemas de compilación y dependencias relacionados con Fabric Loom y la Fabric API.
- Corregido el `mod_id` a `tinyfarmbossaddon` en todos los archivos relevantes.
- Solucionado un error de compilación en `LootTableInjector` (`incompatible types: Builder cannot be converted to LootPool`).
Notes:
- Esta versión marca un hito importante en la portabilidad y estabilidad del mod en Fabric.
- Se recomienda revisar el archivo de configuración generado para personalizar la experiencia.
- La versión del mod ha sido actualizada a 1.2.0 para reflejar la paridad de características con la versión de Forge.
