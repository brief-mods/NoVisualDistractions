package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.fog.environment.MobEffectFogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;

@Mixin(MobEffectFogEnvironment.class)
public class MixinMobEffectFog {
    @Inject(method = "isApplicable", at = @At("HEAD"), cancellable = true)
    private void disableFogEffect(FogType fogType, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
