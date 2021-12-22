package utils.collector.nettytopump;

import cn.hutool.core.util.HexUtil;
import utils.collector.bsbcms.CRC16Util;

import java.nio.charset.StandardCharsets;

/**
 * @author chenyang
 *
 */
public class Crc {

    public static void main(String[] args) {

//        String s = "0f1001135010404818230ce9000000000004050201cfe921072bab0500010000001c2111241542530014000000000018069a17045f01000000000018239a17ffffffff0000000018039a1700000000c0fff0ff18029a173c030300f000303f18129a17ffffffffffffffff18019a1740f03c3f00f00c00180a9a17ffffffffffffffff18209a175c0d50c33268204e18219a175552ff004eff00ff18109a17003000300030003018059a17000000500000ffff18079a178e590200ffffffff18049a170000ffffffff280018229a17720100ff690100ff18259a17ffffffffffffffff18089a17027d01ffffffffffff18099a17ffffffffffffffff18249a17ffffffffffffffff180b9a171c230f180be507ff18119a1700300030c03fffff";
        String s = "090000631234567890127ffff555aa520003100200000000000000000000000000000000000000003132333132333132330000000000000000d1a9c0fbc2fcb1a8d5bec6f70000000031323334353600000000000000000030000000000000000000000000000000000000003166bb";
        byte[] bytes = name(s);
        String s1 = HexUtil.encodeHexStr(bytes);
        StringBuilder sb = new StringBuilder();
        sb.insert(0,"7e");
        sb.append(s1);
        sb.append("7e");
        System.out.println(sb);

        int crc = CRC16Util.CRC16_XMODEM(s.getBytes());
        System.out.println(crc);
    }

    public static byte[] name(String s) {
        byte[] result  = new byte[s.length()/2 + 1];
        int i = 0;
        int j = 0;
        while( i < s.length()) {
            String item = s.substring(i, i + 2);
            result[j] = (byte)Integer.parseInt(item,16);
            i = i + 2;
            j++;

        }
        result[result.length - 1] = GetCheckXor(result,0,result.length - 1);
        return result;
    }

    private static byte GetCheckXor(byte[] data, int pos, int len) {
        byte A = 0;
        for (int i = pos; i < len; i++) {
            A ^= data[i];
        }
        return A;
    }

}
