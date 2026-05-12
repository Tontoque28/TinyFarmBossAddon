# T28's Universal Veinminer v1.0.2

A simple, lightweight, and highly configurable universal Veinminer mod for Minecraft Forge 1.20.1.

## Features

- **Universal Veinmining:** Mine entire veins of ores by simply breaking one block while crouching (`Sneak`).
- **Smart Detection:** Automatically detects ores using two methods for maximum compatibility:
  - **By Tag:** Recognizes blocks with the Forge Tag `forge:ores`.
  - **By Name:** Automatically identifies any block with `_ore` in its ID, ensuring out-of-the-box compatibility with most modded ores.
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

## What's New in 1.0.2?
- **Smart Ore Detection:** The mod now automatically detects any block with `_ore` in its name, providing instant compatibility with most modded ores.
- **Mining Focus:** Veinmining functionality for logs/trees has been removed to focus exclusively on a streamlined mining experience.

## Setup

Simply drop the `.jar` file into your `mods` folder and launch the game.

## Requirements

- Minecraft Forge 1.20.1

---
*Created by Tontoque28*