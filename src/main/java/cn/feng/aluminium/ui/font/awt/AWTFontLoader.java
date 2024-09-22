package cn.feng.aluminium.ui.font.awt;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class AWTFontLoader {
    private static final Map<Float, AWTFontRenderer> notoMap = new HashMap<>();
    private static final Map<Float, AWTFontRenderer> poppinsMap = new HashMap<>();
    private static final Map<Float, AWTFontRenderer> quicksandMap = new HashMap<>();

    public static AWTFontRenderer noto(float size) {
        return get(notoMap, "noto", size);
    }

    public static AWTFontRenderer poppins(float size) {
        return get(poppinsMap, "poppins", size);
    }

    public static AWTFontRenderer quicksand(float size) {
        return get(quicksandMap, "quicksand", size);
    }

    private static AWTFontRenderer get(Map<Float, AWTFontRenderer> map, String resourceName, float size) {
        if (!map.containsKey(size)) {
            map.put(size, new AWTFontRenderer(resourceName, size));
        }
        return map.get(size);
    }
}
