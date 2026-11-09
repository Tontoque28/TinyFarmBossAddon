package com.tontoque28.tinyfarmboss;

import cn.davidma.tinymobfarm.core.util.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TinyFarmBoss.MODID)
public class LassoHandler {

    private static final String LASSO_ID = "tinymobfarm:lasso";

    private static final double CAPTURE_HEALTH_THRESHOLD = 0.5;

    private static final String MOB = "capturedMob";
    private static final String MOB_NAME = "mobName";
    private static final String MOB_ID = "mobId";
    private static final String MOB_DATA = "mobData";
    private static final String MOB_HEALTH = "mobHealth";
    private static final String MOB_MAX_HEALTH = "mobMaxHealth";
    private static final String MOB_HOSTILE = "mobHostile";
    private static final String MOB_LOOTTABLE_LOCATION = "mobLootTableLocation";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.isEmpty()) return;
        ResourceLocation itemKey = Item.REGISTRY.getNameForObject(stack.getItem());
        String itemId = itemKey != null ? itemKey.toString() : "";
        if (!itemId.equals(LASSO_ID)) return;

        Entity target = event.getTarget();
        if (!(target instanceof EntityLivingBase) || target instanceof MultiPartEntityPart) return;
        EntityLivingBase livingEntity = (EntityLivingBase) target;
        if (!(target instanceof EntityLiving) || !target.isEntityAlive()) return;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(MOB)) return;

        ResourceLocation key = EntityList.getKey(target);
        if (key == null) return;
        String entityId = key.toString();
        if (!isTrackedBoss(entityId)) return;

        double healthRatio = livingEntity.getHealth() / livingEntity.getMaxHealth();
        if (healthRatio > CAPTURE_HEALTH_THRESHOLD) {
            event.setCanceled(true);
            if (!event.getWorld().isRemote) {
                player.sendStatusMessage(
                        new TextComponentString("Este boss debe estar por debajo del 50% de vida para ser capturado."),
                        true
                );
            }
            return;
        }

        if (event.getWorld().isRemote) {
            event.setCanceled(true);
            return;
        }

        livingEntity.setHealth(livingEntity.getMaxHealth());

        if (target.isNonBoss()) {
            return;
        }

        captureBossManually(stack, livingEntity, entityId);
        event.setCanceled(true);
    }

    private static void captureBossManually(ItemStack stack, EntityLivingBase target, String entityId) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound(MOB);

        NBTTagCompound mobData = target.serializeNBT();
        NBTTagList rotation = new NBTTagList();
        rotation.appendTag(new NBTTagFloat(0));
        rotation.appendTag(new NBTTagFloat(0));
        mobData.setTag("Rotation", rotation);
        mobData.removeTag("Fire");
        mobData.removeTag("HurtTime");

        nbt.setTag(MOB_DATA, mobData);
        nbt.setString(MOB_NAME, target.getName());
        nbt.setString(MOB_ID, entityId);
        String original = target instanceof EntityLiving ? EntityHelper.getLootTableLocation((EntityLiving) target) : "";
        nbt.setString(MOB_LOOTTABLE_LOCATION, TfbHooks.resolveLootTable(target, original));
        nbt.setDouble(MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
        nbt.setDouble(MOB_MAX_HEALTH, target.getMaxHealth());
        nbt.setBoolean(MOB_HOSTILE, target.isCreatureType(EnumCreatureType.MONSTER, false));

        stack.getTagCompound().setTag(MOB, nbt);

        target.setDead();
    }

    private static boolean isTrackedBoss(String entityId) {
        if (entityId.startsWith("iceandfire:") && entityId.contains("dragon") && Loader.isModLoaded("iceandfire")) {
            return true;
        }
        return entityId.equals("minecraft:wither")
                || entityId.equals("minecraft:ender_dragon");
    }
}
