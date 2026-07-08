package ru.mihadge.loyal_companion.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import ru.mihadge.loyal_companion.Config;

public class WolfRescueHandler {

    private static final String RESCUE_COOLDOWN = "loyal_companion_last_rescue";

    @SubscribeEvent
    public void onWolfDamage(LivingDamageEvent.Pre event) {

        if (!(event.getEntity() instanceof Wolf wolf)) {
            return;
        }

        if (wolf.level().isClientSide()) {
            return;
        }

        if (!wolf.isTame()) {
            return;
        }

        boolean dangerousDamage =
                event.getSource().is(DamageTypes.FALL) ||
                        event.getSource().is(DamageTypes.LAVA) ||
                        event.getSource().is(DamageTypes.DROWN) ||
                        event.getSource().is(DamageTypes.EXPLOSION) ||
                        event.getSource().is(DamageTypes.IN_FIRE);

        if (!dangerousDamage) {
            return;
        }

        if (wolf.getHealth() > event.getNewDamage()) {
            return;
        }

        if (!(wolf.getOwner() instanceof ServerPlayer owner)) {
            return;
        }

        CompoundTag data = wolf.getPersistentData();

        long currentTime = wolf.level().getGameTime();
        long lastRescue = data.getLong(RESCUE_COOLDOWN);

        if (currentTime - lastRescue < Config.RESCUE_COOLDOWN_WOLF.get() * 20 ) {
            return;
        }

        data.putLong(RESCUE_COOLDOWN, currentTime);

        event.setNewDamage(0);

        wolf.setHealth(1.0F);

        wolf.invulnerableTime = Config.EFFECTS_DURATION_WOLF.get() * 20;

        wolf.clearFire();
        wolf.setRemainingFireTicks(0);

        wolf.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
                Config.EFFECTS_DURATION_WOLF.get() * 20,
                4,
                false,
                true
        ));

        wolf.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE,
                Config.EFFECTS_DURATION_WOLF.get() * 20,
                0,
                false,
                true
        ));

        wolf.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                Config.EFFECTS_DURATION_WOLF.get() * 20,
                0,
                false,
                true
        ));

        wolf.moveTo(
                owner.getX(),
                owner.getY() + 0.5,
                owner.getZ(),
                wolf.getYRot(),
                wolf.getXRot()
        );
    }
}