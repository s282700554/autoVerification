package com.shine.TimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateNextPerTimer {

    private static Logger logger = LoggerFactory.getLogger(UpdateNextPerTimer.class);
    // 时间间隔
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static boolean CONTROL = true;

    public static void run() throws Exception {
        if (CONTROL) {
            try {
                Calendar calendar = Calendar.getInstance();
                // 定制每日2:00执行方法
                calendar.set(Calendar.HOUR_OF_DAY, 2);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                // 取得第一次执行任务的时间
                Date date = calendar.getTime();
                // 如果第一次执行定时任务的时间 小于 当前的时间
                // 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
                if (date.before(new Date())) {
                    date = TaskUtils.addDay(date, 1);
                }
                Timer timer = new Timer();
                // 根据设置的时间调用定时器工作
                timer.schedule(new UpdateNextPerTask(), date, PERIOD_DAY);
                CONTROL = false;
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
