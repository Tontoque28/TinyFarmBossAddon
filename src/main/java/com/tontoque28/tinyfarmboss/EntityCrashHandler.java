package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityCrashHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LOG_PREFIX = "[TMF_ADDON][CRASH_DETECTED]";
    private static final Map<UUID, Long> lastCrashTime = new ConcurrentHashMap<>();
    private static final long COOLDOWN_MS = 10000;

    public static boolean enableCrashReports = true;
    public static boolean debugEntitySystem = true;

    public static void safeProcessEntity(Entity entity, String action, Runnable logic) {
        if (entity == null) return;
        try {
            logic.run();
        } catch (Exception e) {
            handleEntityCrash(entity, action, e);
        }
    }

    private static void handleEntityCrash(Entity entity, String action, Exception exception) {
        UUID entityUuid = entity.getUniqueID();
        long currentTime = System.currentTimeMillis();

        if (lastCrashTime.containsKey(entityUuid)) {
            if (currentTime - lastCrashTime.get(entityUuid) < COOLDOWN_MS) {
                applyFallback(entity);
                return;
            }
        }
        lastCrashTime.put(entityUuid, currentTime);

        if (enableCrashReports) {
            generateCrashReport(entity, action, exception);
        }

        LOGGER.error("{} Entity: {} Action: {}", LOG_PREFIX, EntityList.getKey(entity), action);
        if (debugEntitySystem) {
            LOGGER.error("Exception details:", exception);
        }

        applyFallback(entity);
    }

    private static void applyFallback(Entity entity) {
        if (entity instanceof EntitySlime) {
            try {
                ((EntitySlime) entity).setSlimeSize(1, true);
            } catch (Exception ignored) {}
        }
    }

    private static void generateCrashReport(Entity entity, String action, Exception exception) {
        try {
            File logDir = new File(Loader.instance().getConfigDir().getParentFile(), "logs/tmf_entity_crashes");
            if (!logDir.exists() && !logDir.mkdirs()) {
                LOGGER.warn("[TMF_ADDON] Could not create crash report directory.");
                return;
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            File crashFile = new File(logDir, "entity_crash_" + timestamp + ".txt");

            try (PrintWriter writer = new PrintWriter(new FileWriter(crashFile))) {
                writer.println("========================================");
                writer.println("TINY MOB FARM ADDON - ENTITY CRASH REPORT");
                writer.println("========================================");
                writer.println();
                writer.println("Date/Time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                writer.println("Action: " + action);
                writer.println();
                writer.println("Entity Info:");
                writer.println(getEntityDebugInfo(entity));
                writer.println();
                writer.println("Environment:");
                writer.println("- SAFE MODE: " + detectModdedEnvironment());
                writer.println();
                writer.println("Error:");
                writer.println("- Message: " + exception.getMessage());
                writer.println("- Stacktrace:");
                exception.printStackTrace(writer);
                writer.println();
                writer.println("========================================");
            }

            LOGGER.error("{} Report saved at: {}", LOG_PREFIX, crashFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("[TMF_ADDON] Failed to write entity crash report.", e);
        }
    }

    private static String getEntityDebugInfo(Entity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("- Type: ").append(EntityList.getKey(entity)).append("\n");
        sb.append("- Class: ").append(entity.getClass().getName()).append("\n");
        sb.append("- UUID: ").append(entity.getUniqueID()).append("\n");
        sb.append("- Position: ").append(String.format("%.2f, %.2f, %.2f", entity.posX, entity.posY, entity.posZ)).append("\n");
        sb.append("- Dimension: ").append(entity.world.provider.getDimension()).append("\n");

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            sb.append("- Health: ").append(living.getHealth()).append("/").append(living.getMaxHealth()).append("\n");
        }

        if (entity instanceof EntitySlime) {
            try {
                sb.append("- Slime Size: ").append(((EntitySlime) entity).getSlimeSize()).append("\n");
            } catch (Exception e) {
                sb.append("- Slime Size: [Error reading size]\n");
            }
        }

        try {
            NBTTagCompound nbt = new NBTTagCompound();
            entity.writeToNBT(nbt);
            String nbtStr = nbt.toString();
            if (nbtStr.length() > 1000) {
                nbtStr = nbtStr.substring(0, 1000) + "... [TRUNCATED]";
            }
            sb.append("- NBT Summary: ").append(nbtStr).append("\n");
        } catch (Exception e) {
            sb.append("- NBT Summary: [Error reading NBT: ").append(e.getMessage()).append("]\n");
        }

        return sb.toString();
    }

    private static boolean detectModdedEnvironment() {
        return Loader.isModLoaded("radium")
                || Loader.isModLoaded("entity_model_features")
                || Loader.isModLoaded("entityculling")
                || Loader.isModLoaded("optifine");
    }
}
