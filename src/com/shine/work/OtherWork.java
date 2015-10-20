package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.shine.operation.xml.TalkConfigXmlOper;
import com.shine.qq.client.QqMessag;
import com.shine.robot.ConnectionTuling;

public class OtherWork implements ExecutionWork {
    
    /**
     * �Ƿ�������ģʽ,Ĭ��Ϊ����
     */
    public static boolean isOn = true;
    
    /**
     * �Ƿ�����������
     */
    public static boolean isInternet = true;
    
    /**
     * 
     * ִ����������.
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
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        if (isInternet) {
            long userid = msg.getFrom().getUin();
            String message = ConnectionTuling.getMessage(String.valueOf(userid), userMsg);
            msgClient.send(msg, message, -1);
        } else {
            Map<String, String> map = TalkConfigXmlOper.getComInfo(userMsg);
            if (map != null && map.size() > 0) {
                msgClient.send(msg, map.get("RETURN_MSG"),
                        StringUtils.isNotBlank(map.get("RETURN_FACE")) ? Integer.valueOf(map.get("RETURN_FACE")) : 117);
            } else {
                msgClient.send(msg, "ϸ���ӣ����ô��Ŷ��", 81);
            }
        }
    }
}
