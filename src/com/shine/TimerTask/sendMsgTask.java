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
                TaskUtils.sendPer("今天该你验包了哦,你有没有在,如果请假了来个人告诉我一下(@请假)");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * 取得今天是否是工作日.
     * 
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	SGJ	新建
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
