package com.tontoque28.tinyfarmboss;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableInjector {

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                // Wither
                if (id.equals(new ResourceLocation("minecraft", "entities/wither"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.NETHER_STAR));
                    tableBuilder.pool(poolBuilder.build());
                }
                // Ender Dragon
                if (id.equals(new ResourceLocation("minecraft", "entities/ender_dragon"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.DRAGON_EGG));
                    tableBuilder.pool(poolBuilder.build());
                }
                // Warden
                if (id.equals(new ResourceLocation("minecraft", "entities/warden"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.SCULK_CATALYST));
                    tableBuilder.pool(poolBuilder.build());
                }
                // Ice and Fire Dragons
                if (id.toString().startsWith("iceandfire:")) {
                    if (id.getPath().contains("dragon")) {
                        // Lógica para dragones de Ice and Fire
                    }
                }
            }
        });
    }
}