package com.hlhtstudios.taczcustomdeath.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.scores.Team;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import static com.hlhtstudios.taczcustomdeath.client.KillHUDSettings.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class KillHUD implements IGuiOverlay {
    public static final int YELLOW = 0xFFFFBF00;

    public static KillHUD INSTANCE = new KillHUD();

    private final Deque<KillMessage> messages = new LinkedList<>();

    public void pushKill(UUID attacker, UUID victim, String attackerName, String victimName,
                         String attackerTeam, String victimTeam,
            ResourceLocation weaponIcon, boolean isHeadShot) {
        if (this == null) {
            Taczcustomdeath.LOGGER.error("KillHUD.INSTANCE is null!");
        }
        Taczcustomdeath.LOGGER.info("Try pushing kill");
//        Taczcustomdeath.LOGGER.info(attacker.toString());
//        Taczcustomdeath.LOGGER.info(victim.toString());
        if (attacker == null || victim == null || weaponIcon == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            Taczcustomdeath.LOGGER.error("Minecraft level or player is null");
            return;
        }

        int attackerColor = 0xE45A50; // default red
        int victimColor = 0xE45A50;
        boolean includeSelf = false;

        Taczcustomdeath.LOGGER.info("{}{}", attacker.toString(), mc.player.getUUID().toString());
        Taczcustomdeath.LOGGER.info(String.valueOf((Objects.equals(attacker.toString(), mc.player.getUUID().toString()))));


        if (!Objects.equals(attackerTeam, "")){
            Team attackerTeamT = mc.level.getScoreboard().getPlayerTeam(attackerTeam);
            if (mc.player.isAlliedTo(attackerTeamT)) {
                attackerColor = 0x22ff00; // green
            }}

        if (!Objects.equals(victimTeam, "")){
            Team victimTeamT = mc.level.getScoreboard().getPlayerTeam(victimTeam);
            if (mc.player.isAlliedTo(victimTeamT)) {
                victimColor = 0x22ff00; // green
            }}

        if ((Objects.equals(attacker.toString(), mc.player.getUUID().toString()))) {
            attackerColor = YELLOW; // yellow
            includeSelf = true;
        }

        if ((Objects.equals(victim.toString(), mc.player.getUUID().toString()))) {
            victimColor = YELLOW; // yellow
            includeSelf = true;
        }

        if ((Objects.equals(attackerTeam, "MobTeam"))) {
            attackerColor = 0xcdcdcd;
        }

        if ((Objects.equals(victimTeam, "MobTeam"))) {
            victimColor = 0xcdcdcd;
        }

        messages.addLast(new KillMessage(attackerName, attackerColor, victimName, victimColor, weaponIcon, includeSelf));

        if (messages.size() > MAX_MESSAGES) {
            messages.removeFirst();
        }
    }

    public static void drawFlippedIcon(ResourceLocation texture, int x, int y, int width, int height, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        // Set shader color with alpha
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Flip U coords
        buffer.vertex(x,         y + height, 0).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(x + width, y + height, 0).uv(0.0F, 1.0F).endVertex();
        buffer.vertex(x + width, y,          0).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(x,         y,          0).uv(1.0F, 0.0F).endVertex();

        tessellator.end();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // Reset color

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static ResourceLocation get_flipped_texture(ResourceLocation textureLocation){
        Resource resource = Minecraft.getInstance().getResourceManager().getResource(textureLocation).orElseThrow();

        NativeImage image = null;
        try (InputStream is = resource.open()) {
            image = NativeImage.read(NativeImage.Format.RGBA, is);
        } catch (IOException e) {
            Taczcustomdeath.LOGGER.error("Failed to load and flip texture: {}", textureLocation, e);
        }

// Flip horizontally
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width / 2; x++) {
                    int left = image.getPixelRGBA(x, y);
                    int right = image.getPixelRGBA(width - 1 - x, y);
                    image.setPixelRGBA(x, y, right);
                    image.setPixelRGBA(width - 1 - x, y, left);
                }
            }

// Create a new dynamic texture
            DynamicTexture dynTex = new DynamicTexture(image);
            return Minecraft.getInstance().getTextureManager().register("flipped_texture", dynTex);
        }
        return textureLocation;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        // Taczcustomdeath.LOGGER.info("Rendering kill messages: " + messages.size());
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        messages.removeIf(KillMessage::isExpired);
        int baseY = screenHeight - MARGIN_VERTICAL;
        int i = 0;

        Iterator<KillMessage> it = messages.descendingIterator();
        while (it.hasNext()) {

            KillMessage msg = it.next();
            int alphaInt = (int)(msg.getAlpha() * 255);
            int attackerColorWithAlpha = (alphaInt << 24) | (msg.attackerColor & 0x00FFFFFF);
            int victimColorWithAlpha = (alphaInt << 24) | (msg.victimColor & 0x00FFFFFF);
            // Calculate widths
            int attackerWidth =  (int) (font.width(msg.attackerName) * TEXT_SCALE);
            int victimWidth =  (int) (font.width(msg.victimName) * TEXT_SCALE);
            int totalWidth = (int) (((SPACING_X + SPACING_X) * TEXT_SCALE) + ICON_SIZE + victimWidth + attackerWidth);

            int xDelta = (int) (attackerWidth - attackerWidth * TEXT_SCALE);
            int yDelta = (int) ((font.lineHeight - font.lineHeight * TEXT_SCALE) / 2);

// Compute scaled reverse deltas
            int scaledTotalWidth = (int) (totalWidth / TEXT_SCALE);
            int scaledXOffset = (int) (xDelta / TEXT_SCALE);
            int scaledYDelta = (int) (yDelta / TEXT_SCALE);

// Starting X & Y (will be overridden depending on padding)
            int posX, posX2, posY, posY2;

// Auto positioning based on padding
            int entryHeight = (int) (ICON_SIZE2 + SPACING_Y  + 2);

            switch (PADDING) {

                case TOP_LEFT:
                    // X stays on the left
                    posX  = (int) (MARGIN_HORIZONTAL / TEXT_SCALE);
                    posX2 = MARGIN_HORIZONTAL;

                    // Y starts at top: margin + index*entryHeight
                    posY2 = MARGIN_VERTICAL + (i * entryHeight);
                    posY  = (int) ((posY2 / TEXT_SCALE) + yDelta);
                    break;


                case TOP_RIGHT:
                    // X stays on the right
                    posX  = (int) ((screenWidth - MARGIN_HORIZONTAL - totalWidth) / TEXT_SCALE);
                    posX2 = screenWidth - MARGIN_HORIZONTAL - totalWidth;

                    // Y same as LEFT_TOP
                    posY2 = MARGIN_VERTICAL + (i * entryHeight);
                    posY  = (int) ((posY2 / TEXT_SCALE) + yDelta);
                    break;


                case BOTTOM_LEFT:
                    // X on left
                    posX  = (int) (MARGIN_HORIZONTAL / TEXT_SCALE);
                    posX2 = MARGIN_HORIZONTAL;

                    // Y starts from bottom: screenHeight - margin - index*entryHeight
                    posY2 = screenHeight - MARGIN_VERTICAL - (i * entryHeight);
                    posY  = (int) ((posY2 / TEXT_SCALE) + yDelta);
                    break;


                default: // RIGHT_BOTTOM
                    posX  = (int) ((screenWidth - MARGIN_HORIZONTAL - totalWidth) / TEXT_SCALE);
                    posX2 = screenWidth - MARGIN_HORIZONTAL - totalWidth;

                    posY2 = screenHeight - MARGIN_VERTICAL - (i * entryHeight);
                    posY  = (int) ((posY2 / TEXT_SCALE) + yDelta);
                    break;
            }
            int bgAlpha = (int)(msg.getAlpha() * 150); // tweak 150 → 255 depending on opacity
            int bgColor = (bgAlpha << 24) | 0x2B2B2B;
            int frameY = (int)(posY2 - TEXT_SCALE - TEXT_SCALE);
            int frameX = (int)(posX2 - 8*TEXT_SCALE);
            int frameW = (int)(posX2 + totalWidth + 7*TEXT_SCALE);
            int frameH = (int)(posY2 - TEXT_SCALE + ICON_SIZE2 + TEXT_SCALE);
            int borderWidth = 1;
            int bdAlpha = (int)(msg.getAlpha() * 255); // tweak 150 → 255 depending on opacity
            int bdColor = (bdAlpha << 24) | 0xe0e0e0;
            if (HAS_BACKGROUND){
                guiGraphics.fill(
                        frameX,                // left
                        frameY,                // top
                        frameW,   // right
                        frameH,   // bottom (or entryHeight)
                        bgColor
                );
                if (msg.includeSelf){
                    guiGraphics.fill(frameX - borderWidth, frameY - borderWidth, frameW + borderWidth, frameY, bdColor);
                    guiGraphics.fill(frameX - borderWidth, frameH, frameW + borderWidth, frameH + borderWidth, bdColor);
                    guiGraphics.fill(frameX - borderWidth, frameY, frameX, frameH, bdColor);
                    guiGraphics.fill(frameW, frameY, frameW + borderWidth, frameH, bdColor);
                }
                }
            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            // pose.translate(screenWidth - screenWidth / 0.75f, posY / 0.75f, 0);
            pose.scale(TEXT_SCALE, TEXT_SCALE, 1.0f);
            // Draw attacker name
            guiGraphics.drawString(font, msg.attackerName, posX, posY + (ICON_SIZE2 - font.lineHeight) / 2, attackerColorWithAlpha, false);
            pose.popPose();
            // Draw weapon image
            int iconX = (int) (posX2 + attackerWidth + SPACING_X*TEXT_SCALE);;

            drawFlippedIcon(msg.weaponIcon, iconX, posY2-1, ICON_SIZE, ICON_SIZE2, msg.getAlpha());

            // Draw victim name
            pose.pushPose();
            //pose.translate(posX / 0.75f, posY / 0.75f, 0);
            pose.scale(TEXT_SCALE, TEXT_SCALE, 1.0f);
            int victimX = (int) (((iconX + ICON_SIZE) / TEXT_SCALE + SPACING_X));
            guiGraphics.drawString(font, msg.victimName, victimX, posY + (ICON_SIZE2 - font.lineHeight) / 2, victimColorWithAlpha, false);
            pose.popPose();
            i++;
        }
    }

    private static class KillMessage {
        public final String attackerName;
        public final int attackerColor;
        public final String victimName;
        public final int victimColor;
        public final ResourceLocation weaponIcon;
        public final long timestamp;
        public final boolean includeSelf;

        public KillMessage(String attackerName, int attackerColor, String victimName, int victimColor, ResourceLocation weaponIcon, boolean includeSelf) {
            this.attackerName = attackerName;
            this.attackerColor = attackerColor;
            this.victimName = victimName;
            this.victimColor = victimColor;
            this.weaponIcon = weaponIcon;
            this.timestamp = System.currentTimeMillis();
            this.includeSelf = includeSelf;
        }

        public float getAlpha() {
            long age = System.currentTimeMillis() - timestamp;
            if (age >= MESSAGE_LIFETIME) return 0.1f;
            if (age >= FADE_START) return 1.0f - ((age - FADE_START) / 1000f); // Fade out in last 1s
            return 1f;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > MESSAGE_LIFETIME;
        }
    }
}