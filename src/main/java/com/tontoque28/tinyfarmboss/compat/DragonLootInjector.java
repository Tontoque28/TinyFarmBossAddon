package com.tontoque28.tinyfarmboss.compat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class to inject additional drops from Ice and Fire into the Tiny Mob Farm system.
 * This class processes the NBT of a lasso item and returns any corresponding extra drops.
 */
public final class DragonLootInjector {

    // --- Configuration Constants ---
    private static final double STAGE_4_DROP_CHANCE = 0.01; // 1% chance
    private static final double STAGE_5_DROP_CHANCE = 0.03; // 3% chance
    private static final long STAGE_4_TICKS = 1_800_000L;
    private static final long STAGE_5_TICKS = 2_400_000L;

    // --- NBT Key Constants ---
    private static final String CAPTURED_MOB_TAG = "capturedMob";
    private static final String MOB_ID_TAG = "mobId";
    private static final String MOB_DATA_TAG = "mobData";
    private static final String GENDER_TAG = "Gender";
    private static final String AGE_TICKS_TAG = "AgeTicks";
    private static final String VARIANT_TAG = "Variant";

    public static List<ItemStack> getExtraDragonDrops(ItemStack lassoStack) {
        List<ItemStack> extraDrops = new ArrayList<>();

        Optional<CompoundTag> mobDataOpt = getMobData(lassoStack);
        if (mobDataOpt.isEmpty()) {
            return extraDrops;
        }

        CompoundTag mobData = mobDataOpt.get();
        String mobId = getCapturedMobId(lassoStack).orElse("");

        DragonMaturity maturity = getDragonMaturity(mobId, mobData);
        if (maturity == DragonMaturity.NONE) {
            return extraDrops;
        }

        double dropChance = (maturity == DragonMaturity.STAGE_5_MOTHER) ? STAGE_5_DROP_CHANCE : STAGE_4_DROP_CHANCE;

        if (Math.random() <= dropChance) {
            getDragonEggItem(mobId, mobData).ifPresent(egg -> extraDrops.add(new ItemStack(egg)));
        }
        if (Math.random() <= dropChance) {
            extraDrops.add(new ItemStack(Items.ELYTRA));
        }

        return extraDrops;
    }

    private static DragonMaturity getDragonMaturity(String mobId, CompoundTag mobData) {
        if (!mobId.startsWith("iceandfire:")) return DragonMaturity.NONE;
        boolean isFemale = mobData.contains(GENDER_TAG, Tag.TAG_BYTE) && mobData.getByte(GENDER_TAG) == 0;
        if (!isFemale) return DragonMaturity.NONE;

        if (mobData.contains(AGE_TICKS_TAG, Tag.TAG_LONG)) {
            long age = mobData.getLong(AGE_TICKS_TAG);
            if (age >= STAGE_5_TICKS) return DragonMaturity.STAGE_5_MOTHER;
            if (age >= STAGE_4_TICKS) return DragonMaturity.STAGE_4_MOTHER;
        }
        
        return DragonMaturity.NONE;
    }
    
    private static Optional<Item> getDragonEggItem(String mobId, CompoundTag mobData) {
        int variant = mobData.contains(VARIANT_TAG, Tag.TAG_INT) ? mobData.getInt(VARIANT_TAG) : 0;
        String eggColor;

        if (mobId.contains("fire_dragon")) {
            eggColor = switch (variant) {
                case 1 -> "green";
                case 2 -> "bronze";
                case 3 -> "gray";
                default -> "red";
            };
        } else if (mobId.contains("ice_dragon")) {
            eggColor = switch (variant) {
                case 1 -> "white";
                case 2 -> "sapphire";
                case 3 -> "silver";
                default -> "blue";
            };
        } else if (mobId.contains("lightning_dragon")) {
            eggColor = switch (variant) {
                case 1 -> "black";
                case 2 -> "copper";
                case 3 -> "electric";
                default -> "amythest"; // Note: I&F uses this spelling
            };
        } else {
            return Optional.empty();
        }
        
        String eggId = "dragonegg_" + eggColor;
        return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation("iceandfire", eggId)));
    }

    private static Optional<CompoundTag> getMobData(ItemStack stack) {
        return Optional.of(stack)
                .filter(ItemStack::hasTag).map(ItemStack::getTag)
                .filter(tag -> tag.contains(CAPTURED_MOB_TAG, Tag.TAG_COMPOUND)).map(tag -> tag.getCompound(CAPTURED_MOB_TAG))
                .filter(captured -> captured.contains(MOB_DATA_TAG, Tag.TAG_COMPOUND)).map(captured -> captured.getCompound(MOB_DATA_TAG));
    }

    private static Optional<String> getCapturedMobId(ItemStack stack) {
        return Optional.of(stack)
                .filter(ItemStack::hasTag).map(ItemStack::getTag)
                .filter(tag -> tag.contains(CAPTURED_MOB_TAG, Tag.TAG_COMPOUND)).map(tag -> tag.getCompound(CAPTURED_MOB_TAG))
                .filter(captured -> captured.contains(MOB_ID_TAG, Tag.TAG_STRING)).map(captured -> captured.getString(MOB_ID_TAG));
    }

    private enum DragonMaturity { NONE, STAGE_4_MOTHER, STAGE_5_MOTHER }
}
