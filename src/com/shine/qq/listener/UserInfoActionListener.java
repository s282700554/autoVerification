package com.shine.qq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

public class UserInfoActionListener implements QQActionListener {
    
    private static Logger logger = LoggerFactory.getLogger(UserInfoActionListener.class);

    /**
     * 
     * ��ȡ�û���Ϣ�����¼�.
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
    }

}
