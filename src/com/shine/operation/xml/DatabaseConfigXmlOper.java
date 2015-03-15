package com.shine.operation.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatabaseConfigXmlOper {

    private static Logger logger = LoggerFactory.getLogger(DatabaseConfigXmlOper.class);

    private static String filePath = "config/xml/DatabaseConfig.xml";

    /**
     * 取得根节点.
     * 
     * @param fileName
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-21	SGJ	新建
     * </pre>
     */
    private static Element getRootElement() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element root = null;
        try {
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(filePath);
            root = xmldoc.getDocumentElement();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return root;
    }

    /**
     * 
     * 查找
     * 
     * @param express
     * @param source
     * @return
     * 
     *         <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-29    SGJ 新建
     * </pre>
     */
    private static Node selectSingleNode(String express, Object source) {
        Node result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取所有验包配置数据.
     * 
     * @param fileName
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-21	SGJ	新建
     * </pre>
     */
    public static List<Map<String, String>> parserXml() throws Exception {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Element root = getRootElement();
        NodeList nodeList = root.getElementsByTagName("version");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            Element element = (Element) nodeList.item(i);
            map.put("version", element.getAttribute("value"));
            NodeList childNodeList = element.getElementsByTagName("col");
            for (int j = 0; j < childNodeList.getLength(); j++) {
                Element childElement = (Element) childNodeList.item(j);
                map.put(childElement.getAttribute("name"), childElement.getTextContent());
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 根据版本取该版本配置.
     * 
     * @param fileName
     * @param commd
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-29    SGJ 新建
     * </pre>
     */
    public static Map<String, String> getVersionInfo(String version) throws Exception {
        try {
            Element root = getRootElement();
            String express = "/autoPack/version[@value = '" + version + "']";
            Element element = (Element) selectSingleNode(express, root);
            NodeList list = element.getElementsByTagName("col");
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < list.getLength(); i++) {
                Element colElement = (Element) list.item(i);
                map.put(colElement.getAttribute("name"), colElement.getTextContent());
            }
            return map;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
