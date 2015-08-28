package com.shine.robot;

import org.json.JSONArray;
import org.json.JSONObject;

public class Analyze308000 implements SuperAnalyze {

    /**
     * 
     * 处理308000信息.
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
            messge.append(jsonObject.getString("name") + "\n");
            messge.append(jsonObject.getString("info") + "\n");
            messge.append("详情地址：" + jsonObject.getString("detailurl") + "\n");
            messge.append("\n");
        }
        return messge.toString();
    }

}
