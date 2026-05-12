# PROJECT_INFO.md

Nombre: Progressive Time Difficulty (PTD)
Estado: Activo
Última modificación: 07/03/2026

Descripción breve:
Mod de dificultad progresiva basada en tiempo. Aumenta la dificultad de los mobs hostiles cada día que pasa.

Objetivo principal:
Proporcionar un desafío creciente en servidores multijugador, adaptándose tanto a la dificultad del mundo como al tiempo de juego individual de cada jugador.

Regla:
Solo añadir nueva información al final.
No sobrescribir historial.

====================================================

## Versión Actual: 1.1.0
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.0)
1. Dificultad Dinámica: Escala según Easy (0.5%), Normal (0.75%) o Hard (1.0%).
2. Modo Hardcore: Activación manual (1.5% fijo + 50% daño extra).
3. Dificultad Personal: Cada jugador tiene su propio contador de tiempo.
4. Overcharge: Mecánica multijugador donde los mobs acumulan dificultad de varios jugadores cercanos.
5. Boss Support: Soporte nativo para Wither, Dragon, Warden, Elder Guardian, Iron Golem y tag `forge:bosses`.
6. Persistencia: Configuración de Dev Mode y Hardcore Mode guardada en NBT del mundo.

## Versión Actual: 1.1.1
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.1)
1. Rebalanceo Hardcore:
   - Daño: Multiplicador aumentado de x1.5 a x2.0.
   - Knockback Resistance: Multiplicador aumentado de x0.1 a x0.15.
   - Objetivo: Mayor letalidad y resistencia en el modo más difícil.

## Versión Actual: 1.1.2
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.2)
1. Daño Ambiental Escalable:
   - El entorno ahora escala con la dificultad del mundo.
   - Afecta: Caída, Fuego, Lava, Ahogamiento, Explosiones, Magia, etc.
   - Fórmula: DañoBase * (1.0 + FactorDificultad).
   - Bonus Hardcore: +50% extra al factor de escalado ambiental.

## Versión Actual: 1.1.3
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.3)
1. Experiencia Escalable:
   - Los mobs ahora sueltan más experiencia a medida que aumenta su dificultad.
   - Fórmula: `XP_Original * (1.0 + FactorDificultad)`.
   - Objetivo: Recompensar al jugador por derrotar enemigos más fuertes.

## Versión Actual: 1.1.4
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.4)
1. Rebalanceo de Atributos:
   - Resistencia al Empuje: Reducida a la mitad en todos los modos y eliminada para los jefes.
   - Daño de Jefes: Aumentado en un 30% en todos los modos, con un 5% extra en Hardcore.
2. Protección de Armaduras:
   - Daño Híbrido: El daño físico se limita a x5.0. El exceso se aplica como daño mágico (50% atenuado) para no romper armaduras.
3. Reglas de Seguridad:
   - Mobs con > 200 HP no reciben armadura extra.
   - Mobs con <= 20 HP reciben un bono extra de supervivencia.
4. Botellas de XP:
   - Multiplicador aumentado a x5.0.
5. Soporte de Mods:
   - Detección explícita de jefes para Mowzie's Mobs, Cataclysm, Twilight Forest, Ice and Fire, Aquamirae, BOMD, Stalwart Dungeons, Rotten Creatures, etc.
   - Nueva clase `BossRegistry` para gestión centralizada.

## Versión Actual: 1.1.5
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.5)
1. Exclusión de Creepers:
   - Los Creepers ahora están explícitamente excluidos de cualquier buff de dificultad.
   - Se eliminan buffs previos si existían.
   - Objetivo: Evitar conflictos con mods de explosiones (Creeper Insta Boom) y mantener el balance.
2. Nerf General:
   - Vida: Multiplicador reducido de x1.2 a x1.0.
   - Daño Hardcore: Multiplicador reducido de x2.5 a x2.0.
   - Armadura: Reducida la ganancia de armadura (Divisor aumentado de 8.0 a 10.0).
   - Objetivo: Suavizar la curva de dificultad.

## Versión Actual: 1.1.6
- Minecraft: 1.20.1
- Forge: 47.x
- Dependencias: Ninguna externa obligatoria.

## Características Clave (v1.1.6)
1. Indicadores Visuales de Amenaza (Tier System):
   - Los mobs ahora emiten partículas según su nivel de dificultad acumulado.
   - Tier 1 (Factor 0.5 - 1.0): Humo (Endurecido).
   - Tier 2 (Factor 1.0 - 2.0): Fuego (Peligroso).
   - Tier 3 (Factor > 2.0): Fuego de Alma (Amenaza Extrema).
   - Objetivo: Permitir al jugador identificar visualmente el peligro antes de combatir.
2. Rebalanceo Hardcore:
   - Tasa de Incremento: Reducida de 1.5% a 1.2% por día.
   - Daño: Restaurado el multiplicador de daño a x2.5 (en v1.1.5 se bajó a x2.0).
   - Vida y Armadura: Se mantienen los nerfs de la v1.1.5 (x1.0 vida, /10.0 armadura).
