package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.FirstPersonRenderer;

@Mixin(FirstPersonRenderer.class)
public class MixinInGameOverlayRenderer {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void disableFireOverlay(CallbackInfo ci) {
        ci.cancel();
    }
}
