package com.shine.qq.listener;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoginActionListener implements QQActionListener {
    
    private static Logger logger = LoggerFactory.getLogger(LoginActionListener.class);
    
    // �ͻ���
    QQClient client;

    /**
     * 
     * ��½�����¼�.
     * 
     * @param client
     * @throws Exception
     *
     * <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-26    SGJ �½�
     * </pre>
     */
    public LoginActionListener(QQClient client) {
        this.client = client;
    }
    
    /**
     * 
     * ��½�����¼�.
     * 
     * @param event
     *
     * <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-26    SGJ �½�
     * </pre>
     */
    @Override
    public void onActionEvent(QQActionEvent event) {
        logger.warn("LOGIN_STATUS:" + event.getType() + ":" + event.getTarget());
        if (event.getType() == Type.EVT_OK) {
            // ����������ǵ�¼�ɹ���
            // ��ȡ���û���Ϣ
            client.getUserInfo(client.getAccount(), new UserInfoActionListener());
            // ��ȡ�����б�..
            // ��һ�����ã����ܻ��б��ػ���
            client.getBuddyList(new FriendActionListener());
            // ��ȡȺ�б�
            client.getGroupList(new GroupActionListener(client));
            // ��ȡ�������б�
            client.getDiscuzList(new DiscuzActionListener(client));
            // ������ѯʱ����Ҫ��ȡ���к��ѡ�Ⱥ��Ա���������Ա
            // ���е��߼����˺�������Ϣ��ѯ
            client.beginPollMsg();
        }
    }
}
