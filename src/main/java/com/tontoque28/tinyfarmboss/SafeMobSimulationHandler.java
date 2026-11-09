package com.tontoque28.tinyfarmboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeMobSimulationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinyFarmBoss.MODID);

    public static String getSafeLootTable(Entity mob) {
        try {
            ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
            if (entityId == null) {
                return "minecraft:empty";
            }

            String namespace = entityId.getNamespace();
            String path = entityId.getPath();

            LOGGER.debug("[TinyFarmBoss] Handling mob: {} via SAFE_HANDLER", entityId);

            if (namespace.equals("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire")) {
                return getSafeIceAndFireDrop(path, mob);
            }

            if (namespace.equals("minecraft") && path.equals("slime")) {
                return getSlimeSplitSimulation(mob);
            }

            return getDefaultLootTable(entityId);

        } catch (Exception e) {
            LOGGER.error("[TinyFarmBoss] Crash avoided in SafeMobSimulationHandler for mob: {}", mob, e);
            return "minecraft:empty";
        }
    }

    public static String getSafeLootTableFromStoredData(String mobId, CompoundTag mobData) {
        try {
            ResourceLocation entityId = ResourceLocation.tryParse(mobId);
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getNamespace();
            String path = entityId.getPath();

            if (namespace.equals("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire")) {
                String dragonType = null;
                if (path.contains("fire_dragon")) dragonType = "fire";
                else if (path.contains("ice_dragon")) dragonType = "ice";
                else if (path.contains("lightning_dragon")) dragonType = "lightning";

                if (dragonType == null) {
                    return "tinyfarmboss:entities/safe_iceandfire_default";
                }

                long ageTicks = mobData != null ? mobData.getLong("AgeTicks") : 0L;
                int stage = getDragonStage(ageTicks);
                String color = getDragonColor(dragonType, mobData);
                return "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
            }

            if (namespace.equals("minecraft") && path.equals("slime")) {
                return "minecraft:entities/slime";
            }

            return getDefaultLootTable(entityId);
        } catch (Exception e) {
            LOGGER.error("[TinyFarmBoss] Error en getSafeLootTableFromStoredData", e);
            return "minecraft:empty";
        }
    }

    private static String getSafeMineColoniesDrop(String path) {
        if (path.contains("barbarian")) {
            return "tinyfarmboss:entities/safe_minecolonies_barbarian";
        }
        if (path.contains("pirate")) {
            return "tinyfarmboss:entities/safe_minecolonies_pirate";
        }
        return "tinyfarmboss:entities/safe_minecolonies_default";
    }

    private static String getSafeIceAndFireDrop(String path, Entity mob) {
        String dragonType = null;
        if (path.contains("fire_dragon")) dragonType = "fire";
        else if (path.contains("ice_dragon")) dragonType = "ice";
        else if (path.contains("lightning_dragon")) dragonType = "lightning";

        if (dragonType == null) {
            return "tinyfarmboss:entities/safe_iceandfire_default";
        }

        CompoundTag nbt = new CompoundTag();
        mob.saveWithoutId(nbt);
        long ageTicks = nbt.getLong("AgeTicks");
        int stage = getDragonStage(ageTicks);
        String color = getDragonColor(dragonType, nbt);

        return "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
    }

    public static int getDragonStage(long ageTicks) {
        if (ageTicks >= 1920000L) return 5;
        if (ageTicks >= 1440000L) return 4;
        if (ageTicks >= 960000L) return 3;
        if (ageTicks >= 480000L) return 2;
        return 1;
    }

    private static String getDragonColor(String dragonType, CompoundTag data) {
        String[] colors;
        switch (dragonType) {
            case "ice":
                colors = new String[]{"blue", "white", "sapphire", "silver"};
                break;
            case "lightning":
                colors = new String[]{"amethyst", "black", "copper", "electric"};
                break;
            default:
                colors = new String[]{"red", "green", "bronze", "gray"};
                break;
        }
        if (data != null && data.contains("Variant", Tag.TAG_STRING)) {
            String name = data.getString("Variant");
            for (String color : colors) {
                if (color.equalsIgnoreCase(name)) {
                    return color;
                }
            }
            return colors[0];
        }
        int variant = data != null ? data.getInt("Variant") : 0;
        if (variant < 0 || variant >= colors.length) {
            return colors[0];
        }
        return colors[variant];
    }

    private static String getSlimeSplitSimulation(Entity mob) {
        return "minecraft:entities/slime";
    }

    private static String getDefaultLootTable(ResourceLocation entityId) {
        String idString = entityId.toString();
        if (idString.equals("minecraft:wither")) {
            return "tinyfarmboss:entities/wither_custom";
        } else if (idString.equals("minecraft:ender_dragon")) {
            return "tinyfarmboss:entities/ender_dragon_custom";
        } else if (idString.equals("minecraft:warden")) {
            return "tinyfarmboss:entities/warden_custom";
        } else {
            return "minecraft:entities/" + entityId.getPath();
        }
    }
}
