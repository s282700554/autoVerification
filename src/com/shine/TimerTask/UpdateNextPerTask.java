package com.shine.TimerTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackageLogXmlOper;
import com.shine.operation.xml.PackagePerXmlOper;

public class UpdateNextPerTask extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(UpdateNextPerTask.class);

    // ʱ���ʽ
    private static DateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void run() {
        try {
            // �ȶ�ȡ�����Ƿ����
            Date date = TaskUtils.addDay(new Date(), -1);
            List<Map<String, String>> list = PackageLogXmlOper.getLogInfo(format.format(date));
            boolean isVerification = list != null && list.size() > 0 ? true : false;
            // ���ݽ����Ƿ���������������ԱΪ��һ��
            if (isVerification) {
                PackageLogXmlOper.clearLog(format.format(date));
                PackagePerXmlOper.updateNext();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
