package com.shine.Factory;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.qq.client.QqMessag;
import com.shine.work.DatabasePackWork;
import com.shine.work.ExecutionWork;
import com.shine.work.LearningWork;
import com.shine.work.NextPerWork;
import com.shine.work.OtherWork;
import com.shine.work.JavaPacketWork;
import com.shine.work.adminWork;
import com.shine.work.bindingMailWork;
import com.shine.work.sendMailWork;

public class workFactory {

    private static Logger logger = LoggerFactory.getLogger(workFactory.class);

    // 根据命令分发map
    private static Map<String, Object> workMap = new HashMap<String, Object>();
    static {
        workMap.put("验包", new JavaPacketWork());
        workMap.put("日志", new sendMailWork());
        workMap.put("管理", new adminWork());
        workMap.put("学习", new LearningWork());
        workMap.put("请假", new NextPerWork());
        workMap.put("数据", new DatabasePackWork());
        workMap.put("绑定", new bindingMailWork());
    }

    /**
     * 是否开启聊天模式,默认为开启
     */
    public static boolean isOn = true;
    
    /**
     * 根据不同的命令分发.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-28    SGJ 新建
     * </pre>
     */
    public static void createWork(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        logger.warn("接收命令:" + userMsg);
        Object object = null;
        ExecutionWork executionWork = null;
        try {
            object = workMap.get(userMsg.substring(1, 3));
            if (object != null) {
                executionWork = (ExecutionWork) object;
            } else {
                if (isOn) {
                    executionWork = new OtherWork();
                }
            }
        } catch (Exception e) {
            if (isOn) {
                executionWork = new OtherWork();
            }
        }
        if (object != null || isOn) {
            executionWork.executCommand(msgClient, msg);
        }
    }
}
