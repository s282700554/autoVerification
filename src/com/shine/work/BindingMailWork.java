package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class BindingMailWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(BindingMailWork.class);

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
        synchronized (BindingMailWork.class) {
            String userMsg = msg.getText().trim();
            logger.warn("qq��Ϣ:" + userMsg);
            // �汾
            String mail = userMsg.substring(3, userMsg.length()).trim();
            long qq = msg.getFrom().getQQ();
            logger.warn(qq + "������Ϊ:" + mail);
            try {
                AuthorityControlXmlOper.bindingUserMail(String.valueOf(qq), mail);
                msgClient.send(msg, "qq:" + qq + "�ɹ�������Ϊ:" + mail, 109);
            } catch (Exception e) {
                logger.error("qq:" + qq + "������:" + mail + "ʧ��!ԭ��:" + e.getMessage());
                msgClient.send(msg, "qq:" + qq + "������:" + mail + "ʧ��!", 5);
            }
        }
    }
}
