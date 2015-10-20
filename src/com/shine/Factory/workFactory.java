package com.shine.Factory;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.qq.client.QqMessag;
import com.shine.work.DatabasePackWork;
import com.shine.work.ExecutionWork;
import com.shine.work.LearningWork;
import com.shine.work.NextPerWork;
import com.shine.work.OtherWork;
import com.shine.work.JavaPacketWork;
import com.shine.work.AdminWork;
import com.shine.work.BindingMailWork;
import com.shine.work.SendMailWork;

public class workFactory {

    private static Logger logger = LoggerFactory.getLogger(workFactory.class);

    // ��������ַ�map
    private static Map<String, Object> workMap = new HashMap<String, Object>();
    static {
        workMap.put("���", new JavaPacketWork());
        workMap.put("��־", new SendMailWork());
        workMap.put("����", new AdminWork());
        workMap.put("ѧϰ", new LearningWork());
        workMap.put("���", new NextPerWork());
        workMap.put("����", new DatabasePackWork());
        workMap.put("��", new BindingMailWork());
    }
    
    /**
     * ���ݲ�ͬ������ַ�.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-28    SGJ �½�
     * </pre>
     */
    public static void createWork(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        if (!"�Ϲ�".equals(msg.getFrom().getNickname())) {
            logger.warn("��������:" + userMsg);
        }
        Object object = null;
        ExecutionWork executionWork = null;
        try {
            object = workMap.get(userMsg.substring(1, 3));
            if (object != null) {
                executionWork = (ExecutionWork) object;
            } else {
                if (OtherWork.isOn) {
                    executionWork = new OtherWork();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        if (executionWork != null) {
            executionWork.executCommand(msgClient, msg);
        }
    }
}
