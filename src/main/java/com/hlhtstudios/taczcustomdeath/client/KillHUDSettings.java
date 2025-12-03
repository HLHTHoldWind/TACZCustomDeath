package com.hlhtstudios.taczcustomdeath.client;

import com.hlhtstudios.taczcustomdeath.Config;

public class KillHUDSettings {

    public static int MAX_MESSAGES = Config.maxMessages;
    public static int ICON_SIZE = (int) (Config.iconSize * Config.globalScale);
    public static int ICON_SIZE2 = (int) (Config.iconSizeSmall * Config.globalScale);
    public static int SPACING_X = Config.spacingX;
    public static int SPACING_Y = Config.spacingY;
    public static int MARGIN_HORIZONTAL = Config.marginHorizontal;
    public static int MARGIN_VERTICAL = Config.marginVertical;
    public static float TEXT_SCALE = Config.globalScale;
    public static int MESSAGE_LIFETIME = Config.messageLifetime;
    public static int FADE_START = Config.fadeStart;
    public static Config.PaddingPosition PADDING = Config.padding;

    private KillHUDSettings() {}
}
