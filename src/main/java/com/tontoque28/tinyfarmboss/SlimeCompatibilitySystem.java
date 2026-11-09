package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SlimeCompatibilitySystem {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LOG_PREFIX = "[TMF_ADDON][SLIME_SYSTEM]";

    public static boolean isSlimeOrVariant(Entity entity) {
        return entity instanceof EntitySlime;
    }

    public static boolean validateEntityForFarm(Entity entity) {
        if (entity == null) {
            LOGGER.error("{} Entity is null, validation failed.", LOG_PREFIX);
            return false;
        }

        try {
            NBTTagCompound testData = new NBTTagCompound();
            if (!entity.writeToNBTAtomically(testData)) {
                entity.writeToNBT(testData);
            }
            if (testData.hasNoTags()) {
                LOGGER.warn("{} Entity data is extremely empty or corrupted: {}", LOG_PREFIX, EntityList.getKey(entity));
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("{} Entity data is corrupted and threw an exception during validation: {}", LOG_PREFIX, EntityList.getKey(entity), e);
            return false;
        }
    }

    public static void sanitizeAndPrepareSlimeForCapture(Entity entity, NBTTagCompound mobDataOut) {
        if (!isSlimeOrVariant(entity)) return;

        try {
            EntitySlime slime = (EntitySlime) entity;
            int originalSize = slime.getSlimeSize();

            mobDataOut.setInteger("Jarvis_SlimeSize", originalSize);

            slime.motionX = 0;
            slime.motionY = 0;
            slime.motionZ = 0;
            slime.setInvisible(false);
            slime.setNoGravity(false);
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

            NBTTagCompound cleanData = new NBTTagCompound();
            slime.writeToNBT(cleanData);

            for (String key : new ArrayList<>(cleanData.getKeySet())) {
                mobDataOut.setTag(key, cleanData.getTag(key));
            }

            mobDataOut.setInteger("Size", 0);

        } catch (Exception e) {
            LOGGER.error("{} Error sanitizing slime. Applying global fallback.", LOG_PREFIX, e);
            applyGlobalFallback(mobDataOut);
        }
    }

    private static boolean detectModdedEnvironment() {
        return Loader.isModLoaded("radium")
                || Loader.isModLoaded("entity_model_features")
                || Loader.isModLoaded("entityculling")
                || Loader.isModLoaded("optifine");
    }

    private static void applyGlobalFallback(NBTTagCompound mobDataOut) {
        mobDataOut.setInteger("Size", 0);
        mobDataOut.setInteger("Jarvis_SlimeSize", 1);
        LOGGER.warn("{} Applied Global Fallback: Forced basic Slime size 1 logic.", LOG_PREFIX);
    }
}
