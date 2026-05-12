package com.tontoque28.tinyfarmboss;

import com.tontoque28.tinyfarmboss.config.TinyFarmBossConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

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
        String namespace = entityIdLocation.getNamespace();

        if (isBlacklisted(entityIdLocation)) {
             player.sendSystemMessage(Component.translatable("message.tinyfarmbossaddon.blacklist"));
             event.setCanceled(true);
             return;
        }
        
        if (entityId.equals("minecraft:wither") || entityId.equals("minecraft:ender_dragon") || entityId.equals("minecraft:warden")) {
             isBoss = true;
        }

        if (ModList.get().isLoaded("iceandfire")) {
            if (entityId.equals("iceandfire:fire_dragon") || 
                entityId.equals("iceandfire:ice_dragon") || 
                entityId.equals("iceandfire:lightning_dragon")) {
                isBoss = true;
            }
        }

        if (ModList.get().isLoaded("cataclysm")) {
            if (entityId.equals("cataclysm:ignis") || 
                entityId.equals("cataclysm:ender_golem") || 
                entityId.equals("cataclysm:netherite_monstrosity")) {
                isBoss = true;
            }
        }

        if (isWhitelisted(entityId)) {
            isBoss = true;
        }

        if (isBoss) {
            Entity finalTarget = target;
            EntityCrashHandler.safeProcessEntity(target, "CAPTURE_BOSS", () -> {
                captureBoss(player, itemStack, finalTarget, event.getHand());
            });
            event.setCanceled(true);
        }
    }

    private static boolean isBlacklisted(ResourceLocation entityIdLocation) {
        List<? extends String> blacklisted = TinyFarmBossConfig.BLACKLISTED_MOBS.get();
        String fullId = entityIdLocation.toString();
        String namespace = entityIdLocation.getNamespace();

        for (String b : blacklisted) {
            if (b.endsWith(":*")) {
                if (namespace.equals(b.substring(0, b.indexOf(":*")))) return true;
            } else if (b.equals(fullId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWhitelisted(String entityId) {
        return TinyFarmBossConfig.CUSTOM_FARMABLE_MOBS.get().contains(entityId);
    }

    private static void captureBoss(Player player, ItemStack lasso, Entity boss, InteractionHand hand) {
        if (!(boss instanceof LivingEntity livingBoss)) return;

        if (SlimeCompatibilitySystem.isSlimeOrVariant(boss)) {
            if (!SlimeCompatibilitySystem.validateEntityForFarm(boss)) {
                 player.sendSystemMessage(Component.translatable("message.tinyfarmbossaddon.corrupt_entity"));
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
        
        CompoundTag capturedMobTag = new CompoundTag();
        
        capturedMobTag.putString("mobName", boss.getDisplayName().getString());
        capturedMobTag.putBoolean("mobHostile", boss instanceof Enemy);
        capturedMobTag.putDouble("mobMaxHealth", livingBoss.getMaxHealth());
        
        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(boss.getType());
        String idString = entityId != null ? entityId.toString() : "minecraft:pig";
        capturedMobTag.putString("mobId", idString);
        
        // NUEVA LÓGICA: Asignar Loot Table correcta
        String lootTable = SafeMobSimulationHandler.getSafeLootTable(boss);
        if (ModList.get().isLoaded("iceandfire") && idString.startsWith("iceandfire:")) {
            long ageTicks = mobData.getLong("AgeTicks");
            if (ageTicks >= 1728000) { // Etapa 4 o superior
                lootTable = "tinyfarmbossaddon:entities/ice_dragon_stage4_loot";
            }
        }
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        
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
