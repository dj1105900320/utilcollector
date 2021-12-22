package utils.collector.bsbcms;

/**火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的互转
 * Created by macremote on 16/5/3.
 */
public class GPSUtil {
    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat,mgLon};
    }
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }
    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     * */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**将gps84转为bd09
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_bd09(double lat,double lon){
        double[] gcj02 = gps84_To_Gcj02(lat,lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0],gcj02[1]);
        //保留小数点后八位
        bd09[0] = retain8(bd09[0]);
        bd09[1] = retain8(bd09[1]);
        return bd09;
    }

    /**
     * 将bd09转为gps84
     * @param lat
     * @param lon
     * @return
     */
    public static double[] bd09_To_gps84(double lat,double lon){
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后八位
        gps84[0] = retain8(gps84[0]);
        gps84[1] = retain8(gps84[1]);
        return gps84;
    }

    /**
     * 将gps84转为报站器经纬度 并保留小数点后五位
     * @author lk
     * @param d
     * @return
     */
    public static double gps84_To_bsbcms(double d){
        return retain5(Math.floor(d) * 100 + (Math.abs(d) % 1) * 60);
    }

    /**
     * 将报站器经纬度转为gps84
     * @author lk
     * @param d
     * @return
     */
    public static double bsbcms_To_gps84(double d){
        String str = String.valueOf(d);
        String[] split = str.split("\\.");
        return (Double.parseDouble(split[0].substring(split[0].length() - 2)) + (Math.abs(d) % 1)) / 60 + (int) Math.round(d) / 100;
    }

    /**保留小数点后5位
     * @param num
     * @return
     */
    private static double retain5(double num){
        String result = String .format("%.5f", num);
        return Double.parseDouble(result);
    }

    /**保留小数点后6位
     * @param num
     * @return
     */
    private static double retain6(double num){
        String result = String .format("%.6f", num);
        return Double.parseDouble(result);
    }

    /**
     * 保留小数点后8位
     * @param num
     * @return
     */
    public static double retain8(double num){
        String result = String .format("%.8f", num);
        return Double.parseDouble(result);
    }

    public static void main(String[] args) {
//        double[] gps84_To_bd09 = gps84_To_bd09(29.5392195,121.2903893);
//        System.out.println("gps84_To_bd09:   "+gps84_To_bd09[1]+","+gps84_To_bd09[0]+
//                "      "+gps84_To_bd09[0]+","+gps84_To_bd09[1]);
//
        double[] bd09_To_gps84 = bd09_To_gps84(29.90329726, 121.49826977);
        System.out.println("bd09_To_gps84:   "+bd09_To_gps84[1]+","+bd09_To_gps84[0]+
                "      "+bd09_To_gps84[0]+","+bd09_To_gps84[1]);
//            double b = 121.4873072;
//            double c = 29.9000335;
//            double a = 121.4850917;
//            double d = 29.90016467;
//        System.out.println(b + "---------->" + gps84_To_bsbcms(b));
//        System.out.println(c + "---------->" + gps84_To_bsbcms(c));
//        System.out.println(a + "---------->" + gps84_To_bsbcms(a));
//        System.out.println(d + "---------->" + gps84_To_bsbcms(d));

//        double e = 12129.23843; double f = 2954.00201; double g = 12129.10550; double h = 2954.00988;
//        double l = 12129.03893; double m = 2953.92195;
//        System.out.println(e + "---------->" + bsbcms_To_gps84(e));
//        System.out.println(f + "---------->" + bsbcms_To_gps84(f));
//        System.out.println(g + "---------->" + bsbcms_To_gps84(g));
//        System.out.println(h + "---------->" + bsbcms_To_gps84(h));
//        System.out.println(l + "---------->" + bsbcms_To_gps84(l));
//        System.out.println(m + "---------->" + bsbcms_To_gps84(m));
    }
}