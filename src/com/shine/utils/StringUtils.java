package com.shine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 
     * �ж��ַ����Ƿ�Ϊ��.
     * 
     * @param str
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
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
