# More Vanilla Enchantments Levels - Changelog v1.0.1

## Version 1.0.1 (PTD Balance & Fixes)
**Date:** 02/03/2026
**Minecraft:** 1.20.1
**Loader:** Forge

### ⚠️ Major Rebalance
This update significantly increases the maximum levels of enchantments to match the difficulty scaling of **Progressive Time Difficulty (PTD)**.

**Why?**
In PTD's Hardcore Mode, mobs deal **x2.5 damage** and have massive health pools. Vanilla enchantments (even at previous MVEL levels) were not enough to survive.

**New Max Levels:**
- **Protection IV -> X (10):** Essential for surviving one-shots from Overcharged mobs.
- **Sharpness V -> X (10):** Required to cut through high-HP enemies.
- **Efficiency V -> X (10):** Faster mining for reinforced blocks.
- **Looting III -> VII (7):** Greater rewards for greater risks.
- **Unbreaking III -> XII (12):** Improved durability.
- **Depth Strider III -> VIII (8):** Enhanced underwater mobility.
- **Feather Falling, Thorns, Swift Sneak, Respiration:** Reverted to previous balanced levels (7, 6, 6, 6).

### New Feature: True Unusing Support
Added special support for mods that add an "Unusing" enchantment.
- If an item has any enchantment with "unusing" in its ID, it will now be **truly indestructible**, taking 0 durability damage. This fixes issues where PTD's high damage would break even "unusing" armor.

### Fixes
- **Critical:** Fixed a crash on startup in production environments (modpacks) related to the new Unusing feature.
- Fixed a critical crash related to Mixin injection on large modpacks.
- Improved compatibility with other enchantment mods.
- Corrected Mixin warnings related to fishing enchantment class names.

---
*Developed by Tontoque28*
