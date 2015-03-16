package com.shine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatUtils {

    private static Logger logger = LoggerFactory.getLogger(BatUtils.class);

    /**
     * ȡ�ö�ȡ��Ϣ�߳�.
     * 
     * @param messageBr
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-20	SGJ	�½�
     * </pre>
     */
    private static Thread getMessage(final InputStream is) throws Exception {
        Thread thread = new Thread() {
            public void run() {
                try {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    logger.warn("info: " + sb.toString());
                    br.close();
                    isr.close();
                    is.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                    e.printStackTrace();
                }
                // String line = null;
                // try {
                // while ((line = messageBr.readLine()) != null) {
                // logger.warn(line);
                // }
                // } catch (IOException e) {
                // logger.error(e.getMessage());
                // }
            }
        };
        return thread;
    }

    /**
     * ִ�� bat�ļ�.
     * 
     * @param batName
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-20	SGJ	�½�
     * </pre>
     */
    public static void executionbat(String batPath, String batName) throws Exception {
        try {
            batPath = batPath.replaceAll(" ", "\" \"");
            Runtime runtime = Runtime.getRuntime();
            String executionStr = "cmd /c \"" + batPath + "\\" + batName + "\"";
            logger.warn("ִ��" + batName + "���:" + executionStr);
            Process ps = runtime.exec(executionStr);
            ps.getOutputStream().close();
            Thread t1 = getMessage(ps.getInputStream());
            Thread t2 = getMessage(ps.getErrorStream());
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

//    /**
//     * 
//     * ������.
//     * 
//     * @param args
//     * @throws Exception
//     * 
//     *             <pre>
//     * �޸�����		�޸���	�޸�ԭ��
//     * 2014-11-6	SGJ	�½�
//     * </pre>
//     */
//    public static void main(String[] args) throws Exception {
//        try {
//            String batName = "cmd /c start /D \"D:\\Users\\SGJ\\Desktop\\\" test.bat";
//            BatUtils.executionbat(batName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
