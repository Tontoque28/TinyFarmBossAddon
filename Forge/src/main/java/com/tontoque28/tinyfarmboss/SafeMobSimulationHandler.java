package com.tontoque28.tinyfarmboss;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

public class SafeMobSimulationHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static String getSafeLootTable(Entity mob) {
        try {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType());
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
        // Mantenemos esto si alguna vez la blacklist se quita, 
        // pero la blacklist global por defecto en minecolonies:* evitará que llegue aquí.
        if (path.contains("barbarian")) {
            return "tinyfarmbossaddon:entities/safe_minecolonies_barbarian";
        }
        if (path.contains("pirate")) {
             return "tinyfarmbossaddon:entities/safe_minecolonies_pirate";
        }
        return "tinyfarmbossaddon:entities/safe_minecolonies_default";
    }

    private static String getSafeIceAndFireDrop(String path) {
        if (path.contains("dragon")) {
             if (path.contains("fire")) return "tinyfarmbossaddon:entities/safe_iceandfire_fire_dragon";
             if (path.contains("ice")) return "tinyfarmbossaddon:entities/safe_iceandfire_ice_dragon";
             if (path.contains("lightning")) return "tinyfarmbossaddon:entities/safe_iceandfire_lightning_dragon";
             return "tinyfarmbossaddon:entities/safe_iceandfire_dragon_default";
        }
        return "tinyfarmbossaddon:entities/safe_iceandfire_default";
    }

    private static String getSlimeSplitSimulation(Entity mob) {
        return "minecraft:entities/slime";
    }

    private static String getDefaultLootTable(ResourceLocation entityId) {
        String idString = entityId.toString();
        if (idString.equals("minecraft:wither")) {
            return "tinyfarmbossaddon:entities/wither_custom";
        } else if (idString.equals("minecraft:ender_dragon")) {
            return "tinyfarmbossaddon:entities/ender_dragon_custom";
        } else if (idString.equals("minecraft:warden")) {
            return "tinyfarmbossaddon:entities/warden_custom";
        } else {
            // Retorna la tabla vanilla si existe, o la propia del mob "modid:entities/mob_name"
            return entityId.getNamespace() + ":entities/" + entityId.getPath();
        }
    }
}