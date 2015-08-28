package com.shine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 
     * 判断字符串是否为空.
     * 
     * @param str
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    public static boolean isNotBlank(String str) {
        if (str == null) {
            return false;
        } else if (str.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
