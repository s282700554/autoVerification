package com.shine.TimerTask;

import iqq.im.QQClient;
import iqq.im.bean.QQGroup;


public class StartTimerTask {

    // �ͻ���
    public static QQClient client;

    // Ⱥ��¼
    public static QQGroup g;

    public static void runOnStart() throws Exception {
        // ������ԭ��ʱ��
        UpdateNextPerTimer.run();
        // ����������Ϣ��ʱ��
        sendMsgTimer.run();
    }
}
