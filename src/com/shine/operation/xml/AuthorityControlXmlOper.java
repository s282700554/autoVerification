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

public class AuthorityControlXmlOper {

    private static Logger logger = LoggerFactory.getLogger(AuthorityControlXmlOper.class);

    private static String filePath = "config/xml/AuthorityControl.xml";

    /**
     * 
     * 查询用户权限组成Map
     * 
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getControlInfo(String user) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByUser(document, user);
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
     * 根据qq号查询权限.
     * 
     * @param document
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByUser(Document document, String user) throws Exception {
        return document.selectNodes("controls/control[@user = '" + user + "']");
    }

    /**
     * 
     * 根据姓名查询权限.
     * 
     * @param document
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
    public static List<Element> selectElementsByUserName(Document document, String userName) throws Exception {
        return document.selectNodes("controls/control[col = '" + userName + "']");
    }

    /**
     * 
     * 根据姓名查询权限.
     * 
     * @param userName
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String getControlByUserName(String userName) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByUserName(document, userName);
        if (elements != null && elements.size() > 0) {
            List<Element> childElements = elements.get(0).elements();
            for (Element element : childElements) {
                map.put(element.attributeValue("name"), element.getText());
            }
        }
        return map.get("USER_NAME") + ",权限" + map.get("USER_LEVEL") + "级";
    }

    /**
     * 
     * 查询用户权限显示给查询者.
     * 
     * @param user
     * @return String
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    public static String getControlInfoByQQ(String user) throws Exception {
        Map<String, String> map = getControlInfo(user);
        return map.get("USER_NAME") + ",权限" + map.get("USER_LEVEL") + "级";
    }

    /**
     * 
     * 更新用户权限等级.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateXMLNode(String user, String level) throws Exception {
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByUser(document, user);
        for (Element element : elements) {
            List<Element> childElements = element.elements();
            for (Element childeElement : childElements) {
                if (childeElement.attributeValue("name").equals("USER_LEVEL")) {
                    childeElement.setText(level);
                }
            }
        }
        XmlUtils.saveXMLFile(document, filePath);
    }

    /**
     * 
     * 更新用户权限等级.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-7 SGJ 新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateXMLNodeByUserName(String userName, String level) throws Exception {
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByUserName(document, userName);
        for (Element element : elements) {
            List<Element> childElements = element.elements();
            for (Element childeElement : childElements) {
                if (childeElement.attributeValue("name").equals("USER_LEVEL")) {
                    childeElement.setText(level);
                }
            }
        }
        XmlUtils.saveXMLFile(document, filePath);
    }

    /**
     * 添加新用户节点.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
     * </pre>
     */
    public static void addXMLNode(String qq, String name, String level) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("control");
            element.addAttribute("user", qq);
            // 消息
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "USER_NAME");
            msgElement.addText(name);
            element.add(msgElement);
            // 表情
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "USER_LEVEL");
            faceElement.addText(level);
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
     * 删除用户
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    public static void delXMLNode(String qq) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            List<Element> elements = selectElementsByUser(document, qq);
            if (elements != null && elements.size() > 0) {
                document.remove(elements.get(0));
                XmlUtils.saveXMLFile(document, filePath);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 
     * 删除用户
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-7 SGJ 新建
     * </pre>
     */
    public static void delXMLNodeByUserName(String userName) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            List<Element> elements = selectElementsByUserName(document, userName);
            if (elements != null && elements.size() > 0) {
                document.getRootElement().remove(elements.get(0));
                XmlUtils.saveXMLFile(document, filePath);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 
     * 绑定用户邮箱.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-13	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void bindingUserMail(String userQq, String mail) throws Exception {
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByUser(document, userQq);
        // 判断是否存在该节点
        boolean isExist = false;
        for (Element element : elements) {
            List<Element> childElements = element.elements();
            for (Element childeElement : childElements) {
                if (childeElement.attributeValue("name").equals("USER_MAIL")) {
                    childeElement.setText(mail);
                    isExist = true;
                }
            }
        }
        if (!isExist) {
            for (Element element : elements) {
                Element msgElement = DocumentHelper.createElement("col");
                msgElement.addAttribute("name", "USER_MAIL");
                msgElement.addText(mail);
                element.add(msgElement);
            }
        }
        XmlUtils.saveXMLFile(document, filePath);
    }

    // -----------------------------以下为功能模块权限管理----------------------------------------------------------//

    /**
     * 添加新模块权限节点.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
     * </pre>
     */
    public static void addModeXMLNode(String mode, String level) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("control");
            element.addAttribute("mode", mode);
            // 消息
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "MODE_NAME");
            msgElement.addText(mode);
            element.add(msgElement);
            // 表情
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "MODE_LEVEL");
            faceElement.addText(level);
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
     * 删除权限模块
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-7 SGJ 新建
     * </pre>
     */
    public static void delModeXMLNode(String mode) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            List<Element> elements = selectElementsByMode(document, mode);
            if (elements != null && elements.size() > 0) {
                document.getRootElement().remove(elements.get(0));
                XmlUtils.saveXMLFile(document, filePath);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 
     * 更新模块权限等级.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-7 SGJ 新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateModeXMLNode(String mode, String level) throws Exception {
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByMode(document, mode);
        for (Element element : elements) {
            List<Element> childElements = element.elements();
            for (Element childeElement : childElements) {
                if (childeElement.attributeValue("name").equals("MODE_LEVEL")) {
                    childeElement.setText(level);
                }
            }
        }
        XmlUtils.saveXMLFile(document, filePath);
    }

    /**
     * 
     * 查询模块权限组成Map
     * 
     * @param mode
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-7	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getModeInfo(String mode) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询用户权限
        List<Element> elements = selectElementsByMode(document, mode);
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
     * 根据功能名查询权限等级.
     * 
     * @param mode
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
     * </pre>
     */
    public static String getModeInfoModeName(String mode) throws Exception {
        Map<String, String> map = getModeInfo(mode);
        return map.get("MODE_NAME") + "功能,权限" + map.get("MODE_LEVEL") + "级";
    }

    /**
     * 
     * 根据模块查询节点.
     * 
     * @param document
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
    public static List<Element> selectElementsByMode(Document document, String mode) throws Exception {
        return document.selectNodes("controls/control[@mode = '" + mode + "']");
    }

    /**
     * 
     * 根据qq号取邮箱
     * 
     * @param qq
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-13	SGJ	新建
     * </pre>
     */
    public static String getMail(long qqNuber) throws Exception {
        String qq = String.valueOf(qqNuber);
        Map<String, String> map = getControlInfo(qq);
        String mail = map.get("USER_MAIL");
        if (mail == null || mail == "") {
            mail = qq + "@qq.com";
        }
        return mail;
    }
}
