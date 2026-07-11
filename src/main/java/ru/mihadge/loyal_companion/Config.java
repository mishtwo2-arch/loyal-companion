package ru.mihadge.loyal_companion;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue RESCUE_COOLDOWN_WOLF;
    public static final ModConfigSpec.IntValue EFFECTS_DURATION_WOLF;

    public static final ModConfigSpec.IntValue RESCUE_COOLDOWN_CAT;
    public static final ModConfigSpec.IntValue EFFECTS_DURATION_CAT;

    public static final ModConfigSpec.IntValue RESCUE_COOLDOWN_PARROT;
    public static final ModConfigSpec.IntValue EFFECTS_DURATION_PARROT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("wolf");

        RESCUE_COOLDOWN_WOLF = builder
                .comment("Cooldown between rescues in seconds for the wolf")
                .defineInRange("rescueCooldown", 60, 0, 36000);

        EFFECTS_DURATION_WOLF = builder
                .comment("Duration of rescue effects in seconds for the wolf")
                .defineInRange("effectDuration", 3, 0, 120);

        builder.pop();


        builder.push("cat");

        RESCUE_COOLDOWN_CAT = builder
                .comment("Cooldown between rescues in seconds for the cat")
                .defineInRange("rescueCooldown", 60, 0, 36000);

        EFFECTS_DURATION_CAT = builder
                .comment("Duration of rescue effects in seconds for the cat")
                .defineInRange("effectDuration", 3, 0, 120);

        builder.pop();


        builder.push("parrot");

        RESCUE_COOLDOWN_PARROT = builder
                .comment("Cooldown between rescues in seconds for the parrot")
                .defineInRange("rescueCooldown", 60, 0, 36000);

        EFFECTS_DURATION_PARROT = builder
                .comment("Duration of rescue effects in seconds for the parrot")
                .defineInRange("effectDuration", 3, 0, 120);

        builder.pop();

        SPEC = builder.build();
    }
}
