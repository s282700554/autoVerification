package com.shine.operation.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.XmlUtils;

public class PackageLogXmlOper {
    
    // 日志
    private static Logger logger = LoggerFactory.getLogger(PackageLogXmlOper.class);
    
    // 文件名
    private static String filePath = "config/xml/PackageLog.xml";
    
    // 时间格式
    private static DateFormat format = new SimpleDateFormat("yyyyMMdd");
    
    /**
     * 
     * 添加日志节点.
     * 
     * @param userName
     * @param version
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
     * </pre>
     */
    public static void addXmlLog(String userName, String version) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("log");
            element.addAttribute("date", format.format(new Date()));
            // 消息
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "PACK_NAME");
            msgElement.addText(userName);
            element.add(msgElement);
            // 表情
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "PACK_VER");
            faceElement.addText(version);
            element.add(faceElement);
            // 将新节点加入xml
            document.getRootElement().add(element);
            XmlUtils.saveXMLFile(document, filePath);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 
     * 根据命令查找节点.
     * 
     * @param commd
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-6 SGJ 新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getLogInfo(String date) throws Exception {
        List<Map<String, String>> listMaps = new ArrayList<Map<String,String>>();
        Document document = XmlUtils.getDocument(filePath);
        List<Element> elements = selectSingleNode(document, date);
        if (elements != null && elements.size() > 0) {
            for (int i = 0; i < elements.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                List<Element> childElements = elements.get(i).elements();
                for (Element element : childElements) {
                    map.put(element.attributeValue("name"), element.getText());
                }
                listMaps.add(map);
            }
        }
        return listMaps;
    }
    
    /**
     * 
     * 根据时间查询节点.
     * 
     * @param document
     * @param date
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static List<Element> selectSingleNode(Document document, String date) throws Exception {
        return document.selectNodes("autoPack/log[@date = '" + date + "']");
    }
    
    /**
     * 
     * 取得该日期的验包人员日志.
     * 
     * @param date
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
     * </pre>
     */
    public static String getLogByDate(String date) throws Exception {
        StringBuilder sqlSB = new StringBuilder();
        sqlSB.append(date + "验包人员如下: \n");
        List<Map<String, String>> list = getLogInfo(date);
        for (Map<String, String> map : list) {
            sqlSB.append(map.get("PACK_NAME") + ":" + map.get("PACK_VER") + "\n");
        }
        return sqlSB.toString();
    }
}
