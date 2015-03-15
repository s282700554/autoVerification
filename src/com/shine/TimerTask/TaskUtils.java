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
     * ���������Ա��Ϣ.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-4	SGJ	�½�
     * </pre>
     */
    public static void sendPer(String msg) throws Exception {
        QQMsg sendMsg = new QQMsg();
        try {
            String personnel = PackagePerXmlOper.selectVerifyPersonnel();
            sendMsg.setGroup(StartTimerTask.g); // QQ����UIN
            sendMsg.setType(QQMsg.Type.GROUP_MSG); // ��������Ϊ����
            // QQ����
            sendMsg.addContentItem(new TextItem(personnel + "," + msg)); // ����ı�����
            sendMsg.addContentItem(new FaceItem(74)); // QQ idΪ0�ı���
            sendMsg.addContentItem(new FontItem()); // ʹ��Ĭ������
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        StartTimerTask.client.sendMsg(sendMsg, null);
    }

    // ���ӻ��������
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
