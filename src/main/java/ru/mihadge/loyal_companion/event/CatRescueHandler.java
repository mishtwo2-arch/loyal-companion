package ru.mihadge.loyal_companion.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import ru.mihadge.loyal_companion.Config;

public class CatRescueHandler {

    private static final String RESCUE_COOLDOWN = "loyal_companion_last_rescue";

    @SubscribeEvent
    public void onCatDamage(LivingDamageEvent.Pre event) {

        if (!(event.getEntity() instanceof Cat cat)) {
            return;
        }

        if (cat.level().isClientSide()) {
            return;
        }

        if (!cat.isTame()) {
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

        if (cat.getHealth() > event.getNewDamage()) {
            return;
        }

        if (!(cat.getOwner() instanceof ServerPlayer owner)) {
            return;
        }

        CompoundTag data = cat.getPersistentData();

        long currentTime = cat.level().getGameTime();
        long lastRescue = data.getLong(RESCUE_COOLDOWN);

        if (currentTime - lastRescue < Config.RESCUE_COOLDOWN_CAT.get() * 20 ) {
            return;
        }

        data.putLong(RESCUE_COOLDOWN, currentTime);

        event.setNewDamage(0);

        cat.setHealth(1.0F);

        cat.invulnerableTime = Config.EFFECTS_DURATION_CAT.get() * 20;

        cat.clearFire();
        cat.setRemainingFireTicks(0);

        cat.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
                Config.EFFECTS_DURATION_CAT.get() * 20,
                4,
                false,
                true
        ));

        cat.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE,
                Config.EFFECTS_DURATION_CAT.get() * 20,
                0,
                false,
                true
        ));

        cat.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                Config.EFFECTS_DURATION_CAT.get() * 20,
                2,
                false,
                true
        ));

        teleportNearOwner(cat, owner);
    }


    private void teleportNearOwner(TamableAnimal animal, ServerPlayer owner) {

        BlockPos ownerPos = owner.blockPosition();

        BlockPos[] positions = {
                ownerPos.offset(1, 0, 0),
                ownerPos.offset(-1, 0, 0),
                ownerPos.offset(0, 0, 1),
                ownerPos.offset(0, 0, -1)
        };

        for (BlockPos pos : positions) {

            BlockState block = owner.level().getBlockState(pos);
            BlockState below = owner.level().getBlockState(pos.below());

            if (block.isAir()
                    && !below.isAir()
                    && !below.is(Blocks.LAVA)
                    && !below.is(Blocks.FIRE)
                    && !below.is(Blocks.SOUL_FIRE)
                    && !below.is(Blocks.MAGMA_BLOCK)
                    && !below.is(Blocks.AIR)
                    && !below.is(Blocks.CACTUS)) {

                animal.moveTo(
                        pos.getX() + 0.5,
                        pos.getY(),
                        pos.getZ() + 0.5,
                        animal.getYRot(),
                        animal.getXRot()
                );

                animal.getNavigation().stop();
                animal.setDeltaMovement(0, 0, 0);

                return;
            }
        }

        animal.moveTo(
                owner.getX(),
                owner.getY(),
                owner.getZ(),
                animal.getYRot(),
                animal.getXRot()
        );
    }
}