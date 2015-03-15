package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.eMail.sendEMail;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.JavaConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class sendMailWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(sendMailWork.class);

    /**
     * 邮件发送控制
     */
    public static boolean SEND_MAIL = true;

    /**
     * 
     * 执行发送邮件命令.
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
        String userMsg = msg.getText().trim();
        // 发日志
        boolean bool = false;
        // 版本
        String version = userMsg.substring(3, userMsg.length()).trim();
        if (version == null || version.equals("")) {
            msgClient.send(msg, "请注明你要的日志版本", 101);
        } else {
            bool = SEND_MAIL;
            SEND_MAIL = false;
            if (true == bool) {
                Map<String, String> versionInfo = JavaConfigXmlOper.getVersionInfo(version);
                if (versionInfo != null) {
                    long qq = msg.getFrom().getQQ();
                    String mail = AuthorityControlXmlOper.getMail(qq);
                    msgClient.send(msg, "亲,你要的[" + version + "JAVA验包日志]马上就送到[" + mail + "]哦,收到请给好评!", 109);
                    logger.warn("发送日志QQ[" + msg.getFrom().getQQ() + "],版本:[" + version + "]");
                    try {
                        sendEMail.sendLogFile(mail, versionInfo);
                        SEND_MAIL = true;
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                        SEND_MAIL = true;
                        throw e;
                    }
                } else {
                    msgClient.send(msg, "日志版本不正确,请输入正确的版本号", 101);
                    SEND_MAIL = true;
                }
            } else {
                msgClient.send(msg, "正发着呢,别吵,一会再找我", 79);
            }
        }
    }
}
