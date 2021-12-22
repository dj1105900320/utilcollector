package utils.collector.pumpX.pump;

import static com.xueliman.base.pump.server.web.util.CoordTransform.*;

/**
 * @author ：fengliang
 * @date ：Created in 2021/12/13
 * @description：
 * @modified By：
 * @version: $
 */
public enum CoordType {

    wgs84(
            p -> p,
            p -> wgs84togcj02(p),
            p -> gcj02tobd09(wgs84togcj02(p))
    ),
    gcj02(
            p -> gcj02towgs84(p),
            p -> p,
            p -> gcj02tobd09(p)
    ),
    bd09(
            p -> gcj02towgs84(bd09togcj02(p)),
            p -> bd09togcj02(p),
            p -> p
    );

    public Converter WGS84;
    public Converter GCJ02;
    public Converter BD09;

    CoordType(Converter WGS84, Converter GCJ02, Converter BD09) {
        this.WGS84 = WGS84;
        this.GCJ02 = GCJ02;
        this.BD09 = BD09;
    }
}