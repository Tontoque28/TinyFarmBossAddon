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

====================================================

## Versión 1.0.1 (Release)
Fecha: 27/10/2023
Cambios técnicos:
- Sistema de dificultad progresiva basado en tiempo.
- Lógica de Dificultad: (Tiempo Mundo + Offset Global + Tiempo Jugador) / 24000 * 0.015.
- Incremento: 1.5% de dificultad por día de juego (24,000 ticks).
- Overcharge: Mecánica especial donde si hay múltiples jugadores cerca de un mob "elegido" (probabilidad 1/15), se suman sus tiempos para un buff masivo.
- Filtros de Entidad: Excluidos Animales, Peces, Aldeanos, Vendedores Ambulantes, Creepers.
- Incluidos: Monstruos (Enemy), Golems (AbstractGolem), Neutrales con Daño > 0.
- Multiplicadores: Bosses (Wither, Dragon, Warden): +20% (Factor * 1.20). Neutrales (Golems, etc.): 50% (Factor * 0.5).
- Persistencia: DifficultySavedData guarda el tiempo global adicional del mundo. Player NBT (ptd_player_time) guarda el tiempo jugado por cada jugador individualmente.
- Comandos (/progresstd): Gestión Global, Gestión Jugador, Debug.
- Herramientas: Debug Stick, Dev Mode.

## Versión 1.1.0 (Feature & Bugfix Update)
Fecha: 23/02/2026
Cambios técnicos:
- Dificultad Dinámica: El mod ahora lee la dificultad del mundo (Easy/Normal/Hard) para escalar.
  - Easy: 0.5% por día (Daño x0).
  - Normal: 0.75% por día (Daño x0.5).
  - Hard: 1.0% por día (Daño x1.0).
- Modo Hardcore: Activado manualmente (/progresstd hardcore true). Fija el incremento en 1.5% por día y multiplica el daño por 1.5. Tiene prioridad sobre la dificultad del mundo.
- Rebalanceo de Atributos: Vida: Factor * 1.2 (+20% base). Armadura: Vida Extra / 8.0 (Más armadura).
- Persistencia de Configuración: DEV_MODE y Hardcore Mode ahora se guardan en DifficultySavedData.java, persistiendo entre reinicios del mundo.
- Se implementó un listener LevelEvent.Load para asegurar que estos valores se carguen y sincronicen al iniciar el mundo.
- Detección de Jefes Mejorada: Se ha añadido la detección de la etiqueta forge:bosses para dar soporte automático a jefes de otros mods.
- Se ha incluido explícitamente a ElderGuardian y IronGolem en la categoría de jefes.
- Balanceo de Iron Golem: Se aplica un multiplicador de dificultad adicional de x2 exclusivamente al IronGolem para que sus estadísticas sean más acordes a las de un mini-jefe.
- Manejo de Cambio de Dificultad: Se ha refactorizado la lógica para que se ejecute una sola vez desde el Overworld, evitando mensajes triplicados.
- Al cambiar la dificultad del juego (ej. de Hard a Easy), las entidades se actualizan inmediatamente para reflejar los nuevos cálculos.
- Calidad de Código y Corrección de Bugs: Se ha corregido un bug crítico que impedía que el IronGolem recibiera buffs.
- Se han añadido comprobaciones de nulidad para evitar NullPointerException al obtener nombres de entidades.
- Se han modernizado las sentencias switch a la sintaxis "enhanced" de Java.
- Se han eliminado import no utilizados y corregido advertencias del compilador.

## Versión 1.1.1 (Hardcore Balance Update)
Fecha: 23/02/2026
Cambios técnicos:
- Ajuste de balanceo exclusivo para Modo Hardcore.
- Daño: Multiplicador aumentado de x2.0 a x2.5 (factor * 2.5).
- Resistencia al Empuje (Knockback Resistance): Multiplicador aumentado de x0.1 a x0.15 (factor * 0.15).
- Objetivo: Hacer el modo Hardcore significativamente más letal y resistente en etapas avanzadas.

