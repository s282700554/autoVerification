package com.shine.operation.bat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.FileUtils;
import com.shine.utils.StringUtils;

public class GenerationBat {
    /**
     * 生成路径生成bat
     * 
     * @param value
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-24    SGJ 新建
     * </pre>
     */
    public static void GenerateBat(String value) throws Exception {
        List<String> replaceStr = new ArrayList<String>();
        List<String> valueStr = new ArrayList<String>();
        // 生成文件删除bat
        replaceStr.add("(?i)\\$\\{path}");
        valueStr.add(value);
        File file = new File("config/txt");
        String path = file.getAbsolutePath();
        path = path.replace("\\", "\\\\");
        replaceStr.add("(?i)\\$\\{txtPath}");
        valueStr.add(path);
        FileUtils.replaceKeyAndCreateFile("config/txt/del.txt", "CompleteBat/del.bat", replaceStr, valueStr);
        replaceStr.clear();
        valueStr.clear();
        // 生成文件复制bat
        replaceStr.add("(?i)\\$\\{path}");
        valueStr.add(value);
        file = new File("bat");
        path = file.getAbsolutePath();
        path = path.replace("\\", "\\\\");
        replaceStr.add("(?i)\\$\\{srcPath}");
        valueStr.add(path);
        FileUtils.replaceKeyAndCreateFile("config/txt/copy.txt", "CompleteBat/copy.bat", replaceStr, valueStr);
        replaceStr.clear();
        valueStr.clear();
        // 生成调用bat
        replaceStr.add("(?i)\\$\\{path}");
        valueStr.add(value);
        replaceStr.add("(?i)\\$\\{letter}");
        valueStr.add(value.substring(0, 2));
        FileUtils.replaceKeyAndCreateFile("config/txt/dy.txt", "CompleteBat/dy.bat", replaceStr, valueStr);
    }

    /**
     * 生成删除bat
     * 
     * @param value
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-24    SGJ 新建
     * </pre>
     */
    public static void GenerateDelBat(String value) throws Exception {
        List<String> replaceStr = new ArrayList<String>();
        List<String> valueStr = new ArrayList<String>();
        // 生成文件删除bat
        replaceStr.add("(?i)\\$\\{path}");
        valueStr.add(value);
        FileUtils.replaceKeyAndCreateFile("config/txt/delAll.txt", "CompleteBat/delAll.bat", replaceStr, valueStr);
    }

    /**
     * 生成路径生成bat
     * 
     * @param value
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-24    SGJ 新建
     * </pre>
     */
    public static void GenerateDataBat(Map<String, String> map) throws Exception {
        List<String> replaceStr = new ArrayList<String>();
        List<String> valueStr = new ArrayList<String>();
        // 虚拟机还原
        replaceStr.add("(?i)\\$\\{VM_PATH}");
        valueStr.add(map.get("VM_PATH"));
        replaceStr.add("(?i)\\$\\{letter}");
        valueStr.add(map.get("VM_PATH").substring(0, 2));
        replaceStr.add("(?i)\\$\\{SNAP_PATH}");
        valueStr.add(map.get("SNAP_PATH"));
        replaceStr.add("(?i)\\$\\{SNAP_NAME}");
        valueStr.add(map.get("SNAP_NAME"));
        FileUtils.replaceKeyAndCreateFile("config/reSna.txt", "CompleteBat/reSna.bat", replaceStr, valueStr);
    }

    /**
     * 根据配置信息生成bat.
     * 
     * @param map
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-8-7 SGJ 新建
     * </pre>
     */
    public static void generateBat(Map<String, String> map) throws Exception {
        Map<String, String> replaceMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            replaceMap.put("(?i)\\$\\{" + entry.getKey() + "}", entry.getValue());
        }
        // 添加文件所在的路径
        File file = new File("config/txt/");
        String txtPath = file.getAbsolutePath();
        txtPath = txtPath.replace("\\", "\\\\");
        replaceMap.put("(?i)\\$\\{txtPath}", txtPath);
        // 添加bat文件存放路径
        file = new File("bat");
        String batPath = file.getAbsolutePath();
        batPath = batPath.replace("\\", "\\\\");
        replaceMap.put("(?i)\\$\\{srcPath}", batPath);
        for (String fileName : map.get("files").split(",")) {
            if (!fileName.startsWith("svn") && !fileName.startsWith("sleep")) {
                FileUtils.replaceKeyAndCreateFile("config/txt/" + fileName + ".txt", "CompleteBat/" + fileName + ".bat", replaceMap);
            }
        }
    }
    
    /**
     * 
     * 根据配置生成bat文件.
     * 
     * @param configMap
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    public static void createBat(Map<String, String> configMap) throws Exception {
        String createBatPath = null;
        String textPath = null;
        String batPath = null;
        // 取得bat文件基础配置信息
        Map<String, String> batConfig = BasicsConfigXmlOper.getBasicConfigInfo("bat");
        // 生成bat文件存放路径
        if (StringUtils.isNotBlank(configMap.get("CREATE_BAT_PATH"))) {
            createBatPath = configMap.get("CREATE_BAT_PATH");
        } else {
            createBatPath = batConfig.get("CREATE_BAT_PATH");
        }
        // text文件存放路径
        if (StringUtils.isNotBlank(configMap.get("TEXT_PATH"))) {
            textPath = configMap.get("TEXT_PATH");
        } else {
            textPath = batConfig.get("TEXT_PATH");
        }
        // 原有的要执行的bat文件存放路径
        if (StringUtils.isNotBlank(configMap.get("BAT_PATH"))) {
            batPath = configMap.get("BAT_PATH");
        } else {
            batPath = batConfig.get("BAT_PATH");
        }
        // 任务配置信息
        Map<String, String> replaceMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            replaceMap.put("(?i)\\$\\{" + entry.getKey() + "}", entry.getValue());
        }
        for (Map.Entry<String, String> entry : batConfig.entrySet()) {
            replaceMap.put("(?i)\\$\\{" + entry.getKey() + "}", entry.getValue());
        }
        // 生成bat文件存放路径
        replaceMap.put("(?i)\\$\\{CREATE_BAT_PATH}", (new File(createBatPath)).getAbsolutePath().replace("\\", "\\\\"));
        // text文件存放路径
        replaceMap.put("(?i)\\$\\{TEXT_PATH}", (new File(textPath)).getAbsolutePath().replace("\\", "\\\\"));
        // 原有的要执行的bat文件存放路径
        replaceMap.put("(?i)\\$\\{BAT_PATH}", (new File(batPath)).getAbsolutePath().replace("\\", "\\\\"));
        // 取得执行步骤,以bat结尾的步骤生成bat文件
        for (String fileName : configMap.get("EXECUTION_STEP").split(",")) {
            if (fileName.endsWith("bat")) {
                FileUtils.replaceKeyAndCreateFile(textPath + "\\" + fileName + ".txt", createBatPath + "\\" + fileName + ".bat", replaceMap);
            }
        }
    }
}
