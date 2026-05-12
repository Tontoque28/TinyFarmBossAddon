# FULL_CODE_REFERENCES.md

Archivo de referencia completa de código histórico.

REGLAS CRÍTICAS:
- Nunca simplificar snippets.
- Nunca reformatear.
- Nunca modernizar sintaxis.
- Nunca eliminar bloques previos.
- Solo añadir nuevos bloques con separador.

====================================================

## [v1.1.0] DifficultyHandler.java - Cálculo de Dificultad Dinámica
```java
    public static double calculateDifficultyFactor(Level level, long totalTime) {
        long effectiveTime = Math.max(0, totalTime);
        long days = effectiveTime / 24000;

        double percentagePerDay = 0.0075; // Default NORMAL (0.75%)

        if (level instanceof ServerLevel serverLevel) {
            DifficultySavedData data = DifficultySavedData.get(serverLevel);

            if (data.isHardcoreMode()) {
                percentagePerDay = 0.015; // HARDCORE (1.5%)
            } else {
                switch (level.getDifficulty()) {
                    case PEACEFUL -> percentagePerDay = 0.0;
                    case EASY -> percentagePerDay = 0.005; // 0.5%
                    case NORMAL -> percentagePerDay = 0.0075; // 0.75%
                    case HARD -> percentagePerDay = 0.01; // 1.0%
                }
            }
        }

        return days * percentagePerDay;
    }
```

## [v1.1.0] DifficultyHandler.java - Detección de Jefes (Tag Support)
```java
    private static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "bosses"));

    private static boolean isBossEntity(Entity entity) {
        if (entity.getType().is(BOSS_TAG)) {
            return true;
        }
        return entity instanceof WitherBoss ||
               entity instanceof EnderDragon ||
               entity instanceof Warden ||
               entity instanceof IronGolem ||
               entity instanceof ElderGuardian;
    }
```

## [v1.1.0] DifficultySavedData.java - Persistencia Completa
```java
    public static DifficultySavedData load(CompoundTag tag) {
        DifficultySavedData data = new DifficultySavedData();
        data.additionalTime = tag.getLong("AdditionalTime");

        if (tag.contains("HardcoreMode")) {
            data.hardcoreMode = tag.getBoolean("HardcoreMode");
        }

        if (tag.contains("DevMode")) {
            data.devMode = tag.getBoolean("DevMode");
            // Sincronizar con la variable estática global al cargar
            ProgressiveTimeDifficultyMod.DEV_MODE = data.devMode;
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putLong("AdditionalTime", additionalTime);
        tag.putBoolean("HardcoreMode", hardcoreMode);
        tag.putBoolean("DevMode", devMode);
        return tag;
    }
```

## [v1.1.1] DifficultyHandler.java - Rebalanceo Hardcore
```java
    private static void applyBuffs(LivingEntity entity, double factor) {
        // ...
        AttributeInstance damageAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damageAttr != null) {
            damageAttr.removeModifier(DMG_MOD_UUID);

            double damageMultiplier = 0.0;
            if (isHardcore) {
                damageMultiplier = factor * 2.5; // AUMENTADO: 2.0 -> 2.5
            } else {
                switch (entity.level().getDifficulty()) {
                    case EASY -> damageMultiplier = 0.0;
                    case NORMAL -> damageMultiplier = factor * 0.5;
                    case HARD -> damageMultiplier = factor;
                }
            }
            // ...
        }

        AttributeInstance kbResAttr = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (kbResAttr != null) {
             kbResAttr.removeModifier(KB_MOD_UUID);
             double kbBuff = 0.0;
             if (isHardcore) {
                 kbBuff = Math.min(1.0, factor * 0.15); // AUMENTADO: 0.1 -> 0.15
             } else {
                 kbBuff = Math.min(1.0, factor * 0.1);
             }
             kbResAttr.addPermanentModifier(new AttributeModifier(KB_MOD_UUID, "PTD KB Res Buff", kbBuff, AttributeModifier.Operation.ADDITION));
        }
    }
```

