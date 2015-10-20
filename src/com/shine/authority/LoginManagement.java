package com.shine.authority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginManagement implements Runnable {
    
    private static Logger logger = LoggerFactory.getLogger(LoginManagement.class);
    
    private static Thread dispatchThread;
    
    @Override
    public void run() {
        while (true) {
            LoginUsers.clearTimeOut();
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        
    }

    /**
     * 
     * 初始化.
     * 
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static void init() {
        LoginUsers.clear();
        dispatchThread = new Thread(new LoginManagement());
        dispatchThread.setName("LoginManagement");
        dispatchThread.start();
    }
}
