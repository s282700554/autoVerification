package com.shine.robot;

import org.json.JSONObject;

public class Analyze100000 implements SuperAnalyze {

    /**
     * 
     * ����100000��Ϣ.
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
        return messge.toString();
    }

}
