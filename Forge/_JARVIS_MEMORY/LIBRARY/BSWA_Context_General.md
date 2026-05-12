# BSWA_Context_General.md
# Contexto del Proyecto: Better Survival: World Awakening (BSWA)

## 1. Identidad del Proyecto
- **Nombre:** Better Survival: World Awakening
- **ModID:** `worldawakening`
- **Loader:** Minecraft Forge
- **Versión MC:** 1.20.1
- **Java:** 17
- **Paquete Base:** `com.tontoque28.worldawakening`

## 2. Concepto Central (The "Elevator Pitch")
BSWA no es un mod de dificultad estática. Es un **Sistema de Director de IA** (inspirado en *Left 4 Dead 2*) que monitorea al jugador en tiempo real y ajusta el mundo dinámicamente.
El objetivo es romper la monotonía de Minecraft: si el jugador está cómodo, el Director sube la presión. Si el jugador está muriendo, el Director da un respiro.
El mod añade una capa de "progresión invisible" donde el mundo mismo "despierta" y evoluciona (Awakening Stages) a medida que el jugador avanza.

## 3. Filosofía de Diseño (Reglas de Oro)
1.  **Compatibilidad Máxima:**
    - NO crear nuevas entidades (EntityTypes) a menos que sea estrictamente necesario.
    - Usar entidades Vanilla (Zombie, Skeleton, Creeper) y modificarlas en tiempo de ejecución (Atributos, NBT, AI Tasks).
    - Esto asegura compatibilidad con otros mods, resource packs y shaders.

2.  **Rendimiento:**
    - NO escanear chunks globales constantemente.
    - La lógica se centra en el jugador (`DirectorManager` por jugador).
    - Chequeos de lógica cada 100-200 ticks (5-10 segundos), no cada tick.

3.  **Persistencia:**
    - El progreso no se pierde al desconectarse.
    - Se usa `PersistentData` (NBT) en el jugador y `SavedData` en el nivel para guardar estados.

## 4. Arquitectura Técnica

### Core (El Cerebro)
- **`DirectorManager`:** La clase maestra. Se suscribe a `PlayerTickEvent`. Coordina los tres subsistemas principales para cada jugador.
- **`ThreatLevelCalculator`:** Calcula un número flotante (0-100) que representa qué tan peligroso debe ser el juego ahora mismo.
- **`WorldProgressTracker`:** Almacena datos a largo plazo (tiempo jugado real, muertes, kills de bosses).
- **`AwakeningStageManager`:** Determina la "Era" global del mundo (Stage 0 a 4).
- **`EvolutionEventData`:** `SavedData` global para manejar el estado del evento Evolution y las mutaciones del mundo.

### AI & Modificación (Los Músculos)
- **`SpawnController`:** Intercepta o fuerza spawns. Decide qué mob aparece y dónde (lógica de emboscada detrás del jugador).
- **`MobEnhancer`:** Aplica atributos (Vida, Daño, Velocidad) y Rasgos (Traits) a los mobs existentes.
- **`ZombieVariantManager`:** Sistema especial que convierte Zombies normales en variantes (Runner, Brute, etc.) usando NBT y escalado visual.

### Event System (La Acción)
- **`EventScheduler`:** Gestiona los cooldowns y la probabilidad de disparar eventos.
- **Tipos de Eventos:**
    - *Ambush:* Spawn instantáneo y cercano.
    - *Pressure:* Aumento de spawn rate temporal.
    - *Elite Spawn:* Un solo enemigo muy fuerte.
    - *Evolution:* Evento global raro (Hardcore temporal, 3 vidas, inmunidad solar).
    - *Psychological:* Sonidos y partículas para generar tensión sin peligro real.

### Items
- **`Fragment of Evolution`:** Ítem raro dropeado al ganar el evento Evolution. Permite "calmar" al Director y reduce la ganancia de amenaza pasivamente.

## 5. Comandos de Depuración
El mod incluye herramientas para probar sin esperar horas:
- `/wa status`: Ver nivel de amenaza y estadísticas.
- `/wa setthreat <0-100>`: Forzar la dificultad.
- `/wa trigger <evento>`: Forzar un evento específico (incluyendo `evolution`).
- `/wa dev`: Activar HUD en pantalla con datos en tiempo real y visualizar variantes.
- `/wa testvariants`: Spawnear una muestra de cada variante de zombie.
- `/wa synctime`: Sincronizar tiempo manualmente.
- `/wa addMutation <id>` / `/wa clearMutations`: Gestionar mutaciones globales.
