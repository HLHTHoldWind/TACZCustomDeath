package com.hlhtstudios.taczcustomdeath;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Taczcustomdeath.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public enum PaddingPosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT; // default
    }

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    //
    // ────────────────────────────────────────────────────────────────────────────────
    //   KILL HUD SECTION
    // ────────────────────────────────────────────────────────────────────────────────
    //
    private static final ForgeConfigSpec.IntValue MAX_MESSAGES = BUILDER
            .comment("Maximum number of kill messages displayed")
            .defineInRange("maxMessages", 7, 1, 50);

    private static final ForgeConfigSpec.IntValue ICON_SIZE = BUILDER
            .comment("Weapon icon height (in pixels)")
            .defineInRange("iconSize", 32, 1, 128);

    private static final ForgeConfigSpec.IntValue ICON_SIZE_SMALL = BUILDER
            .comment("Weapon icon width (in pixels)")
            .defineInRange("iconSizeSmall", 11, 1, 128);

    private static final ForgeConfigSpec.IntValue SPACING_X = BUILDER
            .comment("Horizontal spacing between HUD elements")
            .defineInRange("spacingX", 8, 0, 50);

    private static final ForgeConfigSpec.IntValue SPACING_Y = BUILDER
            .comment("Vertical spacing between HUD lines")
            .defineInRange("spacingY", 1, 0, 20);

    private static final ForgeConfigSpec.IntValue MARGIN_HORIZONTAL = BUILDER
            .comment("Horizontal margin of the HUD")
            .defineInRange("marginHorizontal", 20, 0, 500);

    private static final ForgeConfigSpec.IntValue MARGIN_VERTICAL = BUILDER
            .comment("Vertical margin of the HUD")
            .defineInRange("marginVertical", 70, 0, 500);

    private static final ForgeConfigSpec.DoubleValue TEXT_SCALE = BUILDER
            .comment("Global scale (1.0 = normal size)")
            .defineInRange("globalScale", 0.75, 0.1, 5.0);

    private static final ForgeConfigSpec.IntValue MESSAGE_LIFETIME = BUILDER
            .comment("How long a kill message stays on screen (ms)")
            .defineInRange("messageLifetime", 6000, 100, 60000);

    private static final ForgeConfigSpec.IntValue FADE_START = BUILDER
            .comment("When fade-out begins (ms)")
            .defineInRange("fadeStart", 5100, 0, 60000);

    public static final ForgeConfigSpec.ConfigValue<String> PADDING_CFG =
            BUILDER.comment("Padding position: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT")
                    .define("paddingPosition", "TOP_RIGHT");


    //
    // Final build
    //
    public static final ForgeConfigSpec SPEC = BUILDER.build();


    //
    // ────────────────────────────────────────────────────────────────────────────────
    //   LOADED VALUES (USED BY THE GAME)
    // ────────────────────────────────────────────────────────────────────────────────
    //
    public static int maxMessages;
    public static int iconSize;
    public static int iconSizeSmall;
    public static int spacingX;
    public static int spacingY;
    public static int marginHorizontal;
    public static int marginVertical;
    public static float globalScale;
    public static int messageLifetime;
    public static int fadeStart;
    public static PaddingPosition padding;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

        maxMessages = MAX_MESSAGES.get();
        iconSize = ICON_SIZE.get();
        iconSizeSmall = ICON_SIZE_SMALL.get();
        spacingX = SPACING_X.get();
        spacingY = SPACING_Y.get();
        marginHorizontal = MARGIN_HORIZONTAL.get();
        marginVertical = MARGIN_VERTICAL .get();
        globalScale = TEXT_SCALE.get().floatValue();
        messageLifetime = MESSAGE_LIFETIME.get();
        fadeStart = FADE_START.get();
        try {
            padding = PaddingPosition.valueOf(PADDING_CFG.get().toUpperCase());
        } catch (Exception e) {
            padding = PaddingPosition.BOTTOM_RIGHT; // fallback
        }
    }
}
