package com.tontoque28.tinyfarmboss;

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

            LOGGER.debug("[TinyMobFarmAddon] Handling mob: {} via SAFE_HANDLER", entityId);

            if (namespace.equals("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire")) {
                return getSafeIceAndFireDrop(path);
            }

            if (namespace.equals("minecraft") && path.equals("slime")) {
                return getSlimeSplitSimulation(mob);
            }

            return getDefaultLootTable(entityId);

        } catch (Exception e) {
            LOGGER.error("[TinyMobFarmAddon] Crash avoided in SafeMobSimulationHandler for mob: {}", mob, e);
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

    private static String getSafeIceAndFireDrop(String path) {
        if (path.contains("dragon")) {
             if (path.contains("fire")) return "tinyfarmboss:entities/safe_iceandfire_fire_dragon";
             if (path.contains("ice")) return "tinyfarmboss:entities/safe_iceandfire_ice_dragon";
             if (path.contains("lightning")) return "tinyfarmboss:entities/safe_iceandfire_lightning_dragon";
             return "tinyfarmboss:entities/safe_iceandfire_dragon_default";
        }
        return "tinyfarmboss:entities/safe_iceandfire_default";
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