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

## Arquitectura v1.0.3-Hard (23/02/2026)

### 1. Main Class
- **Archivo:** `CreeperInstaBoomHard.java`
- **Función:** Punto de entrada.
- **Componentes:**
  - `MODID`: "creeperinstaboomhard".
  - `onRegisterCommands`: Registra `AlphaCreeperCommand`.

### 2. Lógica Principal (Handlers)
- **Archivo:** `CreeperModifier.java`
- **Función:** Modificación de IA y estadísticas de Creepers.
- **Eventos Clave:**
  - `onSpawn`:
    - Elimina `SwellGoal` (IA de detención) para evitar que el Creeper se detenga a cargar.
    - Aplica probabilidad de Charged (10% día / 35% noche).
    - Aplica probabilidad de Alpha (1-7% según dificultad).
    - Ajusta velocidad (hasta x2.25) y rango de visión (64 bloques).
  - `onCreeperTick`:
    - Detecta jugadores en radio de 2.0 bloques.
    - Si encuentra uno, fuerza explosión inmediata (`explodeNow`).
  - `explodeNow`:
    - Calcula potencia (6.0 a 24.0).
    - Aplica daño penetrante (0-60%) como daño mágico.
    - Ignora fuego (`false`) para evitar destrucción masiva de terreno.
    - Aplica efecto `WEAKNESS` a jugadores afectados.

### 3. Comandos
- **Archivo:** `AlphaCreeperCommand.java`
- **Función:** Gestión administrativa.
- **Comandos:**
  - `/alphacreeper`: Spawnea un Alpha Creeper Charged manualmente.

### 4. Fórmulas de Dificultad (v1.0.3-Hard)
- **Velocidad:**
  - Base: 0.30
  - Normal: x1.5
  - Charged: x1.75
  - Alpha: x2.0
  - Alpha Charged: x2.25
- **Potencia de Explosión:**
  - Normal: 6.0
  - Charged: 12.0
  - Alpha: 18.0
  - Alpha Charged: 24.0
- **Daño Penetrante (Magic Damage):**
  - Normal: 0%
  - Charged: 20%
  - Alpha: 40%
  - Alpha Charged: 60%
