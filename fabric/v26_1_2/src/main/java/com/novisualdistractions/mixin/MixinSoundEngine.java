package com.novisualdistractions.mixin;

import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
    private static final Set<Identifier> CANCELED_SOUNDS = Set.of(
        SoundEvents.LAVA_AMBIENT.location(),
        SoundEvents.LAVA_POP.location(),
        SoundEvents.LAVA_EXTINGUISH.location(),
        SoundEvents.FIRE_AMBIENT.location(),
        SoundEvents.FIRE_EXTINGUISH.location(),
        SoundEvents.GENERIC_BURN.location(),
        SoundEvents.GENERIC_EXTINGUISH_FIRE.location(),
        SoundEvents.THORNS_HIT.location(),
        SoundEvents.ENDERMAN_AMBIENT.location(),
        SoundEvents.ENDERMAN_DEATH.location(),
        SoundEvents.ENDERMAN_HURT.location(),
        SoundEvents.ENDERMAN_SCREAM.location(),
        SoundEvents.ENDERMAN_STARE.location(),
        SoundEvents.ENDERMAN_TELEPORT.location(),
        SoundEvents.PLAYER_HURT_DROWN.location(),
        SoundEvents.PLAYER_HURT_FREEZE.location(),
        SoundEvents.PLAYER_HURT_ON_FIRE.location(),
        SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH.location(),
        SoundEvents.FISHING_BOBBER_RETRIEVE.location(),
        SoundEvents.FISHING_BOBBER_SPLASH.location(),
        SoundEvents.FISHING_BOBBER_THROW.location(),
        SoundEvents.PLAYER_ATTACK_CRIT.location(),
        SoundEvents.PLAYER_ATTACK_KNOCKBACK.location(),
        SoundEvents.PLAYER_ATTACK_STRONG.location(),
        SoundEvents.PLAYER_ATTACK_SWEEP.location()
    );

    private static final Set<Identifier> PLAYER_ATTACK_SOUNDS = Set.of(
        SoundEvents.PLAYER_ATTACK_WEAK.location(),
        SoundEvents.PLAYER_ATTACK_NODAMAGE.location()
    );

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void disableCanceledSounds(SoundInstance soundInstance, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        Identifier id = soundInstance.getIdentifier();

        if (CANCELED_SOUNDS.contains(id)) {
            cir.setReturnValue(null);
            return;
        }

        if (PLAYER_ATTACK_SOUNDS.contains(id)) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) {
                cir.setReturnValue(null);
                return;
            }
            double dx = soundInstance.getX() - localPlayer.getX();
            double dz = soundInstance.getZ() - localPlayer.getZ();
            if (dx * dx + dz * dz > 1.0) {
                cir.setReturnValue(null);
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
                    cir.setReturnValue(null);
                }
            }
        }
    }
}
