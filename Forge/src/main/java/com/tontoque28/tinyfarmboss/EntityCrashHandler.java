package com.tontoque28.tinyfarmboss;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityCrashHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LOG_PREFIX = "[TMF_ADDON][CRASH_DETECTED]";
    private static final Map<UUID, Long> lastCrashTime = new ConcurrentHashMap<>();
    private static final long COOLDOWN_MS = 10000; // 10 seconds

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
        UUID entityUuid = entity.getUUID();
        long currentTime = System.currentTimeMillis();

        if (lastCrashTime.containsKey(entityUuid)) {
            if (currentTime - lastCrashTime.get(entityUuid) < COOLDOWN_MS) {
                applyFallback(entity);
                return; // On cooldown, skip report
            }
        }
        lastCrashTime.put(entityUuid, currentTime);

        if (enableCrashReports) {
            generateCrashReport(entity, action, exception);
        }

        LOGGER.error("{} Entity: {} Action: {}", LOG_PREFIX, ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()), action);
        if (debugEntitySystem) {
            LOGGER.error("Exception details:", exception);
        }

        applyFallback(entity);
    }

    private static void applyFallback(Entity entity) {
        if (entity instanceof Slime slime) {
            try {
                slime.setSize(1, true);
            } catch (Exception ignored) {}
        }
        // Generic entities: cancel processing implicitly since the exception was caught and execution stopped.
    }

    private static void generateCrashReport(Entity entity, String action, Exception exception) {
        try {
            File logDir = new File(FMLPaths.GAMEDIR.get().toFile(), "logs/tmf_entity_crashes");
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
                // Limited mod list printing could go here if needed
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
        sb.append("- Type: ").append(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).append("\n");
        sb.append("- Class: ").append(entity.getClass().getName()).append("\n");
        sb.append("- UUID: ").append(entity.getUUID()).append("\n");
        sb.append("- Position: ").append(String.format("%.2f, %.2f, %.2f", entity.getX(), entity.getY(), entity.getZ())).append("\n");
        sb.append("- Dimension: ").append(entity.level().dimension().location()).append("\n");
        
        if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
            sb.append("- Health: ").append(living.getHealth()).append("/").append(living.getMaxHealth()).append("\n");
        }

        if (entity instanceof Slime slime) {
            try {
                sb.append("- Slime Size: ").append(slime.getSize()).append("\n");
            } catch (Exception e) {
                sb.append("- Slime Size: [Error reading size]\n");
            }
        }

        try {
            CompoundTag nbt = new CompoundTag();
            entity.saveWithoutId(nbt);
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
        boolean hasComplexMods = false;
        try {
            if (ModList.get() != null) {
                if (ModList.get().isLoaded("radium") || 
                    ModList.get().isLoaded("entity_model_features") ||
                    ModList.get().isLoaded("entityculling") ||
                    ModList.get().isLoaded("optifine")) {
                    hasComplexMods = true;
                }
            }
        } catch (Exception ignored) {}
        return hasComplexMods;
    }
}
