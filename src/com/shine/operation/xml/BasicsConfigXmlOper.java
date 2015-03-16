package com.shine.operation.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.XmlUtils;

public class BasicsConfigXmlOper {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(BasicsConfigXmlOper.class);

    private static String filePath = "data/BasicsConfig.dat";

    /**
     * 
     * 查询用户权限组成Map
     * 
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-7 SGJ 新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getBasicConfigInfo(String type) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByType(document, type);
        if (elements != null && elements.size() > 0) {
            List<Element> childElements = elements.get(0).elements();
            for (Element element : childElements) {
                map.put(element.attributeValue("name"), element.getText());
            }
        }
        return map;
    }

    /**
     * 
     * 根据配置类型查询配置信息.
     * 
     * @param document
     * @param type
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByType(Document document, String type) throws Exception {
        return document.selectNodes("config/configType[@value = '" + type + "']");
    }
}
