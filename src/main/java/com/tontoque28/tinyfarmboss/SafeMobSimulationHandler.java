package com.tontoque28.tinyfarmboss;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

public class SafeMobSimulationHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static String getSafeLootTable(Entity mob) {
        try {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType());
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getNamespace();
            String path = entityId.getPath();

            // Si es de MineColonies y el mod está cargado
            if (namespace.equals("minecolonies") && ModList.get().isLoaded("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            // Si es de Ice and Fire y el mod está cargado
            if (namespace.equals("iceandfire") && ModList.get().isLoaded("iceandfire")) {
                return getSafeIceAndFireDrop(path);
            }

            return getDefaultLootTable(entityId);
        } catch (Exception e) {
            LOGGER.error("[TinyFarmBossAddon] Error en SafeMobSimulationHandler", e);
            return "minecraft:empty";
        }
    }

    private static String getSafeMineColoniesDrop(String path) {
        if (path.contains("mummy")) return "tinyfarmboss:entities/safe_minecolonies_mummy";
        if (path.contains("norsemenarcher")) return "tinyfarmboss:entities/safe_minecolonies_norsemenarcher";
        if (path.contains("norsemenchief")) return "tinyfarmboss:entities/safe_minecolonies_norsemenchief";
        if (path.contains("pharao")) return "tinyfarmboss:entities/safe_minecolonies_pharao";
        if (path.contains("shieldmaiden")) return "tinyfarmboss:entities/safe_minecolonies_shieldmaiden";
        if (path.contains("amazonchief")) return "tinyfarmboss:entities/safe_minecolonies_amazonchief";
        if (path.contains("amazonspearman")) return "tinyfarmboss:entities/safe_minecolonies_amazonspearman";
        if (path.contains("amazon")) return "tinyfarmboss:entities/safe_minecolonies_amazon";
        if (path.contains("archerbarbarian")) return "tinyfarmboss:entities/safe_minecolonies_archerbarbarian";

        return "tinyfarmboss:entities/safe_minecolonies_default";
    }

    private static String getSafeIceAndFireDrop(String path) {
        if (path.contains("fire_dragon")) return "tinyfarmboss:entities/fire_dragon_stage5_loot";
        if (path.contains("ice_dragon")) return "tinyfarmboss:entities/ice_dragon_stage5_loot";
        if (path.contains("lightning_dragon")) return "tinyfarmboss:entities/lightning_dragon_stage5_loot";

        return "tinyfarmboss:entities/safe_iceandfire_default";
    }

    private static String getDefaultLootTable(ResourceLocation entityId) {
        String idString = entityId.toString();
        if (idString.equals("minecraft:wither")) return "tinyfarmboss:entities/wither_custom";
        if (idString.equals("minecraft:ender_dragon")) return "tinyfarmboss:entities/ender_dragon_custom";
        if (idString.equals("minecraft:warden")) return "tinyfarmboss:entities/warden_custom";
        return "minecraft:empty";
    }
}