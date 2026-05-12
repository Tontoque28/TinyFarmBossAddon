# ARCHITECTURE.md
# Arquitectura del Mod: Better Survival: World Awakening

## 1. Estructura de Paquetes

```
com.tontoque28.worldawakening
 ├── WorldAwakeningMod.java (Main class)
 ├── core
 │    ├── DirectorManager.java (Orquestador principal, persistencia NBT)
 │    ├── ThreatLevelCalculator.java (Cálculo de amenaza 0-100)
 │    ├── EventScheduler.java (Gestión de cooldowns y triggers)
 │    ├── WorldProgressTracker.java (Datos persistentes del jugador)
 │    ├── AwakeningStageManager.java (Cálculo de etapa global: Dormant -> Relentless)
 │    ├── AwakeningStage.java (Enum de etapas)
 │    ├── ZombieVariant.java (Enum de variantes)
 │    └── EvolutionEventData.java (SavedData para evento global)
 ├── ai
 │    ├── PlayerStateAnalyzer.java (Análisis de entorno)
 │    ├── MobEnhancer.java (Aplicación de atributos y rasgos)
 │    ├── SpawnController.java (Lógica de spawn seguro y equipamiento)
 │    └── ZombieVariantManager.java (Asignación de variantes al spawn)
 ├── events
 │    ├── AmbushEvent.java (Lógica de emboscada)
 │    ├── PressureEvent.java (Lógica de presión/horda)
 │    ├── EliteSpawnEvent.java (Spawn de élites)
 │    ├── EvolutionEventManager.java (Evento global raro)
 │    ├── EliteTraitEvents.java (Manejo de habilidades especiales: Web, Explosion, etc.)
 │    ├── ZombieVariantEvents.java (Comportamientos de variantes: Sprint, Knockback)
 │    └── PsychologicalEventManager.java (Efectos de sonido/visuales)
 ├── client
 │    └── ClientZombieVariantHandler.java (Renderizado visual: Escala)
 ├── commands
 │    └── WACommands.java (Comandos de debug y control)
 ├── config
 │    └── WAConfig.java (Forge Config)
 └── util
      └── WAUtils.java
```

## 2. Sistemas Principales

### 2.1 Director AI (Threat System)
- **Threat Level (0-100):** Volátil, cambia según equipo y bioma.
- **Awakening Stage (0-4):** Persistente, cambia según tiempo total y bosses. Define la "Era" del mundo.

### 2.2 Sistema de Variantes (Zombie Evolution)
- No usa nuevas entidades.
- Usa NBT tags para marcar variantes: `NORMAL`, `RUNNER`, `BRUTE`, `STALKER`, `CORRUPTED`.
- **Visual:** Escala de modelo + Partículas específicas.
- **Comportamiento:**
  - Runner: Sprint al acercarse.
  - Brute: Knockback fuerte.
  - Corrupted: Velocidad si jugador low HP.

### 2.3 Sistema de Eventos
- **Ambush:** Spawn táctico detrás del jugador.
- **Pressure:** Oleadas continuas.
- **Evolution (Raro):** Inmunidad solar global + Sistema de 3 vidas ("Hardcore temporal").

### 2.4 Persistencia
- **NBT de Jugador:** `wa_playtime_ticks`, `wa_is_time_synced`, `WA_EvolutionLives`.
- **WorldSavedData:** `wa_evolution_event` (Estado global del evento).

## 3. Principios de Diseño
- **Compatibilidad:** Uso de eventos de Forge estándar.
- **Rendimiento:** Checks cada 100-200 ticks. No escaneo masivo.
- **Modularidad:** Cada sistema (Variantes, Eventos, Director) es independiente.
