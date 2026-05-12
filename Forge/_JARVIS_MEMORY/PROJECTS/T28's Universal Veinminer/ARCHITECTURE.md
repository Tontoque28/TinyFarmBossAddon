# ARCHITECTURE.md
# Arquitectura del Mod: T28's Universal Veinminer

## Visión General
Este mod proporciona una implementación universal de la mecánica "Veinminer" para Minecraft Forge 1.20.1. Permite a los jugadores minar un tipo de bloque conectado de forma masiva al romper uno solo, respetando las mecánicas vanilla.

## Componentes Clave

### 1. Configuración (`VeinminerConfig.java`)
-   **Propósito:** Gestionar las opciones configurables por el usuario.
-   **Características:**
    -   `VEINMINER_ENABLED`: Interruptor global (tecla 'V' por defecto).
    -   `MAX_BLOCKS_MINED`: Límite estricto de bloques por acción (64 por defecto).
    -   `CUSTOM_VEINMINABLE_BLOCKS`: Lista de IDs adicionales que el mod tratará como minerales.

### 2. Manejo de Eventos (`VeinminerEvents.java`)
-   **Propósito:** Interceptar el evento `BlockEvent.BreakEvent`.
-   **Lógica de Activación:**
    -   Jugador debe estar agachado (`isCrouching()`).
    -   Jugador debe tener la herramienta correcta (`isCorrectToolForDrops()`).
    -   Bloque debe ser Ore (Tag Forge), Wood (Tag Forge) o estar en la lista `CUSTOM_VEINMINABLE_BLOCKS`.

### 3. Lógica de Veinminer (`VeinminerLogic.java`)
-   **Propósito:** Búsqueda y destrucción de bloques conectados.
-   **Algoritmo (BFS 3D):**
    -   Usa una Cola (`Queue`) para buscar en anchura y un Set (`HashSet`) para evitar bucles.
    -   Revisa bloques adyacentes del *mismo tipo*.
-   **Integración Vanilla:**
    -   **Loot:** Usa `Block.getDrops(..., tool)` y `Block.popResource` para respetar Fortuna y Toque de Seda.
    -   **Experiencia:** Usa `ForgeHooks.getExperienceDrop(...)` y `block.popExperience` (respeta Fortuna).
    -   **Herramienta:** Daña la herramienta por cada bloque (`tool.hurtAndBreak`), respetando Irrompibilidad. Se detiene si la durabilidad es < 1.
    -   **Agotamiento:** Aplica `causeFoodExhaustion` por bloque.

### 4. Eventos de Cliente (`ClientEvents.java`, `KeyBindings.java`)
-   **Propósito:** Interfaz de usuario para activar/desactivar.
-   **Implementación:**
    -   Asignación de tecla (`KeyMapping`).
    -   Evento `ClientTickEvent` (Bus FORGE) para consumir el click y enviar mensaje al Action Bar.
    -   Evento `RegisterKeyMappingsEvent` (Bus MOD) para registro temprano.

### 5. Comandos (`ModCommands.java`)
-   **Propósito:** Configuración in-game de la lista personalizada.
-   **Comandos:** `/universalveinminer [addblock|removeblock|listblocks] <block_id>`.
-   **Permisos:** Nivel 2 (Op/Game Master).

## Dependencias
-   Minecraft Forge 1.20.1
