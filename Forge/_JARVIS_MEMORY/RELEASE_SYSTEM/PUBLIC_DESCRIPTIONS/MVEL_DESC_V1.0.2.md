# More Vanilla Enchantments Levels (MVEL) - v1.0.2

## Overview
**More Vanilla Enchantments Levels** is a lightweight, server-side mod that increases the maximum levels of vanilla enchantments.

While it works perfectly on its own, it is **specifically designed and balanced to be played alongside [Progressive Time Difficulty (PTD)](https://www.curseforge.com/minecraft/mc-mods/progressive-time-difficulty)**.

In modpacks with scaling difficulty, vanilla enchantments often become obsolete against high-level mobs. MVEL solves this by allowing your gear to scale in power alongside the threats you face.

**Server-Side Friendly:** This mod can be installed on the server, and players can join with a vanilla client.

## Key Features

### 1. Balanced for Hardcore Gameplay
The enchantment levels have been tuned to match the brutal scaling of PTD's Hardcore Mode:
- **Combat Ready:** Sharpness, Protection, and Power now scale up to level **10**. This is essential to counter mobs that can deal 2.5x damage and have massive health pools.
- **Economy:** Looting and Fortune scale to level **7**, rewarding high-risk gameplay with better drops.
- **Durability:** Unbreaking now goes up to level **12**, ensuring your gear survives prolonged battles against tough enemies.
- **Mobility:** Depth Strider scales to level **8** for superior underwater movement.

### 2. High Compatibility & Stability
The mod uses a robust Mixin strategy that targets both the base `Enchantment` class and its specific subclasses. This ensures maximum compatibility with other mods that might also modify enchantment behavior, preventing common crashes.

### 3. Lightweight & Simple
No complex configurations or new items. Just install and play. The mod is designed to be a simple, "fire-and-forget" addition to your modpack.

## Affected Attributes
The primary change is an increase in the return value of `Enchantment.getMaxLevel()`. The exact level increase varies per enchantment and is designed to be balanced for enhanced gameplay.

---
*Developed by Tontoque28*