## Versión 1.1.2 (Environmental Damage Update)
Fecha: 23/02/2026
Cambios técnicos:
- Implementación de escalado de daño ambiental.
- Evento: LivingHurtEvent intercepta daños a jugadores.
- Lista Blanca: Caída, Ahogamiento, Fuego, Lava, Explosiones, Magia, etc.
- Fórmula: DañoBase * (1.0 + FactorDificultad).
- Hardcore Bonus: +50% extra al factor de escalado ambiental.
- Objetivo: Hacer que el mundo sea tan peligroso como los mobs.

## Versión 1.1.3 (Experience Scaling Update)
Fecha: 23/02/2026
Cambios técnicos:
- Implementación de escalado de experiencia.
- Evento: LivingExperienceDropEvent intercepta la experiencia soltada por mobs.
- Condición: Solo se aplica si el mob es asesinado por un jugador.
- Fórmula: `XP_Original * (1.0 + FactorDificultad)`.
- Objetivo: Recompensar al jugador por derrotar enemigos más fuertes.

## Versión 1.1.4 (Attribute Rebalance & Armor Fix)
Fecha: 23/02/2026
Cambios técnicos:
- Resistencia al Empuje: Reducida a la mitad en todos los modos y eliminada para los jefes.
- Daño de Jefes: Aumentado en un 30% en todos los modos, con un 5% extra en Hardcore.
- Daño Híbrido: Implementado límite de daño físico (x5.0). El exceso se aplica como daño mágico atenuado (50%) para proteger la durabilidad de la armadura.
- Regla de Seguridad: Mobs con > 200 HP (buffed) no reciben armadura ni resistencia extra.
- Refuerzo de Mobs Débiles: Mobs con <= 20 HP base reciben un bono extra de vida y armadura.
- Botellas de XP: Multiplicador aumentado a x5.0.
- Refactorización: Creada clase `BossRegistry.java` para manejar la detección de jefes.
- Mod Support: Añadida lista blanca de jefes para Mowzie's Mobs, Cataclysm, Twilight Forest, Ice and Fire, Aquamirae, BOMD, Stalwart Dungeons, Rotten Creatures, etc.
- Debug: Añadido ID de entidad al palo de debug.

## Versión 1.1.5 (Creeper Exclusion & General Nerf)
Fecha: 02/03/2026
Cambios técnicos:
- Exclusión explícita de Creepers: Se ha modificado `DifficultyHandler.java` para asegurar que los Creepers (y cualquier entidad en la BLACKLIST) no reciban ningún buff.
- Limpieza de Buffs: Si un Creeper tenía buffs previos, se eliminan automáticamente.
- Nerf General:
  - Vida: Multiplicador reducido de x1.2 a x1.0.
  - Daño Hardcore: Multiplicador reducido de x2.5 a x2.0.
  - Armadura: Reducida la ganancia de armadura (Divisor aumentado de 8.0 a 10.0).
- Objetivo: Evitar conflictos con mods de Creepers y suavizar la curva de dificultad general.

## Versión 1.1.6 (Visual Threat Indicators & Rebalance)
Fecha: 07/03/2026
Cambios técnicos:
- **Indicadores Visuales de Amenaza:**
  - Implementado sistema de partículas en `DifficultyHandler.java` mediante `LivingEvent.LivingTickEvent`.
  - Los mobs emiten partículas según su nivel de dificultad (Factor):
    - **Tier 1 (0.5 - 1.0):** Humo (`SMOKE`).
    - **Tier 2 (1.0 - 2.0):** Fuego (`FLAME`).
    - **Tier 3 (> 2.0):** Fuego de Alma (`SOUL_FIRE_FLAME`).
  - Se guarda el `difficultyFactor` en el NBT de la entidad para optimizar el rendimiento.
- **Rebalanceo Hardcore:**
  - **Tasa de Incremento:** Reducida de 1.5% a 1.2% por día.
  - **Daño:** Restaurado a x2.5 (se había bajado a x2.0 en v1.1.5).
  - **Vida:** Se mantiene la reducción a x1.0.
  - **Armadura:** Se mantiene la reducción a x1.0 con divisor 10.0.
- **Corrección de Build:** Se añadió `forge_version_range` a `gradle.properties` para solucionar error de compilación.
