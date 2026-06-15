package com.novisualdistractions;

import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("novisualdistractions")
public class NoVisualDistractions {

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

    public NoVisualDistractions() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent event) {
        Identifier id = event.getSound().getIdentifier();

        if (CANCELED_SOUNDS.contains(id)) {
            event.setSound(null);
            return;
        }

        if (PLAYER_ATTACK_SOUNDS.contains(id)) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) {
                event.setSound(null);
                return;
            }
            double dx = event.getSound().getX() - localPlayer.getX();
            double dz = event.getSound().getZ() - localPlayer.getZ();
            if (dx * dx + dz * dz > 1.0) {
                event.setSound(null);
                return;
            }
        }

        SoundSource source = event.getSound().getSource();
        if (source == SoundSource.HOSTILE || source == SoundSource.NEUTRAL) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                double x = event.getSound().getX();
                double y = event.getSound().getY();
                double z = event.getSound().getZ();
                AABB aabb = new AABB(x - 16.0, y - 16.0, z - 16.0, x + 16.0, y + 16.0, z + 16.0);
                List<? extends LivingEntity> nearby = level.getEntities(
                    EntityTypeTest.forClass(LivingEntity.class),
                    aabb,
                    e -> !(e instanceof Player)
                );
                if (nearby.size() > 4) {
                    event.setSound(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event) {
        event.setFarPlaneDistance(Float.MAX_VALUE);
        event.setNearPlaneDistance(Float.MAX_VALUE);
    }

    @SubscribeEvent
    public void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        event.setRed(0.0F);
        event.setGreen(0.0F);
        event.setBlue(0.0F);
    }
}
