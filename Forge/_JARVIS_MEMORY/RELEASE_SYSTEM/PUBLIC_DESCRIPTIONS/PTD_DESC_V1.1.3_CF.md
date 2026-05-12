# Progressive Time Difficulty (PTD) - v1.1.3

**Overview**
Progressive Time Difficulty transforms the conventional survival experience into a dynamic challenge. Unlike other mods that apply global difficulty spikes, PTD scales based on each user's actual progress. The difficulty is calculated by combining the world's age with the specific playtime of nearby players.

This allows veterans to face a brutal challenge without preventing new players from joining the server and progressing at their own pace.

**Server-Side Only:** This mod is strictly server-side. It only needs to be installed on the server; players can join using a vanilla Minecraft client without installing the mod locally.

---

## Key Features

**1. Scalable Experience (New!)**
Mobs now drop more experience as they become stronger, rewarding players for tackling tougher enemies.
*   **Formula:** `BaseXP * (1.0 + (DifficultyFactor * 1.2))`
*   **Effect:** A zombie that drops 5 XP on Day 1 might drop 16 XP on Day 100.

**2. Environmental Damage Scaling**
The world itself is now your enemy. Environmental damage scales with the difficulty factor.
*   **Affected Sources:** Fall damage, Drowning, Fire, Lava, Starvation, Void, Suffocation, Lightning, and more.
*   **Scaling:** Damage increases progressively. In late-game, a simple fall or lava dip can be fatal.
*   **Hardcore Bonus:** In Hardcore Mode, environmental damage scales 50% faster.

**3. Dynamic Difficulty Scaling**
The mod adapts to your world's difficulty setting:
*   **Easy:** Mobs scale slowly (0.5% per day).
*   **Normal:** Balanced scaling (0.75% per day).
*   **Hard:** Faster scaling (1.0% per day).

**4. Hardcore Mode (Rebalanced)**
For players seeking a brutal challenge, especially in large modpacks.
*   **Activation:** `/progresstd hardcore true`
*   **Effect:** Fixed 1.5% daily scaling + **Massive Damage Bonus (x2.5)**.
*   **Note:** This mode overrides world difficulty settings.

**5. Player-Specific Scaling**
The mod tracks individual playtime to balance the experience:
*   **Veterans:** Players with more playtime will encounter significantly stronger and more dangerous monsters.
*   **Newcomers:** Players who just joined will find mobs with base stats, allowing them to gear up safely.

**6. OVERCHARGED Mechanic**
When multiple players group up, there is a chance an enemy will absorb the combined difficulty of everyone present.
*   **Effect:** The mob sums the difficulty time of all nearby players.
*   **Result:** A high-threat entity marked with a red **OVERCHARGED** nameplate and mini-boss statistics.

---

## Admin Commands
*Important: All commands require operator (OP) permissions.*

*   `/progresstd info` - Displays current world and player difficulty factors.
*   `/progresstd add_days <amount>` - Increases the global world difficulty.
*   `/progresstd set_day <day>` - Sets the global difficulty to a specific day.
*   `/progresstd player <name> add_days <amount>` - Modifies a specific player's personal difficulty counter.
*   `/progresstd hardcore <true/false>` - Toggles Hardcore Mode.
*   `/progresstd force_overcharge` - Forces the entity the admin is looking at to become Overcharged.

---
*Developed by Tontoque28*