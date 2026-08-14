package com.tontoque28.tinyfarmboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Monster;
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

    // Umbral de vida requerido para poder capturar a un boss con el lazo.
    private static final double CAPTURE_HEALTH_THRESHOLD = 0.5; // 50%

    // Claves NBT: deben coincidir EXACTAMENTE con com.daqem.tinymobfarm.util.NBTHelper
    // (verificado contra el código fuente de TinyMobFarm) para que el lazo pueda
    // leer/soltar/renderizar el mob capturado sin diferencias.
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
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (stack.isEmpty()) return;
        String itemId = ForgeRegistries.ITEMS.getKey(stack.getItem()) != null
                ? ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()
                : "";
        if (!itemId.equals(LASSO_ID)) return;

        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity livingEntity) || target instanceof EnderDragonPart) return;
        if (!(target instanceof Mob) || !target.isAlive()) return;

        // Si el lazo ya tiene un mob capturado, no lo tocamos: evita sobrescribir
        // accidentalmente un lazo lleno al clicar otra entidad con él en la mano.
        if (stack.hasTag() && stack.getTag().contains(MOB)) return;

        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(target.getType()).toString();
        if (!isTrackedBoss(entityId)) return; // el resto de mobs los maneja TinyMobFarm normalmente

        double healthRatio = livingEntity.getHealth() / livingEntity.getMaxHealth();
        if (healthRatio > CAPTURE_HEALTH_THRESHOLD) {
            // CRITICO: cancelamos en AMBOS lados (cliente y servidor). Si solo cancelamos en
            // el servidor, el lado cliente sigue procesando la interaccion sin trabas y
            // TinyMobFarm alcanza a mostrar su propio mensaje nativo "cannot capture boss"
            // (evaluado del lado cliente) antes de que el paquete llegue siquiera al servidor.
            event.setCanceled(true);
            if (!event.getLevel().isClientSide) {
                player.displayClientMessage(
                        Component.literal("Este boss debe estar por debajo del 50% de vida para ser capturado."),
                        true
                );
            }
            return;
        }

        if (event.getLevel().isClientSide) {
            // La captura real (curar, guardar NBT, eliminar la entidad) solo debe ocurrir
            // en el servidor. Igualmente cancelamos aqui para bloquear el flujo nativo
            // del lado cliente y evitar que se cuele su mensaje de rechazo.
            event.setCanceled(true);
            return;
        }

        // El boss ya está lo bastante débil: lo curamos al 100% antes de que se
        // complete la captura, para que el lazo lo guarde con todos sus usos.
        livingEntity.setHealth(livingEntity.getMaxHealth());

        // Si TinyMobFarm puede capturarlo de forma nativa (canChangeDimensions() == true,
        // como los dragones de Ice and Fire), dejamos que su propio LassoItem#interactMob
        // haga el trabajo: nuestro Mixin sobre LivingEntity#getLootTable ya fuerza la tabla
        // de loot correcta (por tipo/etapa/color) en ese momento.
        if (target.canChangeDimensions()) {
            return;
        }

        // Bosses vanilla como el Wither y el Ender Dragon tienen canChangeDimensions() == false,
        // por lo que LassoItem#interactMob los rechaza SIEMPRE con "cannot capture boss",
        // sin importar la vida. Replicamos aquí la captura manualmente (mismo formato NBT
        // que usa TinyMobFarm) y cancelamos el evento para que su lógica nativa no vuelva
        // a ejecutarse y bloquee la interacción.
        captureBossManually(stack, livingEntity, entityId);
        event.setCanceled(true);
    }

    private static void captureBossManually(ItemStack stack, LivingEntity target, String entityId) {
        CompoundTag nbt = stack.getOrCreateTagElement(MOB);

        CompoundTag mobData = target.saveWithoutId(new CompoundTag());
        ListTag rotation = new ListTag();
        rotation.add(DoubleTag.valueOf(0));
        rotation.add(DoubleTag.valueOf(0));
        mobData.put("Rotation", rotation);
        mobData.remove("Fire");
        mobData.remove("HurtTime");

        nbt.put(MOB_DATA, mobData);
        nbt.putString(MOB_NAME, target.getName().getString());
        nbt.putString(MOB_ID, entityId);
        // target.getLootTable() pasa por nuestro Mixin, que ya fuerza la ruta correcta
        // en tinyfarmboss:entities/... para los bosses rastreados.
        nbt.putString(MOB_LOOTTABLE_LOCATION, target.getLootTable().toString());
        nbt.putDouble(MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
        nbt.putDouble(MOB_MAX_HEALTH, target.getMaxHealth());
        nbt.putBoolean(MOB_HOSTILE, target instanceof Monster);

        stack.getOrCreateTag().put(MOB, nbt);

        target.discard();
    }

    private static boolean isTrackedBoss(String entityId) {
        if (entityId.startsWith("iceandfire:") && entityId.contains("dragon") && ModList.get().isLoaded("iceandfire")) {
            return true;
        }
        return entityId.equals("minecraft:wither")
                || entityId.equals("minecraft:ender_dragon")
                || entityId.equals("minecraft:warden");
    }
}