package com.shine.authority;

import iqq.im.QQActionListener;
import iqq.im.bean.QQMsg;
import iqq.im.bean.QQUser;
import iqq.im.event.QQActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.qq.client.QqMessag;

public class AuthorityVerify implements QQActionListener {

    private static Logger logger = LoggerFactory.getLogger(AuthorityVerify.class);

    // �û���
    private QqMessag msgClient;

    // �û���Ϣ
    private QQMsg msg;

    // ģ��Ȩ��
    private String level = "-9999";

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
                // ���û�д���½��Ϣ
                AuthorityBean authorityBean = LoginUsers.getLoginInfo(qqUser.getUin());
                try {
                    authorityBean.setQq(qq);
                    authorityBean.setModeLevel(this.level);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this.msgClient.send(msg, "����,ԭ��:" + e.getMessage(), 5);
                    return;
                }
                try {
                    if (authorityBean.hasAuthority()) {
                        // ��Ȩ��,ִ�в���
                        workFactory.createWork(msgClient, msg);
                    } else {
                        this.msgClient.send(this.msg, "QQ��:" + qq + "(" + authorityBean.getUserName() + ")����Ȩ����["
                                + msg.getText().trim().substring(1, 3) + "]����.", 108);
                    }
                } catch (Exception e) {
                    this.msgClient.send(msg, "����,ԭ��:" + e.getMessage(), 5);
                }
            } else {
                logger.error("��ȡ�û��˺���Ϣʧ��!");
            }
        }
    }
}
