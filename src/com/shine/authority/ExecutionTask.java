package com.shine.authority;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class ExecutionTask {

    private static Logger logger = LoggerFactory.getLogger(ExecutionTask.class);

    /**
     * ��֤����Ȩ��.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-28	SGJ	�½�
     * </pre>
     */
    public static void verifyAuthority(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        logger.warn("��������:" + userMsg);
        Map<String, String> map = new HashMap<String, String>();
        boolean isVerify = true;
        try {
            map = AuthorityControlXmlOper.getModeInfo(userMsg.substring(1, 3));
            isVerify = map != null && map.size() > 0 ? true : false;
        } catch (Exception e) {
            isVerify = false;
        }
        if (isVerify) {
            // ��������Ȩ�޿���,��ȡ�û���Ϣ
            msgClient.getQqUser(msg, new AuthorityVerify(msgClient, msg, map.get("MODE_LEVEL")));
        } else {
            workFactory.createWork(msgClient, msg);
        }
    }
}
