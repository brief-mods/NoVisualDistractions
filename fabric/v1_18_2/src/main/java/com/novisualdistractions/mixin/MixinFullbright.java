package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinFullbright {
    private static boolean applied = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void applyFullbright(CallbackInfo ci) {
        if (!applied) {
            Minecraft client = (Minecraft)(Object)this;
            if (client.player != null) {
                client.options.gamma = 100.0;
                applied = true;
            }
        }
    }
}
