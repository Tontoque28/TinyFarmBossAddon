package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SafeMobSimulationHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static String getSafeLootTable(Entity mob) {
        try {
            ResourceLocation entityId = ForgeRegistries.ENTITIES.getKey(mob.getType());
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getNamespace();
            String path = entityId.getPath();

            if (namespace.equals("minecolonies") && ModList.get().isLoaded("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire") && ModList.get().isLoaded("iceandfire")) {
                return getSafeIceAndFireDrop(path, mob);
            }

            return getDefaultLootTable(entityId);
        } catch (Exception e) {
            LOGGER.error("[TinyFarmBossAddon] Error en SafeMobSimulationHandler", e);
            return "minecraft:empty";
        }
    }

    public static String getSafeLootTableFromStoredData(String mobId, CompoundNBT mobData) {
        try {
            ResourceLocation entityId = ResourceLocation.tryCreate(mobId);
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getNamespace();
            String path = entityId.getPath();

            if (namespace.equals("minecolonies") && ModList.get().isLoaded("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire") && ModList.get().isLoaded("iceandfire")) {
                String dragonType = null;
                if (path.contains("fire_dragon")) dragonType = "fire";
                else if (path.contains("ice_dragon")) dragonType = "ice";
                else if (path.contains("lightning_dragon")) dragonType = "lightning";

                if (dragonType == null) {
                    return "tinyfarmboss:entities/safe_iceandfire_default";
                }

                long ageTicks = mobData != null ? mobData.getLong("AgeTicks") : 0L;
                int variant = mobData != null ? mobData.getInt("Variant") : 0;
                int stage = getDragonStage(ageTicks);
                String color = getDragonColor(dragonType, variant);
                return "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
            }

            return getDefaultLootTable(entityId);
        } catch (Exception e) {
            LOGGER.error("[TinyFarmBoss] Error en getSafeLootTableFromStoredData", e);
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

    private static String getSafeIceAndFireDrop(String path, Entity mob) {
        String dragonType = null;
        if (path.contains("fire_dragon")) dragonType = "fire";
        else if (path.contains("ice_dragon")) dragonType = "ice";
        else if (path.contains("lightning_dragon")) dragonType = "lightning";

        if (dragonType == null) {
            return "tinyfarmboss:entities/safe_iceandfire_default";
        }

        CompoundNBT nbt = new CompoundNBT();
        mob.writeWithoutTypeId(nbt);
        long ageTicks = nbt.getLong("AgeTicks");
        int variant = nbt.getInt("Variant");
        int stage = getDragonStage(ageTicks);
        String color = getDragonColor(dragonType, variant);

        return "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
    }

    public static int getDragonStage(long ageTicks) {
        if (ageTicks >= 1920000L) return 5;
        if (ageTicks >= 1440000L) return 4;
        if (ageTicks >= 960000L) return 3;
        if (ageTicks >= 480000L) return 2;
        return 1;
    }

    private static String getDragonColor(String dragonType, int variant) {
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
        if (variant < 0 || variant >= colors.length) {
            return colors[0];
        }
        return colors[variant];
    }

    private static String getDefaultLootTable(ResourceLocation entityId) {
        String idString = entityId.toString();
        if (idString.equals("minecraft:wither")) return "tinyfarmboss:entities/wither_custom";
        if (idString.equals("minecraft:ender_dragon")) return "tinyfarmboss:entities/ender_dragon_custom";
        return entityId.getNamespace() + ":entities/" + entityId.getPath();
    }
}
