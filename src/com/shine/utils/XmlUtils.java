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
     * 读取文件.
     * 
     * @param filename
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
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
     * 保存文件.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
     * </pre>
     */
    public static void saveXMLFile(Document document, String filePath) {
        saveEncrypFile(document, filePath);
    }

    /**
     * 读取未加密文件.
     * 
     * @param filename
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
     * </pre>
     */
    public static Document getUnencryptedDocument(String fileName) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(fileName));
        return document;
    }

    /**
     * 
     * 保存未加密文件.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
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
     * 读取解密后的Document.
     * 
     * @param fileName
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2015-3-13	SGJ	新建
     * </pre>
     */
    public static Document getDecrypDocument(String fileName) throws Exception {
        // 解密文件数据生成byte[]
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            byte[] bytes = EncrypAndDecrypUtils.decrypDataToByte(fileName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            document = saxReader.read(inputStream);
        } catch (Exception e) {
            // 出现异常文件本身未被加密,不处理
            document = saxReader.read(new File(fileName));
        }
        return document;
    }

    /**
     * 
     * 保存加密后的文件.
     * 
     * @param document
     * @param filePath
     * 
     *            <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-2 SGJ 新建
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
