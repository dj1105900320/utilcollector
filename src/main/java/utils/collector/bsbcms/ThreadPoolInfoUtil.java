package utils.collector.bsbcms;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yujingwei
 * @version 创建时间:2020-03-21 09:33
 * @description
 */
public class ThreadPoolInfoUtil {
    public static ConcurrentHashMap<String, ThreadPoolTaskExecutor> threadPoolMap = new ConcurrentHashMap<>();
}
