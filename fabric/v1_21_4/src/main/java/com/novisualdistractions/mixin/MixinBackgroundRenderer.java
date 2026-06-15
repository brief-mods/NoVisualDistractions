package com.novisualdistractions.mixin;

import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void disableFog(Camera camera, FogMode fogMode, Vector4f fogColor, float viewDistance, boolean thickFog, float partialTick, CallbackInfoReturnable<FogParameters> cir) {
        cir.setReturnValue(FogParameters.NO_FOG);
    }
}
