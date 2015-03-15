package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackagePerXmlOper;
import com.shine.qq.client.QqMessag;

public class NextPerWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(NextPerWork.class);

    /**
     * 
     * ִ�����������Ա����.
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
        logger.warn("�������Ϊ��һ����!");
        PackagePerXmlOper.updateNext();
        String personnel = PackagePerXmlOper.selectVerifyPersonnel();
        msgClient.send(msg, personnel + "�������,��������ֲ���Ŷ!", 74);
    }
}
