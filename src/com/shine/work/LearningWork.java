package com.shine.work;

import iqq.im.bean.QQMsg;
import iqq.im.bean.content.ContentItem;
import iqq.im.bean.content.FaceItem;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.TalkConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class LearningWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(LearningWork.class);

    /**
     * 
     * 执行学习命令.
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
        synchronized (LearningWork.class) {
            String userMsg = msg.getText().trim();
            // 学习
            String commd = userMsg.substring(3, userMsg.length()).trim();
            logger.warn("学习" + commd);
            // 0 命令 1返回信息
            String[] commdAndMsg = commd.split(":");
            if (commdAndMsg.length > 1 && commdAndMsg[0].trim() != null && commdAndMsg[1].trim() != null) {
                Map<String, String> map = TalkConfigXmlOper.getComInfo(commdAndMsg[0].trim());
                String faceId = "";
                List<ContentItem> items = msg.getContentList();
                for (ContentItem item : items) {
                    if (item.getType() == ContentItem.Type.FACE) {
                        faceId = String.valueOf(((FaceItem) item).getId());
                    }
                }
                if (map != null && map.size() > 0) {
                    TalkConfigXmlOper.updateXMLNode(commdAndMsg[0], commdAndMsg[1].split("\\[")[0], faceId);
                } else {
                    TalkConfigXmlOper.addXMLNode(commdAndMsg[0], commdAndMsg[1].split("\\[")[0], faceId);
                }
                msgClient.send(msg, "我学会了,快来考考我吧!", 79);
            } else {
                msgClient.send(msg, "我不理解你说的什么呢,再教我一遍吧!", 81);
            }
        }
    }
}
