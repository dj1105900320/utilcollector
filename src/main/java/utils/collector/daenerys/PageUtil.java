package utils.collector.daenerys;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2021/4/29
 * @Purpose:
 */

public class PageUtil {


    /**
     * 转换输出分页信息
     * @param sourceList 原数据
     * @param targetList 手动转换完的数据
     * @param <T> 需转成的类型
     * @return
     */
    public static <T> PageInfo<T> change(List<?> sourceList, List<T> targetList){

        PageInfo<?> sourcePage = new PageInfo<>(sourceList);

        PageInfo<T> pageInfo = new PageInfo<T>(targetList);

        List<T> list = pageInfo.getList();

        BeanUtils.copyProperties(sourcePage ,pageInfo);

        pageInfo.setList(list);

        return pageInfo;
    }

}
