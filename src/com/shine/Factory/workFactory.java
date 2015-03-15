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
import com.shine.work.adminWork;
import com.shine.work.bindingMailWork;
import com.shine.work.sendMailWork;

public class workFactory {

    private static Logger logger = LoggerFactory.getLogger(workFactory.class);

    // ��������ַ�map
    private static Map<String, Object> workMap = new HashMap<String, Object>();
    static {
        workMap.put("���", new JavaPacketWork());
        workMap.put("��־", new sendMailWork());
        workMap.put("����", new adminWork());
        workMap.put("ѧϰ", new LearningWork());
        workMap.put("���", new NextPerWork());
        workMap.put("����", new DatabasePackWork());
        workMap.put("��", new bindingMailWork());
    }

    /**
     * �Ƿ�������ģʽ,Ĭ��Ϊ����
     */
    public static boolean isOn = true;
    
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
        logger.warn("��������:" + userMsg);
        Object object = null;
        ExecutionWork executionWork = null;
        try {
            object = workMap.get(userMsg.substring(1, 3));
            if (object != null) {
                executionWork = (ExecutionWork) object;
            } else {
                if (isOn) {
                    executionWork = new OtherWork();
                }
            }
        } catch (Exception e) {
            if (isOn) {
                executionWork = new OtherWork();
            }
        }
        if (object != null || isOn) {
            executionWork.executCommand(msgClient, msg);
        }
    }
}
