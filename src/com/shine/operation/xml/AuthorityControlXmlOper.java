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
     * ��ѯ�û�Ȩ�����Map
     * 
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getControlInfo(String user) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * ����qq�Ų�ѯȨ��.
     * 
     * @param document
     * @param user
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByUser(Document document, String user) throws Exception {
        return document.selectNodes("controls/control[@user = '" + user + "']");
    }

    /**
     * 
     * ����������ѯȨ��.
     * 
     * @param document
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
    public static List<Element> selectElementsByUserName(Document document, String userName) throws Exception {
        return document.selectNodes("controls/control[col = '" + userName + "']");
    }

    /**
     * 
     * ����������ѯȨ��.
     * 
     * @param userName
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String getControlByUserName(String userName) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
        List<Element> elements = selectElementsByUserName(document, userName);
        if (elements != null && elements.size() > 0) {
            List<Element> childElements = elements.get(0).elements();
            for (Element element : childElements) {
                map.put(element.attributeValue("name"), element.getText());
            }
        }
        return map.get("USER_NAME") + ",Ȩ��" + map.get("USER_LEVEL") + "��";
    }

    /**
     * 
     * ��ѯ�û�Ȩ����ʾ����ѯ��.
     * 
     * @param user
     * @return String
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    public static String getControlInfoByQQ(String user) throws Exception {
        Map<String, String> map = getControlInfo(user);
        return map.get("USER_NAME") + ",Ȩ��" + map.get("USER_LEVEL") + "��";
    }

    /**
     * 
     * �����û�Ȩ�޵ȼ�.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateXMLNode(String user, String level) throws Exception {
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * �����û�Ȩ�޵ȼ�.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-7 SGJ �½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateXMLNodeByUserName(String userName, String level) throws Exception {
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * ������û��ڵ�.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static void addXMLNode(String qq, String name, String level) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("control");
            element.addAttribute("user", qq);
            // ��Ϣ
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "USER_NAME");
            msgElement.addText(name);
            element.add(msgElement);
            // ����
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "USER_LEVEL");
            faceElement.addText(level);
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
     * 
     * ɾ���û�
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
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
     * ɾ���û�
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-7 SGJ �½�
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
     * ���û�����.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-13	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void bindingUserMail(String userQq, String mail) throws Exception {
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
        List<Element> elements = selectElementsByUser(document, userQq);
        // �ж��Ƿ���ڸýڵ�
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

    // -----------------------------����Ϊ����ģ��Ȩ�޹���----------------------------------------------------------//

    /**
     * �����ģ��Ȩ�޽ڵ�.
     * 
     * @param commd
     * @param returnMsg
     * @param returnFace
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static void addModeXMLNode(String mode, String level) throws Exception {
        try {
            Document document = XmlUtils.getDocument(filePath);
            Element element = DocumentHelper.createElement("control");
            element.addAttribute("mode", mode);
            // ��Ϣ
            Element msgElement = DocumentHelper.createElement("col");
            msgElement.addAttribute("name", "MODE_NAME");
            msgElement.addText(mode);
            element.add(msgElement);
            // ����
            Element faceElement = DocumentHelper.createElement("col");
            faceElement.addAttribute("name", "MODE_LEVEL");
            faceElement.addText(level);
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
     * 
     * ɾ��Ȩ��ģ��
     * 
     * @param qq
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-7 SGJ �½�
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
     * ����ģ��Ȩ�޵ȼ�.
     * 
     * @param user
     * @param level
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-7 SGJ �½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void updateModeXMLNode(String mode, String level) throws Exception {
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * ��ѯģ��Ȩ�����Map
     * 
     * @param mode
     * @return
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-7	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getModeInfo(String mode) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯ�û�Ȩ��
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
     * ���ݹ�������ѯȨ�޵ȼ�.
     * 
     * @param mode
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    public static String getModeInfoModeName(String mode) throws Exception {
        Map<String, String> map = getModeInfo(mode);
        return map.get("MODE_NAME") + "����,Ȩ��" + map.get("MODE_LEVEL") + "��";
    }

    /**
     * 
     * ����ģ���ѯ�ڵ�.
     * 
     * @param document
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
    public static List<Element> selectElementsByMode(Document document, String mode) throws Exception {
        return document.selectNodes("controls/control[@mode = '" + mode + "']");
    }

    /**
     * 
     * ����qq��ȡ����
     * 
     * @param qq
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-13	SGJ	�½�
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
