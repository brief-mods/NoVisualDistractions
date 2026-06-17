package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private void disableScreenEffects(boolean includeWater, boolean includeFire, float partialTick, SubmitNodeCollector collector, boolean includeLiquid, CallbackInfo ci) {
        ci.cancel();
    }
}
