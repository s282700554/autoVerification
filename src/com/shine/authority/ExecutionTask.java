package com.shine.authority;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;

public class ExecutionTask {

    private static Logger logger = LoggerFactory.getLogger(ExecutionTask.class);

    /**
     * 验证命令权限.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-28	SGJ	新建
     * </pre>
     */
    public static void verifyAuthority(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        logger.warn("接收命令:" + userMsg);
        Map<String, String> map = new HashMap<String, String>();
        boolean isVerify = true;
        try {
            map = AuthorityControlXmlOper.getModeInfo(userMsg.substring(1, 3));
            isVerify = map != null && map.size() > 0 ? true : false;
        } catch (Exception e) {
            isVerify = false;
        }
        if (isVerify) {
            // 该命令有权限控制,获取用户信息
            msgClient.getQqUser(msg, new AuthorityVerify(msgClient, msg, map.get("MODE_LEVEL")));
        } else {
            workFactory.createWork(msgClient, msg);
        }
    }
}
