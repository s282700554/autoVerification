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
     * ��ѯ�û�Ȩ�����Map
     * 
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-7 SGJ �½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getBasicConfigInfo(String type) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * �����������Ͳ�ѯ������Ϣ.
     * 
     * @param document
     * @param type
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByType(Document document, String type) throws Exception {
        return document.selectNodes("config/configType[@value = '" + type + "']");
    }
}
