package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void disableBobHurt(float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }
}
