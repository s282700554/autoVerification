package com.shine.qq.listener;

import java.util.HashMap;
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
    private static boolean flag = false;
    
    // 配置信息
    private Map<String, String> groupMap = new HashMap<String, String>();
    
    // 群号
    private String groupNumber = null;
    
    // 是否已找到群号
    boolean isRun = false;
    
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
            if (!flag) {
                flag = true;
                try {
                    groupMap = BasicsConfigXmlOper.getBasicConfigInfo("group");
                    StartTimerTask.client = client;
                    // 判断是否启动消息提醒功能
                    if ("true".equals(groupMap.get("MESSAGE_ALERTS"))) {
                        groupNumber = groupMap.get("REMIND_GROUP");
                        if (!StringUtils.isNotBlank(groupNumber)) {
                            logger.error("基础配置信息中群号为空!");
                        }
                    }
                } catch (Exception e) {
                    flag = false;
                    e.printStackTrace();
                }
            }
            for (QQGroup g : client.getGroupList()) {
                if(!isRun && g.getName().equals(groupNumber)) {
                    try {
                        StartTimerTask.g = g;
                        StartTimerTask.runOnStart();
                        isRun = true;
                    } catch (Exception e) {
                        isRun = false;
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                };
                client.getGroupInfo(g, null);
                logger.warn("Group: " + g.getName());
            }
            if (!isRun && StringUtils.isNotBlank(groupNumber)) {
                logger.error("没有找到对应的群号:[" + groupNumber + "]");
            }
        }
    }
}
