# ARCHITECTURE.md

Estructura del mod:

Main:
Handlers:
Data:
Events:

Notas técnicas:

Regla:
No simplificar diseño previo.
Solo añadir cambios incrementales.

====================================================

## Arquitectura v1.1.0 (23/02/2026)

### 1. Main Class
- **Archivo:** `ProgressiveTimeDifficultyMod.java`
- **Función:** Punto de entrada.
- **Componentes:**
  - `DEV_MODE`: Variable estática global (boolean) para debug.
  - `onRegisterCommands`: Registra `ModCommands`.
  - `MinecraftForge.EVENT_BUS.register(this)`: Registro de eventos.

### 2. Lógica Principal (Handlers)
- **Archivo:** `DifficultyHandler.java`
- **Función:** Cálculos de dificultad y aplicación de buffs.
- **Eventos Clave:**
  - `onEntityJoinLevel`: Aplica buffs al spawnear.
  - `onLevelTick`: Actualiza dificultad global y periódicamente (cada 3 min).
  - `onPlayerTick`: Incrementa contador personal del jugador.
  - `onEntityInteract`: Debug Stick (solo en DEV_MODE).
  - `onProjectileImpact`: Buff de daño a proyectiles disparados por mobs buffeados.
  - `onLootingLevel`: Aumenta looting si el mob es muy difícil.

### 3. Persistencia de Datos (Data)
- **Archivo:** `DifficultySavedData.java`
- **Función:** Almacenamiento NBT global del mundo.
- **Campos:**
  - `additionalTime` (long): Offset global de tiempo.
  - `hardcoreMode` (boolean): Estado del modo hardcore.
  - `devMode` (boolean): Estado del modo desarrollador.
- **Sincronización:** `LevelEvent.Load` carga estos datos al iniciar el servidor.

### 4. Comandos
- **Archivo:** `ModCommands.java`
- **Función:** Gestión administrativa.
- **Comandos:**
  - `/progresstd add_days/set_day`: Modifica tiempo global.
  - `/progresstd player <name> ...`: Modifica tiempo personal.
  - `/progresstd hardcore <bool>`: Activa/Desactiva modo hardcore.
  - `/progresstd dev_mode <bool>`: Activa/Desactiva modo debug.
  - `/progresstd info`: Muestra estado actual.

### 5. Fórmulas de Dificultad (v1.1.0)
- **Tiempo Efectivo:** `WorldTime + GlobalOffset + PlayerTime`
- **Factor Base:** `(TiempoEfectivo / 24000) * PorcentajeDiario`
- **Porcentajes Diarios:**
  - Easy: 0.5%
  - Normal: 0.75%
  - Hard: 1.0%
  - Hardcore: 1.5% (Fijo)
- **Multiplicadores de Entidad:**
  - Bosses (`forge:bosses`, Wither, Dragon, Warden, Elder Guardian): x1.20
  - Iron Golem: x2.0 (Balanceo especial)
  - Enemigos normales: x1.0
  - Neutrales/Otros: x0.5

### 6. Buffs Aplicados
- **Vida:** `Base * (1 + Factor * 1.2)`
- **Daño:**
  - Hardcore: `Factor * 1.5`
  - Hard: `Factor * 1.0`
  - Normal: `Factor * 0.5`
  - Easy: `0.0`
- **Armadura:** `(VidaExtra) / 8.0` (Aditivo)
- **Knockback Resistance:** `Min(1.0, Factor * 0.1)` (Aditivo)

## Arquitectura v1.1.1 (23/02/2026)

### 1. Rebalanceo Hardcore
- **Daño:** Aumentado de `Factor * 1.5` a `Factor * 2.5`.
- **Knockback Resistance:** Aumentado de `Factor * 0.1` a `Factor * 0.15`.
- **Objetivo:** Mayor letalidad y resistencia en el modo más difícil.

## Arquitectura v1.1.2 (23/02/2026)

### 1. Refactorización (Regla Tontoque28)
- **Nuevo Handler:** `EnvironmentalDamageHandler.java`
- **Función:** Maneja exclusivamente la lógica de daño ambiental (`LivingHurtEvent`).
- **Separación:** Se eliminó esta lógica de `DifficultyHandler.java` para mantener el código limpio y modular.

