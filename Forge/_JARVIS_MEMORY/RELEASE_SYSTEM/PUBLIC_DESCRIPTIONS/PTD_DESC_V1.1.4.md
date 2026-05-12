# Progressive Time Difficulty (PTD) - v1.1.4

## Overview
**Progressive Time Difficulty** transforms the conventional survival experience into a dynamic challenge. Unlike other mods that apply global difficulty spikes, PTD scales based on each user's actual progress. The difficulty is calculated by combining the world's age with the specific playtime of nearby players.

This allows veterans to face a brutal challenge without preventing new players from joining the server and progressing at their own pace.

**Server-Side Only:** This mod is strictly server-side. It only needs to be installed on the server; players can join using a vanilla Minecraft client without installing the mod locally.

## Key Features

### 1. Armor Protection (New!)
Tired of your Netherite armor breaking in seconds?
- **Hybrid Damage:** Physical damage scaling is now capped at **x5.0**.
- **Magic Damage:** Any damage exceeding the cap is applied as magic damage (bypassing armor but **preserving durability**).
- **Result:** Mobs are still lethal, but your armor won't evaporate instantly.

### 2. Boss Rebalance
- **Damage:** Bosses now deal **30% more damage** (up from 20%).
- **Defense:** Bosses no longer receive extra Armor or Knockback Resistance, making them "glass cannons" (high HP/Damage, fair defense).
- **Safety Rule:** Any mob with > 200 HP automatically loses bonus armor to prevent invincibility.

### 3. Weak Mob Buffs
- **Low Tier Mobs:** Zombies, Skeletons, and Spiders (<= 20 HP) receive extra Health and Armor scaling to remain relevant in late-game.

### 4. Scalable Experience
Mobs drop more experience as they become stronger.
- **Formula:** `BaseXP * (1.0 + (DifficultyFactor * 1.2))`
- **XP Bottles:** Thrown experience bottles now scale massively (**x5.0 multiplier**) with difficulty.

### 5. Environmental Damage Scaling
The world itself is now your enemy. Environmental damage scales with the difficulty factor.
- **Affected Sources:** Fall damage, Drowning, Fire, Lava, Starvation, Void, Suffocation, Lightning, and more.
- **Scaling:** Damage increases progressively. In late-game, a simple fall or lava dip can be fatal.
- **Hardcore Bonus:** In Hardcore Mode, environmental damage scales 50% faster.

### 6. Dynamic Difficulty Scaling
The mod adapts to your world's difficulty setting:
- **Easy:** Mobs scale slowly (0.5% per day).
- **Normal:** Balanced scaling (0.75% per day).
- **Hard:** Faster scaling (1.0% per day).

### 7. Hardcore Mode
For players seeking a brutal challenge.
- **Activation:** `/progresstd hardcore true`
- **Effect:** Fixed 1.5% daily scaling + **Massive Damage Bonus (x2.5)**.

## Admin Commands
*Important: All commands require operator (OP) permissions.*

- `/progresstd info` - Displays current world and player difficulty factors.
- `/progresstd add_days <amount>` - Increases the global world difficulty.
- `/progresstd set_day <day>` - Sets the global difficulty to a specific day.
- `/progresstd player <name> add_days <amount>` - Modifies a specific player's personal difficulty counter.
- `/progresstd hardcore <true/false>` - Toggles Hardcore Mode.
- `/progresstd force_overcharge` - Forces the entity the admin is looking at to become Overcharged.

---
*Developed by Tontoque28*