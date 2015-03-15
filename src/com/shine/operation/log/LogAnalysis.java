package com.shine.operation.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.FileUtils;
import com.shine.utils.StringUtils;

public class LogAnalysis {

    // ��־
    private static Logger logger = LoggerFactory.getLogger(LogAnalysis.class);

    /**
     * 
     * ��־�ؼ��ַ���.
     * 
     * @param operation
     * @param configMap
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-7	SGJ	�½�
     * </pre>
     */
    public static boolean analysisLog(String operation, Map<String, String> configMap) throws Exception {
        String endCode = operation.substring(3);
        // Ҫ��������־�ļ���·��
        String logFilePath = configMap.get("LOG_FILE" + endCode);
        // Ҫ�����Ĺؼ���
        String keyWord = configMap.get("LOG_FIND_KEYWORD" + endCode);
        // Ҫͳ�Ƹ����Ĺؼ���
        String keyWordNum = configMap.get("LOG_COUNT_KEYWORD" + endCode);
        // Ҫͳ�Ƹ����Ĺؼ��ִﵽ���ٸ�
        Integer numKey = StringUtils.isNotBlank(configMap.get("LOG_COUNT_KEYWORD_NUM" + endCode)) ? Integer
                .valueOf(configMap.get("LOG_COUNT_KEYWORD_NUM" + endCode)) : 1;
        // �ؼ�������
        String[] nullArray = {};
        String[] keyWords = StringUtils.isNotBlank(keyWord) ? keyWord.split("\\{\\$\\}") : nullArray;
        // ͳ�Ƹ����ؼ�������
        String[] numKeyWords = StringUtils.isNotBlank(keyWordNum) ? keyWordNum.split("\\{\\$\\}") : nullArray;
        boolean bool = true;
        // ���ҹؼ���
        for (String tempKeyWord : keyWords) {
            bool = FileUtils.searchFileKeyWord(logFilePath, tempKeyWord);
            if (bool) {
                logger.warn("���ļ���:" + logFilePath + "���ҵ��ؼ���{" + tempKeyWord + "}");
                break;
            }
        }
        // ͳ�Ƹ���
        if (!bool) {
            for (String tempNumKey : numKeyWords) {
                int num = FileUtils.searchFileKeyWordNum(logFilePath, tempNumKey);
                if (num > numKey) {
                    logger.warn("���ļ��в�ѯ���ַ���[" + tempNumKey + "]" + num + "��,����" + numKey + "��.");
                    bool = true;
                    break;
                }
            }
        }
        return bool;
    }
}
