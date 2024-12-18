package cn.feng.aluminium.util.data;


import cn.feng.aluminium.util.exception.MemberNotFoundException;

import java.time.LocalTime;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class StringUtil {
    public static boolean arrayContains(String[] array, String target, boolean ignoreCase) {
        for (String s : array) {
            if (s.equals(target) || (ignoreCase && s.equalsIgnoreCase(target))) return true;
        }

        return false;
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // 处理空值或空字符串
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }


    public static String getGreeting() {
        LocalTime now = LocalTime.now();

        if (now.isBefore(LocalTime.NOON)) {
            return "早上好";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            return "中午好";
        } else if (now.isBefore(LocalTime.of(22, 0))) {
            return "下午好";
        } else if (now.isBefore(LocalTime.MIDNIGHT)) {
            return "晚上好";
        } else if (now.equals(LocalTime.MIDNIGHT)) {
            return "午夜";
        } else {
            return "清晨";
        }
    }

    public static String convertMillisToMinSec(long millis) {
        long totalSeconds = millis / 1000; // 将毫秒转换为秒
        long minutes = totalSeconds / 60; // 计算分钟
        long seconds = totalSeconds % 60; // 计算剩余的秒
        return String.format("%01d:%02d", minutes, seconds); // 格式化为xx:xx
    }

    public static String[] subArray(String[] array, int startIndex) {
        String[] newArray = new String[array.length - startIndex];
        for (int i = startIndex; i < array.length; i++) {
            newArray[i - startIndex] = array[startIndex];
        }
        return newArray;
    }

    /**
     * 获取在指定子字符串之前的部分
     *
     * @param fullString 原始字符串
     * @param delimiter  要查找的子字符串
     * @return 在子字符串之前的部分
     */
    public static String getSubstringBefore(String fullString, String delimiter) {
        // 查找子字符串的位置
        int index = fullString.indexOf(delimiter);

        // 如果找不到子字符串，返回原始字符串
        if (index == -1) {
            return fullString;
        }

        // 截取在子字符串之前的部分
        return fullString.substring(0, index);
    }

    public static int arrayIndex(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) return i;
        }

        throw new MemberNotFoundException("ARRAY: " + target);
    }
}
