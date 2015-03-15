package com.shine.qq.listener;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoginActionListener implements QQActionListener {
    
    private static Logger logger = LoggerFactory.getLogger(LoginActionListener.class);
    
    // 客户端
    QQClient client;

    /**
     * 
     * 登陆监听事件.
     * 
     * @param client
     * @throws Exception
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-26    SGJ 新建
     * </pre>
     */
    public LoginActionListener(QQClient client) {
        this.client = client;
    }
    
    /**
     * 
     * 登陆监听事件.
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
        if (event.getType() == Type.EVT_OK) {
            // 到这里就算是登录成功了
            // 获取下用户信息
            client.getUserInfo(client.getAccount(), new UserInfoActionListener());
            // 获取好友列表..
            // 不一定调用，可能会有本地缓存
            client.getBuddyList(new FriendActionListener());
            // 获取群列表
            client.getGroupList(new GroupActionListener(client));
            // 获取讨论组列表
            client.getDiscuzList(new DiscuzActionListener(client));
            // 启动轮询时，需要获取所有好友、群成员、讨论组成员
            // 所有的逻辑完了后，启动消息轮询
            client.beginPollMsg();
        }
    }
}
