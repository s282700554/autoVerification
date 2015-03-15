package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackageLogXmlOper;
import com.shine.qq.client.QqMessag;

public class JavaPacketWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(JavaPacketWork.class);

    /**
     * 验包控制
     */
    public static boolean YB_CONTROL = true;

    /**
     * 
     * 执行验包命令.
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
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception {
        // 取命令
        String userMsg = msg.getText().trim();
        // 取验包版本
        String version = userMsg.substring(3, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "请注明你要验包的版本", 101);
        } else {
            // 验包,先检测是否有版本正在验包
            if (JavaPacketWork.YB_CONTROL) {
                JavaPacketWork.YB_CONTROL = false;
                logger.warn("验包:" + version);
                try {
                    JavaWorkThread javaWorkThread = new JavaWorkThread(msgClient, msg, version);
                    javaWorkThread.start();
                    // 记录验包情况
                    PackageLogXmlOper.addXmlLog(msg.getFrom().getNickname(), version);
                } catch (Exception e) {
                    JavaPacketWork.YB_CONTROL = true;
                    logger.warn(e.getMessage());
                    throw e;
                }
            } else {
                msgClient.send(msg, "正在验包中,请稍后.......!", 80);
            }
        }
    }
}
