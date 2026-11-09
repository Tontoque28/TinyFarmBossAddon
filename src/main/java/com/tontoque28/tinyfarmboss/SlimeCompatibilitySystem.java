package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SlimeCompatibilitySystem {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LOG_PREFIX = "[TMF_ADDON][SLIME_SYSTEM]";

    public static boolean isSlimeOrVariant(Entity entity) {
        return entity instanceof SlimeEntity;
    }

    public static boolean validateEntityForFarm(Entity entity) {
        if (entity == null) {
            LOGGER.error("{} Entity is null, validation failed.", LOG_PREFIX);
            return false;
        }

        try {
            CompoundNBT testData = new CompoundNBT();
            if (!entity.writeUnlessPassenger(testData)) {
                entity.writeWithoutTypeId(testData);
            }
            if (testData.isEmpty()) {
                LOGGER.warn("{} Entity data is extremely empty or corrupted: {}", LOG_PREFIX, entity.getType().toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("{} Entity data is corrupted and threw an exception during validation: {}", LOG_PREFIX, entity.getType().toString(), e);
            return false;
        }
    }

    public static void sanitizeAndPrepareSlimeForCapture(Entity entity, CompoundNBT mobDataOut) {
        if (!isSlimeOrVariant(entity)) return;

        try {
            SlimeEntity slime = (SlimeEntity) entity;
            int originalSize = slime.getSlimeSize();

            mobDataOut.putInt("Jarvis_SlimeSize", originalSize);

            slime.setMotion(0, 0, 0);
            slime.setInvisible(false);
            slime.setNoGravity(false);
            slime.setPose(Pose.STANDING);
            slime.extinguish();
            slime.clearActivePotions();

            boolean safeMode = detectModdedEnvironment();
            if (safeMode || originalSize > 4 || originalSize <= 0) {
                LOGGER.info("{} SAFE MODE active or invalid size. Forcing safe size 1. Original size: {}", LOG_PREFIX, originalSize);
                slime.setSlimeSize(1, true);
            } else {
                LOGGER.info("{} Sanitizing Slime. Original Size: {}, Applied Size: 1 (forced for farm compatibility)", LOG_PREFIX, originalSize);
                slime.setSlimeSize(1, true);
            }

            CompoundNBT cleanData = new CompoundNBT();
            slime.writeWithoutTypeId(cleanData);

            for (String key : cleanData.keySet()) {
                mobDataOut.put(key, cleanData.get(key));
            }

            mobDataOut.putInt("Size", 0);

        } catch (Exception e) {
            LOGGER.error("{} Error sanitizing slime. Applying global fallback.", LOG_PREFIX, e);
            applyGlobalFallback(mobDataOut);
        }
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

    private static void applyGlobalFallback(CompoundNBT mobDataOut) {
        mobDataOut.putInt("Size", 0);
        mobDataOut.putInt("Jarvis_SlimeSize", 1);
        LOGGER.warn("{} Applied Global Fallback: Forced basic Slime size 1 logic.", LOG_PREFIX);
    }
}
