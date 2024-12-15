package cn.feng.aluminium.util.data.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {
    public static RGB getMainRgb(BufferedImage image) throws Exception {
        Map<Float, Integer> hueCountMap = new HashMap<>();
        Map<HSL, Integer> hslCountMap = new HashMap<>();
        int width = image.getWidth();
        int height = image.getHeight();
        int minx = image.getMinX();
        int miny = image.getMinY();
        //计算各点的hsl值，并统计数量
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = image.getRGB(i, j);
                Color color = new Color(pixel);
                RGB rgb = new RGB(color.getRed(), color.getGreen(), color.getBlue());
                HSL hsl = ColorConverter.RGB2HSL(rgb);
                float h = computeHue(hsl.getH());
                float s = computeSAndL(hsl.getS());
                float l = computeSAndL(hsl.getL());
                HSL newHSL = new HSL(h, s, l);
                //统计hue值数量
                Integer count = hueCountMap.get(h);
                if (count == null) {
                    hueCountMap.put(h, 1);
                } else {
                    hueCountMap.put(h, count + 1);
                }
                //统计HSL数量
                count = hslCountMap.get(newHSL);
                if (count == null) {
                    hslCountMap.put(newHSL, 1);
                } else {
                    hslCountMap.put(newHSL, count + 1);
                }
            }
        }
        //查找数量最多的hue值
        float maxHue = 0;
        int maxCount = 0;
        for (Map.Entry<Float, Integer> entry : hueCountMap.entrySet()) {
            float hue = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                maxHue = hue;
            }
        }
        //查找maxHue中数量最多的hsl值
        HSL maxHSL = null;
        maxCount = 0;
        for (Map.Entry<HSL, Integer> entry : hslCountMap.entrySet()) {
            HSL hsl = entry.getKey();
            int count = entry.getValue();
            if (hsl.getH() == maxHue && count > maxCount) {
                maxCount = count;
                maxHSL = hsl;
            }
        }
        //hsl转rgb
        return ColorConverter.HSL2RGB(maxHSL);
    }

    /**
     * 按格子划分h值
     *
     * @param h
     * @return
     */
    public static float computeHue(float h) {
        if (h <= 15) {
            return 0;
        }
        if (15 < h && h <= 45) {
            return 30;
        }
        if (45 < h && h <= 75) {
            return 60;
        }
        if (75 < h && h <= 105) {
            return 90;
        }
        if (105 < h && h <= 135) {
            return 120;
        }
        if (135 < h && h <= 165) {
            return 150;
        }
        if (165 < h && h <= 195) {
            return 180;
        }
        if (195 < h && h <= 225) {
            return 210;
        }
        if (225 < h && h <= 255) {
            return 240;
        }
        if (255 < h && h <= 285) {
            return 270;
        }
        if (285 < h && h <= 315) {
            return 300;
        }
        if (315 < h && h <= 345) {
            return 330;
        }
        if (345 < h) {
            return 360;
        }
        return 360;
    }

    /**
     * 按格子划分s和l值
     *
     * @param s
     * @return
     */
    public static float computeSAndL(float s) {
        if (s <= 32) {
            return 0;
        }
        if (32 < s && s <= 96) {
            return 64;
        }
        if (96 < s && s <= 160) {
            return 128;
        }
        if (160 < s && s <= 224) {
            return 192;
        }
        if (s > 224) {
            return 255;
        }
        return 255;
    }

}
