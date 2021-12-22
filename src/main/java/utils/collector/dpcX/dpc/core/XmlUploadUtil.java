package utils.collector.dpcX.dpc.core;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xueliman.iov.dpc.core.entity.domain.Protocol;
import com.xueliman.iov.dpc.core.service.ProtocolService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.time.LocalDateTime;

/**
 * 提交xml到数据库工具
 *
 * @author yjw
 * @date 2021/07/31
 **/
@AllArgsConstructor
public class XmlUploadUtil {
    private ProtocolService protocolService;

    public String get(String protocolName) {
        Protocol updateOne = protocolService.getOne(new LambdaQueryWrapper<Protocol>()
                .eq(Protocol::getName, protocolName));

        return StrUtil.str(updateOne.getContent(), "GBK");
    }

    public void upload(String protocolName, File file) throws FileNotFoundException, DocumentException, UnsupportedEncodingException {
        upload(protocolName, new FileInputStream(file));
    }

    public void upload(String protocolName, InputStream inputStream) throws DocumentException, UnsupportedEncodingException {
        Protocol updateOne = protocolService.getOne(new LambdaQueryWrapper<Protocol>()
                .eq(Protocol::getName, protocolName));

        if (ObjectUtils.isEmpty(updateOne)) {
            updateOne = new Protocol();
            updateOne.setCreateTime(LocalDateTime.now());
        }

        updateOne.setName(protocolName)
                .setContent(decodeFile(inputStream))
                .setUpdateTime(LocalDateTime.now());

        protocolService.saveOrUpdate(updateOne);
    }

    private byte[] decodeFile(InputStream inputStream) throws DocumentException, UnsupportedEncodingException {
        // 把XML文件解析出来,文件统一为UTF-8格式
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document document = reader.read(inputStream);
        Element canProtocol = document.getRootElement();
        String xml = canProtocol.asXML();
        // 存储到数据库用GBK存储，使用utf8时数据库中文显示仍乱码
        return xml.getBytes("GBK");
    }
}
