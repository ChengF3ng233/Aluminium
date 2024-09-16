package cn.feng.aluminium.ui.font;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class SkijaFontLoader {
    public static SkijaFontRenderer playwrite;
    public static SkijaFontRenderer poppins;
    public static SkijaFontRenderer quicksand;
    public static SkijaFontRenderer noto;
    public static SkijaFontRenderer emoji;
    public static void init() {
        playwrite = new SkijaFontRenderer("playwrite");
        poppins = new SkijaFontRenderer("poppins");
        quicksand = new SkijaFontRenderer("quicksand");
        noto = new SkijaFontRenderer("noto");
        emoji = new SkijaFontRenderer("emoji");
    }
}
