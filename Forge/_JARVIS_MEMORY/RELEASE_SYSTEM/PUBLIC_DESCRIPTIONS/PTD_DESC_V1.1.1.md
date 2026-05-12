# Progressive Time Difficulty (PTD) - v1.1.1

## Overview
**Progressive Time Difficulty** transforms the conventional survival experience into a dynamic challenge. Unlike other mods that apply global difficulty spikes, PTD scales based on each user's actual progress. The difficulty is calculated by combining the world's age with the specific playtime of nearby players.

This allows veterans to face a brutal challenge without preventing new players from joining the server and progressing at their own pace.

**Server-Side Only:** This mod is strictly server-side. It only needs to be installed on the server; players can join using a vanilla Minecraft client without installing the mod locally.

## Key Features

### 1. Dynamic Difficulty Scaling (New!)
The mod now adapts to your world's difficulty setting, making it perfect for any playstyle:
- **Easy:** Mobs scale slowly (0.5% per day).
- **Normal:** Balanced scaling (0.75% per day).
- **Hard:** Faster scaling (1.0% per day).

### 2. Hardcore Mode (New!)
For players seeking a brutal challenge, especially in large modpacks where you become powerful quickly.
- **Activation:** `/progresstd hardcore true`
- **Effect:** Fixed 1.5% daily scaling + **Massive Damage Bonus (x2.5)**.
- **Note:** This mode overrides world difficulty settings.

### 3. Player-Specific Scaling
The mod tracks individual playtime to balance the experience:
- **Veterans:** Players with more playtime will encounter significantly stronger and more dangerous monsters.
- **Newcomers:** Players who just joined will find mobs with base stats, allowing them to gear up safely.

### 4. OVERCHARGED Mechanic
When multiple players group up, there is a chance an enemy will absorb the combined difficulty of everyone present.
- **Effect:** The mob sums the difficulty time of all nearby players.
- **Result:** A high-threat entity marked with a red **OVERCHARGED** nameplate and mini-boss statistics.

### 5. Entity Balancing
Scaling does not affect all entities equally to maintain gameplay consistency:
| Mob Type | Difficulty Scaling | Additional Notes |
| :--- | :--- | :--- |
| **Monsters** | 100% | Zombies, skeletons, and common enemies. |
| **Bosses** | 120% | +20% bonus (Wither, Dragon, Warden, `forge:bosses`). |
| **Mini-Bosses** | 120% | Iron Golems and Elder Guardians are treated as bosses. |
| **Neutral Mobs** | 50% | Half scaling (e.g., Piglins). |
| **Passive Mobs** | 0% | Animals and Villagers remain protected. |

### 6. Affected Attributes
Enemies evolve their capabilities through:
- **Max Health:** Multiplicative increase.
- **Attack Damage:** Multiplicative increase (Massive in Hardcore Mode).
- **Adaptive Armor:** +1 Armor point per 8 extra HP.
- **Knockback Resistance:** Progressive increase based on days.

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