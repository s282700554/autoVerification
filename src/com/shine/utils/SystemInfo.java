package com.shine.utils;

/**
 * ϵͳ������Ϣ.
 * 
 *
 * <pre>
 * �޸�����		�޸���	�޸�ԭ��
 * 2015-3-13	SGJ	�½�
 * </pre>
 */
public class SystemInfo {

    /**
     * 
     * ��֤����·��.
     * 
     * @return
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-3-13	SGJ	�½�
     * </pre>
     */
    public static String getTempPath() throws Exception {
        return System.getProperty("java.io.tmpdir");
    }
}
