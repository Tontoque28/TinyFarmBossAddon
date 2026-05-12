PROYECTO ACTIVO: Tiny Mob Farm Addon: Bosses
VERSIÓN: Minecraft 1.20.1 (Forge)
PAQUETE BASE: com.tontoque28.tinymobfarmbosses

OBJETIVO:
Implementar compatibilidad avanzada y segura con mobs de otros mods (MineColonies, Ice and Fire, Vanilla Slime), evitando crashes durante el proceso de farming dentro del Tiny Mob Farm.

====================================================
FUENTES DE REFERENCIA (OBLIGATORIO ANALIZAR)
====================================================

MineColonies:
https://github.com/ldtteam/MineColonies

Ice and Fire:
https://github.com/AlexModGuy/Ice_and_Fire

Tiny Mob Farm (referencia conceptual):
https://www.curseforge.com/minecraft/mc-mods/tiny-mob-farm

====================================================
PROBLEMA PRINCIPAL A SOLUCIONAR
====================================================

Crash al usar mobs de MineColonies (ej: Barbarian Chief) dentro del Tiny Mob Farm.

CAUSA PROBABLE:
- Mobs con lógica compleja (IA, inventories, custom drops, eventos internos)
- Drops que dependen de contexto (ej: ancienttome)
- Acceso a datos NBT no válidos en entorno simulado
- Código del mob ejecutándose fuera de su entorno natural

====================================================
SOLUCIÓN GLOBAL (OBLIGATORIA)
====================================================

Implementar un SISTEMA DE SIMULACIÓN SEGURA DE DROPS:

1. NUNCA ejecutar lógica interna completa del mob
2. NO instanciar comportamiento completo de IA
3. Simular drops mediante:
   - LootTables
   - Drops controlados
   - Overrides seguros

====================================================
SISTEMA: SAFE MOB HANDLER
====================================================

Crear sistema central:

Clase: SafeMobSimulationHandler

Responsabilidades:
- Detectar si el mob es de un mod externo
- Cancelar ejecución de lógica peligrosa
- Obtener drops de forma segura
- Manejar excepciones sin crashear el servidor

Lógica:

IF mob pertenece a MineColonies:
    → usar método SAFE_MINECOLONIES_DROP

IF mob pertenece a Ice and Fire:
    → usar método SAFE_ICE_AND_FIRE_DROP

IF mob es Vanilla Slime:
    → usar método SLIME_SPLIT_SIMULATION

ELSE:
    → usar método estándar

====================================================
MINECOLONIES FIX (CRÍTICO)
====================================================

Problema:
Mobs como Barbarian Chief tienen lógica compleja y causan crash.

SOLUCIÓN:

- NO ejecutar:
  - AI goals
  - Inventory interno
  - Eventos del mod

- Crear sistema de drops manual:

SAFE_MINECOLONIES_DROP:
- Detectar tipo de barbarian
- Generar loot simplificado:
  - random vanilla loot
  - posibilidad de:
    - iron gear
    - gold
    - chance MUY BAJA de items especiales

- EXCLUIR:
  - minecolonies:ancienttome (puede romper lógica)

- Implementar try/catch GLOBAL:
  Nunca permitir crash → fallback a loot vacío

====================================================
ICE AND FIRE SOPORTE
====================================================

Objetivo:
Permitir capturar dragones SIN ejecutar lógica pesada

Restricciones:
- NO pathfinding
- NO crecimiento
- NO ataques especiales

SAFE_ICE_AND_FIRE_DROP:

Si es dragón:
- Drop:
  - dragon scales
  - bones
  - skull (baja probabilidad)

- Basado en:
  edad simulada (random)
  tipo (fire/ice/lightning)

====================================================
SLIME SOPORTE (IMPORTANTE)
====================================================

Problema:
Slime depende de split mechanics

SOLUCIÓN:

SLIME_SPLIT_SIMULATION:

- Detectar tamaño del slime
- Simular división:

size 4 → genera 2-4 slimes size 2
size 2 → genera 2-4 slimes size 1
size 1 → dropea slimeballs

- NO crear entidades reales
- SOLO calcular resultado matemático

Drop final:
- slimeballs según tamaño original

====================================================
SISTEMA ANTI-CRASH GLOBAL
====================================================

OBLIGATORIO:

Todo el sistema debe estar envuelto en:

try {
   lógica
} catch (Exception e) {
   loggear error
   devolver loot vacío
}

Nunca permitir:
- crash del servidor
- crash del cliente

====================================================
LOGGING (DEBUG MODE)
====================================================

Agregar modo debug:

- Loggear:
  - mob procesado
  - mod de origen
  - método aplicado
  - errores capturados

Formato:
[TinyMobFarmAddon] Handling mob: X via SAFE_HANDLER

====================================================
COMPATIBILIDAD
====================================================

NO romper:
- mobs vanilla
- otros mods existentes
- sistema base de Tiny Mob Farm

TODO debe ser:
- modular
- extensible

====================================================
RESULTADO FINAL ESPERADO
====================================================

✔ MineColonies mobs NO crashean
✔ Ice and Fire mobs funcionan sin lag
✔ Slimes funcionan correctamente
✔ Sistema robusto contra cualquier mod externo
✔ Experiencia fluida para el jugador

====================================================
REGLA FINAL
====================================================

PRIORIDAD MÁXIMA:
ESTABILIDAD > REALISMO

Si algo puede causar crash:
→ eliminar esa lógica
→ reemplazar por simulación segura