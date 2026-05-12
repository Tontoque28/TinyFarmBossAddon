# PUBLISH_INFO.md
# Información de Publicación: DeathSound

## Título
Death Sound Mod

## Resumen (Summary)
Plays the death sound from the Hardcore series. Now with global audio and visual enhancements.

## Descripción Completa (Description)

# Death Sound Mod

This mod alters the visual and auditory behavior of death events in Minecraft, standardizing notifications under a highly visible format inspired by Hardcore mode mechanics. Now enhanced with global audio broadcasting and robust stability improvements.

## Key Features

### Global Audio Event
Unlike client-side only implementations, this version utilizes server-side event handling (LivingDeathEvent) to broadcast the amplified hardcore death sound to **all connected players**.
- **Cross-Dimension:** The sound is audible to everyone, regardless of whether they are in the Overworld, Nether, or End.
- **Concurrent Safety:** Optimized to handle player lists safely, preventing crashes even if players disconnect during the event.

### Strict Chat Formatting
The mod intercepts the chat stream (ClientChatReceivedEvent) to enforce a dramatic aesthetic:
- **Visuals:** Forces all death messages to render in **BOLD RED**, flanked by skull symbols (☠).
- **Enhanced Detection:** Now includes a fallback mechanism to detect death messages by text content, ensuring compatibility with mods that do not use standard "death.*" translation keys.
- **False Positive Protection:** Intelligently filters out standard chat messages (e.g., player messages containing ":") to avoid incorrect formatting.

### Graphical Interface Modification
Utilizing **Safe Java Reflection**, the mod accesses private variables within the DeathScreen class to inject the same red and bold formatting into the subtitle indicating the cause of death.
- **Crash Protection:** The reflection logic is wrapped in robust error handling to ensure the game never crashes due to UI modifications, even in modded environments.

### Utility Commands
- /testsound: Audits the correct loading of the sound resource and its volume level for the executing player, without requiring an actual death event.

## Requirements and Compatibility

- **Minecraft:** 1.20.1
- **Loader:** Minecraft Forge (47.2.0 or higher)
- **Side:** Required on both **Client** and **Server**.

## Installation

Drop the .jar file into the mods folder of both your Minecraft client and your server.

---
*Developed by Tontoque28*