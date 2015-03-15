package com.shine.qq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

public class UserInfoActionListener implements QQActionListener {
    
    private static Logger logger = LoggerFactory.getLogger(UserInfoActionListener.class);

    /**
     * 
     * 获取用户信息监听事件.
     * 
     * @param event
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-26    SGJ 新建
     * </pre>
     */
    @Override
    public void onActionEvent(QQActionEvent event) {
        logger.warn("LOGIN_STATUS:" + event.getType() + ":" + event.getTarget());
    }

}
