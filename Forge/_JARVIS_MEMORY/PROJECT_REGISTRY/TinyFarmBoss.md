# TinyFarmBoss.md
# Registro de Proyecto: Tiny Mob Farm Addon: Bosses

## Identidad
- **Nombre:** Tiny Mob Farm Addon: Bosses
- **ModID:** tinyfarmbossaddon
- **Versión Actual:** 1.2.0
- **Minecraft:** 1.20.1
- **Modloader:** Forge & Fabric
- **Autor:** Tontoque28
- **Paquete Base:** com.tontoque28.tinyfarmboss

## Descripción Técnica
Addon utility para Tiny Mob Farm que permite capturar bosses y mobs que de otra manera no serían granjeables.
- **Boss Capture:** Permite capturar al Wither, Ender Dragon y Warden con el Lasso.
- **Custom Loot Tables & GLM:** Los bosses proporcionan su drop real en el farm a pesar de requerir "player kill" en vanilla. Incorpora un `GlobalLootModifier` no destructivo que intercepta y asegura la inyección de ítems clave (Nether Star, Dragon Egg) si otros mods (ej. Progressive Bosses) anulan o sobrescriben la Loot Table vanilla.
- **Mod Compatibility (Anti-Crash):** Captura y generación de botín segura (vía simulación de `Loot Tables` base) para mobs con lógica pesada o problemática como los dragones de Ice and Fire, piratas/bárbaros de MineColonies o Slimes Vanilla. Uso de *Soft Dependencies* (`ModList.get().isLoaded()`) para garantizar compatibilidad con *forks* comunitarios como Ice & Fire: Community Edition sin lanzar `ClassNotFoundException`.
- **Soft Dependency Support:** Compatibilidad extendida y dinámica para jefes de L_Ender's Cataclysm (Ignis, Ender Golem, Netherite Monstrosity) y Terramity.
- **Custom Config & Safeguards:** Archivo `.toml` generado dinámicamente que permite a los usuarios:
  - Whitelist (`custom_farmable_mobs`): Añadir entidades personalizadas.
  - Blacklist (`blacklisted_mobs`): Bloquear namespaces completos (ej. `minecolonies:*`) para prevenir crashes irreparables al intentar granjear entidades complejas como aldeanos o NPCs.
- **Entity Crash Handler:** Sistema integral que envuelve el procesamiento de entidades para evitar crash de servidor, haciendo log a un archivo propio `tmf_entity_crashes` con detalles del uuid, nbt, health y aplicando un fallback seguro dinámico.
- **Multipart Support:** Soporta clickear en las partes secundarias de entidades complejas (ej. alas de dragón).

## Dependencias
- Minecraft 1.20.1
- Forge / Fabric Loader
- Tiny Mob Farm (Requerido)

## Estado
- **Fase:** Release (Versión 1.2.0)
- **Última Acción:** Implementados Global Loot Modifiers no destructivos, soft dependencies para I&F/Cataclysm/Terramity, y configuración Forge Config TOML (Whitelist/Blacklist).