## [v1.1.2] DifficultyHandler.java - Daño Ambiental Escalable
```java
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        DamageSource source = event.getSource();

        if (isEnvironmentalDamage(source)) {
            // ... cálculo de factor ...

            float multiplier = 1.0f + (float) factor;

            if (player.level() instanceof ServerLevel sl && DifficultySavedData.get(sl).isHardcoreMode()) {
                multiplier += (float) (factor * 0.5); // Bonus extra en hardcore
            }

            float originalDamage = event.getAmount();
            float newDamage = originalDamage * multiplier;

            event.setAmount(newDamage);
        }
    }
```

## [v1.1.3] DifficultyHandler.java - Experiencia Escalable
```java
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        if (entity instanceof Player) return;

        AttributeInstance damageAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damageAttr == null || damageAttr.getBaseValue() <= 0) return;

        Player player = event.getAttackingPlayer();
        if (player == null) return;

        // ... cálculo de factor ...

        int originalXp = event.getOriginalExperience();
        int newXp = (int) (originalXp * (1.0 + difficultyFactor));

        event.setDroppedExperience(newXp);
    }
```

## [v1.1.4] DifficultyHandler.java - Daño Híbrido y Reglas de Seguridad
```java
    private static void applyBuffs(LivingEntity entity, double factor) {
        // ...
        // LÍMITE DE DAÑO FÍSICO: Máximo x5.0 para proteger armaduras
        double physicalMultiplier = Math.min(damageMultiplier, 5.0);
        double magicMultiplier = Math.max(0, damageMultiplier - 5.0);

        if (physicalMultiplier > 0) {
            damageAttr.addPermanentModifier(new AttributeModifier(DMG_MOD_UUID, "PTD Damage Buff", physicalMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        if (magicMultiplier > 0) {
            entity.getPersistentData().putDouble(NBT_KEY_MAGIC_DMG_MULT, magicMultiplier);
        }

        // REGLA DE SEGURIDAD: Si la vida FINAL supera 200, NO recibe armadura.
        double finalMaxHealth = entity.getMaxHealth();
        if (!isBossEntity(entity) && finalMaxHealth <= 200.0) {
            // Aplicar armadura
        }
    }
```

## [v1.1.4] EnvironmentalDamageHandler.java - Aplicación de Daño Híbrido
```java
    private static void applyHybridDamage(LivingHurtEvent event, LivingEntity attacker) {
        if (event.getSource().is(DamageTypes.MAGIC)) return;

        double magicMult = attacker.getPersistentData().getDouble(DifficultyHandler.NBT_KEY_MAGIC_DMG_MULT);
        if (magicMult <= 0) return;

        float damage = event.getAmount();
        // ATENUACIÓN MÁGICA: Reducir el daño mágico al 50%
        float magicDamage = (float) (damage * (magicMult / 5.0) * 0.5);

        if (magicDamage > 0) {
            event.getEntity().invulnerableTime = 0;
            event.getEntity().hurt(attacker.damageSources().magic(), magicDamage);
        }
    }
```

