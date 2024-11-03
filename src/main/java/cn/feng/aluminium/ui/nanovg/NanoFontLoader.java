package cn.feng.aluminium.ui.nanovg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontLoader {
    public static List<NanoFontRenderer> renderers = new ArrayList<>();
    public static NanoFontRenderer noto;
    public static NanoFontRenderer emoji;
    public static NanoFontRenderer script;
    public static NanoFontRenderer poppins;
    public static NanoFontRenderer quicksand;
    public static NanoFontRenderer archivo;

    public static void registerFonts() {
        noto = new NanoFontRenderer("Noto", "noto");
        emoji = new NanoFontRenderer("Emoji", "emoji");
        script = new NanoFontRenderer("Script", "script");
        poppins = new NanoFontRenderer("Poppins", "poppins");
        quicksand = new NanoFontRenderer("Quicksand", "quicksand");
        archivo = new NanoFontRenderer("Archivo", "archivo");

        renderers.add(noto);
        renderers.add(emoji);
        renderers.add(script);
        renderers.add(poppins);
        renderers.add(quicksand);
        renderers.add(archivo);
    }

    public static String[] getRenderers() {
        String[] values = new String[renderers.size()];
        for (int i = 0; i < renderers.size(); i++) {
            NanoFontRenderer fontRenderer = renderers.get(i);
            values[i] = fontRenderer.getName();
        }
        return values;
    }
}
