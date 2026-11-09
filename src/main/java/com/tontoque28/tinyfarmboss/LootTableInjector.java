package com.tontoque28.tinyfarmboss;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class LootTableInjector {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin()) {
                ResourceLocation id = key.location();

                if (id.equals(ResourceLocation.fromNamespaceAndPath("minecraft", "entities/wither"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.NETHER_STAR));
                    tableBuilder.pool(poolBuilder.build());
                }

                if (id.equals(ResourceLocation.fromNamespaceAndPath("minecraft", "entities/ender_dragon"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.DRAGON_EGG));
                    tableBuilder.pool(poolBuilder.build());
                }

                if (id.equals(ResourceLocation.fromNamespaceAndPath("minecraft", "entities/warden"))) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.SCULK_CATALYST));
                    tableBuilder.pool(poolBuilder.build());
                }

                if (id.toString().startsWith("iceandfire:")) {
                    if (id.getPath().contains("dragon")) {

                    }
                }
            }
        });
    }
}
