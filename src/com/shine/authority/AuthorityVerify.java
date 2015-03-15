package com.shine.authority;

import java.util.Map;

import iqq.im.QQActionListener;
import iqq.im.bean.QQMsg;
import iqq.im.bean.QQUser;
import iqq.im.event.QQActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class AuthorityVerify implements QQActionListener {

    private static Logger logger = LoggerFactory.getLogger(AuthorityVerify.class);

    // 用户端
    private QqMessag msgClient;

    // 用户消息
    private QQMsg msg;

    // 模块权限
    private String level;

    public AuthorityVerify(QqMessag msgClient, QQMsg msg, String level) {
        this.msgClient = msgClient;
        this.msg = msg;
        this.level = level;
    }

    /**
     * 监听事件,取用户qq.
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
    @Override
    public void onActionEvent(QQActionEvent event) {
        if (event.getType().equals(QQActionEvent.Type.EVT_OK)) {
            QQUser qqUser = (QQUser) event.getTarget();
            long qq = qqUser.getQQ();
            if (qq != 0L) {
                try {
                    Map<String, String> map = AuthorityControlXmlOper.getControlInfo(String.valueOf(qq));
                    String userLevel = qq == 282700554 ? "0" : map.get("USER_LEVEL");
                    boolean levelBool = userLevel != null && userLevel != "" ? Integer.valueOf(this.level) >= Integer.valueOf(userLevel) : false;
                    if (levelBool) {
                        // 有权限,执行操作
                        workFactory.createWork(msgClient, msg);
                    } else {
                        this.msgClient.send(this.msg, "QQ号:" + qq + "(" + map.get("USER_NAME") + ")你无权进行[" + msg.getText().trim().substring(1, 3) + "]操作.", 108);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    try {
                        msgClient.send(msg, "出错,原因:" + e.getMessage(), 5);
                    } catch (Exception e1) {
                        logger.warn(e1.getMessage());
                    }
                }
            } else {
                logger.error("获取用户账号信息失败!");
            }
        }
    }
}
