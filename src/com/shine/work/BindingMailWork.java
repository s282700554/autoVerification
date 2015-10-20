package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class BindingMailWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(BindingMailWork.class);

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
        synchronized (BindingMailWork.class) {
            String userMsg = msg.getText().trim();
            logger.warn("qq消息:" + userMsg);
            // 版本
            String mail = userMsg.substring(3, userMsg.length()).trim();
            long qq = msg.getFrom().getQQ();
            logger.warn(qq + "绑定邮箱为:" + mail);
            try {
                AuthorityControlXmlOper.bindingUserMail(String.valueOf(qq), mail);
                msgClient.send(msg, "qq:" + qq + "成功绑定邮箱为:" + mail, 109);
            } catch (Exception e) {
                logger.error("qq:" + qq + "绑定邮箱:" + mail + "失败!原因:" + e.getMessage());
                msgClient.send(msg, "qq:" + qq + "绑定邮箱:" + mail + "失败!", 5);
            }
        }
    }
}
