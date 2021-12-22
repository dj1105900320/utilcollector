package utils.collector.vehiclestatuscheck;

import cn.hutool.core.util.ReUtil;

import java.util.Collections;
import java.util.List;

/**
 * 报文数据解析
 *
 * @author yjw
 * @date 2021/06/08
 **/
public class CanDataParseUtil {

    public static double extractMile(String hexStr) {
        //里程系数是0.5 结果乘0.5 等于 除以2
        // 正则查找8079A17
        List<String> all = ReUtil.findAll("8079[a|A]17[\\S]{8}", hexStr, 0);
        String s = all.get(0);
        // 删除CANID
        String afterDel = ReUtil.delFirst("8079[a|A]17", s);
        // 两两分组，按字节分组
        List<String> all1 = ReUtil.findAll("[\\d|\\w]{2}", afterDel, 0);
        // 倒装，因为低字节高位置
        Collections.reverse(all1);
        String result = "";
        for (String s1 : all1) {
            result += s1;
        }
        // 计算结果
        int mile = Integer.parseInt(result, 16);
        // 乘以系数
        return mile / 2.0;
    }

    public static int extractSoc(String hexStr) {
        List<String> all = ReUtil.findAll("8209[a|A]17[\\S]{16}", hexStr, 0);
        if (all.isEmpty()){
            return 0;
        }
        String s = all.get(0);
        String afterDel = ReUtil.delFirst("8209[a|A]17", s);
        String result = afterDel.substring(8, 10);
        int soc = Integer.parseInt(result, 16);
        return soc;
    }

    public static void main(String[] args) {
        // SOC 92%  mile 29008.0km
        String hexStr = "7e0f1001138100410644100430000000000004050201aba0b50730c5910009000000012106090809200014000000000058019a17d430cc3f00f10f3358029a170f0f3f30ff00303f58039a170004f104c0ffffff58049a176466ffffffff370058059a17f800ffff0000ffff58069a17000000000000140c58079a179ee20000ffffffff58089a170190ffffffffffff58099a17056ee02500000002580a9a17ffffffffffffffff58129a17ffffffffffffffff58209a17eb186cc35c633f4e58219a172845ff2840ff09ff58229a174c015eff4c0131ff58239a17000000000000000058249a17ffffffffffffffff58259a17ffffffffffffffff58119a175b200120c32fffff580b9a17280a080906e507ff58109a17c926c12640260b224d7e";
        System.out.println(extractMile(hexStr));
        System.out.println(extractSoc(hexStr));
    }
}
