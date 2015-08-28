package com.shine.robot;

import org.json.JSONObject;

public interface SuperAnalyze {

    /**
     * 
     * 处理信息.
     * 
     * @param jsonObj
     * @return
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-8-21	SGJ	新建
     * </pre>
     */
    public String processMessage(JSONObject jsonObj) throws Exception;

}
