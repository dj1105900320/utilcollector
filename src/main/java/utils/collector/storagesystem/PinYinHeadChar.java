package utils.collector.storagesystem;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author ：fengliang
 * @date ：Created in 2021/8/30
 * @description：批量生成汉字首字母拼音缩写
 * @modified By：
 * @version: $
 */
public class PinYinHeadChar {
    /**
     * 提取每个汉字的首字母
     *
     * @param str
     *            汉子输入
     * @return String 拼音缩写输出
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

}
