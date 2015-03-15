package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.eMail.sendEMail;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.JavaConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class sendMailWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(sendMailWork.class);

    /**
     * �ʼ����Ϳ���
     */
    public static boolean SEND_MAIL = true;

    /**
     * 
     * ִ�з����ʼ�����.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-6 SGJ �½�
     * </pre>
     */
    @Override
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        // ����־
        boolean bool = false;
        // �汾
        String version = userMsg.substring(3, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "��ע����Ҫ����־�汾", 101);
        } else {
            bool = SEND_MAIL;
            SEND_MAIL = false;
            if (true == bool) {
                Map<String, String> versionInfo = JavaConfigXmlOper.getVersionInfo(version);
                if (versionInfo != null) {
                    long qq = msg.getFrom().getQQ();
                    String mail = AuthorityControlXmlOper.getMail(qq);
                    msgClient.send(msg, "��,��Ҫ��[" + version + "JAVA�����־]���Ͼ��͵�[" + mail + "]Ŷ,�յ��������!", 109);
                    logger.warn("������־QQ[" + msg.getFrom().getQQ() + "],�汾:[" + version + "]");
                    try {
                        sendEMail.sendLogFile(mail, versionInfo);
                        SEND_MAIL = true;
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                        SEND_MAIL = true;
                        throw e;
                    }
                } else {
                    msgClient.send(msg, "��־�汾����ȷ,��������ȷ�İ汾��", 101);
                    SEND_MAIL = true;
                }
            } else {
                msgClient.send(msg, "��������,��,һ��������", 79);
            }
        }
    }
}
