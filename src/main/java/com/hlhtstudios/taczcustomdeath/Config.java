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
        BOTTOM_RIGHT // default
    }

    public enum DisplayMode {
        PLAYERS,
        ALL,
        PLAYERS_GUN,
        ALL_GUN
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
            .comment("Weapon icon height (in pixels) (This shouldn't be changed to prevent visual issues)")
            .defineInRange("iconSize", 32, 1, 128);

    private static final ForgeConfigSpec.IntValue ICON_SIZE_SMALL = BUILDER
            .comment("Weapon icon width (in pixels) (This shouldn't be changed to prevent visual issues)")
            .defineInRange("iconSizeSmall", 11, 1, 128);

    private static final ForgeConfigSpec.IntValue SPACING_X = BUILDER
            .comment("Horizontal spacing between HUD elements (This shouldn't be changed to prevent visual issues)")
            .defineInRange("spacingX", 8, 0, 50);

    private static final ForgeConfigSpec.IntValue SPACING_Y = BUILDER
            .comment("Vertical spacing between HUD lines (This shouldn't be changed to prevent visual issues)")
            .defineInRange("spacingY", 1, 0, 20);

    private static final ForgeConfigSpec.IntValue MARGIN_HORIZONTAL = BUILDER
            .comment("Horizontal margin of the HUD (How message box close to the horizontal edge of the screen), 20 as default")
            .defineInRange("marginHorizontal", 20, 0, 500);

    private static final ForgeConfigSpec.IntValue MARGIN_VERTICAL = BUILDER
            .comment("Vertical margin of the HUD, it should be changed into similar value as marginHorizontal when display padding on the top side, 70 as default with BOTTOM_RIGHT")
            .defineInRange("marginVertical", 70, 0, 500);

    private static final ForgeConfigSpec.DoubleValue TEXT_SCALE = BUILDER
            .comment("Global scale (1.0 = normal size)")
            .defineInRange("globalScale", 0.75, 0.1, 5.0);

    private static final ForgeConfigSpec.IntValue MESSAGE_LIFETIME = BUILDER
            .comment("How long a kill message stays on screen (ms)")
            .defineInRange("messageLifetime", 6000, 100, 60000);

    private static final ForgeConfigSpec.IntValue FADE_START = BUILDER
            .comment("When kill message starts to fading-out (ms)")
            .defineInRange("fadeStart", 5100, 0, 60000);

    private static final ForgeConfigSpec.BooleanValue BACKGROUND = BUILDER
            .comment("Messages have transparent background or not (true/false)")
            .define("background", true);

    public static final ForgeConfigSpec.ConfigValue<String> PADDING_CFG =
            BUILDER.comment("Padding position: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT")
                    .define("paddingPosition", "TOP_RIGHT");

    public static final ForgeConfigSpec.ConfigValue<String> DISPLAY_MODE =
            BUILDER.comment("Display Filter: PLAYERS, ALL, PLAYERS_GUN, ALL_GUN (Warning: This should be changed on serverside)")
                    .define("displayMode", "PLAYERS_GUN");


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
    public static boolean background;
    public static PaddingPosition padding;
    public static DisplayMode displayMode;


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
        background = BACKGROUND.get();
        fadeStart = FADE_START.get();
        try {
            displayMode = DisplayMode.valueOf(DISPLAY_MODE.get().toUpperCase());
        } catch (Exception e) {
            displayMode = DisplayMode.PLAYERS_GUN; // fallback
        }
        try {
            padding = PaddingPosition.valueOf(PADDING_CFG.get().toUpperCase());
        } catch (Exception e) {
            padding = PaddingPosition.BOTTOM_RIGHT; // fallback
        }
    }
}
