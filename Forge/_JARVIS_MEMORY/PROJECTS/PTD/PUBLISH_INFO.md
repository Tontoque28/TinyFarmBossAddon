# PUBLISH_INFO.md

Archivo destinado a descripciones públicas y changelogs para plataformas (CurseForge, Modrinth).

====================================================

## [v1.1.6] DESCRIPCIÓN GENERAL (Main Page)

**Progressive Time Difficulty**

Survival too easy? This mod makes your world harder the longer you play!

**Features:**
*   **Server-Side Only:** Install it on your server, and players can join with a vanilla client.
*   **Progressive Difficulty:** Mobs gain Health, Damage, Armor, and Knockback Resistance every day.
*   **Visual Threat Indicators (NEW):** Mobs now emit particles based on their difficulty level!
    *   **Smoke:** Hardened Mob (Tier 1).
    *   **Flame:** Dangerous Mob (Tier 2).
    *   **Soul Fire:** Extreme Threat (Tier 3).
*   **Dynamic Scaling:** The mod adapts to your world difficulty (Easy/Normal/Hard).
*   **Hardcore Mode:** For those who want a brutal challenge (1.2% scaling + massive damage bonus).
    *   *Recommended for large modpacks where players become too powerful quickly.*
    *   Activate with: `/progresstd hardcore true`
*   **Player-Specific Scaling:** The difficulty adapts to YOU. Veterans face tougher mobs, while new players on the same server get a fairer start.
*   **Multiplayer Chaos ("Overcharged"):** In multiplayer, if multiple players are near a mob, there's a chance it will become **OVERCHARGED**. These mobs combine the difficulty of ALL nearby players and are marked with a red nametag. Beware!
*   **Smart Buffs:**
    *   **Bosses:** Wither, Ender Dragon, Warden, and any mob tagged `forge:bosses` are 20% harder.
    *   **Mini-Bosses:** Iron Golems and Elder Guardians are also treated as bosses.
    *   **Passive Mobs:** Animals and Villagers are protected and will NOT be buffed.
*   **Admin Control:** Full command support to add/remove difficulty days globally or for specific players.

---

## [v1.1.6] CHANGELOG (Copy & Paste)

**Progressive Time Difficulty 1.1.6**

**Visual Threat Indicators (NEW):**
*   **Know Your Enemy:** Mobs now emit particles based on their difficulty level, allowing you to assess the threat before engaging!
    *   **Tier 1 (Factor 0.5 - 1.0):** Smoke Particles (Hardened).
    *   **Tier 2 (Factor 1.0 - 2.0):** Flame Particles (Dangerous).
    *   **Tier 3 (Factor > 2.0):** Soul Fire Particles (Extreme Threat).

**Hardcore Rebalance:**
*   **Slower Scaling:** Hardcore difficulty increase rate reduced from **1.5%** to **1.2%** per day.
*   **Damage Restored:** Hardcore damage multiplier restored to **x2.5** (was briefly lowered to x2.0 in v1.1.5).
*   **Health & Armor:** Maintained the v1.1.5 nerfs (x1.0 Health, reduced Armor gain) to prevent mobs from becoming unkillable sponges.

**Previous Changes (v1.1.5):**
*   **Creeper Exclusion:** Creepers are now explicitly excluded from all difficulty buffs to prevent unfair one-shots and mod conflicts.
*   **General Nerf:** Reduced overall health and armor scaling to smooth out the difficulty curve.
