package com.shine.operation.bat;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.BatUtils;
import com.shine.utils.StringUtils;

public class InvokeBat {
    
    //日志.
    private static Logger logger = LoggerFactory.getLogger(InvokeBat.class);

    /**
     * 
     * 执行bat文件.
     * 
     * @param batName
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    public void execution(Map<String, String> configMap, String batName) throws Exception {
        if (!StringUtils.isNotBlank(batName)) {
            logger.warn("传入bat文件名为空!");
            throw new NullPointerException("传入bat文件名为空");
        }
        if (!batName.endsWith(".bat")) {
            batName = batName.trim() + ".bat";
        }
        String batPath = null;
        // 取得bat文件存放路径
        if (StringUtils.isNotBlank(configMap.get("CREATE_BAT_PATH"))) {
            batPath = configMap.get("CREATE_BAT_PATH");
        } else {
            Map<String, String> batConfig = BasicsConfigXmlOper.getBasicConfigInfo("bat");
            batPath = batConfig.get("CREATE_BAT_PATH");
        }
        try {
            batName = batPath + "\\" + batName;
            logger.warn("执行bat文件:" + batName);
            BatUtils.executionbat(batName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
