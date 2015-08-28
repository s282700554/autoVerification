package com.shine.robot;

import org.json.JSONArray;
import org.json.JSONObject;

public class Analyze302000 implements SuperAnalyze {

    /**
     * 
     * 处理302000信息.
     * 
     * @param jsonObj
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2015-8-21    SGJ 新建
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
