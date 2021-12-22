package utils.collector.dpcX.dpc.core;


import org.apache.commons.lang3.ObjectUtils;

/**
 * 数组判断工具
 *
 * @author yjw
 * @date 2021/08/04
 **/
public class ArrayUtil {
    /**
     * 数组不为空且长度大于传入要求
     *
     * @param arr       目标数组
     * @param maxLength 需满足的长度
     * @return boolean
     */
    public static boolean isNotEmptyAndLongEnough(Object[] arr, int maxLength) {
        return ObjectUtils.isNotEmpty(arr) && arr.length >= maxLength;
    }

    /**
     * 数组不为空且长度大于传入要求
     *
     * @param arr       目标数组
     * @param maxLength 需满足的长度
     * @return boolean
     */
    public static boolean isNotEmptyAndLongEnough(String[] arr, int maxLength) {
        return arr != null && arr.length >= maxLength;
    }
}
