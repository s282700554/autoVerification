package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.eMail.sendEMail;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.DatabaseConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class DatabasePackWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(DatabasePackWork.class);

    /**
     * 数据库验包控制
     */
    public static boolean YB_CONTROL = true;

    /**
     * 发送日志控制
     */
    public static boolean LZ_CONTROL = true;

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
        String version = userMsg.substring(6, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "请注明操作版本", 101);
        } else {
            if (userMsg.substring(4, userMsg.length()).startsWith("验包")) {
                // 验包,先检测是否有版本正在验包
                if (DatabasePackWork.YB_CONTROL) {
                    // @数据库验包取验包版本
                    DatabasePackWork.YB_CONTROL = false;
                    logger.warn("验包:" + version);
                    try {
                        DatabaseWorkThread datebaseWorkThread = new DatabaseWorkThread(msgClient, msg, version);
                        datebaseWorkThread.start();
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                        DatabasePackWork.YB_CONTROL = true;
                        throw e;
                    }
                } else {
                    msgClient.send(msg, "正在验包中,请稍后.......!", 80);
                }
            } else if (userMsg.substring(4, userMsg.length()).startsWith("日志")) {
                if (LZ_CONTROL) {
                    Map<String, String> map = DatabaseConfigXmlOper.getVersionInfo(version);
                    if (map != null) {
                        LZ_CONTROL = false;
                        long qq = msg.getFrom().getQQ();
                        String mail = AuthorityControlXmlOper.getMail(qq);
                        msgClient.send(msg, "亲,你要的[" + version + "数据库日志]马上就送到[" + mail + "]哦,收到请给好评!", 109);
                        sendEMail.sendLogFile(mail, map);
                        LZ_CONTROL = true;
                    } else {
                        msgClient.send(msg, "日志版本不正确,请输入正确的版本号", 101);
                    }
                } else {
                    msgClient.send(msg, "亲,你要的[" + version + "数据库日志]正在送货路上,请稍候!", 109);
                }
            }
        }
    }
}
