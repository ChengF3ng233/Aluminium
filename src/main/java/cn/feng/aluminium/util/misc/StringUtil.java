package cn.feng.aluminium.util.misc;

import java.time.LocalTime;

public class StringUtil {
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
}
