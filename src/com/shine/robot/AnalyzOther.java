package com.shine.robot;

import org.json.JSONObject;

public class AnalyzOther implements SuperAnalyze {
    /**
     * 
     * 处理4000开头异常信息.
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
        return messge.toString();
    }
}
