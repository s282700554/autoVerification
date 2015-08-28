package com.shine.robot;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.utils.StringUtils;

public class AnalyzeFactory {

    // 日志
    private static Logger logger = LoggerFactory.getLogger(ConnectionTuling.class);

    /**
     * 
     * 数据处理工厂类.
     * 
     * @param type
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-8-21	SGJ	新建
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
            logger.error("数据类型为空!");
            throw new Exception("数据类型为空!");
        }
    }
}
