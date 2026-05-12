package com.tontoque28.tinyfarmboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TinyFarmBoss.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LassoHandler {

    private static final String LASSO_ID = "tinymobfarm:lasso";
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;

        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        Entity target = event.getTarget();

        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(itemStack.getItem());

        if (itemRegistryName == null || !itemRegistryName.toString().equals(LASSO_ID)) {
            return;
        }

        // Manejo especial para partes del Ender Dragon
        if (target instanceof EnderDragonPart part) {
            target = part.parentMob;
        }

        boolean isBoss = false;
        if (target instanceof LivingEntity livingTarget) {
            if (!livingTarget.canChangeDimensions()) {
                isBoss = true;
            }
        }
        
        ResourceLocation entityIdLocation = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (entityIdLocation == null) return;
        String entityId = entityIdLocation.toString();
        
        if (entityId.equals("minecraft:wither") || entityId.equals("minecraft:ender_dragon") || entityId.equals("minecraft:warden")) {
             isBoss = true;
        }

        if (isBoss) {
            Entity finalTarget = target;
            EntityCrashHandler.safeProcessEntity(target, "CAPTURE_BOSS", () -> {
                captureBoss(player, itemStack, finalTarget, event.getHand());
            });
            event.setCanceled(true);
        } else {
            // Re-intercepting for specific complex mods
            String namespace = entityIdLocation.getNamespace();
            if (entityId.equals("minecraft:slime") || namespace.equals("minecolonies") || namespace.equals("iceandfire")) {
               Entity finalTarget = target;
               EntityCrashHandler.safeProcessEntity(target, "CAPTURE_COMPLEX_MOB", () -> {
                   captureBoss(player, itemStack, finalTarget, event.getHand());
               });
               event.setCanceled(true);
            }
        }
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
        
        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(boss.getType());
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