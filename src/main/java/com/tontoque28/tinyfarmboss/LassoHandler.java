package com.tontoque28.tinyfarmboss;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.item.component.LassoData;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class LassoHandler {

    private static final String LASSO_ID = "tinymobfarm:lasso";

    public static InteractionResult onEntityInteract(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.PASS;

        ItemStack itemStack = player.getItemInHand(hand);
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        if (itemRegistryName == null || !itemRegistryName.toString().equals(LASSO_ID)) {
            return InteractionResult.PASS;
        }

        if (target instanceof EnderDragonPart) {
            target = ((EnderDragonPart) target).parentMob;
        }

        ResourceLocation entityIdLocation = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        if (entityIdLocation == null) return InteractionResult.PASS;
        String entityId = entityIdLocation.toString();

        for (String blacklisted : TinyFarmBoss.config.blacklisted_mobs) {
            if (entityId.startsWith(blacklisted.replace("*", ""))) {
                player.sendSystemMessage(Component.literal("§c[TinyFarmBoss] §fEsta entidad está en la lista negra."));
                return InteractionResult.FAIL;
            }
        }

        boolean isBoss = false;
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.isAlive() && !livingTarget.canUsePortal(true)) {
                isBoss = true;
            }
        }

        if (entityId.equals("minecraft:wither") || entityId.equals("minecraft:ender_dragon") || entityId.equals("minecraft:warden")) {
            isBoss = true;
        }

        if (FabricLoader.getInstance().isModLoaded("cataclysm")) {
            if (entityId.equals("cataclysm:ignis") || entityId.equals("cataclysm:ender_golem") || entityId.equals("cataclysm:netherite_monstrosity")) {
                isBoss = true;
            }
        }

        if (FabricLoader.getInstance().isModLoaded("iceandfire")) {
            if (entityId.equals("iceandfire:fire_dragon") || entityId.equals("iceandfire:ice_dragon") || entityId.equals("iceandfire:lightning_dragon")) {
                isBoss = true;
            }
        }

        if (FabricLoader.getInstance().isModLoaded("terramity")) {
            if (entityIdLocation.getNamespace().equals("terramity")) {
                isBoss = true;
            }
        }

        if (TinyFarmBoss.config.custom_farmable_mobs.contains(entityId)) {
            isBoss = true;
        }

        boolean isCoreTrackedBoss = entityId.equals("minecraft:wither")
                || entityId.equals("minecraft:ender_dragon")
                || entityId.equals("minecraft:warden")
                || entityId.equals("iceandfire:fire_dragon")
                || entityId.equals("iceandfire:ice_dragon")
                || entityId.equals("iceandfire:lightning_dragon");

        if (isBoss && isCoreTrackedBoss && target instanceof LivingEntity livingTarget) {
            double healthRatio = livingTarget.getHealth() / livingTarget.getMaxHealth();
            if (healthRatio > 0.5D) {
                player.sendSystemMessage(Component.literal("§c[TinyFarmBoss] §fEste boss debe estar por debajo del 50% de vida para ser capturado."));
                return InteractionResult.FAIL;
            }
            livingTarget.setHealth(livingTarget.getMaxHealth());
        }

        if (isBoss) {
            Entity finalTarget = target;
            EntityCrashHandler.safeProcessEntity(target, "CAPTURE_BOSS", () -> {
                captureBoss(player, itemStack, finalTarget, hand);
            });
            return InteractionResult.SUCCESS;
        } else {

            String namespace = entityIdLocation.getNamespace();
            if (entityId.equals("minecraft:slime") || namespace.equals("minecolonies") || namespace.equals("iceandfire")) {
                Entity finalTarget = target;
                EntityCrashHandler.safeProcessEntity(target, "CAPTURE_COMPLEX_MOB", () -> {
                    captureBoss(player, itemStack, finalTarget, hand);
                });
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static void captureBoss(Player player, ItemStack lasso, Entity boss, InteractionHand hand) {
        if (!(boss instanceof LivingEntity livingBoss)) return;

        if (SlimeCompatibilitySystem.isSlimeOrVariant(boss)) {
            if (!SlimeCompatibilitySystem.validateEntityForFarm(boss)) {
                player.sendSystemMessage(Component.literal("§c[TinyFarmBoss] §fError: Entidad corrupta, captura cancelada."));
                return;
            }
        }

        CompoundTag mobData = new CompoundTag();
        try {
            boss.save(mobData);

            if (SlimeCompatibilitySystem.isSlimeOrVariant(boss)) {
                SlimeCompatibilitySystem.sanitizeAndPrepareSlimeForCapture(boss, mobData);
            }
        } catch (Exception e) {

            mobData = new CompoundTag();
        }

        mobData.remove("UUID");
        mobData.remove("Pos");
        mobData.remove("Motion");
        mobData.remove("Rotation");

        boolean isHostile = boss instanceof Enemy;

        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(boss.getType());

        String lootTable = SafeMobSimulationHandler.getSafeLootTable(boss);
        ResourceLocation lootTableId = ResourceLocation.tryParse(lootTable);
        if (lootTableId == null) {
            lootTableId = ResourceLocation.withDefaultNamespace("empty");
        }

        LassoData lassoData = new LassoData(
                boss.getDisplayName().getString(),
                entityId,
                mobData,
                livingBoss.getHealth(),
                livingBoss.getMaxHealth(),
                isHostile,
                lootTableId
        );
        lasso.set(TinyMobFarm.LASSO_DATA.get(), lassoData);

        if (boss.hasCustomName()) {
            lasso.set(DataComponents.CUSTOM_NAME, boss.getCustomName());
        } else {
            lasso.set(DataComponents.CUSTOM_NAME, boss.getDisplayName());
        }

        boss.discard();

        player.setItemInHand(hand, lasso);
    }
}
