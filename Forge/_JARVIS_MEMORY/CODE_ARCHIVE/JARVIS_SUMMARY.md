# JARVIS_SUMMARY.md - Historial de Cambios y Publicaciones

Este archivo contiene exclusivamente informes de cambios, changelogs para plataformas (CurseForge/Modrinth) y resúmenes de versiones. No contiene código ni instrucciones de memoria.

---

## 📦 Versión: 1.1.0 (Feature & Bugfix Update)
**Fecha:** 23/02/2026
**Minecraft:** 1.20.1
**Loader:** Forge

### 📝 Resumen Técnico (Informe Interno)
*   **Dificultad Dinámica:** El mod ahora lee la dificultad del mundo (Easy/Normal/Hard) para escalar.
    *   Easy: 0.5% por día (Daño x0).
    *   Normal: 0.75% por día (Daño x0.5).
    *   Hard: 1.0% por día (Daño x1.0).
*   **Modo Hardcore:** Activado manualmente (`/progresstd hardcore true`). Fija el incremento en 1.5% por día y multiplica el daño por 1.5. Tiene prioridad sobre la dificultad del mundo.
*   **Rebalanceo de Atributos:**
    *   Vida: Factor * 1.2 (+20% base).
    *   Armadura: Vida Extra / 8.0 (Más armadura).
*   **Persistencia de Configuración:**
    *   `DEV_MODE` y `Hardcore Mode` ahora se guardan en `DifficultySavedData.java`, persistiendo entre reinicios del mundo.
    *   Se implementó un listener `LevelEvent.Load` para asegurar que estos valores se carguen y sincronicen al iniciar el mundo.
*   **Detección de Jefes Mejorada:**
    *   Se ha añadido la detección de la etiqueta `forge:bosses` para dar soporte automático a jefes de otros mods.
    *   Se ha incluido explícitamente a `ElderGuardian` y `IronGolem` en la categoría de jefes.
*   **Balanceo de Iron Golem:**
    *   Se aplica un multiplicador de dificultad adicional de **x2** exclusivamente al `IronGolem` para que sus estadísticas sean más acordes a las de un mini-jefe.
*   **Manejo de Cambio de Dificultad:**
    *   Se ha refactorizado la lógica para que se ejecute una sola vez desde el Overworld, evitando mensajes triplicados.
    *   Al cambiar la dificultad del juego (ej. de Hard a Easy), las entidades se actualizan inmediatamente para reflejar los nuevos cálculos.
*   **Calidad de Código y Corrección de Bugs:**
    *   Se ha corregido un bug crítico que impedía que el `IronGolem` recibiera buffs.
    *   Se han añadido comprobaciones de nulidad para evitar `NullPointerException` al obtener nombres de entidades.
    *   Se han modernizado las sentencias `switch` a la sintaxis "enhanced" de Java.
    *   Se han eliminado `import` no utilizados y corregido advertencias del compilador.

### 🌍 Changelog Público (CurseForge / Modrinth)
**Progressive Time Difficulty 1.1.0**

This is a major update focused on compatibility, bug fixes, and quality-of-life improvements!

**New Features:**
*   **Dynamic Difficulty:** The mod now respects your world difficulty!
    *   **Easy:** Mobs scale slower (0.5%/day) and don't gain extra damage.
    *   **Normal:** Balanced scaling (0.75%/day).
    *   **Hard:** Faster scaling (1.0%/day) and full damage buffs.
*   **Hardcore Mode:** Want a brutal challenge? Enable it with `/progresstd hardcore true`. This mode overrides the world difficulty with a fixed 1.5% scaling and +50% damage bonus.
*   **Modded Boss Support:** Mobs tagged as `forge:bosses` will now automatically receive the boss bonus! This adds out-of-the-box compatibility with many other mods.
*   **New "Bosses":** The `ElderGuardian` and `IronGolem` are now considered bosses and will receive the difficulty bonus.
*   **Iron Golem Rebalance:** Iron Golems now receive an **extra x2 difficulty multiplier** to make them the formidable guardians they are meant to be.

**Fixes & Improvements:**
*   **Settings Now Save:** Hardcore Mode and Dev Mode settings are now saved with the world and will persist after restarting.
*   **Difficulty Change Logic:**
    *   Changing the world difficulty (e.g., from Hard to Easy) now correctly updates all mob stats instantly.
    *   Fixed a bug that caused multiple "Difficulty Changed" messages to appear in chat.
*   **Critical Bug Fix:** Fixed a major bug that prevented Iron Golems from receiving any buffs at all.
*   **Code Quality:** Cleaned up the codebase, removed unused imports, and fixed several minor warnings to improve stability.

---

### 🌍 DESCRIPCIÓN GENERAL (Main Page)
**Progressive Time Difficulty**

Survival too easy? This mod makes your world harder the longer you play!

