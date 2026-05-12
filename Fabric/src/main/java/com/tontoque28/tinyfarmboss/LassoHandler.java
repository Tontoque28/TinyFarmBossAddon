package com.tontoque28.tinyfarmboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.loader.api.FabricLoader;

public class LassoHandler {

    private static final String LASSO_ID = "tinymobfarm:lasso";
    
    public static InteractionResult onEntityInteract(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.PASS;

        ItemStack itemStack = player.getItemInHand(hand);
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        if (itemRegistryName == null || !itemRegistryName.toString().equals(LASSO_ID)) {
            return InteractionResult.PASS;
        }

        // Manejo especial para partes del Ender Dragon
        if (target instanceof EnderDragonPart) {
            target = ((EnderDragonPart) target).parentMob;
        }

        ResourceLocation entityIdLocation = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        if (entityIdLocation == null) return InteractionResult.PASS;
        String entityId = entityIdLocation.toString();

        // Blacklist
        for (String blacklisted : TinyFarmBoss.config.blacklisted_mobs) {
            if (entityId.startsWith(blacklisted.replace("*", ""))) {
                player.sendSystemMessage(Component.literal("§c[TinyFarmBoss] §fEsta entidad está en la lista negra."));
                return InteractionResult.FAIL;
            }
        }

        boolean isBoss = false;
        if (target instanceof LivingEntity livingTarget) {
            if (!livingTarget.canChangeDimensions()) {
                isBoss = true;
            }
        }
        
        if (entityId.equals("minecraft:wither") || entityId.equals("minecraft:ender_dragon") || entityId.equals("minecraft:warden")) {
             isBoss = true;
        }
        
        // Soft Dependencies: Cataclysm
        if (FabricLoader.getInstance().isModLoaded("cataclysm")) {
            if (entityId.equals("cataclysm:ignis") || entityId.equals("cataclysm:ender_golem") || entityId.equals("cataclysm:netherite_monstrosity")) {
                isBoss = true;
            }
        }

        // Soft Dependencies: Ice & Fire
        if (FabricLoader.getInstance().isModLoaded("iceandfire")) {
            if (entityId.equals("iceandfire:fire_dragon") || entityId.equals("iceandfire:ice_dragon") || entityId.equals("iceandfire:lightning_dragon")) {
                isBoss = true;
            }
        }

        // Soft Dependencies: Terramity
        if (FabricLoader.getInstance().isModLoaded("terramity")) {
            if (entityIdLocation.getNamespace().equals("terramity")) {
                isBoss = true;
            }
        }

        // Whitelist
        if (TinyFarmBoss.config.custom_farmable_mobs.contains(entityId)) {
            isBoss = true;
        }

        if (isBoss) {
            Entity finalTarget = target;
            EntityCrashHandler.safeProcessEntity(target, "CAPTURE_BOSS", () -> {
                captureBoss(player, itemStack, finalTarget, hand);
            });
            return InteractionResult.SUCCESS;
        } else {
            // Re-intercepting for specific complex mods
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

        // FASE 1: Validación general de Slime y entidades complejas para evitar crash preventivo
        if (SlimeCompatibilitySystem.isSlimeOrVariant(boss)) {
            if (!SlimeCompatibilitySystem.validateEntityForFarm(boss)) {
                 player.sendSystemMessage(Component.literal("§c[TinyFarmBoss] §fError: Entidad corrupta, captura cancelada."));
                 return;
            }
        }

        CompoundTag mobData = new CompoundTag();
        try {
            boss.save(mobData);
            
            // FASE 2, 3 y 4: Sanitizar en caso de que sea un slime
            if (SlimeCompatibilitySystem.isSlimeOrVariant(boss)) {
                SlimeCompatibilitySystem.sanitizeAndPrepareSlimeForCapture(boss, mobData);
            }
        } catch (Exception e) {
            // Fallback seguro si falla el guardado nativo del mob
            mobData = new CompoundTag(); 
        }
        
        mobData.remove("UUID");
        mobData.remove("Pos");
        mobData.remove("Motion");
        mobData.remove("Rotation");
        
        CompoundTag capturedMobTag = new CompoundTag();
        
        capturedMobTag.putString("mobName", boss.getDisplayName().getString());
        
        boolean isHostile = boss instanceof Enemy;
        capturedMobTag.putBoolean("mobHostile", isHostile);
        
        capturedMobTag.putDouble("mobMaxHealth", livingBoss.getMaxHealth());
        
        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(boss.getType());
        String idString = entityId != null ? entityId.toString() : "minecraft:pig";
        capturedMobTag.putString("mobId", idString);
        
        // --- LÓGICA DE LOOT TABLE PERSONALIZADA SEGURA ---
        String lootTable = SafeMobSimulationHandler.getSafeLootTable(boss);
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        // ------------------------------------------
        
        capturedMobTag.put("mobData", mobData);
        
        capturedMobTag.putDouble("mobHealth", livingBoss.getHealth());

        CompoundTag itemTag = lasso.getOrCreateTag();
        itemTag.put("capturedMob", capturedMobTag);
        
        itemTag.remove("Entity");
        itemTag.remove("EntityData");
        
        if (boss.hasCustomName()) {
            lasso.setHoverName(boss.getCustomName());
        } else {
            lasso.setHoverName(boss.getDisplayName());
        }

        boss.discard();
        
        player.setItemInHand(hand, lasso);
    }
}