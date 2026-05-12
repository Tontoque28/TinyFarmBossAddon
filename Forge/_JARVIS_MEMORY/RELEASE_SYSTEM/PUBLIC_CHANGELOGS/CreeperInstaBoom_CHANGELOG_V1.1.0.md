## Version 1.1.0
Date: 2024-05-22
Changes:
- **Major Update:**
  - Rebranding: Mod name reverted to **Creeper Insta Boom**.
  - Dynamic Difficulty Selection: The mod is no longer locked to "Hard" mode.
  - Added `/creeperdifficulty <easy|normal|hard>` command to change difficulty in real-time.
    - Easy: Fast Creepers (1.2x), No Charged, No Alpha.
    - Normal: Faster Creepers (1.4x), Moderate Charged, No Alpha.
    - Hard: Insane Speed (1.5x+), High Charged, Alphas Active. (Default)
- Explosion interception (`ExplosionEvent.Start`) for compatibility with other mods (e.g., Creeper Overhaul).
- Forced `swell = -1` to prevent vanilla fuse animation and ensure "Insta Boom".
- Added `/alphacreeper` command to manually spawn Alpha Creepers.
- Increased "Insta Boom" detection radius to 4.0 blocks for priority over native AIs.
- Code refactoring to strictly comply with style standards (no comments).
Fixes:
- Creepers from other mods generated fire or did not explode instantly.
- Vanilla Creepers initiated fuse animation before exploding.