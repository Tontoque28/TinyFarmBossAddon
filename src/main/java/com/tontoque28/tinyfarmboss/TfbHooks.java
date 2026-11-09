package com.tontoque28.tinyfarmboss;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TfbHooks {

    public static boolean forceWorking(Object farmObject) {
        try {
            TileEntityMobFarm farm = (TileEntityMobFarm) farmObject;
            ItemStack lasso = farm.getLasso();
            if (!lasso.isEmpty() && lasso.hasTagCompound()) {
                NBTTagCompound baseTag = NBTHelper.getBaseTag(lasso);
                if (baseTag != null) {
                    String customLocation = baseTag.getString("mobLootTableLocation");
                    return !customLocation.isEmpty() && !farm.isPowered();
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static String resolveLootTable(EntityLivingBase entity, String original) {
        if (!(entity instanceof EntityLiving)) return original;
        ResourceLocation key = EntityList.getKey(entity);
        if (key == null) return original;
        String safe = SafeMobSimulationHandler.getSafeLootTable(entity);
        if (safe == null) return original;
        String defaultTable = key.getResourceDomain() + ":entities/" + key.getResourcePath();
        return safe.equals(defaultTable) ? original : safe;
    }

    public static String lootTableLocation(EntityLiving entity, String original) {
        try {
            return resolveLootTable(entity, original);
        } catch (Exception e) {
            return original;
        }
    }

    public static ResourceLocation lootTable(ResourceLocation original, EntityLivingBase entity) {
        try {
            if (!(entity instanceof EntityLiving)) return original;
            String originalName = original != null ? original.toString() : null;
            String resolved = resolveLootTable(entity, originalName);
            if (resolved == null || resolved.equals(originalName)) return original;
            return new ResourceLocation(resolved);
        } catch (Exception e) {
            return original;
        }
    }
}
