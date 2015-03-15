package com.shine.qq.listener;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQDiscuz;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

public class DiscuzActionListener implements QQActionListener {
    
    // 客户端
    QQClient client;
    
    public DiscuzActionListener (QQClient client) {
        this.client = client;
    }
    
    
    /**
     * 
     * 获取讨论组信息监听事件.
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
        if (event.getType() == Type.EVT_OK) {
            for (QQDiscuz d : client.getDiscuzList()) {
                client.getDiscuzInfo(d, null);
                System.out.println("Discuz: " + d.getName());
            }
        }
    }
}
