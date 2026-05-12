# BSWA_Context_Mechanics.md
# Mecánicas Detalladas: Better Survival: World Awakening

## 1. El Cálculo de Amenaza (Threat Level)
El `ThreatLevel` es un valor dinámico (0.0 a 100.0) que cambia constantemente.

**Fórmula Simplificada:**
`Threat = (TiempoSobrevivido * Factor) + (ValorArmadura * Factor) + (BossKills * Bono) + (FactorBioma)`

- **Decaimiento:** Si el jugador muere, el ThreatLevel baja drásticamente (sistema de piedad).
- **Picos:** Entrar al Nether o End multiplica el ThreatLevel.
- **Modificador Pasivo:** Si el jugador tiene un `Fragment of Evolution` en el inventario, la ganancia de amenaza se reduce un 5%.

## 2. Awakening Stages (La Evolución del Mundo)
El mundo tiene 5 etapas de "Despertar". Esto es persistente y global (o por jugador).

| Etapa | Nombre | Requisitos Aprox. | Efectos |
| :--- | :--- | :--- | :--- |
| 0 | **Dormant** | Inicio | Minecraft Vanilla. |
| 1 | **Aware** | 2h juego, Threat > 20 | Aparecen variantes "Runner". |
| 2 | **Hostile** | 10h juego, 1 Boss | Aparecen "Brute" y "Stalker". Evento Evolution desbloqueado. |
| 3 | **Aggressive** | 25h juego, 3 Bosses | Aparecen "Corrupted". Frecuencia de eventos alta. Evento "The World Remembers". |
| 4 | **Relentless** | 50h juego, 5 Bosses | Máxima dificultad. Variantes complejas comunes. |

## 3. Sistema de Variantes de Zombies
No son entidades nuevas. Son `minecraft:zombie` con etiquetas NBT y atributos modificados. Se distinguen visualmente por escala y partículas.

- **RUNNER:**
  - *Visual:* Escala 0.85x, Partículas de humo (Cloud).
  - *Stats:* +25% Velocidad, -10% Daño.
  - *AI:* Sprinta si está muy cerca.
- **BRUTE:**
  - *Visual:* Escala 1.25x, Partículas críticas (Crit).
  - *Stats:* +40% Vida, +50% Resistencia Knockback, -10% Velocidad.
  - *AI:* Empuje fuerte al golpear.
- **STALKER:**
  - *Visual:* Escala 1.0x, Partículas verdes (Happy Villager).
  - *Stats:* +10% Velocidad.
  - *AI:* (Planeado) Flanqueo.
- **CORRUPTED:**
  - *Visual:* Escala 1.1x, Partículas moradas (Dragon Breath).
  - *Stats:* +20% Vida, +15% Daño.
  - *AI:* Frenesí de velocidad si el jugador tiene < 50% de vida.

## 4. Rasgos de Élite (Elite Traits)
Cualquier mob hostil puede nacer como "Élite" con uno de estos rasgos:

- **EXPLOSION_ON_DEATH:** Explota al morir (Daño físico).
- **LIFESTEAL:** Se cura el 50% del daño que inflige.
- **FIRE_ASPECT:** Prende fuego al jugador por 5s.
- **WEB_TRAP (Arañas):** Si golpea, coloca una `cobweb` en los pies del jugador.
- **HORDE_CALLER (Esqueletos):** Si ve al jugador, lanza un cohete y activa un evento de "Horda" (Pressure Event). Lleva un estandarte rojo.
- **PUNISHER (Creepers):** Explosión silenciosa que aplica Ceguera, Náuseas y Debilidad en lugar de daño masivo.
- **COORDINATED_BURST (Zombies):** Al detectar al jugador, da velocidad a los zombies cercanos.

## 5. Eventos Especiales

### Evolution Event (El "Día del Juicio")
- **Trigger:** Raro (2%), solo al amanecer, solo si Stage >= 2.
- **Duración:** Todo el día minecraftiano.
- **Atmósfera:** Cielo rojizo/gris, niebla, sonidos ambientales graves.
- **Reglas:**
  1. Mobs hostiles **NO se queman con el sol**.
  2. Spawn rate aumentado drásticamente.
  3. **Sistema de 3 Vidas:** Si mueres, revives instantáneamente en spawn sin perder inventario, pero pierdes una "Vida de Evento".
  4. Si vidas llegan a 0: El evento termina en derrota.
  5. Si sobrevives al anochecer: Victoria (recompensa: Fragment of Evolution).

### Psychological Events (Terror Sutil)
Si el ThreatLevel es muy alto (>80), el Director intenta asustar al jugador sin peligro real:
- Sonidos de pasos o respiración justo detrás del jugador.
- Partículas de humo en el borde de la visión.
- Sonido de latido de corazón.

## 6. Sistema de Mutaciones Progresivas
Cada 3 victorias en el evento Evolution, el mundo adquiere una mutación permanente (máximo 4 activas).
- **SWIFT_UNDEAD:** +5% velocidad zombies.
- **EAGLE_EYE:** +10% rango detección esqueletos.
- **HORDE_MENTALITY:** +5% spawn rate en hordas.
- **LETHAL_VARIANTS:** +5% daño variantes.
- **LEAPING_SPIDERS:** +10% salto arañas.

## 7. Ítem: Fragment of Evolution
- **Tipo:** Ítem raro (Epic).
- **Visual:** Textura de fragmento oscuro con brillo de encantamiento y partículas de Dragon Breath al sostenerlo.
- **Uso:** Click derecho consume carga para "calmar" al Director (reduce amenaza/spawn temporalmente).
- **Carga:** Se recarga matando mobs de élite.
