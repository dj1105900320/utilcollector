package utils.collector.dpcX.dpc.core;

import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * 进制转换工具
 *
 * @author yjw
 * @date 2021/08/03
 **/
@Slf4j
public class HexConvertUtil {
    /**
     * 二进制字符串转byte
     *
     * @param bitStr 二进制字符串
     * @return 字节
     */
    public static byte bitStr2Byte(String bitStr) {
        byte result = 0x00;
        for (int i = bitStr.length() - 1, j = 0; i >= 0; i--, j++) {
            result += (Byte.parseByte(bitStr.charAt(i) + "") * Math.pow(2, j));
        }
        return result;

    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param data 待转换的二进制数组
     * @return 十六进制字符串
     */
    public static String encodeHexStr(byte[] data) {
        return HexUtil.encodeHexStr(data);
    }

    /**
     * 根据位计算结果
     *
     * @param bytes
     * @return 将字节数组装换成二进制字符串，并根据位数计算结果
     */
    public static int toIntByBytesIndex(byte[] bytes, int low, int high) {
        int result;
        String bitStr = new BigInteger(1, bytes).toString(2);
        int bitStrLeng = bitStr.length();
        int bytesLeng = bytes.length * 8;
        String finalResult;
        if (bitStrLeng < bytesLeng) {
            //1.使用字符串的格式化，先左填充空格
            String format = "%" + bytesLeng + "s";
            String tempResult = String.format(format, bitStr);
            //2.使用String的replace函数将空格转换为指定字符即可
            finalResult = tempResult.replace(" ", "0");
        } else {
            finalResult = bitStr;
        }
        // 2进制
        result = Integer.parseInt(finalResult.substring(low, high), 2);
        return result;
    }

    /**
     * 根据传入的字节数组提取指定位数进行计算
     *
     * @param bytes 原字节数组
     * @param start 起始位
     * @param end   结束位
     * @return 结果值
     */
    public static int shiftAndDecimal(byte[] bytes, int start, int end) {
        int byteLength = bytes.length;
        if (byteLength == 0) {
            throw new IllegalArgumentException("byte数组不能为空");
        }

        // 填充字符串
        String supplementStr = "00000000";
        StringBuilder supplementStrBuilder = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            supplementStrBuilder.append(supplementStr);
        }

        String bitStr = new BigInteger(1, bytes).toString(2);
        // 比对并进行填充
        bitStr = supplementStrBuilder.substring(0, supplementStrBuilder.length() - bitStr.length()) + bitStr;
        return Integer.parseInt(bitStr.substring(start, end), 2);
    }

}
