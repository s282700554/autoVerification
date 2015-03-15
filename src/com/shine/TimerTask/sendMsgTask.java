package com.shine.TimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sendMsgTask extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(sendMsgTask.class);

    @Override
    public void run() {
        try {
            if(isWeek()) {
                TaskUtils.sendPer("������������Ŷ,����û����,�������������˸�����һ��(@���)");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * ȡ�ý����Ƿ��ǹ�����.
     * 
     * @return
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-7	SGJ	�½�
     * </pre>
     */
    public static boolean isWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            return false;
        }
        return true;
    }
}