**Features:**
*   **Server-Side Only:** Install it on your server, and players can join with a vanilla client.
*   **Progressive Difficulty:** Mobs gain Health, Damage, Armor, and Knockback Resistance every day.
*   **Dynamic Scaling:** The mod adapts to your world difficulty (Easy/Normal/Hard).
*   **Hardcore Mode:** For those who want a brutal challenge (1.5% scaling + 50% damage bonus).
*   **Player-Specific Scaling:** The difficulty adapts to YOU. Veterans face tougher mobs, while new players on the same server get a fairer start.
*   **Multiplayer Chaos ("Overcharged"):** In multiplayer, if multiple players are near a mob, there's a chance it will become **OVERCHARGED**. These mobs combine the difficulty of ALL nearby players and are marked with a red nametag. Beware!
*   **Smart Buffs:**
    *   **Bosses:** Wither, Ender Dragon, Warden, and any mob tagged `forge:bosses` are 20% harder.
    *   **Mini-Bosses:** Iron Golems and Elder Guardians are also treated as bosses.
    *   **Passive Mobs:** Animals and Villagers are protected and will NOT be buffed.
*   **Admin Control:** Full command support to add/remove difficulty days globally or for specific players.

---

## 📦 Versión: 1.0.1 (Release)
**Fecha:** 21/02/2026
**Minecraft:** 1.20.1
**Loader:** Forge
**Mod:** Tiny Mob Farm Addon: Bosses

### 📝 Resumen Técnico (Informe Interno)
*   **Core:** Addon para Tiny Mob Farm que permite capturar bosses.
*   **Mecánica de Captura:**
    *   Intercepta `PlayerInteractEvent.EntityInteract` con el ítem `tinymobfarm:lasso`.
    *   Detecta bosses (Wither, Ender Dragon, Warden) que normalmente son ignorados por el mod base.
    *   Construye manualmente la estructura NBT `capturedMob` requerida por Tiny Mob Farm.
*   **Soporte Ender Dragon:**
    *   Detecta interacción con `EnderDragonPart` (hitbox multipart) y redirige la captura a la entidad padre (`parentMob`).
*   **Loot Tables Personalizadas:**
    *   Inyecta loot tables propias (`tinyfarmboss:entities/wither_custom`, etc.) en el NBT del lasso.
    *   Esto garantiza drops (Nether Star, Dragon Egg, Echo Shards) incluso en granjas que no simulan "Player Kill".
*   **Comandos:**
    *   `/tinyfarmboss give <boss>`: Entrega un lasso pre-configurado con el boss capturado.

---

### 🌍 DESCRIPCIÓN GENERAL (Main Page)
**Tiny Mob Farm Addon: Bosses**

**⚠️ DISCLAIMER: This is an UNOFFICIAL addon. It is not developed, endorsed, or supported by DAQEM.**

### 🔗 Original Mod & Credits
This project is an addon for **Tiny Mob Farm** by **DAQEM**.
*   **Original Mod:** Tiny Mob Farm on CurseForge
*   **Original Author:** DAQEM
*   **License:** The original mod is licensed under the **MIT License**, which allows addons and modifications. This addon respects that license.

### ℹ️ About this Addon
This mod **does NOT redistribute** any assets or code from the original Tiny Mob Farm mod. It is a standalone utility that requires the original mod to be installed to function.

**What does it do?**
The original Tiny Mob Farm mod restricts the `Lasso` from capturing boss entities. This addon extends that functionality by:

1.  Allowing the capture of **The Wither**, **Ender Dragon**, and **The Warden**.
2.  Injecting custom loot tables to ensure these bosses drop their valuable loot (Nether Stars, Dragon Eggs, Echo Shards) when processed by the farm, bypassing the vanilla "player kill only" requirement.
3.  Adding support for multipart entities (like the Ender Dragon), so you can click any part of the boss to capture it.

### 🛠️ How it works
It listens for the interaction event with the Lasso item and, if the target is a supported boss, it manually constructs the NBT data required by Tiny Mob Farm to recognize the entity.

### 📦 Requirements
*   Minecraft 1.20.1
*   Forge
*   **Tiny Mob Farm** (Required Dependency)

---

### 📜 CHANGELOG 1.0.1 (File Update)
**Tiny Mob Farm Addon: Bosses 1.0.1**

**New Features:**
*   **Boss Capture:** Added ability to capture **The Wither**, **Ender Dragon**, and **The Warden** using the Tiny Mob Farm Lasso.
*   **Guaranteed Loot:** Implemented custom loot tables to ensure bosses drop their rare items (Nether Star, Dragon Egg, Echo Shards) even in basic farms without player kill simulation.
*   **Admin Commands:** Added `/tinyfarmboss give <boss>` command to quickly obtain pre-filled lassos.

