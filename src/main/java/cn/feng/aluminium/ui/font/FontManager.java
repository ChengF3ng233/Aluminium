package cn.feng.aluminium.ui.font;

import cn.feng.aluminium.ui.font.impl.UFontRenderer;

import java.util.HashMap;

public class FontManager {
    private static final HashMap<Integer, UFontRenderer> notoMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> notoBoldMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> playwriteMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> poppinsMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> poppinsBoldMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> archivoMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> pingfangMap = new HashMap<>();
    private static final HashMap<Integer, UFontRenderer> pingfangBoldMap = new HashMap<>();

    private static UFontRenderer getRenderer(String name, int size, HashMap<Integer, UFontRenderer> map) {
        if (map.containsKey(size)) {
            return map.get(size);
        }
        UFontRenderer newRenderer = new UFontRenderer(name, size);
        map.put(size, newRenderer);
        return newRenderer;
    }

    public static UFontRenderer pingfang(int size) {
        return getRenderer("pingfang", size, pingfangMap);
    }

    public static UFontRenderer pingfangBold(int size) {
        return getRenderer("pingfang-bold", size, pingfangBoldMap);
    }

    public static UFontRenderer noto(int size) {
        return getRenderer("noto", size, notoMap);
    }

    public static UFontRenderer notoBold(int size) {
        return getRenderer("noto-bold", size, notoBoldMap);
    }

    public static UFontRenderer playwrite(int size) {
        return getRenderer("playwrite", size, playwriteMap);
    }

    public static UFontRenderer poppins(int size) {
        return getRenderer("poppins", size, poppinsMap);
    }

    public static UFontRenderer poppinsBold(int size) {
        return getRenderer("poppins-bold", size, poppinsBoldMap);
    }

    public static UFontRenderer archivo(int size) {
        return getRenderer("archivo", size, archivoMap);
    }
}
