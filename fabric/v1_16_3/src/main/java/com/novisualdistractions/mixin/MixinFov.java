package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class MixinFov {
    @Shadow private float movementFovMultiplier;
    @Shadow private float lastMovementFovMultiplier;

    @Inject(method = "updateMovementFovMultiplier", at = @At("HEAD"), cancellable = true)
    private void disableFovChanges(CallbackInfo ci) {
        this.lastMovementFovMultiplier = 1.0f;
        this.movementFovMultiplier = 1.0f;
        ci.cancel();
    }
}
