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
     * 替换字符串.
     * 
     * @param filePath 文件路径.
     * @throws IOException
     * @throws InterruptedException
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-8-8 SGJ 新建
     * </pre>
     */
    public static void replaceKeyWord(String operation, Map<String, String> configMap) throws Exception {
        // 取得后缀
        String endCode = operation.substring(3);
        // 取得文件路径
        String filePath = configMap.get("REPLACE_FILE_PATH" + endCode);
        // 取得文件类型
        String fileType = configMap.get("REPLACE_FILE_TYPE" + endCode);
        // 取得查询关键字
        String keyWordFrom = configMap.get("REPLACE_KEYWORD_FROM" + endCode);
        // 取得替换关键字
        String keyWordTo = configMap.get("REPLACE_KEYWORD_TO" + endCode);
        // 结果
        List<File> resultList = new ArrayList<File>();
        // 替换
        FileUtils.replaceFileKeyWord(filePath, fileType, keyWordFrom, keyWordTo, resultList);
        if (resultList.size() == 0) {
            logger.warn("No File Fount.");
        }
        logger.warn("共有" + resultList.size() + "个文件被修改");
    }

    /**
     * 
     * 去除历史分区.
     * 
     * @param filePath
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-8-8 SGJ 新建
     * </pre>
     */
    public static void removeCode(String operation, Map<String, String> configMap) throws Exception {
        // 取得后缀
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
        logger.warn("删除指定文件中的" + deleteKeyWord + "完成!");
    }
}
