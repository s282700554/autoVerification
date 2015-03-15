package com.shine.TimerTask;

import iqq.im.QQClient;
import iqq.im.bean.QQGroup;


public class StartTimerTask {

    // 客户端
    public static QQClient client;

    // 群记录
    public static QQGroup g;

    public static void runOnStart() throws Exception {
        // 启动还原定时器
        UpdateNextPerTimer.run();
        // 启动发送消息定时器
        sendMsgTimer.run();
    }
}
