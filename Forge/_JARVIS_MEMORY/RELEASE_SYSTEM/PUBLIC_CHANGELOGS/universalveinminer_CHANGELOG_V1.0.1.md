## Version 1.0.1
Date: 2026-04-11

Changes:
- Added special handling for Ancient Debris:
  - With Fortune, now drops Netherite Scraps directly with a chance for extra drops based on the enchantment level.
  - With Silk Touch, drops the Ancient Debris block as expected.
- Improved the block detection algorithm to search in a 3x3x3 area, including diagonals.
- Optimized the search logic for better performance and to ensure the configured block limit is reached more consistently.

Fixes:
- Fixed an issue where the veinminer would not mine all connected blocks in large or irregularly shaped veins.

Notes:
- This is a recommended update for all users.
- The configuration file remains the same.
