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

    // 文件路径
    private static String filePath = "config/xml/TalkConfig.xml";

    /**
     * 添加新命令节点.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-2	SGJ	新建
     * </pre>
     */
    public static void addXMLNode(String commd, String returnMsg, String returnFace) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("command");
            element.addAttribute("value", commd);
            // 消息
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "RETURN_MSG");
            msgElement.addText(returnMsg);
            element.add(msgElement);
            // 表情
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "RETURN_FACE");
            faceElement.addText(returnFace);
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
     * 修改指令命令节点值.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
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
     * 根据命令查找节点.
     * 
     * @param commd
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
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
     * 查找指定命令节点.
     * 
     * @param document
     * @param value
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-2	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static List<Element> selectSingleNode(Document document, String value) throws Exception {
        return document.selectNodes("autoPack/command[contains(@value,'" + value + "')]");
    }
}