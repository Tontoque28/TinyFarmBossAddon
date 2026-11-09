package com.tontoque28.tinyfarmboss.mixin;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityMobFarm.class, remap = false)
public abstract class MobFarmBlockEntityMixin {

    @Shadow public abstract ItemStack getLasso();
    @Shadow public abstract boolean isPowered();

    @Inject(method = "isWorking", at = @At("HEAD"), cancellable = true)
    private void forceWorkingForBosses(CallbackInfoReturnable<Boolean> cir) {
        ItemStack lasso = this.getLasso();
        if (!lasso.isEmpty() && lasso.hasTag()) {
            CompoundNBT baseTag = NBTHelper.getBaseTag(lasso);
            if (baseTag != null) {
                String customLocation = baseTag.getString("mobLootTableLocation");

                if (!customLocation.isEmpty() && !this.isPowered()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
