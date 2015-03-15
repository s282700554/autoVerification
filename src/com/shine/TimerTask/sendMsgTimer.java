package com.shine.TimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sendMsgTimer {
    
    private static Logger logger = LoggerFactory.getLogger(sendMsgTimer.class);
    // 时间间隔
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static boolean CONTROL = true;

    public static void run() throws Exception {
        if (CONTROL) {
            try {
                Calendar calendar = Calendar.getInstance();
                // 定制每日15:30执行方法
                calendar.set(Calendar.HOUR_OF_DAY, 15);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 0);
                // 取得第一次执行任务的时间
                Date date = calendar.getTime();
                if (date.before(new Date())) {
                    date = TaskUtils.addDay(date, 1);
                }
                Timer timer = new Timer();
                // 根据设置的时间调用定时器工作
                timer.schedule(new sendMsgTask(), date, PERIOD_DAY);
                CONTROL = false;
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
