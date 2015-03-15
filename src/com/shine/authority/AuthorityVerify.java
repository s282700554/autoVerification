package com.shine.authority;

import java.util.Map;

import iqq.im.QQActionListener;
import iqq.im.bean.QQMsg;
import iqq.im.bean.QQUser;
import iqq.im.event.QQActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class AuthorityVerify implements QQActionListener {

    private static Logger logger = LoggerFactory.getLogger(AuthorityVerify.class);

    // �û���
    private QqMessag msgClient;

    // �û���Ϣ
    private QQMsg msg;

    // ģ��Ȩ��
    private String level;

    public AuthorityVerify(QqMessag msgClient, QQMsg msg, String level) {
        this.msgClient = msgClient;
        this.msg = msg;
        this.level = level;
    }

    /**
     * �����¼�,ȡ�û�qq.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-28    SGJ �½�
     * </pre>
     */
    @Override
    public void onActionEvent(QQActionEvent event) {
        if (event.getType().equals(QQActionEvent.Type.EVT_OK)) {
            QQUser qqUser = (QQUser) event.getTarget();
            long qq = qqUser.getQQ();
            if (qq != 0L) {
                try {
                    Map<String, String> map = AuthorityControlXmlOper.getControlInfo(String.valueOf(qq));
                    String userLevel = qq == 282700554 ? "0" : map.get("USER_LEVEL");
                    boolean levelBool = userLevel != null && userLevel != "" ? Integer.valueOf(this.level) >= Integer.valueOf(userLevel) : false;
                    if (levelBool) {
                        // ��Ȩ��,ִ�в���
                        workFactory.createWork(msgClient, msg);
                    } else {
                        this.msgClient.send(this.msg, "QQ��:" + qq + "(" + map.get("USER_NAME") + ")����Ȩ����[" + msg.getText().trim().substring(1, 3) + "]����.", 108);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    try {
                        msgClient.send(msg, "����,ԭ��:" + e.getMessage(), 5);
                    } catch (Exception e1) {
                        logger.warn(e1.getMessage());
                    }
                }
            } else {
                logger.error("��ȡ�û��˺���Ϣʧ��!");
            }
        }
    }
}
