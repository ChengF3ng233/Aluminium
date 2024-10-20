package cn.feng.aluminium.util.data;

/**
 * @author ChengFeng
 * @since 2024/7/30
 **/
public class CharUtil {
    private static final String englishChar = " !\\\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    public static char[] getEnglishCharArray() {
        return englishChar.toCharArray();
    }
}
