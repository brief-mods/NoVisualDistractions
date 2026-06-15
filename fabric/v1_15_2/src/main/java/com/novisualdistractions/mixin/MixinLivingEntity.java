package com.novisualdistractions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    private void suppressHurtSound(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof ClientPlayerEntity) {
            ci.cancel();
            return;
        }
        Entity attacker = damageSource.getAttacker();
        if (!(attacker instanceof ClientPlayerEntity)) {
            ci.cancel();
        }
    }
}
