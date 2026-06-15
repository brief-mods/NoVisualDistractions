package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void disableBobHurt(PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }
}
