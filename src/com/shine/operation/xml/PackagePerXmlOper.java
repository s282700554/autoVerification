package com.shine.operation.xml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shine.utils.XmlUtils;

public class PackagePerXmlOper {

    private static String filePath = "config/xml/PackagePer.xml";

    /**
     * 
     * ���½ڵ�.
     * 
     * @param elements
     * @param flag
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    public static void updateXMLNode(List<Element> elements, String flag) throws Exception {
        for (Element element : elements) {
            element.attribute("flag").setValue(flag);
        }
    }

    /**
     * 
     * ���µ���һ�������Ա.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    public static void updateNext() throws Exception {
        // ��ȡXML�ļ�
        Document document = XmlUtils.getDocument(filePath);
        // ��ѯһ���м��������Ա
        int number = document.getRootElement().elements().size();
        // ��ѯ���������Ա�������
        String order = PackagePerXmlOper.selectVerifyOrder(document);
        // ������һ�������Ա
        Integer orderInteger = Integer.valueOf(order) + 1;
        if (orderInteger > number) {
            orderInteger = 1;
        }
        // ��ѯ��ǰ�����Ա�ڵ�,����ʶ����Ϊ1(�����)
        List<Element> elements = PackagePerXmlOper.selectElementsByFlag(document, "0");
        updateXMLNode(elements, "1");
        // ��������Ų�ѯ��һ�������Ա,����ʶ����Ϊ0(���)
        List<Element> nextElements = PackagePerXmlOper.selectElementsByOrder(document, String.valueOf(orderInteger));
        updateXMLNode(nextElements, "0");
        // �������ļ�
        XmlUtils.saveXMLFile(document, filePath);
        // ��ѯ���º����������Ա,��д�������ļ�����ѯ.
        String per = PackagePerXmlOper.selectVerifyPersonnel();
        TalkConfigXmlOper.updateXMLNode("@����˭���?", per + "����,������������!", "74");
    }

    /**
     * 
     * ��������������ڵ�.
     * 
     * @param document
     * @param order
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByOrder(Document document, String order) throws Exception {
        return document.selectNodes("Verify/personnel[col = '" + order + "']");
    }

    /**
     * 
     * ���ݱ�ʾ��������ڵ�.
     * 
     * @param document
     * @param flag
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByFlag(Document document, String flag) throws Exception {
        return document.selectNodes("Verify/personnel[@flag = '" + flag + "']");
    }

    /**
     * 
     * �������Ա.
     * 
     * @param document
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String selectVerifyPersonnel() throws Exception {
        Document document = XmlUtils.getDocument(filePath);
        List<Element> elements = document.selectNodes("Verify/personnel[@flag = '0']/col[@name = 'PER_NAME']");
        return elements.get(0).getText();
    }

    /**
     * 
     * ��ѯ�����Ա����.
     * 
     * @param document
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String selectVerifyOrder(Document document) throws Exception {
        List<Element> elements = document
                .selectNodes("Verify/personnel[@flag = '0']/col[@name = 'PER_ORDER']");
        return elements.get(0).getText();
    }
}
