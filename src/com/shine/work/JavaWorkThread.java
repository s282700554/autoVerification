package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.bat.GenerationBat;
import com.shine.operation.bat.InvokeBat;
import com.shine.operation.log.LogAnalysis;
import com.shine.operation.svn.UpdateSvn;
import com.shine.operation.xml.JavaConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class JavaWorkThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(JavaWorkThread.class);

    private QqMessag msgClient;

    private QQMsg msg;

    private String version;

    public JavaWorkThread(QqMessag msgClient, QQMsg msg, String version) {
        this.msgClient = msgClient;
        this.msg = msg;
        this.version = version;
    }

    /**
     * 
     * �����߳�,ִ���������.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-6 SGJ �½�
     * </pre>
     */
    @Override
    public void run() {
        try {
            boolean bool = false;
            Map<String, String> map = JavaConfigXmlOper.getVersionInfo(version);
            if (map != null) {
                msgClient.send(msg, "�� �� �� ��������ʼ�ɻ��ˡ�", 23);
                // ����Ҫʹ�õ�bat�ļ�
                GenerationBat.createBat(map);
                for(String operation : map.get("EXECUTION_STEP").split(",")) {
                    // �����bat�ļ���ִ��bat
                    if (operation.endsWith("bat")) {
                        InvokeBat invokeBat = new InvokeBat();
                        invokeBat.execution(map, operation);
                    }
                    // ����svn
                    else if (operation.startsWith("svn")) {
                        UpdateSvn.updateOrCheckoutSvn(operation, map);
                    }
                    // ������־
                    else if (operation.startsWith("log")) {
                        bool = LogAnalysis.analysisLog(operation, map);
                    }
                }
                if (bool) {
                    // ������Ϣ
                    msgClient.send(msg, version + "JAVA�������,���������������ֹ���֤!", 5);
                } else {
                    msgClient.send(msg, version + "JAVA���ͨ��!", 74);
                }
            } else {
                msgClient.send(msg, "����汾����ȷ,��������ȷ�İ汾��", 101);
            }
            // �����,��ԭ����
            JavaPacketWork.YB_CONTROL = true;
        } catch (Exception e) {
            // �����,��ԭ����
            JavaPacketWork.YB_CONTROL = true;
            logger.error(e.getMessage());
            try {
                msgClient.send(msg, "��������,�������Լ�����!", 85);
            } catch (Exception e1) {
                logger.error(e1.getMessage());
            }
        }
        logger.warn("�߳��������");
    }
}
