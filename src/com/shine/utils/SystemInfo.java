package com.shine.utils;

/**
 * 系统参数信息.
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2015-3-13	SGJ	新建
 * </pre>
 */
public class SystemInfo {

    /**
     * 
     * 验证码存放路径.
     * 
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-3-13	SGJ	新建
     * </pre>
     */
    public static String getTempPath() throws Exception {
        return System.getProperty("java.io.tmpdir");
    }
}
