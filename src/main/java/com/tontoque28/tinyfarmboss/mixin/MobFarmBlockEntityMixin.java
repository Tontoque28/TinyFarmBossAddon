package com.tontoque28.tinyfarmboss.mixin;

import com.daqem.tinymobfarm.blockentity.MobFarmBlockEntity;
import com.daqem.tinymobfarm.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MobFarmBlockEntity.class, remap = false)
public abstract class MobFarmBlockEntityMixin {

    @Shadow public abstract ItemStack getLasso();
    @Shadow public abstract boolean isPowered();

    // 1. FORZAMOS A LA GRANJA A ENCENDERSE
    @Inject(method = "isWorking", at = @At("HEAD"), cancellable = true)
    private void forceWorkingForBosses(CallbackInfoReturnable<Boolean> cir) {
        ItemStack lasso = this.getLasso();
        if (!lasso.isEmpty() && lasso.hasTag()) {
            CompoundTag baseTag = NBTHelper.getBaseTag(lasso); // Ya estamos dentro de "capturedMob"
            if (baseTag != null) {
                String customLocation = baseTag.getString("mobLootTableLocation");

                // Si tiene nuestra tabla personalizada y la granja no está apagada por redstone
                if (!customLocation.isEmpty() && !this.isPowered()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    // 2. UN LOG PARA CONFIRMAR CUANDO CAIGA EL LOOT
    @Inject(method = "generateDrops", at = @At("HEAD"))
    private void logDrops(CallbackInfo ci) {
        ItemStack lasso = this.getLasso();
        if (!lasso.isEmpty() && lasso.hasTag()) {
            CompoundTag baseTag = NBTHelper.getBaseTag(lasso);
            if (baseTag != null) {
                String customLocation = baseTag.getString("mobLootTableLocation");
                if (!customLocation.isEmpty()) {
                    System.out.println("TINYFARMBOSS: ¡Granja procesada! Generando loot desde -> " + customLocation);
                }
            }
        }
    }
}