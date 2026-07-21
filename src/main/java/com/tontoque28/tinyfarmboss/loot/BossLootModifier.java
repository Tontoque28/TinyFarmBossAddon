package com.tontoque28.tinyfarmboss.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tontoque28.tinyfarmboss.compat.DragonLootInjector;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BossLootModifier extends LootModifier {

    public static final Codec<BossLootModifier> CODEC = RecordCodecBuilder.create(inst ->
        codecStart(inst).apply(inst, BossLootModifier::new)
    );

    public BossLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Esta es la clave: no tenemos acceso directo al lazo aquí.
        // La forma de pasar información al GLM es a través de LootContext.
        // Asumiremos que la entidad "this_entity" en el contexto es la que se está procesando.
        // O, si TinyMobFarm lo soporta, que el "tool" es el lazo.
        
        // Por ahora, no podemos llamar a DragonLootInjector porque no tenemos el ItemStack del lazo.
        // Dejaremos la estructura lista para cuando sepamos cómo obtener el lazo.

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
