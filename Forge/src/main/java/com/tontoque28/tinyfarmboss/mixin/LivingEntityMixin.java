package com.tontoque28.tinyfarmboss.mixin;

import com.tontoque28.tinyfarmboss.SafeMobSimulationHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "getLootTable", at = @At("HEAD"), cancellable = true)
    private void onGetLootTable(CallbackInfoReturnable<ResourceLocation> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        String lootTable = SafeMobSimulationHandler.getSafeLootTable(entity);
        if (lootTable != null && !lootTable.equals(entity.getType().getDefaultLootTable().toString())) {
            cir.setReturnValue(new ResourceLocation(lootTable));
        }
    }
}
