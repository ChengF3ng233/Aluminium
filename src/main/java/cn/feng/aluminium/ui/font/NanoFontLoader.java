package cn.feng.aluminium.ui.font;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class NanoFontLoader {
    public static NanoFontRenderer noto;
    public static NanoFontRenderer emoji;
    public static NanoFontRenderer playwrite;
    public static NanoFontRenderer script;
    public static NanoFontRenderer poppins;
    public static NanoFontRenderer quicksand;

    public static void init() {
        noto = new NanoFontRenderer("Noto", "noto");
        emoji = new NanoFontRenderer("Emoji", "emoji");
        playwrite = new NanoFontRenderer("Playwrite", "playwrite");
        script = new NanoFontRenderer("Script", "script");
        poppins = new NanoFontRenderer("Poppins", "poppins");
        quicksand = new NanoFontRenderer("Quicksand", "quicksand");
    }
}