## [v1.1.4] BossRegistry.java - Detección de Jefes
```java
package com.Tontoque28.ProgressiveTimeDifficulty;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class BossRegistry {

    private static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "bosses"));
    private static final Set<String> KNOWN_BOSS_IDS = new HashSet<>();

    static {
        // Mowzie's Mobs
        KNOWN_BOSS_IDS.add("mowziesmobs:frostmaw");
        KNOWN_BOSS_IDS.add("mowziesmobs:ferrous_wroughtnaut");
        KNOWN_BOSS_IDS.add("mowziesmobs:naga");
        KNOWN_BOSS_IDS.add("mowziesmobs:barako");

        // Twilight Forest
        KNOWN_BOSS_IDS.add("twilightforest:naga");
        KNOWN_BOSS_IDS.add("twilightforest:lich");
        KNOWN_BOSS_IDS.add("twilightforest:hydra");
        KNOWN_BOSS_IDS.add("twilightforest:ur_ghast");
        KNOWN_BOSS_IDS.add("twilightforest:snow_queen");
        KNOWN_BOSS_IDS.add("twilightforest:minoshroom");
        KNOWN_BOSS_IDS.add("twilightforest:knight_phantom");
        KNOWN_BOSS_IDS.add("twilightforest:alpha_yeti");

        // Cataclysm
        KNOWN_BOSS_IDS.add("cataclysm:ender_guardian");
        KNOWN_BOSS_IDS.add("cataclysm:netherite_monstrosity");
        KNOWN_BOSS_IDS.add("cataclysm:ignis");
        KNOWN_BOSS_IDS.add("cataclysm:the_harbinger");
        KNOWN_BOSS_IDS.add("cataclysm:the_leviathan");
        KNOWN_BOSS_IDS.add("cataclysm:ancient_remnant");

        // Born in Chaos
        KNOWN_BOSS_IDS.add("born_in_chaos_v1:nightmare_stalker");
        KNOWN_BOSS_IDS.add("born_in_chaos_v1:supreme_bonescaller");
        KNOWN_BOSS_IDS.add("born_in_chaos_v1:missioner");
        KNOWN_BOSS_IDS.add("born_in_chaos_v1:lifestealer");
        KNOWN_BOSS_IDS.add("born_in_chaos_v1:fallen_chaos_knight");

        // Ice and Fire
        KNOWN_BOSS_IDS.add("iceandfire:fire_dragon");
        KNOWN_BOSS_IDS.add("iceandfire:ice_dragon");
        KNOWN_BOSS_IDS.add("iceandfire:lightning_dragon");
        KNOWN_BOSS_IDS.add("iceandfire:cyclops");
        KNOWN_BOSS_IDS.add("iceandfire:gorgon");
        KNOWN_BOSS_IDS.add("iceandfire:hydra");

        // Aquamirae
        KNOWN_BOSS_IDS.add("aquamirae:captain_cornelia");

        // Bosses of Mass Destruction
        KNOWN_BOSS_IDS.add("bomd:night_lich");
        KNOWN_BOSS_IDS.add("bomd:obsidilith");
        KNOWN_BOSS_IDS.add("bomd:gauntlet");
        KNOWN_BOSS_IDS.add("bomd:void_blossom");

        // Stalwart Dungeons
        KNOWN_BOSS_IDS.add("stalwart_dungeons:awful_ghast");
        KNOWN_BOSS_IDS.add("stalwart_dungeons:nether_keeper");

        // Rotten Creatures
        KNOWN_BOSS_IDS.add("rottencreatures:immortal");

        // Block Factory's Bosses (Estimado)
        KNOWN_BOSS_IDS.add("block_factorys_bosses:red_creeper_boss");
        KNOWN_BOSS_IDS.add("block_factorys_bosses:blue_creeper_boss");

        // Call From The Depth (Estimado)
        KNOWN_BOSS_IDS.add("callfromthedepth:m_boss");

        // Threateningly Mobs (Estimado)
        KNOWN_BOSS_IDS.add("threateningly_mobs:solifuge");

        // Wardens Plus (Estimado)
        KNOWN_BOSS_IDS.add("wardens_plus:reinforced_warden");

        // Bosses Rise (Estimado)
        KNOWN_BOSS_IDS.add("bosses_rise:boss_golem");
        KNOWN_BOSS_IDS.add("bosses_rise:boss_spider");
        KNOWN_BOSS_IDS.add("bosses_rise:boss_skeleton");
        KNOWN_BOSS_IDS.add("bosses_rise:boss_zombie");
    }

    public static boolean isBoss(Entity entity) {
        if (entity.getType().is(BOSS_TAG)) {
            return true;
        }

        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (id != null) {
            String idString = id.toString();

            if (KNOWN_BOSS_IDS.contains(idString)) {
                return true;
            }

            // Detección heurística para mods desconocidos de la lista
            if (idString.startsWith("block_factorys_bosses:") ||
                idString.startsWith("wardens_plus:") ||
                idString.startsWith("threateningly_mobs:")) {
                return true;
            }
        }

        return entity instanceof WitherBoss ||
               entity instanceof EnderDragon ||
               entity instanceof Warden ||
               entity instanceof IronGolem ||
               entity instanceof ElderGuardian;
    }
}
```

## [v1.1.5] DifficultyHandler.java - Exclusión de Creepers
```java
    public static boolean updateEntityBuffs(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        if (entity instanceof Player) return false;

        // EXCLUSIÓN EXPLÍCITA: Creepers y Blacklist
        if (livingEntity instanceof Creeper || BLACKLIST.contains(livingEntity.getType())) {
            removeBuffs(livingEntity); // Limpiar buffs si existían
            return false;
        }
        // ...
    }
```