package com.shine.robot;

import org.json.JSONObject;

public class AnalyzOther implements SuperAnalyze {
    /**
     * 
     * ����4000��ͷ�쳣��Ϣ.
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
