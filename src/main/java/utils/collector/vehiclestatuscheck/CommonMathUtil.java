package utils.collector.vehiclestatuscheck;

import java.math.BigDecimal;

/**
 * @author yujingwei
 * @version 创建时间:2020-04-09 09:30
 * @description 常用算数工具类
 */
public class CommonMathUtil {

    /**
     * 两数相除并保留指定位小数 四舍五入
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static Double divide(double a, double b, int scale) {
        if (b == 0) {
            return 0d;
        }
        BigDecimal bd1 = BigDecimal.valueOf(a);
        BigDecimal bd2 = BigDecimal.valueOf(b);
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留小数,四舍五入
     *
     * @param data
     * @param amount
     * @return
     */
    public static Double round(Double data, int amount) {
        if (data == null) {
            return 0d;
        }
        //利用BigDecimal来实现四舍五入.保留一位小数
        double result = BigDecimal.valueOf(data).setScale(amount, BigDecimal.ROUND_HALF_UP).doubleValue();
        //1代表保留1位小数,保留两位小数就是2,依此累推
        //BigDecimal.ROUND_HALF_UP 代表使用四舍五入的方式
        return result;
    }

    /**
     * 参数是Double 会有精度问题
     * BigDecimal也需要设置小数点后保留位数
     *
     * 数值转百分比字符串
     *
     * @param val
     * @return
     */

    public static String convert2Percent(BigDecimal val) {
        if (val == null) {
            return "0%";
        }
        return  (val.multiply(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_UP)) + "%";
    }

}
