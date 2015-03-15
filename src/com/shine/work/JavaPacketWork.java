package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackageLogXmlOper;
import com.shine.qq.client.QqMessag;

public class JavaPacketWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(JavaPacketWork.class);

    /**
     * �������
     */
    public static boolean YB_CONTROL = true;

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
        // ȡ����汾
        String version = userMsg.substring(3, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "��ע����Ҫ����İ汾", 101);
        } else {
            // ���,�ȼ���Ƿ��а汾�������
            if (JavaPacketWork.YB_CONTROL) {
                JavaPacketWork.YB_CONTROL = false;
                logger.warn("���:" + version);
                try {
                    JavaWorkThread javaWorkThread = new JavaWorkThread(msgClient, msg, version);
                    javaWorkThread.start();
                    // ��¼������
                    PackageLogXmlOper.addXmlLog(msg.getFrom().getNickname(), version);
                } catch (Exception e) {
                    JavaPacketWork.YB_CONTROL = true;
                    logger.warn(e.getMessage());
                    throw e;
                }
            } else {
                msgClient.send(msg, "���������,���Ժ�.......!", 80);
            }
        }
    }
}
