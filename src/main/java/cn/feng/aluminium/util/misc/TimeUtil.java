package cn.feng.aluminium.util.misc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class TimeUtil {
    public static Period getTimeBetween(long startTimestamp, long endTimestamp) {
        // 将时间戳转换为 LocalDate
        LocalDate startDate = Instant.ofEpochMilli(startTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(endTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 计算年、月、天的差异
        return Period.between(startDate, endDate);
    }

    public static String millisToMMSS(long millis) {
        // 将毫秒转换为秒
        long seconds = millis / 1000;

        // 计算分钟和秒
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        // 格式化输出为 mm:ss 格式
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
