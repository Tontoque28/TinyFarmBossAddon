package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class SafeMobSimulationHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern VALID = Pattern.compile("^([a-z0-9_.-]+:)?[a-z0-9/._-]+$");

    public static String getSafeLootTable(Entity mob) {
        try {
            ResourceLocation entityId = EntityList.getKey(mob);
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getResourceDomain();
            String path = entityId.getResourcePath();

            if (namespace.equals("minecolonies") && Loader.isModLoaded("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire") && Loader.isModLoaded("iceandfire")) {
                return getSafeIceAndFireDrop(path, mob);
            }

            return getDefaultLootTable(entityId);
        } catch (Exception e) {
            LOGGER.error("[TinyFarmBossAddon] Error en SafeMobSimulationHandler", e);
            return "minecraft:empty";
        }
    }

    public static String getSafeLootTableFromStoredData(String mobId, NBTTagCompound mobData) {
        try {
            ResourceLocation entityId = tryCreate(mobId);
            if (entityId == null) return "minecraft:empty";

            String namespace = entityId.getResourceDomain();
            String path = entityId.getResourcePath();

            if (namespace.equals("minecolonies") && Loader.isModLoaded("minecolonies")) {
                return getSafeMineColoniesDrop(path);
            }

            if (namespace.equals("iceandfire") && Loader.isModLoaded("iceandfire")) {
                String dragonType = getDragonType(path);

                if (dragonType == null) {
                    return "tinyfarmboss:entities/safe_iceandfire_default";
                }

                long ageTicks = mobData != null ? mobData.getLong("AgeTicks") : 0L;
                int variant = mobData != null ? mobData.getInteger("Variant") : 0;
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

    private static ResourceLocation tryCreate(String id) {
        if (id == null || !VALID.matcher(id).matches()) return null;
        return new ResourceLocation(id);
    }

    private static String getDragonType(String path) {
        if (path.contains("fire_dragon") || path.contains("firedragon")) return "fire";
        if (path.contains("ice_dragon") || path.contains("icedragon")) return "ice";
        if (path.contains("lightning_dragon") || path.contains("lightningdragon")) return "lightning";
        return null;
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
        String dragonType = getDragonType(path);

        if (dragonType == null) {
            return "tinyfarmboss:entities/safe_iceandfire_default";
        }

        NBTTagCompound nbt = new NBTTagCompound();
        mob.writeToNBT(nbt);
        long ageTicks = nbt.getLong("AgeTicks");
        int variant = nbt.getInteger("Variant");
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
        return entityId.getResourceDomain() + ":entities/" + entityId.getResourcePath();
    }
}
