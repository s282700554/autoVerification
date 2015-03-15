package com.shine.TimerTask;

import iqq.im.bean.QQMsg;
import iqq.im.bean.content.FaceItem;
import iqq.im.bean.content.FontItem;
import iqq.im.bean.content.TextItem;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackagePerXmlOper;

public class TaskUtils {

    private static Logger logger = LoggerFactory.getLogger(TaskUtils.class);

    /**
     * 
     * 发送验包人员消息.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    public static void sendPer(String msg) throws Exception {
        QQMsg sendMsg = new QQMsg();
        try {
            String personnel = PackagePerXmlOper.selectVerifyPersonnel();
            sendMsg.setGroup(StartTimerTask.g); // QQ好友UIN
            sendMsg.setType(QQMsg.Type.GROUP_MSG); // 发送类型为好友
            // QQ内容
            sendMsg.addContentItem(new TextItem(personnel + "," + msg)); // 添加文本内容
            sendMsg.addContentItem(new FaceItem(74)); // QQ id为0的表情
            sendMsg.addContentItem(new FontItem()); // 使用默认字体
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        StartTimerTask.client.sendMsg(sendMsg, null);
    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
