# FULL_CODE_REFERENCES.md
# Referencias de Código Histórico: MVEL

*Este archivo almacenará snippets de código críticos que no deben perderse.*

## Mixins (v1.0.2 - Stable Release)

### EnchantmentMixin.java
```java
package com.tontoque28.mvel.mixin;

import com.tontoque28.mvel.MVEL;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = {"getMaxLevel", "m_6586_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 0)
    private void onGetMaxLevel(CallbackInfoReturnable<Integer> cir) {
        // cir.getReturnValue() contiene el valor original de Minecraft.
        // Lo pasamos como default para evitar llamar a getMaxLevel() de nuevo.
        int customMax = MVEL.getMaxLevelFor((Enchantment) (Object) this, cir.getReturnValue());
        if (customMax != cir.getReturnValue()) {
            cir.setReturnValue(customMax);
        }
    }
}
```

### VanillaEnchantmentSubclassesMixin.java
```java
package com.tontoque28.mvel.mixin;

import com.tontoque28.mvel.MVEL;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
    ArrowDamageEnchantment.class,
    ArrowKnockbackEnchantment.class,
    KnockbackEnchantment.class,
    FireAspectEnchantment.class,
    LootBonusEnchantment.class,
    SweepingEdgeEnchantment.class,
    ThornsEnchantment.class,
    TridentImpalerEnchantment.class,
    TridentRiptideEnchantment.class,
    TridentLoyaltyEnchantment.class,
    QuickChargeEnchantment.class,
    ArrowPiercingEnchantment.class,
    FrostWalkerEnchantment.class,
    SoulSpeedEnchantment.class,
    SwiftSneakEnchantment.class,
    DamageEnchantment.class,
    ProtectionEnchantment.class,
    DiggingEnchantment.class,
    DigDurabilityEnchantment.class
})
public abstract class VanillaEnchantmentSubclassesMixin {

    @Inject(method = {"getMaxLevel", "m_6586_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 0)
    private void onGetMaxLevel(CallbackInfoReturnable<Integer> cir) {
        // Usamos el valor de retorno actual para evitar recursión infinita
        int customMax = MVEL.getMaxLevelFor((Enchantment) (Object) this, cir.getReturnValue());
        if (customMax != cir.getReturnValue()) {
            cir.setReturnValue(customMax);
        }
    }
}
```

## Core Logic (v1.0.2 - PTD Balanced)

### MVEL.java (Values)
```java
    public static int getMaxLevelFor(Enchantment enchantment, int defaultValue) {
        ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
        if (id == null) return defaultValue;
        String path = id.getPath();
        switch (path) {
            // Utility & Durability
            case "unbreaking": return 12;
            case "mending": return 1;

            // Looting & Economy
            case "looting": case "fortune": case "luck_of_the_sea": case "lure": return 7;

            // Offensive (Melee & Ranged) - Scaled for PTD
            case "sharpness": case "smite": case "bane_of_arthropods": case "power": case "impaling": return 10;
            case "sweeping_edge": return 6;
            case "knockback": case "punch": return 5;
            case "flame": case "fire_aspect": return 4;
            case "infinity": return 1;
            case "piercing": return 6;
            case "multishot": return 1;

            // Defensive
            case "protection": case "fire_protection": case "blast_protection": case "projectile_protection": return 10;
            case "feather_falling": return 7;
            case "thorns": return 6;
            case "respiration": return 6;
            case "aqua_affinity": return 1;

            // Tools & Movement
            case "efficiency": return 10;
            case "silk_touch": return 1;
            case "depth_strider": return 8; // BUFFED: Agilidad Acuática
            case "frost_walker": return 5;
            case "soul_speed": case "swift_sneak": return 6;

            // Special
            case "quick_charge": return 5;
            case "channeling": return 1;
            case "loyalty": case "riptide": return 6;

            default: return defaultValue;
        }
    }
```
