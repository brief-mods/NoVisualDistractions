package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private void disableFog(Camera camera, int fogType, CallbackInfo ci) {
        ci.cancel();
    }
}
