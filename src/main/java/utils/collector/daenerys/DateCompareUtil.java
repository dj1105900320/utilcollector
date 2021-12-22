package utils.collector.daenerys;

import java.time.LocalDate;

/**
 * java8日期比较工具
 *
 * @author yjw
 * @date 2021/04/29
 **/
public class DateCompareUtil {
    /**
     * 判断某个日期是否在时间段内
     *
     * @param target 目标日期
     * @param start  开始日期
     * @param end    结束日期
     * @return boolean
     */
    public static boolean localDateIsBetween(LocalDate target, LocalDate start, LocalDate end) {
        return (target.isAfter(start) || target.isEqual(start)) && (target.isBefore(end) || target.isEqual(end));
    }
}
