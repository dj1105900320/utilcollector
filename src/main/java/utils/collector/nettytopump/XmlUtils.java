//package utils.collector.nettytopump;
//
//import com.xueliman.iov.nettytopump.domain.entity.CanData;
//import com.xueliman.iov.nettytopump.domain.entity.Protocol;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import java.io.ByteArrayInputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * xml工具
// *
// * @author deng jie
// * @date 2021/11/8 15:11
// */
//public class XmlUtils {
//
//    public static List<CanData> xmlToClass(Protocol protocol) throws UnsupportedEncodingException, DocumentException {
//        String xmlStr = new String(protocol.getContent(),"GB2312");
//        SAXReader saxReader = new SAXReader();
//        Document document = saxReader.read(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
//        Element root = document.getRootElement();
//        CanData canData = null;
//        Element element = null;
//        List<CanData> canDataList = new ArrayList<>();
//        for (Iterator i = root.elementIterator("canData"); i.hasNext(); ){
//            element = (Element) i.next();
//            canData = new CanData();
//            canData.setCanId(element.attributeValue("canId"));
//            canData.setType(element.attributeValue("type"));
//            canData.setName(element.attributeValue("name"));
//            canData.setLabel(element.attributeValue("label"));
//            canData.setByteIndex(element.attributeValue("byteIndex"));
//            canData.setByteInclude(element.attributeValue("byteInclude"));
//            canData.setBitIndex(element.attributeValue("bitIndex"));
//            canData.setBitInclude(element.attributeValue("bitInclude"));
//            canData.setOptionVal(element.attributeValue("optionVal"));
//            canData.setOptionMean(element.attributeValue("optionMean"));
//            canData.setOptionAlias(element.attributeValue("optionAlias"));
//            canData.setWarnValue(element.attributeValue("warnValue"));
//            canData.setWarnLevel(element.attributeValue("warnLevel"));
//            canData.setResolution(element.attributeValue("resolution"));
//            canData.setOffset(element.attributeValue("offset"));
//            canData.setScope(element.attributeValue("scope"));
//            canData.setInvalid(element.attributeValue("invalid"));
//            canData.setUnit(element.attributeValue("unit"));
//            canData.setGroup(element.attributeValue("group"));
//            canData.setGroup(element.attributeValue("continues"));
//            canData.setCanByteMode(element.attributeValue("canByteMode"));
//            canDataList.add(canData);
//        }
//        return canDataList;
//    }
//}
