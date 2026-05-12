# CHANGELOG_INTERNAL.md
# Historial de Cambios Internos y Públicos para Better Survival: World Awakening

Este documento registra todos los cambios, adiciones y correcciones realizadas durante el desarrollo del mod. También contendrá el texto listo para ser publicado como changelog público.

---

## [Sesión de Desarrollo - Fecha Actual] - Implementación Core & Expansión L4D2

### Core Systems (Director AI)
- **DirectorManager:** Implementado orquestador central. Gestiona `ThreatLevel`, `EventScheduler` y `WorldProgressTracker` por jugador.
- **ThreatLevelCalculator:** Algoritmo de cálculo de amenaza basado en tiempo, equipo, bioma y dimensión.
- **WorldProgressTracker:** Sistema de persistencia (NBT) para rastrear tiempo de juego real, muertes y kills de bosses.
- **AwakeningStageManager:** Nueva capa de dificultad global (Dormant -> Relentless) basada en progreso a largo plazo.

### Event System
- **Ambush Event:** Spawnea mobs detrás del jugador. Lógica mejorada para variar mobs según bioma y amenaza (Husks, Strays, Witches, etc.).
- **Pressure Event:** Evento de duración media que genera oleadas constantes de mobs.
- **Elite Spawn Event:** Spawnea mobs con atributos mejorados y rasgos especiales.
- **Evolution Event (Global):** Evento raro (2% chance al amanecer, cada 7 días). Activa inmunidad solar para mobs y sistema de 3 vidas para el jugador.

### AI & Mobs (Behavior)
- **Zombie Variants:** Sistema de variantes dinámicas sin nuevas entidades (Render scaling + Partículas).
  - *Runner:* Rápido, poca vida. Partículas de humo.
  - *Brute:* Lento, tanque, mucho knockback. Partículas críticas.
  - *Stalker:* Velocidad media. Partículas verdes.
  - *Corrupted:* Daño alto, velocidad explosiva si jugador < 50% HP. Partículas moradas.
- **Elite Traits:**
  - *Explosion on Death:* Explota al morir.
  - *Lifesteal:* Se cura al atacar.
  - *Fire Aspect:* Quema al jugador.
  - *Web Trap (Spider):* Atrapa al jugador en telarañas.
  - *Horde Caller (Skeleton):* Lanza cohetes y activa eventos de horda.
  - *Punisher (Creeper):* Aplica ceguera/náuseas en lugar de romper bloques.

### Visuals & UX
- **Action Bar HUD:** Modo desarrollador muestra Threat Level, Tiempo y Bioma en tiempo real.
- **Partículas:** Indicadores visuales para variantes de zombies.
- **Inmunidad Solar:** Mobs de evento equipados con cascos o inmunidad lógica durante Evolution Event.

### Commands
- `/wa status`: Ver estadísticas.
- `/wa setthreat <0-100>`: Forzar nivel de amenaza.
- `/wa trigger <event>`: Forzar eventos (ambush, pressure, elite, evolution).
- `/wa dev`: Activar HUD de debug.
- `/wa testvariants`: Spawnear variantes de prueba.
- `/wa synctime`: Sincronizar tiempo manualmente.

---

## Versión 1.0.0 (Fecha: [Fecha de Lanzamiento])

### Cambios Internos:
- Inicialización del proyecto "Better Survival: World Awakening".
- Definición de la arquitectura y estructura de paquetes.
- Configuración inicial de los archivos de memoria del proyecto.

### Changelog Público (para CurseForge/Modrinth):
- **Better Survival: World Awakening - Initial Release**
  - ¡Bienvenido a una nueva experiencia de supervivencia! Este mod introduce un sistema de Director de IA dinámico que adapta el mundo a tu progresión.
  - **Características Principales:**
    - **Nivel de Amenaza Dinámico:** El mundo reacciona a tu fuerza y progreso, haciendo que cada aventura sea única.
    - **Eventos Hostiles Adaptativos:** Prepárate para emboscadas, presión de mobs y encuentros con élites mejoradas.
    - **Rendimiento Optimizado:** Diseñado para ser ligero y compatible con tus mods favoritos.
    - **Configurable:** Personaliza la experiencia a tu gusto con amplias opciones de configuración.
  - ¡Explora, sobrevive y adáptate! El mundo está despertando.

---
