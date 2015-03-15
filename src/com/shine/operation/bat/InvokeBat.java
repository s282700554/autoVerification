package com.shine.operation.bat;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.BatUtils;
import com.shine.utils.StringUtils;

public class InvokeBat {
    
    //��־.
    private static Logger logger = LoggerFactory.getLogger(InvokeBat.class);

    /**
     * 
     * ִ��bat�ļ�.
     * 
     * @param batName
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    public void execution(Map<String, String> configMap, String batName) throws Exception {
        if (!StringUtils.isNotBlank(batName)) {
            logger.warn("����bat�ļ���Ϊ��!");
            throw new NullPointerException("����bat�ļ���Ϊ��");
        }
        if (!batName.endsWith(".bat")) {
            batName = batName.trim() + ".bat";
        }
        String batPath = null;
        // ȡ��bat�ļ����·��
        if (StringUtils.isNotBlank(configMap.get("CREATE_BAT_PATH"))) {
            batPath = configMap.get("CREATE_BAT_PATH");
        } else {
            Map<String, String> batConfig = BasicsConfigXmlOper.getBasicConfigInfo("bat");
            batPath = batConfig.get("CREATE_BAT_PATH");
        }
        try {
            batName = batPath + "\\" + batName;
            logger.warn("ִ��bat�ļ�:" + batName);
            BatUtils.executionbat(batName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
