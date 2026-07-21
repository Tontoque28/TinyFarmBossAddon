package com.tontoque28.tinyfarmboss.init;

import com.mojang.serialization.Codec;
import com.tontoque28.tinyfarmboss.TinyFarmBoss;
import com.tontoque28.tinyfarmboss.loot.BossLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootModifiers {

    private ModLootModifiers() {}

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TinyFarmBoss.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> BOSS_LOOT =
            REGISTRY.register("boss_loot", () -> BossLootModifier.CODEC);
}
