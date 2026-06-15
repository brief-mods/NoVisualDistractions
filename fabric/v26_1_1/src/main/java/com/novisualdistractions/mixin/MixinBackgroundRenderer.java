package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void disableFog(Camera camera, int renderDistance, DeltaTracker deltaTracker, float scale, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        FogData fogData = new FogData();
        fogData.renderDistanceStart = Float.MAX_VALUE;
        fogData.renderDistanceEnd = Float.MAX_VALUE;
        fogData.environmentalStart = Float.MAX_VALUE;
        fogData.environmentalEnd = Float.MAX_VALUE;
        fogData.skyEnd = Float.MAX_VALUE;
        fogData.cloudEnd = Float.MAX_VALUE;
        cir.setReturnValue(fogData);
    }
}
