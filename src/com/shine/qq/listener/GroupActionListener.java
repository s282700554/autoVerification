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

    // �ͻ���
    QQClient client;
    
    // �Ƿ��Ѽ�¼
    private static boolean flag = false;
    
    // ������Ϣ
    private Map<String, String> groupMap = new HashMap<String, String>();
    
    // Ⱥ��
    private String groupNumber = null;
    
    // �Ƿ����ҵ�Ⱥ��
    boolean isRun = false;
    
    public GroupActionListener(QQClient client) {
        this.client = client;
    }

    /**
     * 
     * ��ȡȺ��Ϣ�����¼�.
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
        if (event.getType() == Type.EVT_OK) {
            if (!flag) {
                flag = true;
                try {
                    groupMap = BasicsConfigXmlOper.getBasicConfigInfo("group");
                    StartTimerTask.client = client;
                    // �ж��Ƿ�������Ϣ���ѹ���
                    if ("true".equals(groupMap.get("MESSAGE_ALERTS"))) {
                        groupNumber = groupMap.get("REMIND_GROUP");
                        if (!StringUtils.isNotBlank(groupNumber)) {
                            logger.error("����������Ϣ��Ⱥ��Ϊ��!");
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
                logger.error("û���ҵ���Ӧ��Ⱥ��:[" + groupNumber + "]");
            }
        }
    }
}
