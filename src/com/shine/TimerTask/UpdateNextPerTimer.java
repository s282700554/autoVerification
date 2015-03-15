package com.shine.TimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateNextPerTimer {

    private static Logger logger = LoggerFactory.getLogger(UpdateNextPerTimer.class);
    // ʱ����
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static boolean CONTROL = true;

    public static void run() throws Exception {
        if (CONTROL) {
            try {
                Calendar calendar = Calendar.getInstance();
                // ����ÿ��2:00ִ�з���
                calendar.set(Calendar.HOUR_OF_DAY, 2);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                // ȡ�õ�һ��ִ�������ʱ��
                Date date = calendar.getTime();
                // �����һ��ִ�ж�ʱ�����ʱ�� С�� ��ǰ��ʱ��
                // ��ʱҪ�� ��һ��ִ�ж�ʱ�����ʱ�� ��һ�죬�Ա���������¸�ʱ���ִ�С��������һ�죬���������ִ�С�
                if (date.before(new Date())) {
                    date = TaskUtils.addDay(date, 1);
                }
                Timer timer = new Timer();
                // �������õ�ʱ����ö�ʱ������
                timer.schedule(new UpdateNextPerTask(), date, PERIOD_DAY);
                CONTROL = false;
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
