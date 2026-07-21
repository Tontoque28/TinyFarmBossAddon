package com.tontoque28.tinyfarmboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TinyFarmBoss.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LassoHandler {

    private static final String LASSO_ID = "tinymobfarm:lasso";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (!stack.isEmpty() && stack.getDescriptionId().contains(LASSO_ID)) {
            Entity entity = event.getTarget();
            if (entity instanceof LivingEntity && !(entity instanceof EnderDragonPart)) {
                if (event.getLevel().isClientSide) return;

                if (ModList.get().isLoaded("iceandfire")) {
                    String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();

                    if (entityId.startsWith("iceandfire:") && entityId.contains("dragon")) {
                        CompoundTag nbt = new CompoundTag();
                        entity.saveWithoutId(nbt);
                        long ageTicks = nbt.getLong("AgeTicks");

                        String dragonType = "fire";
                        if (entityId.contains("ice")) dragonType = "ice";
                        if (entityId.contains("lightning")) dragonType = "lightning";

                        // Ruta Maestra Directa
                        String lootTable = "tinyfarmboss:entities/" + dragonType + "_dragon_" +
                                (ageTicks >= 2400000L ? "stage5" : "stage4") + "_loot";

                        // Forzamos la escritura en el NBT del lazo capturado
                        if (stack.hasTag() && stack.getTag().contains("capturedMob")) {
                            CompoundTag capturedMob = stack.getTag().getCompound("capturedMob");
                            capturedMob.putString("mobLootTableLocation", lootTable);
                            stack.getTag().put("capturedMob", capturedMob);
                        }
                    }
                }
            }
        }
    }
}