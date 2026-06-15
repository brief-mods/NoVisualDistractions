package com.novisualdistractions.mixin;

import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
    private static final Set<ResourceLocation> CANCELED_SOUNDS = Set.of(
        SoundEvents.LAVA_AMBIENT.getLocation(),
        SoundEvents.LAVA_POP.getLocation(),
        SoundEvents.LAVA_EXTINGUISH.getLocation(),
        SoundEvents.FIRE_AMBIENT.getLocation(),
        SoundEvents.FIRE_EXTINGUISH.getLocation(),
        SoundEvents.GENERIC_BURN.getLocation(),
        SoundEvents.GENERIC_EXTINGUISH_FIRE.getLocation(),
        SoundEvents.THORNS_HIT.getLocation(),
        SoundEvents.ENDERMAN_AMBIENT.getLocation(),
        SoundEvents.ENDERMAN_DEATH.getLocation(),
        SoundEvents.ENDERMAN_HURT.getLocation(),
        SoundEvents.ENDERMAN_SCREAM.getLocation(),
        SoundEvents.ENDERMAN_STARE.getLocation(),
        SoundEvents.ENDERMAN_TELEPORT.getLocation(),
        SoundEvents.PLAYER_HURT_DROWN.getLocation(),
        SoundEvents.PLAYER_HURT_FREEZE.getLocation(),
        SoundEvents.PLAYER_HURT_ON_FIRE.getLocation(),
        SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH.getLocation(),
        SoundEvents.FISHING_BOBBER_RETRIEVE.getLocation(),
        SoundEvents.FISHING_BOBBER_SPLASH.getLocation(),
        SoundEvents.FISHING_BOBBER_THROW.getLocation(),
        SoundEvents.PLAYER_ATTACK_CRIT.getLocation(),
        SoundEvents.PLAYER_ATTACK_KNOCKBACK.getLocation(),
        SoundEvents.PLAYER_ATTACK_STRONG.getLocation(),
        SoundEvents.PLAYER_ATTACK_SWEEP.getLocation()
    );

    private static final Set<ResourceLocation> PLAYER_ATTACK_SOUNDS = Set.of(
        SoundEvents.PLAYER_ATTACK_WEAK.getLocation(),
        SoundEvents.PLAYER_ATTACK_NODAMAGE.getLocation()
    );

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void disableCanceledSounds(SoundInstance soundInstance, CallbackInfo ci) {
        ResourceLocation id = soundInstance.getLocation();

        if (CANCELED_SOUNDS.contains(id)) {
            ci.cancel();
            return;
        }

        if (PLAYER_ATTACK_SOUNDS.contains(id)) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) {
                ci.cancel();
                return;
            }
            double dx = soundInstance.getX() - localPlayer.getX();
            double dz = soundInstance.getZ() - localPlayer.getZ();
            if (dx * dx + dz * dz > 1.0) {
                ci.cancel();
                return;
            }
        }

        SoundSource source = soundInstance.getSource();
        if (source == SoundSource.HOSTILE || source == SoundSource.NEUTRAL) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                double x = soundInstance.getX();
                double y = soundInstance.getY();
                double z = soundInstance.getZ();
                AABB aabb = new AABB(x - 16.0, y - 16.0, z - 16.0, x + 16.0, y + 16.0, z + 16.0);
                List<? extends LivingEntity> nearby = level.getEntities(
                    EntityTypeTest.forClass(LivingEntity.class),
                    aabb,
                    e -> !(e instanceof Player)
                );
                if (nearby.size() > 4) {
                    ci.cancel();
                }
            }
        }
    }
}
