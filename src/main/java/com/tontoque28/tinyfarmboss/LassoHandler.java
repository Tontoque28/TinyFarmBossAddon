package com.tontoque28.tinyfarmboss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TinyFarmBoss.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.isEmpty()) return;
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(stack.getItem());
        String itemId = itemKey != null ? itemKey.toString() : "";
        if (!itemId.equals(LASSO_ID)) return;

        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity) || target instanceof EnderDragonPartEntity) return;
        LivingEntity livingEntity = (LivingEntity) target;
        if (!(target instanceof MobEntity) || !target.isAlive()) return;

        if (stack.hasTag() && stack.getTag().contains(MOB)) return;

        String entityId = ForgeRegistries.ENTITIES.getKey(target.getType()).toString();
        if (!isTrackedBoss(entityId)) return;

        double healthRatio = livingEntity.getHealth() / livingEntity.getMaxHealth();
        if (healthRatio > CAPTURE_HEALTH_THRESHOLD) {
            event.setCanceled(true);
            if (!event.getWorld().isRemote) {
                player.sendStatusMessage(
                        new StringTextComponent("Este boss debe estar por debajo del 50% de vida para ser capturado."),
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

        if (target.canChangeDimension()) {
            return;
        }

        captureBossManually(stack, livingEntity, entityId);
        event.setCanceled(true);
    }

    private static void captureBossManually(ItemStack stack, LivingEntity target, String entityId) {
        CompoundNBT nbt = stack.getOrCreateChildTag(MOB);

        CompoundNBT mobData = target.writeWithoutTypeId(new CompoundNBT());
        ListNBT rotation = new ListNBT();
        rotation.add(DoubleNBT.valueOf(0));
        rotation.add(DoubleNBT.valueOf(0));
        mobData.put("Rotation", rotation);
        mobData.remove("Fire");
        mobData.remove("HurtTime");

        nbt.put(MOB_DATA, mobData);
        nbt.putString(MOB_NAME, target.getName().getString());
        nbt.putString(MOB_ID, entityId);
        nbt.putString(MOB_LOOTTABLE_LOCATION, target.getLootTableResourceLocation().toString());
        nbt.putDouble(MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
        nbt.putDouble(MOB_MAX_HEALTH, target.getMaxHealth());
        nbt.putBoolean(MOB_HOSTILE, target instanceof MonsterEntity);

        stack.getOrCreateTag().put(MOB, nbt);

        target.remove();
    }

    private static boolean isTrackedBoss(String entityId) {
        if (entityId.startsWith("iceandfire:") && entityId.contains("dragon") && ModList.get().isLoaded("iceandfire")) {
            return true;
        }
        return entityId.equals("minecraft:wither")
                || entityId.equals("minecraft:ender_dragon");
    }
}
