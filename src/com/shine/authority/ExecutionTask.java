package com.shine.authority;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;
import com.shine.utils.StringUtils;
import com.shine.work.ExecutionWork;
import com.shine.work.OtherWork;

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
        if (userMsg.length() > 2) {
            String mode = userMsg.substring(1, 3);
            logger.warn("接收命令:" + userMsg);
            Map<String, String> map = new HashMap<String, String>();
            try {
                map = AuthorityControlXmlOper.getModeInfo(mode);
                if (StringUtils.isNotBlank(map.get("MODE_LEVEL"))) {
                    // 该命令有权限控制,获取用户信息
                    msgClient.getQqUser(msg, new AuthorityVerify(msgClient, msg, map.get("MODE_LEVEL")));
                } else {
                    workFactory.createWork(msgClient, msg);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                msgClient.send(msg, "获取" + mode + "权限失败:" + e.getMessage(), 108);
            }
        } else if (workFactory.isOn) {
            ExecutionWork executionWork = new OtherWork();
            executionWork.executCommand(msgClient, msg);
        }
    }
}
