package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.bat.GenerationBat;
import com.shine.operation.bat.InvokeBat;
import com.shine.operation.eMail.sendEMail;
import com.shine.operation.file.OperFile;
import com.shine.operation.log.LogAnalysis;
import com.shine.operation.svn.UpdateSvn;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.DatabaseConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class DatabaseWorkThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(DatabaseWorkThread.class);

    private QqMessag msgClient;

    private QQMsg msg;

    private String version;

    public DatabaseWorkThread(QqMessag msgClient, QQMsg msg, String version) {
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
            Map<String, String> map = DatabaseConfigXmlOper.getVersionInfo(version);
            if (map != null) {
                msgClient.send(msg, version + "�汾���ݿ⿪ʼ����ˡ�", 23);
                boolean bool = false;
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
                    // ɾ��ָ���ļ��е�ָ���ļ���
                    else if (operation.startsWith("del")) {
                        OperFile.removeCode(operation, map);
                    }
                    // �滻�ļ��е�ָ�����
                    else if (operation.startsWith("rep")) {
                        OperFile.replaceKeyWord(operation, map);
                    } else if (operation.startsWith("sleep")) {
                        String time = operation.split(":")[1];
                        sleep(Long.valueOf(time));
                    }
                }
                if (bool) {
                    long qq = msg.getFrom().getQQ();
                    String eMail = AuthorityControlXmlOper.getMail(qq);
                    sendEMail.sendLogFile(eMail, map);
                    msgClient.send(msg, version + "���ݿ��������,����[ora-]����, [sp2]�������[���̱���]����,���ֹ���Ԥ,�ʼ��ѷ�����[" + eMail + "]����!�����!", 5);
                } else {
                    msgClient.send(msg, version + "���ݿ�������,��[ora-��sp2�����̱���]����,���Դ��!", 74);
                }
                // �����,��ԭ����
                DatabasePackWork.YB_CONTROL = true;
            } else {
                msgClient.send(msg, "����汾����ȷ,��������ȷ�İ汾��", 101);
                DatabasePackWork.YB_CONTROL = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            try {
                msgClient.send(msg, "��������,�������Լ�����!", 85);
                // �����,��ԭ����
                DatabasePackWork.YB_CONTROL = true;
            } catch (Exception e1) {
                DatabasePackWork.YB_CONTROL = true;
                logger.error(e1.getMessage());
            }
        }
        logger.warn("�߳��������");
    }
}
