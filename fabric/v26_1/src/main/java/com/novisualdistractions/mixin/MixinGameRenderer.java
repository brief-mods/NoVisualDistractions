package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void disableBobHurt(CameraRenderState cameraRenderState, PoseStack poseStack, CallbackInfo ci) {
        ci.cancel();
    }
}
