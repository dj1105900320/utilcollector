package utils.collector.vehiclestatuscheck;

import lombok.Data;

/**
 * 为了不让EmailUtil报错
 *
 * @author deng jie
 * @date 2021/7/19 8:05
 * @description 发送邮件统一数据类型
 */
@Data
public class EmailData {

    private Integer vehicleId;

    private Integer depId;

    private String lineName;

    private String ownPlateNum;

    private String plateNum;
    /**
     * 日期范围
     */
    private String week;

    private String problemRecord;
}
