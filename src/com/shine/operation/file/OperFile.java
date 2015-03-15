package com.shine.operation.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.FileUtils;

public class OperFile {

    private static Logger logger = LoggerFactory.getLogger(OperFile.class);

    /**
     * 
     * �滻�ַ���.
     * 
     * @param filePath �ļ�·��.
     * @throws IOException
     * @throws InterruptedException
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static void replaceKeyWord(String operation, Map<String, String> configMap) throws Exception {
        // ȡ�ú�׺
        String endCode = operation.substring(3);
        // ȡ���ļ�·��
        String filePath = configMap.get("REPLACE_FILE_PATH" + endCode);
        // ȡ���ļ�����
        String fileType = configMap.get("REPLACE_FILE_TYPE" + endCode);
        // ȡ�ò�ѯ�ؼ���
        String keyWordFrom = configMap.get("REPLACE_KEYWORD_FROM" + endCode);
        // ȡ���滻�ؼ���
        String keyWordTo = configMap.get("REPLACE_KEYWORD_TO" + endCode);
        // ���
        List<File> resultList = new ArrayList<File>();
        // �滻
        FileUtils.replaceFileKeyWord(filePath, fileType, keyWordFrom, keyWordTo, resultList);
        if (resultList.size() == 0) {
            logger.warn("No File Fount.");
        }
        logger.warn("����" + resultList.size() + "���ļ����޸�");
    }

    /**
     * 
     * ȥ����ʷ����.
     * 
     * @param filePath
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static void removeCode(String operation, Map<String, String> configMap) throws Exception {
        // ȡ�ú�׺
        String endCode = operation.substring(3);
        String filePath = configMap.get("DELETE_FILEPATH" + endCode);
        String deleteKeyWord = configMap.get("DELETE_KEYWORD" + endCode);
        StringBuilder findStr = new StringBuilder();
        findStr.setLength(0);
        findStr.append(deleteKeyWord);
        File file = new File(filePath);
        String cont = FileUtils.read(file, null);
        Long fileDate = file.lastModified();
        cont = cont.replaceAll(findStr.toString(), "");
        FileUtils.write(cont, file);
        file.setLastModified(fileDate);
        logger.warn("ɾ��ָ���ļ��е�" + deleteKeyWord + "���!");
    }
}
