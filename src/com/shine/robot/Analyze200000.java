package com.shine.robot;

import org.json.JSONObject;

public class Analyze200000 implements SuperAnalyze {

    /**
     * 
     * ����200000��Ϣ.
     * 
     * @param jsonObj
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2015-8-21    SGJ �½�
     * </pre>
     */
    @Override
    public String processMessage(JSONObject jsonObj) throws Exception {
        StringBuffer messge = new StringBuffer();
        messge.append(jsonObj.getString("text") + "\n");
        messge.append("��ַ:" + jsonObj.getString("url") + "\n");
        return messge.toString();
    }

}