### 2. Daño Ambiental Escalable
- **Evento:** `LivingHurtEvent` (Intercepta daño antes de armadura).
- **Objetivo:** Jugadores (`Player`).
- **Lista Blanca:** Caída, Fuego, Lava, Ahogamiento, Explosiones, Magia, etc.
- **Fórmula:** `DañoOriginal * (1.0 + FactorDificultad)`.
- **Bonus Hardcore:** `+ (FactorDificultad * 0.5)` extra al multiplicador.
- **Ejemplo:** Si el factor es 1.0 (100% dificultad) y el daño base es 5 (2.5 corazones):
  - Normal: 5 * 2.0 = 10 daño (5 corazones).
  - Hardcore: 5 * 2.5 = 12.5 daño (6+ corazones).

## Arquitectura v1.1.3 (23/02/2026)

### 1. Experiencia Escalable
- **Evento:** `LivingExperienceDropEvent`.
- **Objetivo:** Mobs que sueltan XP al ser matados por un jugador.
- **Fórmula:** `XP_Original * (1.0 + FactorDificultad)`.
- **Objetivo:** Recompensar al jugador por derrotar enemigos más fuertes.

## Arquitectura v1.1.4 (23/02/2026)

### 1. Refactorización de Jefes
- **Nueva Clase:** `BossRegistry.java`.
- **Función:** Centraliza la detección de jefes mediante Tags (`forge:bosses`) y una lista blanca de IDs (`KNOWN_BOSS_IDS`).
- **Soporte:** Mowzie's Mobs, Cataclysm, Twilight Forest, Ice and Fire, Aquamirae, BOMD, Stalwart Dungeons, Rotten Creatures, etc.

### 2. Protección de Armaduras (Daño Híbrido)
- **Límite Físico:** El multiplicador de `ATTACK_DAMAGE` se limita a x5.0.
- **Daño Mágico:** El exceso de daño se aplica como daño mágico (atenuado al 50%) mediante `EnvironmentalDamageHandler`.
- **Objetivo:** Evitar que la armadura se rompa instantáneamente por golpes de 100+ daño.

### 3. Reglas de Seguridad
- **Límite de Vida:** Si un mob tiene > 200 HP (buffed), no recibe armadura ni resistencia extra.
- **Mobs Débiles:** Si un mob tiene <= 20 HP base, recibe un bono extra de vida y armadura.

## Arquitectura v1.1.5 (Creeper Exclusion)
Fecha: 23/02/2026
Cambios técnicos:
- **Exclusión de Creepers:** Se ha modificado `DifficultyHandler.java` para que los Creepers (y cualquier entidad en la `BLACKLIST`) sean ignorados por el sistema de buffs.
- **Limpieza de Buffs:** Se ha añadido una llamada a `removeBuffs` para asegurar que si un Creeper tenía buffs por alguna razón, estos sean eliminados.
- **Razón:** Compatibilidad con otros mods que modifican Creepers y para mantener el balance del juego base.

## Arquitectura v1.1.6 (Visual Threat Indicators & Rebalance)
Fecha: 07/03/2026
Cambios técnicos:
- **Indicadores Visuales:**
  - **Evento:** `LivingEvent.LivingTickEvent` (cada 20 ticks).
  - **Lógica:** Lee el `ptd_factor` del NBT y emite partículas.
  - **Tiers:**
    - Factor < 0.5: Sin partículas.
    - Factor 0.5 - 1.0: `ParticleTypes.SMOKE`.
    - Factor 1.0 - 2.0: `ParticleTypes.FLAME`.
    - Factor >= 2.0: `ParticleTypes.SOUL_FIRE_FLAME`.
- **Rebalanceo Hardcore:**
  - **Tasa de Incremento:** Reducida de 1.5% a 1.2% por día.
  - **Daño:** Restaurado a x2.5.
  - **Vida:** Reducida a x1.0.
  - **Armadura:** Reducida a x1.0 con divisor 10.0.
