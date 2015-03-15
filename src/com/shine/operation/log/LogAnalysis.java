package com.shine.operation.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.FileUtils;
import com.shine.utils.StringUtils;

public class LogAnalysis {

    // 日志
    private static Logger logger = LoggerFactory.getLogger(LogAnalysis.class);

    /**
     * 
     * 日志关键字分析.
     * 
     * @param operation
     * @param configMap
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-7	SGJ	新建
     * </pre>
     */
    public static boolean analysisLog(String operation, Map<String, String> configMap) throws Exception {
        String endCode = operation.substring(3);
        // 要分析的日志文件的路径
        String logFilePath = configMap.get("LOG_FILE" + endCode);
        // 要分析的关键字
        String keyWord = configMap.get("LOG_FIND_KEYWORD" + endCode);
        // 要统计个数的关键字
        String keyWordNum = configMap.get("LOG_COUNT_KEYWORD" + endCode);
        // 要统计个数的关键字达到多少个
        Integer numKey = StringUtils.isNotBlank(configMap.get("LOG_COUNT_KEYWORD_NUM" + endCode)) ? Integer
                .valueOf(configMap.get("LOG_COUNT_KEYWORD_NUM" + endCode)) : 1;
        // 关键字数组
        String[] nullArray = {};
        String[] keyWords = StringUtils.isNotBlank(keyWord) ? keyWord.split("\\{\\$\\}") : nullArray;
        // 统计个数关键字数组
        String[] numKeyWords = StringUtils.isNotBlank(keyWordNum) ? keyWordNum.split("\\{\\$\\}") : nullArray;
        boolean bool = true;
        // 查找关键字
        for (String tempKeyWord : keyWords) {
            bool = FileUtils.searchFileKeyWord(logFilePath, tempKeyWord);
            if (bool) {
                logger.warn("在文件中:" + logFilePath + "中找到关键字{" + tempKeyWord + "}");
                break;
            }
        }
        // 统计个数
        if (!bool) {
            for (String tempNumKey : numKeyWords) {
                int num = FileUtils.searchFileKeyWordNum(logFilePath, tempNumKey);
                if (num > numKey) {
                    logger.warn("在文件中查询到字符串[" + tempNumKey + "]" + num + "个,大于" + numKey + "个.");
                    bool = true;
                    break;
                }
            }
        }
        return bool;
    }
}
