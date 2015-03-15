package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.shine.operation.xml.TalkConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class OtherWork implements ExecutionWork {

    /**
     * 
     * 执行聊天命令.
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
        Map<String, String> map = TalkConfigXmlOper.getComInfo(userMsg);
        if (map != null && map.size() > 0) {
            msgClient.send(msg, map.get("RETURN_MSG"), StringUtils.isNotBlank(map.get("RETURN_FACE")) ? Integer.valueOf(map.get("RETURN_FACE")) : 117);
        } else {
            msgClient.send(msg, "你很无聊?", 80);
        }
    }
}
