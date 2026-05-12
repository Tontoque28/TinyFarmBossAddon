# Guía de Portabilidad a Fabric (Versión 1.2.0)

Este documento resume todos los cambios y adiciones realizados en la versión de Forge 1.2.0 para que puedan ser aplicados en el proyecto independiente de Fabric.

---

## 1. Actualización de Loot Tables (JSON)
Las tablas de botín base deben ser reemplazadas para usar múltiples `pools` (para evitar conflictos internos). Copiar exactamente los siguientes archivos de Forge a Fabric en `src/main/resources/data/tinyfarmbossaddon/loot_tables/entities/`:
- `wither_custom.json`
- `warden_custom.json`
- `ender_dragon_custom.json`

*(Nota: los archivos `safe_iceandfire_*.json` y `safe_minecolonies_*.json` también fueron movidos al namespace `tinyfarmbossaddon` en Forge, replicar este movimiento en Fabric).*

---

## 2. Inyección de Botín Global (Loot Modifier / Eventos)
En Forge creamos `BossLootModifier` usando el sistema GLM nativo de Forge.
**Para Fabric:**
Se debe usar la API de eventos de botín de Fabric (`LootTableEvents.MODIFY`).
La lógica **CRÍTICA** (Universal y No Destructiva) a portar es la siguiente:
1. No borrar drops originales (`removeIf` no debe usarse en tablas ajenas).
2. Usar filtro inteligente (`noneMatch` equivalente) para revisar si el ítem (ej. `NETHER_STAR`) ya fue inyectado por la tabla vanilla o por otro mod como *Progressive Bosses*.
3. Si el ítem falta, añadir un nuevo `LootPool` a la tabla en tiempo real con el botín forzado.
4. Aplicar esto a: Wither, Ender Dragon, Warden, y los tres Dragones de Ice and Fire (`safe_iceandfire_fire_dragon`, etc).

---

## 3. Sistema de Seguridad y Anti-Crasheos
Se deben portar dos clases nuevas y fundamentales que son totalmente agnósticas (código Java puro):

**`SlimeCompatibilitySystem.java`**
- Valida que el `CompoundTag` (`NbtCompound` en mappings de Fabric/Yarn) del Slime se pueda guardar correctamente.
- Limpia los NBT problemáticos (Motion, Efectos, Invisibilidad).
- Fuerza el tamaño a `1` (`setSize(1, true)`) para evitar desbordes visuales en el bloque de granja.

**`EntityCrashHandler.java`**
- Un wrapper `try/catch` global con el método estático `safeProcessEntity(Entity, String action, Runnable)`.
- Si ocurre un error, en lugar de crashear, cancela el guardado y escribe un reporte detallado en `logs/tmf_entity_crashes/entity_crash_[FECHA].txt`.
- Incluye cooldown de 10 segundos por UUID usando un `ConcurrentHashMap` para no spamear logs.

---

## 4. Soft Dependencies (Cataclysm, Terramity, Ice & Fire)
En la clase encargada de interceptar el lazo (`LassoHandler` equivalente en Fabric):
- Eliminar cualquier import duro a clases de mods de terceros (ej. `com.github.alexthe666...`).
- Usar verificaciones de mods cargados en Fabric: `FabricLoader.getInstance().isModLoaded("iceandfire")`, etc.
- Interceptar las IDs estáticas directamente por String:
  - **Ice & Fire:** `iceandfire:fire_dragon`, `iceandfire:ice_dragon`, `iceandfire:lightning_dragon`.
  - **Cataclysm:** `cataclysm:ignis`, `cataclysm:ender_golem`, `cataclysm:netherite_monstrosity`.
  - **Terramity:** `terramity:*` (o validando el namespace `terramity`).

---

## 5. Configuración (Whitelist / Blacklist)
Implementar una configuración para Fabric (puede ser JSON puro con GSON o MidnightConfig / AutoConfig).
Debe incluir:
- **`custom_farmable_mobs`** (Whitelist): Lista de Strings donde el usuario pone "modid:entidad" que se agregará a la lista de Bosses permitidos.
- **`blacklisted_mobs`** (Blacklist): Lista de Strings para bloquear namespaces completos (ej. `minecolonies:*`). Si la entidad interceptada coincide con la Blacklist, se cancela la captura y se avisa al jugador para proteger el servidor de crashes.

---

## 6. Comandos
- Renombrar el comando base a `/tinyfarmbossaddon` (ya que el ModID fue actualizado).
- Implementar el comando `/tinyfarmbossaddon test` que otorga todos los lazos configurados (Wither, Warden, Ender Dragon, Slime, Barbarian, Fire/Ice/Lightning Dragons) de forma inmediata al jugador.

---

## 7. Refactorizaciones Generales
- Asegurar que todas las llamadas a `ResourceLocation` usen el método moderno si corresponde (en Fabric suele ser `new Identifier(...)` o `Identifier.of(...)` dependiendo de la versión exacta de la API/Minecraft).
- Validar que el `modId` global es `tinyfarmbossaddon`.