# T28's Universal Veinminer v1.0.1

A simple, lightweight, and highly configurable universal Veinminer mod for Minecraft Forge 1.20.1.

## Features

- **Universal Veinmining:** Mine entire veins of ores or chop down entire trees by simply breaking one block while crouching (`Sneak`).
- **Dynamic Detection:** Automatically detects ores and logs using Forge Tags (`Tags.Blocks.ORES` and `BlockTags.LOGS`), ensuring seamless compatibility with most modded ores and trees out of the box.
- **Vanilla Integration:**
  - Fully respects **Fortune** and **Silk Touch** enchantments.
  - **Special Ancient Debris Handling:** Drops Netherite Scraps directly, with extra drops from Fortune!
  - Accurately drops the corresponding experience orbs.
  - Properly damages your tool for every block mined (respects **Unbreaking** enchantment natively).
  - Safety first: Automatically stops before your tool breaks (leaves 1 durability point).
  - Applies balanced player exhaustion for every block mined.
- **Highly Configurable (In-Game):**
  - **Toggle Key:** Use a dedicated keybinding (default 'V') to quickly enable or disable the Veinminer functionality on the fly, with on-screen action bar notifications.
  - **Custom Blocks (Commands):** Easily add or remove any block to the veinminer list using in-game commands with native auto-completion (Requires OP Level 2):
    - `/universalveinminer addblock <modid:block_id>`
    - `/universalveinminer removeblock <modid:block_id>`
    - `/universalveinminer listblocks`
  - **Block Limit:** Set a maximum limit of blocks to mine in a single action via the config file (`universalveinminer-common.toml`) to prevent server lag (default is 64, max 1024).
- **Localization Support:** Includes English (`en_us`) and Spanish (`es_es`) translations.

## What's New in 1.0.1?
- **Ancient Debris Support:** Veinmining Ancient Debris with Fortune now drops Netherite Scraps directly.
- **Improved Detection Algorithm:** The block search algorithm has been completely rewritten to explore a 3x3x3 area around each block, including diagonals. This fixes the issue of floating blocks being left behind in large or irregularly shaped veins and ensures a much more thorough and satisfying mining experience.

## Setup

Simply drop the `.jar` file into your `mods` folder and launch the game.

## Requirements

- Minecraft Forge 1.20.1

---
*Created by Tontoque28*