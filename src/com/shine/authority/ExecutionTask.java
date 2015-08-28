package com.shine.authority;

import iqq.im.bean.QQMsg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.qq.client.QqMessag;
import com.shine.utils.StringUtils;
import com.shine.work.ExecutionWork;
import com.shine.work.OtherWork;

public class ExecutionTask {

    private static Logger logger = LoggerFactory.getLogger(ExecutionTask.class);

    /**
     * ��֤����Ȩ��.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-28	SGJ	�½�
     * </pre>
     */
    public static void verifyAuthority(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        if (userMsg.length() > 2) {
            String mode = userMsg.substring(1, 3);
            logger.warn("��������:" + userMsg);
            Map<String, String> map = new HashMap<String, String>();
            try {
                map = AuthorityControlXmlOper.getModeInfo(mode);
                if (StringUtils.isNotBlank(map.get("MODE_LEVEL"))) {
                    // ��������Ȩ�޿���,��ȡ�û���Ϣ
                    msgClient.getQqUser(msg, new AuthorityVerify(msgClient, msg, map.get("MODE_LEVEL")));
                } else {
                    workFactory.createWork(msgClient, msg);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                msgClient.send(msg, "��ȡ" + mode + "Ȩ��ʧ��:" + e.getMessage(), 108);
            }
        } else if (workFactory.isOn) {
            ExecutionWork executionWork = new OtherWork();
            executionWork.executCommand(msgClient, msg);
        }
    }
}
