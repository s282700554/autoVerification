package com.shine.qq.listener;

import java.util.Map;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQGroup;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.TimerTask.StartTimerTask;
import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.StringUtils;

public class GroupActionListener implements QQActionListener {

    private static Logger logger = LoggerFactory.getLogger(GroupActionListener.class);

    // 客户端
    QQClient client;
    
    // 是否已记录
    private static boolean flag = true;
    
    public GroupActionListener(QQClient client) {
        this.client = client;
    }

    /**
     * 
     * 获取群信息监听事件.
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
            String groupNumber = null;
            if (flag) {
                StartTimerTask.client = client;
                flag = false;
                try {
                    Map<String, String> groupMap = BasicsConfigXmlOper.getBasicConfigInfo("group");
                    groupNumber = groupMap.get("REMIND_GROUP");
                    if (!StringUtils.isNotBlank(groupNumber)) {
                        throw new NullPointerException("基础配置信息中群号为空!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            boolean isRun = false;
            for (QQGroup g : client.getGroupList()) {
                if(g.getName().equals(groupNumber)) {
                    try {
                        StartTimerTask.g = g;
                        StartTimerTask.runOnStart();
                        isRun = true;
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                };
                client.getGroupInfo(g, null);
                logger.warn("Group: " + g.getName());
            }
            if (!isRun) {
                throw new NullPointerException("没有找到对应的群号:[" + groupNumber + "]");
            }
        }
    }
}
