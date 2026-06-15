package com.novisualdistractions.mixin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(SoundManager.class)
public class MixinSoundManager {
    private static final Set<Identifier> CANCELED_SOUNDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        SoundEvents.BLOCK_LAVA_AMBIENT.getId(),
        SoundEvents.BLOCK_LAVA_POP.getId(),
        SoundEvents.BLOCK_LAVA_EXTINGUISH.getId(),
        SoundEvents.BLOCK_FIRE_AMBIENT.getId(),
        SoundEvents.BLOCK_FIRE_EXTINGUISH.getId(),
        SoundEvents.ENTITY_GENERIC_BURN.getId(),
        SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE.getId(),
        SoundEvents.ENCHANT_THORNS_HIT.getId(),
        SoundEvents.ENTITY_ENDERMAN_AMBIENT.getId(),
        SoundEvents.ENTITY_ENDERMAN_DEATH.getId(),
        SoundEvents.ENTITY_ENDERMAN_HURT.getId(),
        SoundEvents.ENTITY_ENDERMAN_SCREAM.getId(),
        SoundEvents.ENTITY_ENDERMAN_STARE.getId(),
        SoundEvents.ENTITY_ENDERMAN_TELEPORT.getId(),
        SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE.getId(),
        SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.getId(),
        SoundEvents.ENTITY_FISHING_BOBBER_THROW.getId(),
        SoundEvents.ENTITY_PLAYER_ATTACK_CRIT.getId(),
        SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK.getId(),
        SoundEvents.ENTITY_PLAYER_ATTACK_STRONG.getId(),
        SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP.getId(),
        SoundEvents.ENTITY_PLAYER_ATTACK_WEAK.getId()
    )));

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void disableCanceledSounds(SoundInstance soundInstance, CallbackInfo ci) {
        Identifier id = soundInstance.getId();

        if (CANCELED_SOUNDS.contains(id)) {
            ci.cancel();
            return;
        }

        SoundCategory source = soundInstance.getCategory();
        if (source == SoundCategory.HOSTILE || source == SoundCategory.NEUTRAL) {
            World world = MinecraftClient.getInstance().world;
            if (world != null) {
                double x = soundInstance.getX();
                double y = soundInstance.getY();
                double z = soundInstance.getZ();
                Box box = new Box(x - 16.0, y - 16.0, z - 16.0, x + 16.0, y + 16.0, z + 16.0);
                List<LivingEntity> nearby = world.getEntitiesByClass(LivingEntity.class, box, e -> !(e instanceof PlayerEntity));
                if (nearby.size() > 4) {
                    ci.cancel();
                }
            }
        }
    }
}
