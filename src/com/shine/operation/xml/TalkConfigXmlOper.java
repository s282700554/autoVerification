package com.shine.operation.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.XmlUtils;

public class TalkConfigXmlOper {

    private static Logger logger = LoggerFactory.getLogger(TalkConfigXmlOper.class);

    // �ļ�·��
    private static String filePath = "config/xml/TalkConfig.xml";

    /**
     * ���������ڵ�.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-2	SGJ	�½�
     * </pre>
     */
    public static void addXMLNode(String commd, String returnMsg, String returnFace) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("command");
            element.addAttribute("value", commd);
            // ��Ϣ
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "RETURN_MSG");
            msgElement.addText(returnMsg);
            element.add(msgElement);
            // ����
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "RETURN_FACE");
            faceElement.addText(returnFace);
            element.add(faceElement);
            // ���½ڵ����xml
            document.getRootElement().add(element);
            XmlUtils.saveXMLFile(document, filePath);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * �޸�ָ������ڵ�ֵ.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateXMLNode(String commd, String returnMsg, String returnFace) throws Exception {
        Document document = XmlUtils.getDocument(filePath);
        List<Element> elements = selectSingleNode(document, commd);
        List<Element> childElements = elements.get(0).elements();
        for (Element element : childElements) {
            if (element.attributeValue("name").equals("RETURN_MSG")) {
                element.setText(returnMsg);
            } else if (element.attributeValue("name").equals("RETURN_FACE")) {
                element.setText(returnFace);
            }
        }
        XmlUtils.saveXMLFile(document, filePath);
    }

    /**
     * 
     * ����������ҽڵ�.
     * 
     * @param commd
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-6	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getComInfo(String commd) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document document = XmlUtils.getDocument(filePath);
        List<Element> elements = selectSingleNode(document, commd);
        if (elements != null && elements.size() > 0) {
            List<Element> childElements = elements.get(0).elements();
            for (Element element : childElements) {
                map.put(element.attributeValue("name"), element.getText());
            }
        }
        return map;
    }

    /**
     * ����ָ������ڵ�.
     * 
     * @param document
     * @param value
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-2	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static List<Element> selectSingleNode(Document document, String value) throws Exception {
        return document.selectNodes("autoPack/command[contains(@value,'" + value + "')]");
    }
}