**Fixes & Improvements:**
*   **Multipart Support:** Added support for Ender Dragon multipart entities (you can click any part of the dragon to capture it).
*   **NBT Compatibility:** Fixed NBT data structure to be fully compatible with Tiny Mob Farm logic (`capturedMob` tag).
*   **Warden Loot:** Added Echo Shards to the Warden's custom loot table.
*   **Chat Spam:** Removed debug messages from chat upon capture.

---

## 📦 Versión: 1.0.1 (Release) - Progressive Time Difficulty (PTD)
**Fecha:** 27/10/2023
**Minecraft:** 1.20.1
**Loader:** Forge

### 📝 Resumen Técnico (Informe Interno)
*   **Core:** Sistema de dificultad progresiva basado en tiempo.
*   **Lógica de Dificultad:**
    *   Fórmula: `(Tiempo Mundo + Offset Global + Tiempo Jugador) / 24000 * 0.015`.
    *   Incremento: 1.5% de dificultad por día de juego (24,000 ticks).
    *   **Overcharge:** Mecánica especial donde si hay múltiples jugadores cerca de un mob "elegido" (probabilidad 1/15), se suman sus tiempos para un buff masivo.
*   **Filtros de Entidad:**
    *   **Excluidos:** Animales, Peces, Aldeanos, Vendedores Ambulantes, Creepers.
    *   **Incluidos:** Monstruos (`Enemy`), Golems (`AbstractGolem`), Neutrales con Daño > 0.
    *   **Multiplicadores:**
        *   Bosses (Wither, Dragon, Warden): **+20%** (Factor * 1.20).
        *   Neutrales (Golems, etc.): **50%** (Factor * 0.5).
*   **Persistencia:**
    *   `DifficultySavedData`: Guarda el tiempo global adicional del mundo.
    *   `Player NBT (ptd_player_time)`: Guarda el tiempo jugado por cada jugador individualmente.
*   **Comandos (`/progresstd`):**
    *   Gestión Global: `add_days`, `set_day`, `remove_days`.
    *   Gestión Jugador: `player <target> add_days/set_day`.
    *   Debug: `info`, `update`, `sync`, `force_overcharge`.
*   **Herramientas:**
    *   **Debug Stick:** Muestra stats (Base -> Actual), factor de dificultad y estado Overcharge.
    *   **Dev Mode:** Interruptor interno para habilitar/deshabilitar herramientas de debug.

---

### 🌍 DESCRIPCIÓN GENERAL (Main Page)
**Progressive Time Difficulty**

Survival too easy? This mod makes your world harder the longer you play!

**Features:**
*   **Server-Side Only:** Install it on your server, and players can join with a vanilla client.
*   **Progressive Difficulty:** Mobs gain Health, Damage, Armor, and Knockback Resistance every day (1.5% increase per in-game day).
*   **Player-Specific Scaling:** The difficulty adapts to YOU. Veterans face tougher mobs, while new players on the same server get a fairer start.
*   **Multiplayer Chaos ("Overcharged"):** In multiplayer, if multiple players are near a mob, there's a chance it will become **OVERCHARGED**. These mobs combine the difficulty of ALL nearby players and are marked with a red nametag. Beware!
*   **Smart Buffs:**
    *   **Bosses:** Wither, Ender Dragon, and Warden are 20% harder.
    *   **Neutral Mobs:** Iron Golems and others scale at 50% rate to keep them balanced.
    *   **Passive Mobs:** Animals and Villagers are protected and will NOT be buffed.
*   **Admin Control:** Full command support to add/remove difficulty days globally or for specific players.

---

### 📜 CHANGELOG 1.0.1 (File Update)
**Progressive Time Difficulty 1.0.1**

This update introduces smarter difficulty scaling, player-specific tracking, and the new "Overcharged" mechanic for multiplayer chaos!

**New Features:**
*   **Player-Specific Difficulty:** Mobs are harder for veterans and easier for new players. The mod tracks each player's playtime individually.
*   **OVERCHARGED Mobs:** In multiplayer, if multiple players are near a mob, there's a chance (1/15) it will become **OVERCHARGED**. These mobs combine the difficulty of ALL nearby players and are marked with a red nametag.
*   **Smart Buffs:**
    *   **Bosses:** Wither, Ender Dragon, and Warden get a **+20%** difficulty bonus.
    *   **Neutral Mobs:** Iron Golems and others get a **50%** scaling to keep them balanced.
    *   **Passive Mobs:** Animals and Villagers are now fully protected.
*   **New Commands:**
    *   Manage player time: `/progresstd player <name> add_days <amount>`

**Fixes & Tweaks:**
*   Fixed passive mobs (Cats, etc.) receiving buffs.
*   Optimized update logic to run every 3 minutes.
*   Added "Server-Side Only" metadata.

---
