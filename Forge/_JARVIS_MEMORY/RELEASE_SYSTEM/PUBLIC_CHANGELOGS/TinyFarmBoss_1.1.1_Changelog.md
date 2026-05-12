v1.1.1:
- Implemented Advanced Entity Crash Handler: A new robust system that intercepts any exceptions or crashes caused by highly modified entities (like corrupted Slimes in large modpacks).
- Automatic Fallback System: If an entity fails to process, it will no longer crash the server or client. Instead, it will safely revert to a baseline state (e.g. Slime size 1) and continue functioning.
- Detailed Crash Logging: Instead of crashing the game, isolated entity errors are now saved as detailed crash reports inside the `logs/tmf_entity_crashes` folder, providing exact UUIDs, positions, NBT data, and Stacktraces for advanced debugging.
- Safe Mode Environment Detection: The mod now automatically detects heavy visual or optimization mods (like Optifine, Entity Culling, EMF, etc.) and preemptively sanitizes risky entities to prevent them from breaking the farm.
- Added Anti-Spam protection for error logs to prevent console/log flooding.