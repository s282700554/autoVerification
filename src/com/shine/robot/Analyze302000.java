package com.shine.robot;

import org.json.JSONArray;
import org.json.JSONObject;

public class Analyze302000 implements SuperAnalyze {

    /**
     * 
     * ����302000��Ϣ.
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
        JSONArray jSONArray = jsonObj.getJSONArray("list");
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jsonObject = jSONArray.getJSONObject(i);
            messge.append(jsonObject.getString("article") + ":" + jsonObject.getString("detailurl") + "\n");
        }
        return messge.toString();
    }

}
