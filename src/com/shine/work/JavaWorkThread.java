package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.bat.GenerationBat;
import com.shine.operation.bat.InvokeBat;
import com.shine.operation.log.LogAnalysis;
import com.shine.operation.svn.UpdateSvn;
import com.shine.operation.xml.JavaConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class JavaWorkThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(JavaWorkThread.class);

    private QqMessag msgClient;

    private QQMsg msg;

    private String version;

    public JavaWorkThread(QqMessag msgClient, QQMsg msg, String version) {
        this.msgClient = msgClient;
        this.msg = msg;
        this.version = version;
    }

    /**
     * 
     * 运行线程,执行验包命令.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-6 SGJ 新建
     * </pre>
     */
    @Override
    public void run() {
        try {
            boolean bool = false;
            Map<String, String> map = JavaConfigXmlOper.getVersionInfo(version);
            if (map != null) {
                msgClient.send(msg, "啦 啦 啦 。。。开始干活了。", 23);
                // 生成要使用的bat文件
                GenerationBat.createBat(map);
                for(String operation : map.get("EXECUTION_STEP").split(",")) {
                    // 如果是bat文件刚执行bat
                    if (operation.endsWith("bat")) {
                        InvokeBat invokeBat = new InvokeBat();
                        invokeBat.execution(map, operation);
                    }
                    // 操作svn
                    else if (operation.startsWith("svn")) {
                        UpdateSvn.updateOrCheckoutSvn(operation, map);
                    }
                    // 操作日志
                    else if (operation.startsWith("log")) {
                        bool = LogAnalysis.analysisLog(operation, map);
                    }
                }
                if (bool) {
                    // 发送消息
                    msgClient.send(msg, version + "JAVA验包出错,请今天验包负责人手工验证!", 5);
                } else {
                    msgClient.send(msg, version + "JAVA验包通过!", 74);
                }
            } else {
                msgClient.send(msg, "验包版本不正确,请输入正确的版本号", 101);
            }
            // 验完包,还原控制
            JavaPacketWork.YB_CONTROL = true;
        } catch (Exception e) {
            // 验完包,还原控制
            JavaPacketWork.YB_CONTROL = true;
            logger.error(e.getMessage());
            try {
                msgClient.send(msg, "我生病了,这活你得自己干了!", 85);
            } catch (Exception e1) {
                logger.error(e1.getMessage());
            }
        }
        logger.warn("线程运行完成");
    }
}
