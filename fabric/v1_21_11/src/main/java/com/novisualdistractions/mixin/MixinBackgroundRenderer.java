package com.novisualdistractions.mixin;

import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void disableFog(Camera camera, int renderDistance, DeltaTracker deltaTracker, float scale, ClientLevel level, CallbackInfoReturnable<Vector4f> cir) {
        cir.setReturnValue(new Vector4f(0, 0, 0, 0));
    }
}
