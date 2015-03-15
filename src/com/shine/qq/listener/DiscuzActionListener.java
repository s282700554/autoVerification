package com.shine.qq.listener;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQDiscuz;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

public class DiscuzActionListener implements QQActionListener {
    
    // �ͻ���
    QQClient client;
    
    public DiscuzActionListener (QQClient client) {
        this.client = client;
    }
    
    
    /**
     * 
     * ��ȡ��������Ϣ�����¼�.
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
            for (QQDiscuz d : client.getDiscuzList()) {
                client.getDiscuzInfo(d, null);
                System.out.println("Discuz: " + d.getName());
            }
        }
    }
}
