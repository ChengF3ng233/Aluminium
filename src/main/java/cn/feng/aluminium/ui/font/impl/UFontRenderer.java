package cn.feng.aluminium.ui.font.impl;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

public class UFontRenderer extends FontRenderer {
    private final int FONT_HEIGHT = 8;
    private StringCache stringCache;

    public UFontRenderer(String name, int size) {
        super(
                Minecraft.getMinecraft().gameSettings,
                new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().getTextureManager(),
                false
        );
        boolean antiAlias = true;
        Font font;
        try {
            InputStream is = FontManager.class.getResourceAsStream("/assets/minecraft/aluminium/font/" + name + ".ttf");
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            Util.logger.error("Error loading font {}", name);
            font = new Font("Arial", Font.PLAIN, size);
        }

        ResourceLocation res = new ResourceLocation("textures/font/ascii.png");
        int[] colorCode = new int[32];
        for (int i = 0; i <= 31; i++) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | (i1 & 255);
        }

        try {
            if (Minecraft.getMinecraft().getResourceManager().getResource(res).getResourceLocation().getResourcePath().equalsIgnoreCase("textures/font/ascii.png")) {
                stringCache = new StringCache(colorCode);
                stringCache.setDefaultFont(font, size, antiAlias);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Draws the specified string with a shadow.
     */
    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        Color color1 = new Color(color);
        this.drawString(text, x, y, new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).getRGB(), true);
        return getStringWidth(text);
    }


    public String trimStringToWidth(String text, float width) {
        return trimString(text, width, false, false);
    }

    public String trimString(String text, float width, boolean more, boolean reverse) {
        String realText = reverse? new StringBuilder(text).reverse().toString() : text;
        StringBuilder stringbuilder = new StringBuilder();
        for (char c : realText.toCharArray()) {
            if (getStringWidth(stringbuilder.toString() + c) < width)
                stringbuilder.insert(reverse? 0 : stringbuilder.length(), c);
            else
                break;
        }
        if (more) {
            if (!stringbuilder.toString().equals(text)) {
                int extraWidth = getStringWidth("...");
                do {
                    stringbuilder.deleteCharAt(reverse? 0 : stringbuilder.length() - 1);
                } while (getStringWidth(stringbuilder.toString()) > width - extraWidth);
            }
        }
        return (more && reverse && !stringbuilder.toString().equals(text)? "..." : "") + stringbuilder + (more && !reverse && !stringbuilder.toString().equals(text)? "..." : "");
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, float x, int y, int color) {
        Color color1 = new Color(color);
        return this.drawString(text, x, y, new Color(color1.getRed(), color1.getGreen(), color1.getBlue()).getRGB(), false);
    }

    public int drawString(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, false);
        return getStringWidth(text);
    }

    /**
     * Automatically 换行
     * @return Actual rows that are rendered.
     */
    public float drawTrimString(String text, float x, float y, float maxWidth, int maxRows, float gap, int color) {
        float currentY = y;
        int row = 1;
        while (row <= maxRows) {
            String toRender = trimString(text, maxWidth, row == maxRows, false);
            text = text.replace(toRender.replace("...", ""), "");
            drawString(toRender, x, currentY, color);
            currentY += toRender.isEmpty()? 0 : getHeight() + gap;
            row++;
        }
        return currentY;
    }

    public int drawStringCapableWithEmoji(String text, float x, float y, int color) {
        char[] chars = text.toCharArray();
        int lastCut = 0;
        float xOffset = x;
        for (int i = 0; i < chars.length; i++) {
            if (isEmojiCharacter(text.codePointAt(i))) {
                xOffset += this.drawString(text.substring(0, i), xOffset, y, color, false);
                this.drawString(String.valueOf(chars[i]), xOffset, y, color, false);
                xOffset += this.getStringWidth(String.valueOf(chars[i]));
                lastCut = i + 1;
            }
        }
        this.drawString(text.substring(lastCut), xOffset, y, color, false);
        return getStringWidth(text);
    }

    public static boolean isEmojiCharacter(int codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                (codePoint >= 0x20 && codePoint <= 0xD7FF) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD));
    }

    /**
     * Draws the specified string.
     */
    @Override
    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        int i;
        if (dropShadow) {
            if (new Color(color).getAlpha() > 50) {
                stringCache.renderString(
                        text,
                        x + 0.5f,
                        y + 0.5f,
                        new Color(20, 20, 20, new Color(color).getAlpha()).getRGB(),
                        true
                );
            }
        }
        i = stringCache.renderString(text, x, y, color, false);
        return i;
    }

    @Override
    public int getStringWidth(String text) {
        return stringCache.getStringWidth(text);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - stringCache.getStringWidth(text) / 2f, y - stringCache.height / 4f - 1f, color, false);
    }

    public void drawCenteredStringH(String text, float x, float y, int color) {
        drawString(text, x - stringCache.getStringWidth(text) / 2f, y, color, false);
    }

    public void drawCenteredStringV(String text, float x, float y, int color) {
        drawString(text, x, y - stringCache.height / 4f - 1f, color, false);
    }

    public int getHeight() {
        return stringCache.height / 2;
    }

    public float drawStringCapableWithEmojiWithShadow(String text, float x, float y, int color) {
        String[] sbs = new String[]{"\uD83C\uDF89", "\uD83C\uDF81", "\uD83D\uDC79", "\uD83C\uDFC0", "⚽", "\uD83C\uDF6D", "\uD83C\uDF20", "\uD83D\uDC7E", "\uD83D\uDC0D"
                , "\uD83D\uDD2E", "\uD83D\uDC7D", "\uD83D\uDCA3", "\uD83C\uDF6B", "\uD83C\uDF82"};
        for (String sb : sbs) {
            text = text.replaceAll(sb, "");
        }
        return drawStringWithShadow(text, x, y, color);
    }
}
