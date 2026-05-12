# Progressive Time Difficulty (PTD) - Changelog v1.1.4

## Version 1.1.4 (Balance & Protection Update)
**Date:** 23/02/2026
**Minecraft:** 1.20.1
**Loader:** Forge

### 🛡️ Armor Protection (Hybrid Damage)
We heard your feedback about armor durability vanishing instantly!
- **Physical Cap:** Physical damage scaling is now capped at **x5.0**.
- **Magic Damage:** Any damage exceeding this cap is applied as **Magic Damage** (50% effectiveness).
- **Result:** Mobs are still extremely lethal, but they won't break your armor in 3 hits.

### ⚖️ Balance Changes
**Bosses:**
- **Damage:** Increased boss damage bonus to **+30%** (was +20%).
- **Defense:** Bosses no longer receive bonus Armor or Knockback Resistance.
- **Safety:** Mobs with > 200 HP (buffed) will NOT receive extra armor.

**Weak Mobs:**
- **Buffs:** Zombies, Skeletons, and other low-tier mobs (<= 20 HP) receive extra Health and Armor scaling to stay relevant.

**Experience:**
- **XP Bottles:** Thrown experience bottles now scale massively (**x5.0**) with difficulty.

### 🛠️ Fixes & Improvements
- **Mod Support:** Added explicit boss detection for **Mowzie's Mobs**, **Cataclysm**, **Twilight Forest**, **Ice and Fire**, **Aquamirae**, **Bosses of Mass Destruction**, **Stalwart Dungeons**, and **Rotten Creatures**.
- **Code Cleanup:** Major refactoring for better performance and stability.

---
*Developed by Tontoque28*