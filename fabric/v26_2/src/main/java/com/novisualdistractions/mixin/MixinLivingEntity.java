package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    private void suppressHurtSound(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof LocalPlayer) {
            ci.cancel();
            return;
        }
        Entity attacker = damageSource.getEntity();
        if (!(attacker instanceof LocalPlayer)) {
            ci.cancel();
        }
    }

    @Inject(method = "playSecondaryHurtSound", at = @At("HEAD"), cancellable = true)
    private void suppressSecondaryHurtSound(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof LocalPlayer) {
            ci.cancel();
            return;
        }
        Entity attacker = damageSource.getEntity();
        if (!(attacker instanceof LocalPlayer)) {
            ci.cancel();
        }
    }
}
