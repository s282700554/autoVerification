package com.shine.robot;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.StringUtils;

public class AnalyzeFactory {

    // ��־
    private static Logger logger = LoggerFactory.getLogger(ConnectionTuling.class);

    /**
     * 
     * ���ݴ�������.
     * 
     * @param type
     * @return
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-8-21	SGJ	�½�
     * </pre>
     */
    public static SuperAnalyze create(String type) throws Exception {
        if (StringUtils.isNotBlank(type)) {
            SuperAnalyze superAnalyze = null;
            ClassLoader classLoader = AnalyzeFactory.class.getClassLoader();
            try {
                Class<?> superAnalyzeClass = classLoader.loadClass("com.shine.robot.Analyze" + type);
                Constructor<?> constructor = superAnalyzeClass.getConstructor();
                superAnalyze = (SuperAnalyze) constructor.newInstance();
            } catch (Exception e) {
                Class<?> superAnalyzeClass = classLoader.loadClass("com.shine.robot.AnalyzOther");
                Constructor<?> constructor = superAnalyzeClass.getConstructor();
                superAnalyze = (SuperAnalyze) constructor.newInstance();
            }
            return superAnalyze;
        } else {
            logger.error("��������Ϊ��!");
            throw new Exception("��������Ϊ��!");
        }
    }
}
