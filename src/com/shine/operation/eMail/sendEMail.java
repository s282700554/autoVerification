package com.shine.operation.eMail;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.MailUtils;

public class sendEMail {

    // ��־.
    private static Logger logger = LoggerFactory.getLogger(sendEMail.class);

    /**
     * 
     * �����ʼ�.
     * 
     * @param eMail
     * @param configInfo
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    public static void sendLogFile(String eMail, Map<String, String> configInfo) throws Exception {
        String operation = configInfo.get("SEND_EMAIL");
        // ȡ�÷�������������Ϣ
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
            // ���û�ж���ļ�����ȡĬ�ϵ�
            String filePath = configInfo.get("LOG_FILE");
            file = new File(filePath);
            if (file.isFile()) {
                sendmail.attachfile(filePath);
                logger.warn("������־�ļ�:" + filePath);
            }
        }
        // ���log�ļ������
        for (String logFile : logFiles) {
            // ���ݺ�׺�õ�key,ȡ��log�ļ�·��.
            String filePath = configInfo.get("LOG_FILE" + logFile);
            file = new File(filePath);
            if (file.isFile()) {
                sendmail.attachfile(filePath);
                logger.warn("������־�ļ�:" + filePath);
            }
        }
        sendmail.sendMail();
    }

}
