package com.shine.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.shine.work.AdminWork;

public class XmlUtils {

    /**
     * ��ȡ�ļ�.
     * 
     * @param filename
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static Document getDocument(String fileName) throws Exception {
        Document document = null;
        try {
            document = getDecrypDocument(fileName);
        } catch (Exception e) {
            AdminWork.encrypData(null, null, "data");
            document = getDecrypDocument(fileName);
            if (document == null) {
                e.printStackTrace();
            }
        }
        return document;
    }

    /**
     * 
     * �����ļ�.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static void saveXMLFile(Document document, String filePath) {
        saveEncrypFile(document, filePath);
    }

    /**
     * ��ȡδ�����ļ�.
     * 
     * @param filename
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static Document getUnencryptedDocument(String fileName) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(fileName));
        return document;
    }

    /**
     * 
     * ����δ�����ļ�.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static void saveUnencryptedXMLFile(Document document, String filePath) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filePath)), format);
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * ��ȡ���ܺ��Document.
     * 
     * @param fileName
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-3-13	SGJ	�½�
     * </pre>
     */
    public static Document getDecrypDocument(String fileName) throws Exception {
        // �����ļ���������byte[]
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            byte[] bytes = EncrypAndDecrypUtils.decrypDataToByte(fileName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            document = saxReader.read(inputStream);
        } catch (Exception e) {
            // �����쳣�ļ�����δ������,������
            document = saxReader.read(new File(fileName));
        }
        return document;
    }

    /**
     * 
     * ������ܺ���ļ�.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-2 SGJ �½�
     * </pre>
     */
    public static void saveEncrypFile(Document document, String filePath) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");
            XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
            writer.write(document);
            writer.close();
            EncrypAndDecrypUtils.encryp(filePath, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
