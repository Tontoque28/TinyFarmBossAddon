## Tiny Mob Farm Addon: Bosses

**Now available for both Forge and Fabric!**

Tiny Mob Farm Addon: Bosses is an unofficial utility addon designed to extend the functionality of **Tiny Mob Farm** by DAQEM.

🔗 Original Mod: [https://www.curseforge.com/minecraft/mc-mods/tiny-mob-farm-remastered](https://www.curseforge.com/minecraft/mc-mods/tiny-mob-farm-remastered)

All credits for the original mod belong to DAQEM.

***

### What does this addon do?

Have you ever wanted to farm Withers or the Ender Dragon in your tiny 1x1 farm but realized the Lasso just won't catch them? This addon solves exactly that!

### Features

• **Boss Capture:** Enables the Lasso to capture The Wither, Ender Dragon, and The Warden.

• **Universal Loot Injection:** Ensures that bosses drop their valuable loot (Nether Stars, Dragon Eggs, Sculk Catalysts, Echo Shards) even if other mods attempt to overwrite their loot tables, doing so in a completely non-destructive way.

• **Multipart Support:** You can click on any part of the Ender Dragon (wings, head, tail) to capture it instantly.

• **Mod Compatibility & Anti-Crash:** Safely capture and farm complex mobs from other mods and Vanilla Slimes without crashing your game or server. 
  - **Currently supported mods:**
    - **Ice and Fire:** Dragons (Fire, Ice, Lightning)
    - **L_Ender's Cataclysm:** Ignis, Ender Golem, Netherite Monstrosity
    - **Terramity:** All bosses
    - **MineColonies:** Barbarians and Pirates

• **Customization (Config File):** The addon generates a configuration file (`tinyfarmbossaddon-common.toml` in Forge, `tinyfarmbossaddon.json` in Fabric) that lets you:
  - **Whitelist:** Add any custom modded mob to be captured by the Lasso using its ID (e.g. `modid:entity`).
  - **Blacklist:** Block specific mobs or entire namespaces (e.g., `minecolonies:*`) to safeguard your server from complex entities.

• **Entity Crash Handler:** Instead of breaking your world, any errors caused by corrupted entities being lassoed will be isolated, automatically corrected with safe fallbacks, and saved to a dedicated crash log file (`logs/tmf_entity_crashes`).

• **Commands:** Includes `/tinyfarmbossaddon give <boss>` to quickly obtain a lasso with a boss already inside, and `/tinyfarmbossaddon test` to get all available boss lassos at once (useful for admins or testing).

***

### How to Use

1.  Craft a Lasso from Tiny Mob Farm.
2.  Right-click on a boss (Wither, Ender Dragon, or Warden) or supported compatible mobs.
3.  The mob will be captured into the lasso.
4.  Place the lasso into your Mob Farm block.
5.  Profit!

***

### Requirements

• Minecraft 1.20.1 
• **Forge** OR **Fabric Loader** (+ Fabric API)
• Tiny Mob Farm (Required Dependency)

***

## Important Disclaimer

This is an unofficial addon. It is not developed, endorsed, or supported by DAQEM.

This project does NOT redistribute any code or assets from Tiny Mob Farm. It only extends its functionality through separate addon logic.

Please do not report issues related to this addon to the original mod's issue tracker.

***

## License

This addon is released under the MIT License.

Addon code by Tontoque28. Original mod Tiny Mob Farm belongs to DAQEM.