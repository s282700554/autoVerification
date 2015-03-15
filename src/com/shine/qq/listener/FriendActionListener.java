package com.shine.qq.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.bean.QQBuddy;
import iqq.im.bean.QQCategory;
import iqq.im.event.QQActionEvent;

public class FriendActionListener implements QQActionListener {
    
    // ��־
    private static Logger logger = LoggerFactory.getLogger(FriendActionListener.class);
    
    
    /**
     * 
     * ��ȡ������Ϣ�����¼�.
     * 
     * @param event
     *
     * <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-26    SGJ �½�
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onActionEvent(QQActionEvent event) {
        logger.warn("******** " + event.getType() + " ********");
        if (event.getType() == QQActionEvent.Type.EVT_OK) {
            logger.warn("******** �����б�  ********");
            List<QQCategory> qqCategoryList = (List<QQCategory>) event.getTarget();
            for (QQCategory c : qqCategoryList) {
                logger.warn("��������:" + c.getName());
                List<QQBuddy> buddyList = c.getBuddyList();
                for (QQBuddy b : buddyList) {
                    logger.warn("---- QQ nick:" + b.getNickname() + " markname:" + b.getMarkname() + " uin:"
                            + b.getUin() + " isVip:" + b.isVip() + " vip_level:" + b.getVipLevel());
                }
            }
        } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
            logger.warn("** �����б��ȡʧ�ܣ��������»�ȡ");
        }
    }

}
