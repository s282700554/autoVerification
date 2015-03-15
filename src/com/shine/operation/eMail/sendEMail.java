package com.shine.operation.eMail;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.MailUtils;

public class sendEMail {

    // 日志.
    private static Logger logger = LoggerFactory.getLogger(sendEMail.class);

    /**
     * 
     * 发送邮件.
     * 
     * @param eMail
     * @param configInfo
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    public static void sendLogFile(String eMail, Map<String, String> configInfo) throws Exception {
        String operation = configInfo.get("SEND_EMAIL");
        // 取得发送邮箱配置信息
        Map<String, String> sendEMail = BasicsConfigXmlOper.getBasicConfigInfo("eMail");
        MailUtils sendmail = new MailUtils();
        sendmail.setHost(sendEMail.get("HOST"));
        sendmail.setUserName(sendEMail.get("USER_NAME"));
        sendmail.setPassWord(sendEMail.get("PASS_WORD"));
        sendmail.setTo(eMail.trim());
        sendmail.setFrom(sendEMail.get("USER_NAME"));
        sendmail.setSubject(sendEMail.get("SUBJECT"));
        sendmail.setContent(sendEMail.get("CONTENT"));
        File file = null;
        // sendEMail:1,2,3
        String[] sendFiles = operation.split(":");
        String[] logFiles = {};
        if (sendFiles.length > 1) {
            logFiles = sendFiles[1].split(",");
        } else {
            // 如果没有多个文件，则取默认的
            String filePath = configInfo.get("LOG_FILE");
            file = new File(filePath);
            if (file.isFile()) {
                sendmail.attachfile(filePath);
                logger.warn("发送日志文件:" + filePath);
            }
        }
        // 多个log文件的情况
        for (String logFile : logFiles) {
            // 根据后缀得到key,取得log文件路径.
            String filePath = configInfo.get("LOG_FILE" + logFile);
            file = new File(filePath);
            if (file.isFile()) {
                sendmail.attachfile(filePath);
                logger.warn("发送日志文件:" + filePath);
            }
        }
        sendmail.sendMail();
    }

}
