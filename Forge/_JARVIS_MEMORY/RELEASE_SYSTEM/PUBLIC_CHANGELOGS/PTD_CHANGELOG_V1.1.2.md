# Progressive Time Difficulty (PTD) - Changelog v1.1.2

## Version 1.1.2 (Environmental Damage Update)
**Date:** 23/02/2026
**Minecraft:** 1.20.1
**Loader:** Forge

### ⚠️ Major Changes
**Environmental Damage Scaling:**
The world itself is now a threat. Environmental damage sources now scale with the difficulty factor.
- **Affected Sources:** Fall damage, Drowning, Fire, Lava, Starvation, Void, Suffocation, Lightning, Explosions, Magic, and more.
- **Scaling Formula:** `BaseDamage * (1.0 + (Factor * 2.0))`.
- **Hardcore Bonus:** In Hardcore Mode, environmental damage receives an additional **+50% scaling bonus**.
- **Lava/Fire Lethality:** Lava and Fire damage tick 50% harder to simulate faster lethality.

### 🛠️ Fixes & Improvements
- **Code Refactor:** Separated environmental damage logic into a dedicated handler for better stability.
- **Hardcore Rebalance:** Increased Hardcore damage multiplier to **x2.5** (up from x2.0) to challenge high-tier armor players.
- **Compatibility:** Fixed a crash related to `DamageTypes.OUT_OF_WORLD` mapping issues.

---
*Developed by Tontoque28*