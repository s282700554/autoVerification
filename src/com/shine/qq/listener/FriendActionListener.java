package com.shine.qq.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.bean.QQBuddy;
import iqq.im.bean.QQCategory;
import iqq.im.event.QQActionEvent;

public class FriendActionListener implements QQActionListener {
    
    // 日志
    private static Logger logger = LoggerFactory.getLogger(FriendActionListener.class);
    
    
    /**
     * 
     * 获取好友信息监听事件.
     * 
     * @param event
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-26    SGJ 新建
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onActionEvent(QQActionEvent event) {
        logger.warn("******** " + event.getType() + " ********");
        if (event.getType() == QQActionEvent.Type.EVT_OK) {
            logger.warn("******** 好友列表  ********");
            List<QQCategory> qqCategoryList = (List<QQCategory>) event.getTarget();
            for (QQCategory c : qqCategoryList) {
                logger.warn("分组名称:" + c.getName());
                List<QQBuddy> buddyList = c.getBuddyList();
                for (QQBuddy b : buddyList) {
                    logger.warn("---- QQ nick:" + b.getNickname() + " markname:" + b.getMarkname() + " uin:"
                            + b.getUin() + " isVip:" + b.isVip() + " vip_level:" + b.getVipLevel());
                }
            }
        } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
            logger.warn("** 好友列表获取失败，处理重新获取");
        }
    }

}
