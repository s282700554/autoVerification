package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.eMail.sendEMail;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.DatabaseConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class DatabasePackWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(DatabasePackWork.class);

    /**
     * ���ݿ��������
     */
    public static boolean YB_CONTROL = true;

    /**
     * ������־����
     */
    public static boolean LZ_CONTROL = true;

    /**
     * 
     * ִ���������.
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
        // ȡ����
        String userMsg = msg.getText().trim();
        String version = userMsg.substring(6, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "��ע�������汾", 101);
        } else {
            if (userMsg.substring(4, userMsg.length()).startsWith("���")) {
                // ���,�ȼ���Ƿ��а汾�������
                if (DatabasePackWork.YB_CONTROL) {
                    // @���ݿ����ȡ����汾
                    DatabasePackWork.YB_CONTROL = false;
                    logger.warn("���:" + version);
                    try {
                        DatabaseWorkThread datebaseWorkThread = new DatabaseWorkThread(msgClient, msg, version);
                        datebaseWorkThread.start();
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                        DatabasePackWork.YB_CONTROL = true;
                        throw e;
                    }
                } else {
                    msgClient.send(msg, "���������,���Ժ�.......!", 80);
                }
            } else if (userMsg.substring(4, userMsg.length()).startsWith("��־")) {
                if (LZ_CONTROL) {
                    Map<String, String> map = DatabaseConfigXmlOper.getVersionInfo(version);
                    if (map != null) {
                        LZ_CONTROL = false;
                        long qq = msg.getFrom().getQQ();
                        String mail = AuthorityControlXmlOper.getMail(qq);
                        msgClient.send(msg, "��,��Ҫ��[" + version + "���ݿ���־]���Ͼ��͵�[" + mail + "]Ŷ,�յ��������!", 109);
                        sendEMail.sendLogFile(mail, map);
                        LZ_CONTROL = true;
                    } else {
                        msgClient.send(msg, "��־�汾����ȷ,��������ȷ�İ汾��", 101);
                    }
                } else {
                    msgClient.send(msg, "��,��Ҫ��[" + version + "���ݿ���־]�����ͻ�·��,���Ժ�!", 109);
                }
            }
        }
    }
}
