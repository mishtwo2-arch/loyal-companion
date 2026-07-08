package ru.mihadge.loyal_companion;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue RESCUE_COOLDOWN_WOLF;
    public static final ModConfigSpec.IntValue EFFECTS_DURATION_WOLF;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        RESCUE_COOLDOWN_WOLF = builder
                .comment("Cooldown between rescues in seconds")
                .defineInRange(
                        "rescueCooldown",
                        60,
                        0,
                        36000
                );

        EFFECTS_DURATION_WOLF = builder
                .comment("Duration of rescue effects in seconds")
                .defineInRange(
                        "effectDuration",
                        3,
                        0,
                        120
                );

        SPEC = builder.build();
    }
}
