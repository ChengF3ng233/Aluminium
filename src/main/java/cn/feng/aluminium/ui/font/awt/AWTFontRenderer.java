package cn.feng.aluminium.ui.font.awt;

import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class AWTFontRenderer {
    private static final int[] COLOR_CODES = new int[32];

    static {
        setupColorCodes();
    }

    private final Font font;
    private final float fontHeight;
    private final Map<Character, FontCharacter> characterMap = new HashMap<>();
    private final AWTFontRenderer boldRenderer;

    /**
     * AWT font renderer
     *
     * @param resourceName name
     * @param size         size
     */
    public AWTFontRenderer(String resourceName, float size) {
        try (InputStream stream = ResourceUtil.getResourceAsStream(resourceName + ".ttf", ResourceType.FONT)) {
            if (stream == null) throw new NullPointerException("Resource not found: " + resourceName);
            font = Font.createFont(0, stream).deriveFont(size);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
        fontHeight = (float) (font.getStringBounds("ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new FontRenderContext(new AffineTransform(), true, true)).getHeight() / 4.0D);
        boldRenderer = new AWTFontRenderer(resourceName, size, font);
    }

    /**
     * Bold renderer constructor
     *
     * @param resourceName name
     * @param size         size
     * @param plainFont    default
     */
    private AWTFontRenderer(String resourceName, float size, Font plainFont) {
        try (InputStream stream = ResourceUtil.getResourceAsStream(resourceName + "-bold.ttf", ResourceType.FONT)) {
            font = stream == null ? plainFont : Font.createFont(0, stream).deriveFont(size);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        fontHeight = (float) (font.getStringBounds("ABCDEFGHOKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new FontRenderContext(new AffineTransform(), true, true)).getHeight() / 4.0D);
        boldRenderer = this;
    }

    /**
     * Setup Minecraft color codes
     */
    private static void setupColorCodes() {
        for (int i = 0; i < 32; ++i) {
            int amplifier = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + amplifier;
            int green = (i >> 1 & 1) * 170 + amplifier;
            int blue = (i & 1) * 170 + amplifier;
            if (i == 6) {
                red += 85;
            }

            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            COLOR_CODES[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public static String removeColorCodes(String text) {
        return Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]").matcher(text).replaceAll("");
    }

    public AWTFontRenderer bold() {
        return boldRenderer;
    }

    public float getFontHeight() {
        return fontHeight;
    }

    private void uploadTexture(int texture, BufferedImage image, int width, int height) {
        int[] pixels = image.getRGB(0, 0, width, height, new int[width * height], 0, width);
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int pixel = pixels[x + y * width];
                byteBuffer.put((byte) (pixel >> 16 & 255));
                byteBuffer.put((byte) (pixel >> 8 & 255));
                byteBuffer.put((byte) (pixel & 255));
                byteBuffer.put((byte) (pixel >> 24 & 255));
            }
        }

        byteBuffer.flip();
        GlStateManager.bindTexture(texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, 6408, width, height, 0, 6408, 5121, byteBuffer);
        byteBuffer.clear();
    }

    /**
     * 填充单个字符
     *
     * @param character 字符
     */
    private void uploadCharacter(char character) {
        BufferedImage fontImage = new BufferedImage(1, 1, 2);
        Graphics2D fontGraphics = (Graphics2D) fontImage.getGraphics();
        FontMetrics fontMetrics = fontGraphics.getFontMetrics(font);

        Rectangle2D charRectangle = fontMetrics.getStringBounds(character + "", fontGraphics);
        BufferedImage charImage = new BufferedImage(MathHelper.ceiling_float_int((float) charRectangle.getWidth()) + 8, MathHelper.ceiling_float_int((float) charRectangle.getHeight()), 2);
        Graphics2D charGraphics = (Graphics2D) charImage.getGraphics();
        int width = charImage.getWidth();
        int height = charImage.getHeight();

        charGraphics.setColor(ColorUtil.TRANSPARENT_COLOR);
        charGraphics.fillRect(0, 0, width, height);
        charGraphics.setFont(font);

        preDraw(charGraphics);
        charGraphics.drawString(character + "", 4, font.getSize());

        int charTexture = GL11.glGenTextures();
        uploadTexture(charTexture, charImage, width, height);

        characterMap.put(character, new FontCharacter(charTexture, (float) width, (float) height));
    }

    private void preDraw(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    public String trimStringToWidth(String text, float width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        if (text == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            int startIndex = reverse ? text.length() - 1 : 0;
            int step = reverse ? -1 : 1;

            String nextChar = "";
            for (int i = startIndex; i <= text.length() - 1 && i >= 0 && getStringWidth(builder + nextChar) <= width; i += step) {
                builder.append(text.charAt(i));
                String next = String.valueOf(text.charAt(i + step));
                nextChar = reverse ? next : i == text.length() - 1 ? "" : next;
            }

            if (reverse) builder.reverse();
            return builder.toString();
        }
    }

    public int drawString(String text, double x, double y, Color color) {
        return this.renderString(text, x, y, color.getRGB(), false);
    }

    public int drawCenteredString(String text, double x, double y, Color color, CenterType type) {
        return drawCenteredString(text, x, y, color, type, false);
    }

    public int drawCenteredString(String text, double x, double y, Color color, CenterType type, boolean shadow) {
        boolean xCenter = type == CenterType.Both || type == CenterType.Horizontal;
        boolean yCenter = type == CenterType.Both || type == CenterType.Vertical;

        double realX = x - (xCenter ? getStringWidth(text) / 2d : 0d);
        double realY = y - (yCenter ? fontHeight / 2d : 0d);

        if (shadow)
            this.renderString(text, realX + 0.25d, realY + 0.25d, color.getRGB(), true);
        return this.drawString(text, realX, realY, color);
    }

    public int drawString(String text, double x, double y, Color color, boolean shadow) {
        if (shadow) {
            this.renderString(text, x + 0.5, y + 0.5, color.getRGB(), true);
        }
        return this.renderString(text, x, y, color.getRGB(), false);
    }

    public int renderString(String text, double x, double y, int color, boolean shadowMode) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScalef(0.5F, 0.5F, 0.5F);

        double startX = x;
        x -= 2.0D;
        y -= 2.0D;
        x *= 2.0D;
        y *= 2.0D;
        y -= this.fontHeight / 2.5F;

        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        if (shadowMode) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }

        // 先上一遍色，防止之前渲染的遗留问题
        RenderUtil.color(color);

        boolean specialColor = false;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            switch (character) {
                case '\n': {
                    x = startX;
                    y += fontHeight * 2.0F;
                    specialColor = false;
                    break;
                }

                case '§': {
                    int colorIndex = 21;

                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    } catch (Exception var19) {
                        Util.logger.warn("Illegal color code: {}", text.charAt(i + 1));
                    }

                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }

                    if (colorIndex < 16) {
                        if (shadowMode) {
                            colorIndex += 16;
                        }

                        int mcColor = COLOR_CODES[colorIndex];

                        GlStateManager.color((float) (mcColor >> 16 & 255) / 255.0F, (float) (mcColor >> 8 & 255) / 255.0F, (float) (mcColor & 255) / 255.0F, (color >> 24 & 255) / 255.0F);
                        specialColor = true;
                    }

                    i++;
                    break;
                }

                default: {
                    if (!specialColor) {
                        RenderUtil.color(color);
                    }

                    if (characterMap.containsKey(character)) {
                        /* 英文字母 gyj 这类比较靠下导致 font height 较高，渲染高度一致的中文字符会显得偏上
                         * 所以渲染中文字符时将 y 下调
                         */
                        FontCharacter fontCharacter = characterMap.get(character);
                        float realY = (character > 256) ? (float) y + 1 : (float) y;
                        fontCharacter.render((float) x, realY);
                        x += fontCharacter.width() - 8.0F;
                    } else {
                        uploadCharacter(character);
                    }
                }
            }
        }

        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GlStateManager.bindTexture(0);
        GlStateManager.resetColor();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        return (int) (startX + getStringWidth(text));
    }

    public float getStringWidth(String text) {
        text = removeColorCodes(text);

        float width = 0;
        for (char c : text.toCharArray()) {
            if (characterMap.containsKey(c)) {
                width += (characterMap.get(c).width() - 8f);
            } else {
                uploadCharacter(c);
            }
        }

        return width / 2f;
    }
}
